import j2html.TagCreator
import org.eclipse.jetty.websocket.api.*
import org.eclipse.jetty.websocket.api.annotations.*
import org.json.JSONArray
import org.json.JSONObject
import java.awt.event.KeyEvent
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

data class Vector2D(var x: Int, var y: Int)

@WebSocket
class ChatWebSocketHandler {

    var nextUserNumber = 1
    val userUsernameMap: MutableMap<Session, String> = ConcurrentHashMap()
    var userPositions: MutableMap<Session, Vector2D> = ConcurrentHashMap()

    @OnWebSocketConnect
    fun onConnect(user: Session) {
        val username = "User" + nextUserNumber++
        userUsernameMap.put(user, username)
        broadcastMessage(sender = "Server", message = "$username joined the chat")
        userPositions.put(user, Vector2D(50, 50))
    }

    @OnWebSocketClose
    fun onClose(user: Session, statusCode: Int, reason: String) {
        val username = userUsernameMap.get(user)
        userUsernameMap.remove(user)
        broadcastMessage(sender = "Server", message = "$username left the chat")
    }

    @OnWebSocketMessage
    fun onMessage(user: Session, message: String) {
        try {
            // Update position based on input
            var inputs = JSONObject(message)
            val keys = inputs.getJSONArray("keys")
            val LEFT = keys[KeyEvent.VK_A] == 1
            val RIGHT = keys[KeyEvent.VK_D] == 1
            val UP = keys[KeyEvent.VK_W] == 1
            val DOWN = keys[KeyEvent.VK_S] == 1
            var x = userPositions[user]!!.x
            var y = userPositions[user]!!.y

            if (LEFT && !RIGHT) {
                x--
            } else if (!LEFT && RIGHT) {
                x++
            } else if (UP && !DOWN) {
                y--
            } else if (!UP && DOWN) {
                y++
            }
            userPositions[user]!!.x = x
            userPositions[user]!!.y = y
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val positions = mutableListOf<Int>()
        for (entry in userPositions) {
            positions.add(entry.value.x)
            positions.add(entry.value.y)
        }

        // Send data to user
        if (user.isOpen) {
            try {
                user.remote.sendString(JSONObject().put("positions", JSONArray(positions)).toString())
            } catch (e: Exception) {
                user.close()
//                e.printStackTrace();
            }
        }
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
