package com.vchernogorov.handler

import com.vchernogorov.Logout
import com.google.gson.JsonElement
import com.vchernogorov.User
import com.vchernogorov.gson
import com.vchernogorov.log
import org.eclipse.jetty.websocket.api.CloseStatus
import org.eclipse.jetty.websocket.api.Session

class LogoutHandler : MessageHandler<Logout>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Logout::class.java)

  override fun handle(user: User, session: Session, message: Logout) {
    session.close(CloseStatus(4000,
            "Closed by request. ${message.cause}"))
    log.info { "User ${user.name} logged out." }
  }
}