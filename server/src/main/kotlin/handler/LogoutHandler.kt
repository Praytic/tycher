package handler

import Logout
import User
import com.google.gson.JsonElement
import gson
import log
import org.eclipse.jetty.websocket.api.CloseStatus
import org.eclipse.jetty.websocket.api.Session
import users

class LogoutHandler : MessageHandler<Logout>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Logout::class.java)

  override fun handle(user: User, session: Session, message: Logout) {
    session.close(CloseStatus(4000, "Closed by request."))
    log.info { "User ${message.username} logged out." }
  }
}