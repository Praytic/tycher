import Command.*
import com.github.salomonbrys.kotson.keys
import com.github.salomonbrys.kotson.plus
import com.github.salomonbrys.kotson.put
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.eclipse.jetty.util.ConcurrentHashSet
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Serialization library.
 */
val gson = GsonBuilder()
        .registerTypeAdapter(TychRequest::class.java, tychRequestAdapter)
        .registerTypeAdapter(TychResponse::class.java, tychResponseAdapter)
        .create()

/**
 * Map of [Session]s to [User]s.
 */
val users: MutableMap<Session, User?> = ConcurrentHashMap()

val tychs: MutableSet<Tych> = ConcurrentHashSet()
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