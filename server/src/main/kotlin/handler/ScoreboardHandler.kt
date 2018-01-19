package handler

import com.google.gson.JsonElement
import gson
import org.eclipse.jetty.websocket.api.Session
import tychs
import Scoreboard
import User
import log
import toJsonMessage

/**
 * Extension of [MessageHandler] for [Scoreboard] entity.
 */
class ScoreboardHandler : MessageHandler<Scoreboard>() {

  override fun parse(message: JsonElement) = gson.fromJson(message, Scoreboard::class.java)

  override fun handle(user: User, session: Session, message: Scoreboard) {
//    log.info { "Sending $message to $user." }
    session.remote.sendString(gson.toJsonMessage(message))
  }
}