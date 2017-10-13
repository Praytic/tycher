import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * [Tych] unit tests.
 */
class TychTest {

  val defaultScore = 100
  val defaultTimePast = 1

  /**
   * [Tych.getCurrentRadius], [Tych.isConsumedBy]
   */
  @Test
  fun test1() {
    val now = Date()
    val tych1 = Tych(tycher = User(score = defaultScore), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = defaultScore - 1), spawnTime = now.time)

    assertTrue(tych2.getCurrentRadius(now) < tych1.getCurrentRadius(now),
               "Initial radius of $tych1 should be greater than $tych2 " +
                   "radius because $tych1 has greater score and they both were created at " +
                   "the same time.")
    assertTrue(tych2.isConsumedBy(tych1, Date()),
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
    val tych1 = Tych(tycher = User(score = defaultScore), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = defaultScore - 1), spawnTime = now.time)

    val actualCurrentRadius1 = tych1.getCurrentRadius(future)
    val actualCurrentRadius2 = tych2.getCurrentRadius(future)

    assertTrue(actualCurrentRadius2 < actualCurrentRadius1,
               "Although current radius of both tychs have changed, they " +
                   "were created at the same time and it doesn't matter what was " +
                   "the initial score, $tych1 always should be greater than $tych2.")
    assertTrue(tych2.isConsumedBy(tych1, Date()),
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
    val tych1 = Tych(tycher = User(score = defaultScore), spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = defaultScore), spawnTime = now.time)

    assertTrue(tych2.getCurrentRadius(now) == tych1.getCurrentRadius(now),
               "Initial radius of tych1 should be greater than tych2's " +
                   "radius because tych1 has greater score and they both were created at " +
                   "the same time.")
    assertTrue(!tych2.isConsumedBy(tych1, Date()),
               "Tych2 should be consumed by tych1 because tych1 has " +
                   "greater current radius and they both were created at the same " +
                   "position.")

  }

  /**
   * [Tych.getScoreReductionPerMillis]
   */
  @Test
  fun test4() {
    val now = Date()
    val future = Date(now.time + defaultTimePast)
    val tych1 = Tych(tycher = User(score = defaultScore), spawnTime = now.time)

    val actualCurrentRadius1 = tych1.getCurrentRadius(future)

    assertEquals(tych1.tycher.score - tych1.getScoreReductionPerMillis(),
                 actualCurrentRadius1,
                 "Initial radius should be reduced by ${tych1.getScoreReductionPerMillis()} " +
                     "because $defaultTimePast ms have past.")
  }
}