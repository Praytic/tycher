package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.Scoreboard
import com.vchernogorov.User
import com.vchernogorov.gson
import com.vchernogorov.toJsonMessage
import org.eclipse.jetty.websocket.api.Session

/**
 * Extension of [MessageHandler] for [Scoreboard] entity.
 */
class ScoreboardHandler : MessageHandler<Scoreboard>() {

  override fun parse(message: JsonElement) = gson.fromJson(message, Scoreboard::class.java)

  override fun handle(user: User, session: Session, message: Scoreboard) {
    session.remote.sendString(gson.toJsonMessage(message))
  }
}