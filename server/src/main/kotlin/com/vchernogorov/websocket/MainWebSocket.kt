package com.vchernogorov.websocket

import com.github.salomonbrys.kotson.keys
import com.google.gson.JsonObject
import com.vchernogorov.*
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.*

/**
 * [MainWebSocket] manages com.vchernogorov.websocket connection and messages which are passed
 * through it.
 */
@WebSocket
class MainWebSocket {

  /**
   * Adds unauthorized [User] to the [users] map when the connection is opened.
   */
  @OnWebSocketConnect
  fun onConnect(session: Session) {
    val connectedUser = users.put(session, User())
    log.info { "Connection was established for ${session.upgradeRequest.getHeader("User-Agent")}." }
  }

  /**
   * Handles messages sent via com.vchernogorov.websocket. Delegates [message] handling
   * to the appropriate [MessageHandler].
   */
  @OnWebSocketMessage
  fun onMessage(session: Session, message: String) {
    log.debug { "Received message:\n$message" }
    val jsonMessage = gson.fromJson(message, JsonObject::class.java)
    val rawCommand = jsonMessage.keys().single()
    val command = Command.valueOf(rawCommand.toUpperCase())
    commandHandlerMapper[command]?.handle(session, jsonMessage[rawCommand])
            ?: throw IllegalArgumentException(
                    "No handler was found for the command:\n$command")
  }

  /**
   * Removes [User] from the [users] map when the connection is closed.
   */
  @OnWebSocketClose
  fun onClose(session: Session, statusCode: Int, reason: String) {
    val removedUser = users.remove(session)
    val removedTych = tychs.remove(removedUser)
    log.info { "Connection was closed for $removedUser with status " +
            "code $statusCode. Reason: $reason" }
  }

  @OnWebSocketError
  fun onError(session: Session, error: Throwable) {
    val reason = "Error occurred for user ${users[session]}."
    log.error(reason, error)
    onClose(session, -1, reason)
  }
}

