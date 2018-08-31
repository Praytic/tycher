package com.vchernogorov.handler

import com.vchernogorov.Tych
import com.vchernogorov.User
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.eclipse.jetty.websocket.api.Session
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TychHandlerTest {

  val tychs: MutableMap<User, Tych> = spy(mutableMapOf())
  val jobs: MutableMap<Tych, Job> = spy(mutableMapOf())
  val tychHandler = TychHandler(tychs, jobs)

  @Before
  fun setUp() {
    tychs.clear()
    jobs.clear()
  }

  /**
   * [TychHandler.consumeTychsBy]
   */
  @Test
  fun test1() {
    val now = Date()
    val tych1 = Tych(spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = tych1.score - 1), spawnTime = now.time)

    tychs.put(tych1.tycher, tych1)
    tychs.put(tych2.tycher, tych2)
    val actualResult = tychHandler.consumeTychsBy(tych1.tycher, tych1)
    assertTrue(actualResult.isNotEmpty(),
        "$tych1 should consume another tych.")
    assertEquals(tych2, actualResult[0],
        "$tych1 should consume $tych2 and return it.")
  }

  /**
   * [TychHandler.handle]
   */
  @Test
  fun test2() {
    val now = Date()
    val tych1 = Tych(spawnTime = now.time)
    val tych2 = Tych(tycher = User(score = tych1.score - 2), spawnTime = now.time)

    with(tychHandler) {
      handle(tych2.tycher, mock(Session::class.java), tych2)
      assertTrue(tychs.contains(tych2.tycher), "New $tych2 should appear in the map.")
      assertEquals(jobs.size, 1, "New task for $tych2 should be started.")
      val task2 = jobs.values.first()

      handle(tych1.tycher, mock(Session::class.java), tych1)
      assertTrue(tychs.contains(tych1.tycher), "$tych1 should appear in the map")
      assertEquals(jobs.size, 1, "The task for $tych2 should be removed from the map.")
      assertFalse(tychs.contains(tych2.tycher), "Thus $tych1 consumed $tych2, it should be removed from the map.")

      assertFalse(task2.cancel(), "Task can't be canceled because it was already canceled.")
    }
  }

  /**
   * [TychHandler.handle]
   */
  @Test
  fun test3() {
    val now = Date()
    val tych1 = Tych(spawnTime = now.time)

    with(tychHandler) {
      runBlocking {
        assertTrue(handle(tych1.tycher, mock(Session::class.java), tych1),
            "$tych1 should be successfully handled.")
        delay(tych1.getLifeDurationMillis(now) / 2)
        val tych2 = Tych(tycher = tych1.tycher, spawnTime = Date().time)
        assertFalse(handle(tych2.tycher, mock(Session::class.java), tych2),
            "$tych2 should not be handled by the same user because $tych1 is still alive.")
        delay(tych1.getLifeDurationMillis(now) / 2)
        val tych3 = Tych(tycher = tych1.tycher, spawnTime = Date().time)
        assertTrue(handle(tych3.tycher, mock(Session::class.java), tych3),
            "$tych3 should be successfully handled by the same user because $tych1 is dead.")
      }
    }
  }
}