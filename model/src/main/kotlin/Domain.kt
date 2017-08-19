import Command.*
import java.util.*

/**
 * Defines entities which should be transported as a json message via websocket.
 */
abstract class Message(
        val command: Command)

/**
 * [User] entity defines a single user.
 */
data class User(
        val name: String? = null,
        var score: Int = 100,
        var reloadSpeed: Double = 1.0,
        var tychIsReady: Boolean = true) {

    /**
     * User can click if he has no non-dummy [Tych]s active.
     */
    val clickable = { tych: Tych? -> tych == null || tych.isDummy }
}

/**
 * [Tych] entity which is transferred from frontend to backend.
 */
data class TychRequest(
        val position: Position,
        val spawnTime: Long) : Message(TYCH)

/**
 * [Tych] entity which is transferred from backend to frontend.
 */
data class TychResponse(
        val position: Position,
        val radius: Double,
        val shrinkSpeed: Double) : Message(TYCH) {
    constructor(tych: Tych): this(tych.position, tych.radius(), tych.shrinkSpeed())
}

/**
 * [Tych] entity defines the object appearing after user's click action.
 */
data class Tych(
        val tycher: User,
        val position: Position,
        val spawnTime: Long,
        val isDummy: Boolean = false) {

    companion object {
        val SCORE_TO_RADIUS = 1.0
        val SCORE_TO_SHRINK_SPEED = 0.01
    }

    constructor(tycher: User, tychRequest: TychRequest) :
            this(tycher, tychRequest.position, tychRequest.spawnTime)

    val lifeDuration = { (radius() / shrinkSpeed()).toLong() }
    val lifetime = { Date().getTime() - spawnTime }
    val currentRadius = { radius() - shrinkSpeed() * lifetime() }
    val isConsumedBy = { tych: Tych -> currentRadius() < tych.currentRadius() }
    val shrinkSpeed = { tycher.score * SCORE_TO_SHRINK_SPEED }
    val radius = { tycher.score * SCORE_TO_RADIUS }
    val consumedTychs = { tychs: Iterable<Tych> -> tychs.filter { it.isConsumedBy(this) } }
    val calculateScore = { (currentRadius() / SCORE_TO_RADIUS).toInt() }
}

/**
 * [Food] entity defines food unit.
 */
data class Food(
        val position: Position,
        val initialRadius: Double) : Message(FOOD)

data class Position(
        val x: Double,
        val y: Double)

data class Scoreboard(
        val limit: Int,
        val scores: Map<String, Int> = mapOf()) : Message(SCOREBOARD)

data class Login(val username: String) : Message(LOGIN)