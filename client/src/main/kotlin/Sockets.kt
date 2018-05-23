import org.w3c.dom.WebSocket
import kotlin.browser.window
import kotlin.js.Json
import kotlin.js.json

val gameSocket: WebSocket by lazyOf(initWebSocket())

/**
 * The main [WebSocket] for handling user actions.
 */
fun initWebSocket(): WebSocket {
  console.logWithTime("Init web sockets...")
  val gameSocket = WebSocket("ws://${window.location.hostname}:$webSocketPort/game")

  gameSocket.onopen = { event ->
    console.logWithTime("Open game socket connection.")
    val username = window.prompt("Choose username:")
    val login = json("username" to username)
    val greetings = json("login" to login)
    gameSocket.send(JSON.stringify(greetings))
    event
  }

  gameSocket.onmessage = { event ->
    val message = JSON.parse<Json>(event.asDynamic().data)
    val tych = message.asDynamic().tych
    if (tych != null) {
      handleTych(tych)
    }
    val sb = message.asDynamic().scoreboard
    if (sb != null) {
      handleScoreboard(sb)
    }
    val dtych = message.asDynamic().removeTych
    if (dtych != null) {
      handleRemoveTych(dtych)
    }
    event
  }

  gameSocket.onclose = { event ->
    console.logWithTime("Close game socket connection.")
  }
  return gameSocket
}