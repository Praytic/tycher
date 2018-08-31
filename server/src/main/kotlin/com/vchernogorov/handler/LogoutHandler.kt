package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.User
import com.vchernogorov.gson
import com.vchernogorov.log
import org.eclipse.jetty.websocket.api.CloseStatus
import org.eclipse.jetty.websocket.api.Session

class LogoutHandler : MessageHandler<User>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, User::class.java)

  override fun handle(user: User, session: Session, message: User): Boolean {
    session.close(CloseStatus(4000,
            "Closed by request from ${message}."))
    log.info { "User ${user.name} logged out." }
    return true
  }
}