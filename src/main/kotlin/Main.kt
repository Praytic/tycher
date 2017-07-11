import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import java.awt.Point
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
val users: MutableMap<Session, User?> = ConcurrentHashMap()
val tychs: MutableMap<String, Tych> = ConcurrentHashMap()

fun main(args: Array<String>) {
    staticFiles.location("/public")
    webSocket("/game", GameHandler::javaClass)
    webSocket("/board", BoardHandler::javaClass)
    init()
}


data class User(val name: String,
                var score: Int = 0,
                var reloadSpeed: Double = 1.0,
                var tychIsReady: Boolean = true)

data class Tych(val tycher: User,
                val position: Point,
                val initialRadius: Double,
                val shrinkSpeed: Double = 1.0,
                val spawnTime: Long,
                val dummy: Boolean = false) {

    fun currentRadius(): Double {
        // NOTE: not sure how spawnTime is determined, but Date().getTime() is milliseconds since Unix epoch
        val currentTime = Date().getTime()
        return initialRadius - shrinkSpeed * (currentTime - spawnTime)
    }

    fun isConsumedBy(tych: Tych): Boolean {
        return !tych.dummy && currentRadius() < tych.currentRadius()
    }
}

data class Food(val position: Point,
                val initialRadius: Double)