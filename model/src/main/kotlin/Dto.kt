
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
                                 tych.getShrinkSpeedScore())
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
 * [Login] entity which is transferred from frontend to backend.
 */
data class Login(val username: String) : Message(Command.LOGIN)