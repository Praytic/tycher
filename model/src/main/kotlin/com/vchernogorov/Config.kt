package com.vchernogorov

object GameConf {
  const val SECOND_TO_MILLIS = 1000.0
  const val TIMER_DELAY_MILLIS = SECOND_TO_MILLIS.toLong()
  const val TYCH_REFRESH_PER_SEC = 100
  const val SCOREBOARD_REFRESH_PER_SEC = 1
  const val LOG_REFRESH_PER_SEC = 0.2
}

object PlayerConf {
  const val START_SCORE = 100
  const val SCOREBOARD_LIMIT = 10
}

object TychConf {
  const val SCORE_TO_RADIUS_RATE = 1.0
  const val SCORE_TO_SHRINK_SPEED_RATE = 0.1
  const val STATIC_SHRINK_SPEED = 20.0
  const val USE_STATIC_SHRINK_SPEED = true
}
