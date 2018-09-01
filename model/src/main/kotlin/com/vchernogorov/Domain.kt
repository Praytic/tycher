package com.vchernogorov

import com.vchernogorov.TychConf.DYNAMIC_LIFETIME_MS
import com.vchernogorov.TychConf.DYNAMIC_SCORE_TO_RADIUS_RATE
import com.vchernogorov.TychConf.LIFETIME_MS
import com.vchernogorov.TychConf.SCORE_TO_RADIUS_RATE
import com.vchernogorov.TychConf.USE_STATIC_LIFETIME
import com.vchernogorov.TychConf.USE_STATIC_RADIUS_RATE
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
   * Returns current radius of the [Tych].
   * Formula: max(radius - (score / ms) * ms / (score / radius))
   */
  override fun getCurrentRadius(now: Date) =
      Math.max(getRadius() - getScoreReductionPerMs() * getLifetimeMs(now) / getScoreToRadiusRate(), 0.0)

  /**
   * Returns score reduction rate for the [Tych] per millisecond.
   * Formula: score / ms
   */
  fun getScoreReductionPerMs() = score.toDouble() / getLifetimeMs()

  /**
   * Formula: ms
   */
  fun getLifetimeMs() = if (USE_STATIC_LIFETIME) LIFETIME_MS else DYNAMIC_LIFETIME_MS(score)

  /**
   * Returns radius reduction rate for the [Tych] per millisecond.
   * Formula: (score / ms) / (score / radius) = radius / ms
   */
  fun getRadiusReductionPerMs() = getScoreReductionPerMs() / getScoreToRadiusRate()

  /**
   * Returns initial [Tych] radius.
   * Formula: score / (score / radius) = radius
   */
  fun getRadius() = score / getScoreToRadiusRate()

  /**
   * Returns a current score of the [Circle] depending on [currentRadius].
   * Formula: radius * (score / radius) = score
   */
  fun getCurrentScore(now: Date = Date()) = (getCurrentRadius(now) * getScoreToRadiusRate()).toInt()

  /**
   * Formula: score / radius
   */
  fun getScoreToRadiusRate() = if (USE_STATIC_RADIUS_RATE) SCORE_TO_RADIUS_RATE else DYNAMIC_SCORE_TO_RADIUS_RATE(score)

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
  fun getLifetimeMs(now: Date = Date()) = now.time - spawnTime

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

    return distance < r1 - r2
  }

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