import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketException

class TychHandler : MessageHandler<TychRequest>() {

    override fun parse(message: JsonElement) = gson.fromJson(message, TychRequest::class.java)

    override fun handle(user: User, session: Session, message: TychRequest) {
        val tych = toTych(user, message)
        consumeTychs(tych)
        tychs.add(tych)
        sendTych(tych)
    }

    fun consumeTychs(tych: Tych) {
        val userScoreChange = mutableMapOf<User, Int>()
        val consumedTychs = tych.consumedTychs(tychs)
        val gainedScore = consumedTychs.map { it to it.calculateScore() }.toMap()
        userScoreChange[tych.tycher] = gainedScore.entries.sumBy { it.value }
        consumedTychs.forEach {
            it.tycher.tychIsReady = true
            userScoreChange[it.tycher] = -it.calculateScore()
            tychs.remove(it)
        }
    }

    fun toTych(tycher: User, tychRequest: TychRequest): Tych {
        val spawnTime = tychRequest.spawnTime
        val position = tychRequest.position
        return Tych(tycher, position, spawnTime, dummy = false)
    }

    fun sendTych(tych: Tych) {
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
