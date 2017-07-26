import org.w3c.dom.WebSocket
import kotlin.browser.window
import kotlin.js.Json
import kotlin.js.json

data class Position(val x: Double, val y: Double)
data class Tych(val args: Array<Double>)

val gameSocket = WebSocket("ws://localhost:4567/game")

fun initWebSockets() {
    gameSocket.onopen = { event ->
        val username = window.prompt("Choose username:")
        val login = json(Pair("username", username))
        val greetings = json(Pair("login", login))
        gameSocket.send(JSON.stringify(greetings))
        event
    }
    gameSocket.onmessage = { event ->
        val message = JSON.parse<Json>(event.asDynamic().data)
        val tych = message.asDynamic().tych
        val pos = Position(tych[0], tych[1])
        drawTych(pos)
        event
    }
}
