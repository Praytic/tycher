package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketException
import java.util.*

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler : MessageHandler<TychRequest>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, TychRequest::class.java)

  override fun handle(user: User, session: Session, message: TychRequest) {
    val tych = Tych(user, message)
    log.info { "Handling $message." }
    if (user.isClickable(tychs[user])) {
      val consumedTychs = consumeTychs(tych)
      tychs.putTemp(user, tych)
      send(tych)
      consumedTychs.forEach { remove(it) }
    }
  }

  /**
   * Removes [Tych]s which were overlapped by specified [tych].
   * Returns consumed [Tych]s. They will no longer be presented in [tychs].
   */
  fun consumeTychs(tych: Tych): List<Tych> {
    log.info { tychs.values }
    val consumedTychs = tych.getAllConsumedTychs(tychs.values)
    val gainedScore = consumedTychs.map { it to it.calculateScore() }.toMap()
    tych.tycher.score += gainedScore.entries.sumBy { it.value }
    consumedTychs.forEach {
      val tycherScore = it.calculateScore()
      it.tycher.score -= tycherScore
      tychs.remove(it.tycher)
      log.info { "$tych consumed $it and gains $tycherScore points." }
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
  
  fun remove(tych: Tych) {
    val tychResponse = TychResponse(tych)
    val receivers = users
        .filter { it.value != null }
        .filter { it.key.isOpen }
    receivers.forEach(
        { session, user ->
          try {
            log.info { "Removing $tych for $user." }
            session.remote.sendString(gson.toJsonMessage(tychResponse,
                                                         Command.REMOVE_TYCH))
          } catch (ex: WebSocketException) {
            session.close()
          }
        })
  }
}