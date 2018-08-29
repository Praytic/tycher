package com.vchernogorov.task

import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.WebSocketException
import org.eclipse.jetty.websocket.api.WriteCallback
import java.util.*

class SendTychsTask(val tychs: Collection<Tych>) : TimerTask() {
  override fun run() {
    if (tychs.isEmpty()) return
    getActiveUsers().forEach({ session, user ->
      try {
        log.debug { "Sending $tychs to $user." }
        session.remote.sendString(gson.toJsonMessage(tychs.toList(), Command.TYCHS), object : WriteCallback {
          override fun writeFailed(x: Throwable?) {
            log.error { "Error sending tych: $x" }
          }

          override fun writeSuccess() {
          }
        })
      } catch (ex: WebSocketException) {
        log.error { "Error during tych sending.\n$ex" }
        session.close()
      }
    })
  }
}
