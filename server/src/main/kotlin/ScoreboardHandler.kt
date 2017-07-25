import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

class ScoreboardHandler : MessageHandler() {

    override fun handle(session: Session, message: JsonElement) {
        val scoreboardRequest = fromJson<Scoreboard>(message)
        val limit = scoreboardRequest.limit
        val usersScores = users.values
                .filterNotNull()
                .sortedByDescending { it.score }
                .take(limit)
                .filter { it.name != null }
                .map { it.name!! to it.score }
                .toMap()
        val scoreboardResponse = Scoreboard(limit, usersScores)
        session.remote.sendString(Gson().toJson(scoreboardResponse))
    }
}