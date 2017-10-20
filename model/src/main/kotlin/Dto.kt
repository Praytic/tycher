
/**
 * Defines entities which should be transported as a json message via websocket.
 */
abstract class Message(
    val command: Command)

/**
 * [Tych] entity which is transferred from frontend to backend.
 */
data class TychRequest(
    val position: Position,
    val spawnTime: Long) : Message(Command.TYCH)

/**
 * [Tych] entity which is transferred from backend to frontend.
 */
data class TychResponse(
    val position: Position,
    val radius: Double,
    val shrinkSpeed: Double) : Message(Command.TYCH) {
  constructor(tych: Tych) : this(tych.position,
                                 tych.getRadius(),
                                 tych.getScoreReductionPerMillis())
}

/**
 * [Scoreboard] entity which is transferred from frontend to backend.
 */
data class Scoreboard(
    val limit: Int) : Message(Command.SCOREBOARD) {

  val scores = { users: Collection<User> ->
    val scores = users.sortedByDescending { it.score }.take(limit).map {
      it.name to it.score
    }
    scores
  }
}

/**
 * [User] entity defines a single user.
 */
data class User(
    val name: String? = null,
    var score: Int = 100) {

  /**
   * User can click if he has no non-dummy [Tych]s active.
   */
  fun isClickable(tych: Tych?) = tych == null || tych.isDummy
}

/**
 * [Login] entity which is transferred from frontend to backend.
 */
data class Login(val username: String) : Message(Command.LOGIN)