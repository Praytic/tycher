import Tych.Companion.SCORE_TO_RADIUS
import java.util.*

/**
 * [User] entity defines a single user.
 */
class User(
    val name: String? = null,
    var score: Int = 100) {

  /**
   * User can click if he has no non-dummy [Tych]s active.
   */
  fun isClickable(tych: Tych?) = tych == null || tych.isDummy
}

/**
 * [Tych] entity defines the object appearing after user's click action.
 */
class Tych(
    position: Position = Position(),
    spawnTime: Long = Date().time,
    val tycher: User = User(),
    val isDummy: Boolean = false) : Circle(position, spawnTime) {

  companion object {
    val SCORE_TO_RADIUS = 1.0
    val SCORE_TO_SHRINK_SPEED = 0.01
  }

  constructor(tycher: User, tychRequest: TychRequest) :
      this(tychRequest.position, tychRequest.spawnTime, tycher)

  /**
   * Returns how much milliseconds left for this [Tych] to be removed.
   */
  fun getLifeDurationMillis(now: Date = Date()) =
      (getCurrentRadius(now) / getShrinkSpeedRadius()).toLong()

  /**
   * Returns current radius of the [Tych].
   */
  override fun getCurrentRadius(now: Date) =
      Math.max(getRadius() - getShrinkSpeedScore() * getLifetimeMillis(now), 0.0)

  /**
   * Returns true if current [Tych] was consumed by other [Tych].
   */
  fun isConsumedBy(tych: Tych, now: Date = Date()): Boolean {
    return position.x + getCurrentRadius(now) < tych.position.x + tych.getCurrentRadius(now) &&
        position.y + getCurrentRadius(now) < tych.position.y + tych.getCurrentRadius(now) &&
        position.x - getCurrentRadius(now) > tych.position.x - tych.getCurrentRadius(now) &&
        position.y - getCurrentRadius(now) > tych.position.y - tych.getCurrentRadius(now)
  }

  /**
   * Returns score reduction rate for the [Tych] per millisecond.
   */
  fun getShrinkSpeedScore() = tycher.score * SCORE_TO_SHRINK_SPEED

  /**
   * Returns radius reduction rate for the [Tych] per millisecond.
   */
  fun getShrinkSpeedRadius() = getShrinkSpeedScore() * SCORE_TO_RADIUS

  /**
   * Returns initial [Tych] radius.
   */
  fun getRadius() = tycher.score * SCORE_TO_RADIUS

  /**
   * Returns a list of [Tych]s which are [isConsumedBy] the current [Tych].
   */
  fun getAllConsumedTychs(tychs: Iterable<Tych>) = tychs.filter {
    it.isConsumedBy(this)
  }
}

/**
 * Abstract entity for all circle-like entities.
 */
abstract class Circle(
    val position: Position = Position(),
    val spawnTime: Long = Date().time) {

  /**
   * Returns how much milliseconds ago this [Tych] was created.
   */
  fun getLifetimeMillis(now: Date = Date()) = now.time - spawnTime

  /**
   * Returns current radius of the [Circle].
   */
  abstract fun getCurrentRadius(now: Date = Date()): Double

  /**
   * Returns true if current [Tych] was consumed by other [Tych].
   */
  fun isConsumedBy(circle: Circle, now: Date = Date()): Boolean {
    return position.x + getCurrentRadius(now) < circle.position.x + circle.getCurrentRadius(now) &&
        position.y + getCurrentRadius(now) < circle.position.y + circle.getCurrentRadius(now) &&
        position.x - getCurrentRadius(now) > circle.position.x - circle.getCurrentRadius(now) &&
        position.y - getCurrentRadius(now) > circle.position.y - circle.getCurrentRadius(now)
  }

  /**
   * Returns a current score of the [Circle] depending on [currentRadius].
   */
  fun calculateScore(now: Date = Date()) = (getCurrentRadius(now) / SCORE_TO_RADIUS).toInt()
}

/**
 * [Food] entity defines food unit.
 */
class Food(
    val initialRadius: Double = 0.0,
    spawnTime: Long = Date().time,
    position: Position = Position()) : Circle(position, spawnTime) {

  override fun getCurrentRadius(now: Date) = initialRadius
}

class Position(
    val x: Double = 0.0,
    val y: Double = 0.0)