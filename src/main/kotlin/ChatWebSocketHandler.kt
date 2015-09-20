import j2html.TagCreator
import org.eclipse.jetty.websocket.api.*
import org.eclipse.jetty.websocket.api.annotations.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@WebSocket
class ChatWebSocketHandler {

    var nextUserNumber = 1
    val userUsernameMap: MutableMap<Session, String> = ConcurrentHashMap()

    @OnWebSocketConnect
    fun onConnect(user: Session) {
        val username = "User" + nextUserNumber++
        userUsernameMap.put(user, username)
        broadcastMessage(sender = "Server", message = "$username joined the chat")
    }

    @OnWebSocketClose
    fun onClose(user: Session, statusCode: Int, reason: String) {
        val username = userUsernameMap.get(user)
        userUsernameMap.remove(user)
        broadcastMessage(sender = "Server", message = "$username left the chat")
    }

    @OnWebSocketMessage
    fun onMessage(user: Session, message: String) {
        broadcastMessage(sender = userUsernameMap.get(user) ?: "undefiend", message = message)
    }

    //Sends a message from one user to all users, along with a list of current usernames
    fun broadcastMessage(sender: String, message: String) {
        userUsernameMap.keys.stream().filter({ it.isOpen() }).forEach { session ->
            try {
                session.remote.sendString(JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values).toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    fun createHtmlMessageFromSender(sender: String, message: String): String {
        return TagCreator.article(
                TagCreator.b("$sender says:"),
                TagCreator.span(TagCreator.attrs(".timestamp"), SimpleDateFormat("HH:mm:ss").format(Date())),
                TagCreator.p(message)
        ).render()
    }

}
