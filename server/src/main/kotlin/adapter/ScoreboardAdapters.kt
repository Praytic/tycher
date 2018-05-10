package adapter

import com.github.salomonbrys.kotson.typeAdapter
import java.lang.UnsupportedOperationException
import java.util.*
import Scoreboard
import User
import users

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