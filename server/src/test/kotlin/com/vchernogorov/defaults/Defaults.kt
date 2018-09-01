package com.vchernogorov.defaults

import com.vchernogorov.Tych
import org.junit.Test

class DefaultsTest {

  @Test
  fun test1() {
    with(Tych()) {
      println("Tychs' score to radius rate:")
      for (i in 1..10) {
        printTychDefaults(Tych(score = i * 100))
      }
    }
  }

  fun printTychDefaults(tych: Tych) {
    println(with(tych) {
      mutableMapOf(
          "InitialScore" to score,
          "ScoreReductionPerMs" to "%.2f".format(getScoreReductionPerMs()),
          "Radius" to "%.2f".format(getRadius()),
          "RadiusReductionPerMs" to "%.2f".format(getRadiusReductionPerMs()),
          "LifetimeMs" to getLifetimeMs(),
          "CurrentRadius" to "%.2f".format(getCurrentRadius()),
          "CurrentScore" to getCurrentScore(),
          "spawnTime" to spawnTime
      )
    })
  }
}