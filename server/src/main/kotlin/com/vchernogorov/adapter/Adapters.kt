/**
 * This kotlin file contains all adapters for serializing/deserializing game objects.
 */
package com.vchernogorov.adapter

import com.github.salomonbrys.kotson.typeAdapter
import java.lang.UnsupportedOperationException
import java.util.*
import com.vchernogorov.Scoreboard
import com.vchernogorov.TychRequest
import com.vchernogorov.Position
import com.vchernogorov.TychResponse
import com.vchernogorov.users

val scoreboardRequestAdapter = typeAdapter<Scoreboard> {
  write {
    beginArray()
    val availableUsers = users.values.filterNotNull()
    it.scores(availableUsers).forEach {
      beginArray()
      value(it.first)
      value(it.second)
      endArray()
    }
    endArray()
  }
  read {
    val limit = nextInt()
      Scoreboard(limit)
  }
}

val tychRequestAdapter = typeAdapter<TychRequest> {
  write {
    throw UnsupportedOperationException("This should not be used.")
  }
  read {
    beginArray()
    val xpos = nextDouble()
    val ypos = nextDouble()
    endArray()
      TychRequest(Position(xpos, ypos), Date().time)
  }
}

val tychResponseAdapter = typeAdapter<TychResponse> {
  write {
    beginArray()
    value(it.position.x)
    value(it.position.y)
    value(it.radius)
    value(it.shrinkSpeed)
    endArray()
  }
  read {
    throw UnsupportedOperationException("This should not be used.")
  }
}