package com.vchernogorov

import com.vchernogorov.GameConf.SECOND_TO_MILLIS
import com.vchernogorov.TychConf.SCORE_TO_RADIUS_RATE
import com.vchernogorov.TychConf.SCORE_TO_SHRINK_SPEED_RATE
import com.vchernogorov.TychConf.STATIC_SHRINK_SPEED
import com.vchernogorov.TychConf.USE_STATIC_SHRINK_SPEED
import java.util.*

/**
 * [Tych] entity defines the object appearing after user's click action.
 */
class Tych(
    position: Position = Position(),
    spawnTime: Long = Date().time,
    val tycher: User = User(),
    val isDummy: Boolean = false,
    val score: Int = tycher.score) : Circle(position, spawnTime) {

  /**
   * Returns how much milliseconds left for this [Tych] to be removed.
   */
  fun getLifeDurationMillis(now: Date = Date()) =
      (getCurrentRadius(now) / getShrinkSpeedRadius() * SECOND_TO_MILLIS).toLong()

  /**
   * Returns current radius of the [Tych].
   */
  override fun getCurrentRadius(now: Date) =
      Math.max(getRadius() - getScoreReductionPerMillis() * getLifetimeMillis(now) / SECOND_TO_MILLIS, 0.0)

  /**
   * Returns score reduction rate for the [Tych] per millisecond.
   */
  fun getScoreReductionPerMillis() = if (USE_STATIC_SHRINK_SPEED) STATIC_SHRINK_SPEED else
    score * SCORE_TO_SHRINK_SPEED_RATE

  /**
   * Returns radius reduction rate for the [Tych] per millisecond.
   */
  fun getShrinkSpeedRadius() = getScoreReductionPerMillis() * SCORE_TO_RADIUS_RATE

  /**
   * Returns initial [Tych] radius.
   */
  fun getRadius() = score * SCORE_TO_RADIUS_RATE

  /**
   * Returns a list of [Tych]s which are [isConsumedBy] the current [Tych].
   */
  fun getAllConsumedTychs(tychs: Iterable<Tych>) = tychs.filter {
    it.isConsumedBy(this)
  }

  override fun toString(): String {
    return "Tych(tycher=$tycher, isDummy=$isDummy, circle=${super.toString()}, score=$score)"
  }
}

/**
 * Abstract entity for all circle-like entities.
 */
abstract class Circle(
    val position: Position = Position(),
    val spawnTime: Long = Date().time) : Message(Command.TYCHS) {

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
    val x1 = circle.position.x
    val x2 = this.position.x
    val y1 = circle.position.y
    val y2 = this.position.y
    val r1 = circle.getCurrentRadius(now)
    val r2 = this.getCurrentRadius(now)

    val distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))

    if (distance > (r1 + r2))
      return false
    else
      return distance < Math.abs(r1 - r2)
  }

  /**
   * Returns a current score of the [Circle] depending on [currentRadius].
   */
  fun calculateScore(now: Date = Date()) = (getCurrentRadius(now) / SCORE_TO_RADIUS_RATE).toInt()

  override fun toString(): String {
    return "com.vchernogorov.Circle(position=$position, spawnTime=$spawnTime)"
  }
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

data class Position(
    val x: Double = 0.0,
    val y: Double = 0.0)