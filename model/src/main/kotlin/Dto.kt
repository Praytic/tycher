import java.awt.Point
import java.util.*

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
        val currentTime = Date().getTime()
        return initialRadius - shrinkSpeed * (currentTime - spawnTime)
    }

    fun isConsumedBy(tych: Tych): Boolean {
        return currentRadius() < tych.currentRadius()
    }
}
data class Food(val position: Point,
                val initialRadius: Double)
data class Position(val x: Double, val y: Double)
data class UserDto(val username: String, val score: Int)