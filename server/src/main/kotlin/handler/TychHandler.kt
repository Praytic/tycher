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
import tychs
import users

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler : MessageHandler<TychRequest>() {

    override fun parse(message: JsonElement) = gson.fromJson(message, TychRequest::class.java)

    override fun handle(user: User, session: Session, message: TychRequest) {
        val tych = toTych(user, message)
        if (user.clickable.invoke(tychs[user])) {
            consumeTychs(tych)
            tychs.put(user, tych)
            send(tych)
        }
    }

    fun consumeTychs(tych: Tych) {
        val userScoreChange = mutableMapOf<User, Int>()
        val consumedTychs = tych.consumedTychs(tychs.values)
        val gainedScore = consumedTychs.map { it to it.calculateScore() }.toMap()
        userScoreChange[tych.tycher] = gainedScore.entries.sumBy { it.value }
        consumedTychs.forEach {
            it.tycher.tychIsReady = true
            userScoreChange[it.tycher] = -it.calculateScore()
            tychs.remove(it.tycher)
        }
    }

    fun toTych(tycher: User, tychRequest: TychRequest): Tych {
        val spawnTime = tychRequest.spawnTime
        val position = tychRequest.position
        return Tych(tycher, position, spawnTime, isDummy = false)
    }

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
