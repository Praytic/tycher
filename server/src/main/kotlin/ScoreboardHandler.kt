import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session

class ScoreboardHandler : MessageHandler<Scoreboard>() {

    override fun parse(message: JsonElement) = gson.fromJson(message, Scoreboard::class.java)

    override fun handle(user: User, session: Session, message: Scoreboard) {
        val limit = message.limit
        val usersScores = users.values
                .filterNotNull()
                .sortedByDescending { it.score }
                .take(limit)
                .filter { it.name != null }
                .map { it.name!! to it.score }
                .toMap()
        val scoreboardResponse = Scoreboard(limit, usersScores)
        session.remote.sendString(gson.toJson(scoreboardResponse))
    }
}