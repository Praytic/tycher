package main.kotlin

import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.json.JSONObject
import java.util.*

/**
 * [BoardHandler] operates only with [Tych] entities which
 * is sent from the client via WebSocket.
 */
@WebSocket
class BoardHandler {

    /**
     * Handles messages sent via WebSocket. Allowed message format:
     * {
     *   "name": {
     *     <main.kotlin.Tych entity>
     *   }
     * }
     * *name* can be any value from the [Command] enum. If the command
     * is undefined or the [Tych] can't be parsed, the exception will be thrown.
     */
    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val messageJson = JSONObject(message)
        val handlerName = messageJson.keys().next() ?:
                throw IllegalArgumentException("No handler name was provided by message $message.")
        val adapter = moshi.adapter(Tych::class.java)
        val tych = adapter.fromJson(messageJson.getJSONObject(handlerName).toString()) ?:
                throw IllegalArgumentException("Unable to parse tych object from message $message.")
        when (handlerName) {
            Command.TYCH.id -> synchronized (this) {
                val user = users[session] ?:
                        throw IllegalStateException("Tycher can't be an unauthorized user.")
                val userScoreChange = mutableMapOf<User, Int>()
                val newTych = initTych(user, tych)
                val consumedTychs = consumedTychs(newTych)
                val gainedScore = consumedTychs.values.map { it to calculateScore(it) }.toMap()
                userScoreChange[user] = gainedScore.entries.sumBy { it.value }
                consumedTychs.forEach {
                    it.value.tycher.tychIsReady = true
                    userScoreChange[it.value.tycher] = -calculateScore(it.value)
                    tychs.remove(it.key)
                }
                sendTych(tych)
            }
            Command.DUMMY_TYCH.id -> synchronized (this) {
                val user = users[session] ?:
                        throw IllegalStateException("Tycher can't be an unauthorized user.")
                val newDummy = initTych(user, tych)
            }
        }
    }

    fun initTych(tycher: User, tych: Tych): Tych {
        val id = UUID.randomUUID().toString()
        val radius = tychRadius(tycher)
        val shrinkSpeed = shrinkSpeed(tycher)
        return Tych(tycher, tych.position, radius, shrinkSpeed, tych.spawnTime, tych.dummy)
    }

    fun sendTych(tych: Tych) {
        val adapter = moshi.adapter(Tych::class.java)
        val tychJson = adapter.toJson(tych)
        val receivers = users
                .filter { it.value != null }
                .filter { it.key.isOpen }
        receivers.forEach({ session, user ->
            session.remote.sendString(JSONObject().put("tych", tychJson).toString())
        })
    }

    val shrinkSpeed = { tycher: User -> tycher.score * SCORE_TO_SHRINK_SPEED }

    val tychRadius = { tycher: User -> tycher.score * SCORE_TO_RADIUS }

    val consumedTychs = { tych: Tych -> tychs.filter { it.value.isConsumedBy(tych) } }

    val calculateScore = { tych: Tych -> (tych.currentRadius() / SCORE_TO_RADIUS).toInt() }

    enum class Command(val id: String) {
        TYCH("tych"),
        DUMMY_TYCH("dummy_tych")
    }
}

