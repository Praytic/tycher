import java.util.*

/**
 * [User] entity defines a single user.
 */
data class User(val name: String? = null,
                var score: Int = 0,
                var reloadSpeed: Double = 1.0,
                var tychIsReady: Boolean = true)

/**
 * [Tych] entity which is transferred from frontend to backend.
 */
data class TychRequest(val position: Position,
                       val spawnTime: Long)

/**
 * [Tych] entity which is transferred from backend to frontend.
 */
data class TychResponse(val position: Position,
                        val initialRadius: Double,
                        val shrinkSpeed: Double) {
    constructor(tych: Tych): this(tych.position, tych.initialRadius, tych.shrinkSpeed)
}

/**
 * [Tych] entity defines the object appearing after user's click action.
 */
data class Tych(val tycher: User,
                val position: Position,
                val spawnTime: Long,
                val initialRadius: Double = 1.0,
                val shrinkSpeed: Double = 1.0,
                val dummy: Boolean = false) {

    val currentRadius = {
        val currentTime = Date().getTime()
        initialRadius - shrinkSpeed * (currentTime - spawnTime)
    }

    val isConsumedBy = { tych: Tych ->
        currentRadius() < tych.currentRadius()
    }
}

/**
 * [Food] entity defines food unit.
 */
data class Food(val position: Position,
                val initialRadius: Double)

data class Position(val x: Double, val y: Double)

data class Scoreboard(val limit: Int, val scores: Map<String, Int> = mapOf())