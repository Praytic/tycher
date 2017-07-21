import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import java.util.concurrent.ConcurrentHashMap

val SCORE_TO_RADIUS = 1.0
val SCORE_TO_SHRINK_SPEED = 0.1

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
val users: MutableMap<Session, User?> = ConcurrentHashMap()
val tychs: MutableMap<String, Tych> = ConcurrentHashMap()

fun main(args: Array<String>) {
    staticFiles.location("/public")
    webSocket("/game", GameHandler::javaClass)
    webSocket("/board", BoardHandler::javaClass)
    init()
}