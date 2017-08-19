import Command.*
import adapter.tychRequestAdapter
import adapter.tychResponseAdapter
import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import handler.LoginHandler
import handler.ScoreboardHandler
import handler.TychHandler
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import websocket.MainWebSocket
import java.util.concurrent.ConcurrentHashMap

/**
 * Serialization library.
 */
val gson = GsonBuilder()
        .registerTypeAdapter(TychRequest::class.java, tychRequestAdapter)
        .registerTypeAdapter(TychResponse::class.java, tychResponseAdapter)
        .create()

/**
 * Map of [Session]s to [User]s. If the [User] is not presented in this map
 * then he is treated as unauthorized user.
 */
val users: MutableMap<Session, User?> = ConcurrentHashMap()

/**
 * Map of [User]s to [Tych]s. Only one [Tych] is permitted per user. [User]
 * cannot make another [Tych] if he is presented as a key in this map.
 */
val tychs: MutableMap<User, Tych> = ConcurrentHashMap()

/**
 * Map of [Command]s to [MessageHandler]s. Each command should have a
 * [MessageHandler].
 */
val commandHandlerMapper = mapOf(
        TYCH to TychHandler(),
        DUMMY_TYCH to TychHandler(),
        SCOREBOARD to ScoreboardHandler(),
        LOGIN to LoginHandler()
)

fun main(args: Array<String>) {
    staticFiles.location("/public")
    webSocket("/game", MainWebSocket::class.java)
    init()
}

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T): String {
    val commandPair = Pair(src.command.toString(), gson.toJsonTree(src, T::class.java))
    val jsonCommand = JsonObject().plus(commandPair)
    return gson.toJson(jsonCommand)
}