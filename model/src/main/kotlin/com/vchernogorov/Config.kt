package com.vchernogorov

import kotlin.math.log10

object GameConf {
  const val SECOND_TO_MILLIS = 1000.0
  const val TIMER_DELAY_MS = SECOND_TO_MILLIS.toLong()
  const val TYCH_REFRESH_PER_SEC = 100
  const val SCOREBOARD_REFRESH_PER_SEC = 1
  const val LOG_REFRESH_PER_SEC = 0.2
}

object PlayerConf {
  const val START_SCORE = 100
  const val SCOREBOARD_LIMIT = 50
}

object TychConf {
  const val SCORE_TO_RADIUS_RATE = 0.5
  const val LIFETIME_MS: Long = 1000

  val DYNAMIC_SCORE_TO_RADIUS_RATE = { score: Int -> log10(score.toDouble()) }
  val DYNAMIC_LIFETIME_MS = { score: Int -> (DYNAMIC_SCORE_TO_RADIUS_RATE(score) * LIFETIME_MS).toLong() }

  const val USE_STATIC_RADIUS_RATE = false
  const val USE_STATIC_LIFETIME = false
}
