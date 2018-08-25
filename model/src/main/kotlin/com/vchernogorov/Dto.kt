package com.vchernogorov

/**
 * Defines entities which should be transported as a json message via com.vchernogorov.websocket.
 */
abstract class Message(val command: Command)

/**
 * [Scoreboard] entity which is transferred from frontend to backend.
 */
data class Scoreboard(val limit: Int) : Message(Command.SCOREBOARD) {

  val scores = { users: Collection<User> ->
    users
        .filter { it.name != null }
        .sortedByDescending { it.score }
        .take(limit)
        .map { it.name to it.score }
  }
}

/**
 * [User] entity defines a single user.
 */
data class User(val name: String? = null, var score: Int = 100) : Message(Command.LOGIN) {

  /**
   * User can click if he has no non-dummy [Tych]s active.
   */
  fun isClickable(tych: Tych?) = tych == null || tych.isDummy

  override fun equals(other: Any?): Boolean {
    if (other == null) return false
    if (other is User) {
      if (other.name == null) return false
      return other.name == name
    }
    return false
  }

  override fun hashCode(): Int {
    return name?.hashCode() ?: 0
  }
}