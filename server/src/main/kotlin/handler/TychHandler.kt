package handler

import com.google.gson.JsonElement
import gson
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketException
import toJsonMessage
import TychRequest
import User
import Tych
import TychResponse
import putTemp
import tychs
import users

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler : MessageHandler<TychRequest>() {

    override fun parse(message: JsonElement) = gson.fromJson(message, TychRequest::class.java)

    override fun handle(user: User, session: Session, message: TychRequest) {
        val tych = Tych(user, message)
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
        }
        return consumedTychs
    }

    /**
     * Converts [Tych] to [TychResponse] and sends it to frontend.
     */
    fun send(tych: Tych) {
        val tychResponse = TychResponse(tych)
        val receivers = users
                .filter { it.value != null }
                .filter { it.key.isOpen }
        receivers.forEach({ session, user ->
            try {
                session.remote.sendString(gson.toJsonMessage(tychResponse))
            }
            catch (ex: WebSocketException) {
                session.close()
            }
        })
    }
}
