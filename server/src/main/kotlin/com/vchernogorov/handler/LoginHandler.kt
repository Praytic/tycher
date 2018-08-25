package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.User
import com.vchernogorov.gson
import com.vchernogorov.log
import com.vchernogorov.users
import org.eclipse.jetty.websocket.api.Session

class LoginHandler : MessageHandler<User>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, User::class.java)

  override fun handle(user: User, session: Session, message: User) {
    users[session] = User(name = message.name)
    log.info { "User ${message.name} logged in." }
  }
}