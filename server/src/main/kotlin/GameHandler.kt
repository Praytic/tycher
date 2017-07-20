package main.kotlin

import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.json.JSONArray
import org.json.JSONObject

@WebSocket
class GameHandler {

    @OnWebSocketConnect
    fun onConnect(session: Session) {
        users.put(session, null)
    }

    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val messageJson = JSONObject(message)
        val handlerName = messageJson.keys().next() ?:
                throw IllegalArgumentException("No handler name was provided by message $message.")
        val params = messageJson.getJSONObject(handlerName)
        when (handlerName) {
            "greetings" -> login(session, params.getString("username"))
            "scoreboard" -> scoreboard(session, params.getInt("limit"))
        }
    }

    @OnWebSocketClose
    fun onClose(session: Session, statusCode: Int, reason: String) {
        users.remove(session)
    }

    fun login(session: Session, username: String) {
        users[session] = User(username)
    }

    fun scoreboard(session: Session, limit: Int) {
        val users = users.values
                .filterNotNull()
                .sortedByDescending { it.score }
                .take(limit)
                .map { JSONObject()
                        .put("username", it.name)
                        .put("score", it.score).toString() }
        session.remote.sendString(JSONObject().put("scoreboard", JSONArray(users)).toString())
    }
}
