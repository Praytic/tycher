@file:JvmName("Main")
import Command.*
import adapter.scoreboardRequestAdapter
import adapter.tychRequestAdapter
import adapter.tychResponseAdapter
import com.google.gson.GsonBuilder
import handler.LoginHandler
import handler.MessageHandler
import handler.ScoreboardHandler
import handler.TychHandler
import mu.KotlinLogging
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import websocket.MainWebSocket
import java.util.concurrent.ConcurrentHashMap

val log = KotlinLogging.logger {}

/**
 * Serialization library.
 */
val gson = GsonBuilder()
    .registerTypeAdapter(TychRequest::class.java, tychRequestAdapter)
    .registerTypeAdapter(TychResponse::class.java, tychResponseAdapter)
    .registerTypeAdapter(Scoreboard::class.java, scoreboardRequestAdapter)
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
  println(Tych().getDefaults())
  webSocket("/game", MainWebSocket::class.java)
  init()
}