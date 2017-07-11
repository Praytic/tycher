import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.json.JSONObject
import java.util.*

@WebSocket
class BoardHandler {

    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val messageJson = JSONObject(message)
        val handlerName = messageJson.keys().next() ?:
                throw IllegalArgumentException("No handler name was provided by message $message.")
        val adapter = moshi.adapter(Tych::class.java)
        val tych = adapter.fromJson(messageJson.getJSONObject(handlerName).toString()) ?:
                throw IllegalArgumentException("Unable to parse tych object from message $message.")
        when (handlerName) {
            "tych" -> synchronized (this) {
                val user = users[session] ?:
                        throw IllegalStateException("Tycher can't be an unauthorized user.")
                val userScoreChange = mutableMapOf<User, Int>()
                val newTych = initTych(user, tych)
                val consumedTychs = findConsumedTychs(newTych)
                val gainedScore = consumedTychs.values.map { it to calculateScore(it) }.toMap()
                userScoreChange[user] = gainedScore.entries.sumBy { it.value }
                consumedTychs.forEach {
                    it.value.tycher.tychIsReady = true
                    userScoreChange[it.value.tycher] = -calculateScore(it.value)
                    tychs.remove(it.key)
                }
            }
            "dummy_tych" -> TODO()
        }
    }

    fun initTych(tycher: User, tych: Tych): Tych {
        val id = UUID.randomUUID().toString()
        val radius = calculateTychRadius(tycher)
        val shrinkSpeed = calculateShrinkSpeed(tycher)
        return Tych(tycher, tych.position, radius, shrinkSpeed, tych.spawnTime, tych.dummy)
    }

    fun sendTych(tych: Tych) {
        val receivers = users
                .filter { it.value != null }
                .filter { it.key.isOpen }
        receivers.forEach({ session, user ->

        })
    }

    fun calculateTychRadius(tycher: User): Double {
        TODO()
    }

    fun calculateShrinkSpeed(tycher: User): Double {
        TODO()
    }

    fun findConsumedTychs(tych: Tych): Map<String, Tych> {
        return tychs.filter { it.value.isConsumedBy(tych) }
    }

    fun calculateScore(tych: Tych): Int {
        TODO()
    }
}

