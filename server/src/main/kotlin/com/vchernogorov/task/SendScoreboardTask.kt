package com.vchernogorov.task

import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.WebSocketException
import org.eclipse.jetty.websocket.api.WriteCallback
import java.util.*

class SendScoreboardTask(val scoreboard: Scoreboard) : TimerTask() {
  override fun run() {
    users.forEach({ session, user ->
      try {
        session.remote.sendString(gson.toJsonMessage(scoreboard), object : WriteCallback {
          override fun writeFailed(x: Throwable?) {
            log.error { x }
          }

          override fun writeSuccess() {
          }
        })
      } catch (ex: WebSocketException) {
        log.error { ex }
      }
    })
  }
}
