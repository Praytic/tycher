import com.github.salomonbrys.kotson.keys
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket

/**
 * [MainWebSocket] manages websocket connection and messages
 * which are passed through it.
 *
 * The message is parsed and mapped to the specific [MessageHandler]
 * via the [Command] name.
 */
@WebSocket
class MainWebSocket {

    /**
     * Adds unauthorized [User] to the [users] map when
     * the connection is opened.
     */
    @OnWebSocketConnect
    fun onConnect(session: Session) {
        users.put(session, User())
    }

    /**
     * Handles messages sent via websocket. Delegates [message] handling
     * to the appropriate [MessageHandler].
     */
    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val jsonMessage = gson.fromJson(message, JsonObject::class.java)
        val rawCommand = jsonMessage.keys().single()
        val command = Command.valueOf(rawCommand.toUpperCase())
        commandHandlerMapper[command]?.handle(session, jsonMessage[rawCommand]) ?:
                throw IllegalArgumentException("No handler was found for the command:\n$command")
    }

    /**
     * Removes [User] from the [users] map when the connection
     * is closed.
     */
    @OnWebSocketClose
    fun onClose(session: Session, statusCode: Int, reason: String) {
        users.remove(session)
    }
}

