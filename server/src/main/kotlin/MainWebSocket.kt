import com.github.salomonbrys.kotson.keys
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket

@WebSocket
class MainWebSocket {

    @OnWebSocketConnect
    fun onConnect(session: Session) {
        users.put(session, User())
    }

    /**
     * Handles messages sent via WebSocket. Delegates [message] handling
     * to the appropriate [MessageHandler].
     */
    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val jsonMessage = Gson().fromJson(message, JsonObject::class.java)
        val rawCommand = jsonMessage.keys().single()
        val command = Command.valueOf(rawCommand.toUpperCase())
        command.handler.handle(session, jsonMessage[rawCommand])
    }

    @OnWebSocketClose
    fun onClose(session: Session, statusCode: Int, reason: String) {
        users.remove(session)
    }
}

