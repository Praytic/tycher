import org.w3c.dom.WebSocket
import kotlin.browser.window
import kotlin.js.json

data class Position(val x: Double, val y: Double)

val gameSocket = WebSocket("ws://localhost:4567/game")

fun initWebSockets() {
    gameSocket.onopen = { event ->
        val username = window.prompt("Choose username:")
        val login = json(Pair("name", username))
        val greetings = json(Pair("greetings", login))
        gameSocket.send(JSON.stringify(greetings))
        event
    }
    gameSocket.onmessage = { event ->
        val data = event.asDynamic().data
        val pos = JSON.parse<Position>(data)
        drawTych(pos)
        event
    }
}
