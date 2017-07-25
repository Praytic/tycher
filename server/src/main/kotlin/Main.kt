import com.google.gson.Gson
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import java.util.concurrent.ConcurrentHashMap

val SCORE_TO_RADIUS = 1.0
val SCORE_TO_SHRINK_SPEED = 0.1

val users: MutableMap<Session, User?> = ConcurrentHashMap()
val tychs: MutableMap<String, Tych> = ConcurrentHashMap()

fun main(args: Array<String>) {
    staticFiles.location("/public")
    webSocket("/game", MainWebSocket::class.java)
    init()
}