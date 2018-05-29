package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.gson
import org.eclipse.jetty.websocket.api.Session
import com.vchernogorov.Scoreboard
import com.vchernogorov.User
import com.vchernogorov.toJsonMessage

/**
 * Extension of [MessageHandler] for [Scoreboard] entity.
 */
class ScoreboardHandler : MessageHandler<Scoreboard>() {

  override fun parse(message: JsonElement) = gson.fromJson(message, Scoreboard::class.java)

  override fun handle(user: User, session: Session, message: Scoreboard) {
//    com.vchernogorov.getLog.info { "Sending $message to $user." }
    session.remote.sendString(gson.toJsonMessage(message))
  }
}