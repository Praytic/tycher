/**
 * This kotlin file contains all adapters for serializing/deserializing game objects.
 */
package com.vchernogorov.adapter

import com.github.salomonbrys.kotson.typeAdapter
import com.vchernogorov.*
import java.util.*

val userAdapter = typeAdapter<User> {
  write {
    beginArray()
    value(it.name)
    value(it.score)
    endArray()
  }
  read {
    beginArray()
    val name = nextString()
    User(name)
  }
}

val scoreboardRequestAdapter = typeAdapter<Scoreboard> {
  write {
    beginArray()
    it.scores(getActiveUsers().values).forEach {
      beginArray()
      value(it.first)
      value(it.second)
      endArray()
    }
    endArray()
  }
  read {
    beginArray()
    val limit = nextInt()
      Scoreboard(limit)
  }
}

val tychAdapter = typeAdapter<Tych> {
  write {
    beginArray()
    value(it.position.x)
    value(it.position.y)
    value(it.getCurrentRadius())
    value(it.getScoreReductionPerMs())
    endArray()
  }
  read {
    beginArray()
    val xpos = nextDouble()
    val ypos = nextDouble()
    endArray()
    Tych(Position(xpos, ypos), Date().time)
  }
}