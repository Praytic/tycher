package handler

import Login
import User
import com.google.gson.JsonElement
import gson
import log
import org.eclipse.jetty.websocket.api.Session
import users

class LoginHandler : MessageHandler<Login>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Login::class.java)

  override fun handle(user: User, session: Session, message: Login) {
    users[session] = User(name = message.username)
    log.info { "User ${message.username} logged in." }
  }
}