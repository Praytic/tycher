package com.vchernogorov.task

import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.WebSocketException
import org.eclipse.jetty.websocket.api.WriteCallback
import java.util.*

class LogTask : TimerTask() {
  override fun run() {
    users.forEach({ session, user ->
      log.info { "Users count: ${users.size}" }
      log.info { "Active users count: ${getActiveUsers().size}" }
      log.info { "Tychs count: ${tychs.size}" }
      log.info { "Broken tychs count: ${tychs.filter { it.value.getCurrentRadius() <= 0 }.size}" }
      log.info { "Scores: ${users.map { it.value?.score }}"}
      log.info { "Tychs: ${tychs}" }
      log.info { "Users: ${users.map { it.value?.name }}" }
      log.info { "========================================" }
    })
  }
}
