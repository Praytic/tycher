import spark.Session
import spark.Spark
import java.awt.Point

data class User(val name: String,
                var score: Int = 0,
                var reloadSpeed: Double = 1.0)
data class Tych(val tycherName: String,
                val position: Point,
                val initialRadius: Double,
                val shrinkSpeed: Double = 1.0,
                val spawnTime: Long,
                val dummy: Boolean = false)
data class ScoreBoard(val scores: Map<String, Int>,
                      val limit: Int = 10)
data class Food(val position: Point,
                val initialRadius: Double)

fun main(args: Array<String>) {
    Spark.staticFiles.location("/public") //index.html is served at localhost:4567 (default port)
    Spark.staticFiles.expireTime(600)
    Spark.webSocket("/chat", ChatWebSocketHandler::class.java)
    Spark.init()
}
