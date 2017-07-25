import Command.TYCH
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

class TychHandler : MessageHandler() {

    override fun handle(session: Session, message: JsonElement) {
        val user = users[session] ?:
                throw IllegalStateException("Tycher can't be an unauthorized user.")
        val userScoreChange = mutableMapOf<User, Int>()
        val tychRequest = fromJson<TychRequest>(message)
        val tych = toTych(user, tychRequest)
        val consumedTychs = consumedTychs(tych)
        val gainedScore = consumedTychs.values.map { it to calculateScore(it) }.toMap()
        userScoreChange[user] = gainedScore.entries.sumBy { it.value }
        consumedTychs.forEach {
            it.value.tycher.tychIsReady = true
            userScoreChange[it.value.tycher] = -calculateScore(it.value)
            tychs.remove(it.key)
        }
        sendTych(tych)
    }

    fun toTych(tycher: User, tychRequest: TychRequest): Tych {
        val radius = tychRadius(tycher)
        val shrinkSpeed = shrinkSpeed(tycher)
        val spawnTime = tychRequest.spawnTime
        val position = tychRequest.position
        return Tych(tycher, position, spawnTime, radius, shrinkSpeed, false)
    }

    fun sendTych(tych: Tych) {
        val tychResponse = Gson().toJson(TychResponse(tych))
        val receivers = users
                .filter { it.value != null }
                .filter { it.key.isOpen }
        receivers.forEach({ session, user ->
            session.remote.sendString(JSONObject().put(TYCH.toString(), tychResponse).toString())
        })
    }

    val shrinkSpeed = { tycher: User -> tycher.score * SCORE_TO_SHRINK_SPEED }

    val tychRadius = { tycher: User -> tycher.score * SCORE_TO_RADIUS }

    val consumedTychs = { tych: Tych -> tychs.filter { it.value.isConsumedBy(tych) } }

    val calculateScore = { tych: Tych -> (tych.currentRadius() / SCORE_TO_RADIUS).toInt() }
}
