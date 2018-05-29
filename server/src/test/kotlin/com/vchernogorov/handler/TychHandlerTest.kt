package com.vchernogorov.handler

import com.vchernogorov.Position
import com.vchernogorov.Tych
import com.vchernogorov.User
import org.junit.Test
import com.vchernogorov.tychs
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TychHandlerTest {

  val tychHandler = TychHandler()
  val defaultScore = 100

  /**
   * [TychHandler.consumeTychs]
   */
  @Test
  fun test1() {
    val now = Date()
    val tych1 = Tych(position = Position(0.0, 0.0),
            tycher = User(score = defaultScore),
            spawnTime = now.time)
    val tych2 = Tych(position = Position(0.0, 0.0),
            tycher = User(score = defaultScore - 1),
            spawnTime = now.time)

    tychs.put(tych1.tycher, tych1)
    tychs.put(tych2.tycher, tych2)
    val actualResult = tychHandler.consumeTychs(tych1)
    assertTrue(actualResult.isNotEmpty(),
               "$tych1 should consume another tych.")
    assertEquals(tych2, actualResult[0],
                 "$tych1 should consume $tych2 and return it.")
  }
}