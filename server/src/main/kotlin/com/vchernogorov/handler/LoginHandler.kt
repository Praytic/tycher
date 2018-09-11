package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WriteCallback

class LoginHandler : MessageHandler<User>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, User::class.java)

  override fun handle(user: User, session: Session, message: User): Boolean {
    user.name = message.name
    users[session] = user
    log.info { "User ${message.name} logged in." }
    session.remote.sendString(gson.toJsonMessage(Tych(tycher = user), Command.PLAYER_TYCH), object : WriteCallback {
      override fun writeFailed(x: Throwable?) {
        log.error { "Error sending player tych: $x" }
      }

      override fun writeSuccess() {
      }
    })
    return true
  }
}