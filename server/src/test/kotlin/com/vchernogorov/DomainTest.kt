package com.vchernogorov

import com.vchernogorov.GameConf.SECOND_TO_MILLIS
import com.vchernogorov.PlayerConf.START_SCORE
import com.vchernogorov.TychConf.SCORE_TO_RADIUS_RATE
import org.junit.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [Tych] unit tests.
 */
class TychTest {

  val defaultTimePast = 1

  /**
   * [Tych.getCurrentRadius], [Tych.isConsumedBy]
   */
  @Test
  fun test1() {
    val now = Date()
    val tych1 = Tych(tycher = User(score = START_SCORE), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = START_SCORE - 1), spawnTime = now.time)

    assertTrue(tych2.getCurrentRadius(now) < tych1.getCurrentRadius(now),
               "Initial radius of $tych1 should be greater than $tych2 " +
                   "radius because $tych1 has greater score and they both were created at " +
                   "the same time.")
    assertTrue(tych2.isConsumedBy(tych1),
               "$tych2 should be consumed by $tych1 because $tych1 has" +
                   "greater current radius and they both were created at the same " +
                   "position.")
  }

  /**
   * [Tych.getCurrentRadius], [Tych.isConsumedBy]
   */
  @Test
  fun test2() {
    val now = Date()
    val future = Date(now.time + 1)
    val tych1 = Tych(tycher = User(score = START_SCORE), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = START_SCORE - 1), spawnTime = now.time)

    val actualCurrentRadius1 = tych1.getCurrentRadius(future)
    val actualCurrentRadius2 = tych2.getCurrentRadius(future)

    assertTrue(actualCurrentRadius2 < actualCurrentRadius1,
               "Although current radius of both com.vchernogorov.getTychs have changed, they " +
                   "were created at the same time and it doesn't matter what was " +
                   "the initial score, $tych1 always should be greater than $tych2.")
    assertTrue(tych2.isConsumedBy(tych1),
               "$tych2 should be consumed by $tych1 because $tych1 has" +
                   "greater current radius and they both were created at the same " +
                   "position.")
  }

  /**
   * [Tych.getCurrentRadius], [Tych.isConsumedBy]
   */
  @Test
  fun test3() {
    val now = Date()
    val tych1 = Tych(tycher = User(score = START_SCORE), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = START_SCORE), spawnTime = now.time)

    assertTrue(tych2.getCurrentRadius(now) == tych1.getCurrentRadius(now),
               "Initial radius of $tych1 should be greater than $tych2's " +
                   "radius because $tych1 has greater score and they both were created at " +
                   "the same time.")
    assertFalse(tych2.isConsumedBy(tych1),
               "$tych2 should be consumed by $tych1 because $tych1 has " +
                   "greater current radius and they both were created at the same " +
                   "position.")
  }

  /**
   * [Tych.getScoreReductionPerMillis]
   */
  @Test
  fun test4() {
    val now = Date()
    val future = Date(now.time + defaultTimePast * SECOND_TO_MILLIS.toLong())
    val tych1 = Tych(tycher = User(score = START_SCORE), spawnTime = now.time)

    val actualCurrentRadius1 = tych1.getCurrentRadius(future)

    assertTrue(actualCurrentRadius1 < tych1.getCurrentRadius(now),
        "Initial radius should be reduced after time.")
  }

  /**
   * [Tych.isConsumedBy]
   */
  @Test
  fun test5() {
    val now = Date()
    val tych1 = Tych(position = Position(1.0, 1.0),
            tycher = User(score = START_SCORE),
            spawnTime = now.time)
    val tych2 = Tych(position = Position(0.0, 0.0),
            tycher = User(score = START_SCORE - 1),
            spawnTime = now.time)

    assertTrue(!tych2.isConsumedBy(tych1),
               "Although $tych1 has greater radius than $tych2, it" +
                   "cannot consume $tych2 because its position is different, so" +
                   "$tych1's circle doesn't fully contain $tych2's circle.")
  }

  /**
   * [Tych.isConsumedBy]
   */
  @Test
  fun test6() {
    val now = Date()
    val tych1 = Tych(position = Position(1.0, 1.0),
            tycher = User(score = START_SCORE),
            spawnTime = now.time)
    val tych2 = Tych(position = Position(0.0, 0.0),
            tycher = User(score = (START_SCORE - 2 / SCORE_TO_RADIUS_RATE).toInt()),
            spawnTime = now.time)

    assertTrue(tych2.isConsumedBy(tych1),
               "Although $tych1 and $tych2 have different position," +
                   "$tych1's circle is big enough to fully contain $tych2's circle.")
  }

  /**
   * [Tych.getAllConsumedTychs]
   */
  @Test
  fun test7() {
    val now = Date()
    val tych1 = Tych(position = Position(0.0, 0.0),
        tycher = User(score = START_SCORE),
        spawnTime = now.time)
    val tych2 = Tych(position = Position(0.0, 0.0),
        tycher = User(score = START_SCORE - 1),
        spawnTime = now.time)
    
    assertTrue(tych2.getAllConsumedTychs(listOf(tych1, tych2)).isEmpty(),
        "$tych2 has the lowest score so it should consume nothing.")
  }
}