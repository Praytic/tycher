package handler

import Tych
import TychRequest
import TychResponse
import User
import com.google.gson.JsonElement
import gson
import log
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketException
import putTemp
import toJsonMessage
import tychs
import users

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler : MessageHandler<TychRequest>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, TychRequest::class.java)

  override fun handle(user: User, session: Session, message: TychRequest) {
    val tych = Tych(user, message)
    log.info { "Handling $tych." }
    if (user.clickable.invoke(tychs[user])) {
      consumeTychs(tych)
      tychs.putTemp(user, tych)
      send(tych)
    }
  }

  /**
   * Removes [Tych]s which were overlapped by specified [tych].
   * Returns consumed [Tych]s. They will no longer be presented in [tychs].
   */
  fun consumeTychs(tych: Tych): List<Tych> {
    val userScoreChange = mutableMapOf<User, Int>()
    val consumedTychs = tych.consumedTychs(tychs.values)
    val gainedScore = consumedTychs.map { it to it.calculateScore() }.toMap()
    userScoreChange[tych.tycher] = gainedScore.entries.sumBy { it.value }
    consumedTychs.forEach {
      userScoreChange[it.tycher] = -it.calculateScore()
      tychs.remove(it.tycher)
      log.info { "$tych consumed $it." }
    }
    return consumedTychs
  }

  /**
   * Converts [Tych] to [TychResponse] and sends it to all clients.
   */
  fun send(tych: Tych) {
    val tychResponse = TychResponse(tych)
    val receivers = users
        .filter { it.value != null }
        .filter { it.key.isOpen }
    receivers.forEach(
        { session, user ->
          try {
            log.info { "Sending $tych to $user." }
            session.remote.sendString(gson.toJsonMessage(tychResponse))
          } catch (ex: WebSocketException) {
            session.close()
          }
        })
  }
}
