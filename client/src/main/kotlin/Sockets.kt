
import org.w3c.dom.WebSocket
import kotlin.browser.window
import kotlin.js.*

val gameSocket: WebSocket by lazyOf(initWebSocket())

/**
 * The main [WebSocket] for handling user actions.
 */
fun initWebSocket(): WebSocket {
  console.logWithTime("Init web sockets...")
  val gameSocket = WebSocket("ws://${window.location.hostname}:$webSocketPort/game")

  return with(gameSocket) {
    onopen = { event ->
      console.logWithTime("Open game socket connection.")
      val username = window.prompt("Choose username:")
      val greetings = json("login" to arrayOf(username))
      send(JSON.stringify(greetings))
      event
    }

    onmessage = { event ->
      val message = JSON.parse<Json>(event.asDynamic().data).asDynamic()
      val newTychs = message.tychs
      if (newTychs != null) {
        val convertedTychs = convertToTychs(newTychs)
        tychs.clear()
        tychs.putAll(convertedTychs.map { it.position to it })
      }
      val sb = message.scoreboard
      if (sb != null) {
        scoreboard.clear()
        scoreboard.putAll(convertToScoreboard(sb).values)
      }
      val ptych = message.playerTych
      if (ptych != null) {
        playerTych = convertToTych(ptych)
      }
      event
    }

    onclose = { event ->
      console.logWithTime("Close game socket connection.")
    }
    this
  }
}