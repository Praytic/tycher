package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.Session

class LoginHandler : MessageHandler<Login>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Login::class.java)

  override fun handle(user: User, session: Session, message: Login) {
    users[session] = User(name = message.username)
    log.info { "User ${message.username} logged in." }
  }
}