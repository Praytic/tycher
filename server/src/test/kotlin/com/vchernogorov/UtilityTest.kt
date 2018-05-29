package com.vchernogorov

import org.junit.Test
import org.mockito.Spy
import java.util.*
import java.util.concurrent.TimeUnit

class UtilityTest {

  @Spy
  val mapSpy = mutableMapOf<User, Tych>()

  /**
   * [putTemp]
   */
  @Test
  fun test1() {
    val tenSeconds = TimeUnit.SECONDS.toMillis(5)
    val tych = Tych(tycher = User(score = 100), spawnTime = 0)
    mapSpy.putTemp(tych.tycher, tych, Date(tenSeconds))
  }
}
