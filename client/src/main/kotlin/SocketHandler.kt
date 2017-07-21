import org.w3c.dom.WebSocket

val gameSocket: WebSocket by lazy {
    val socket = WebSocket("ws://localhost:4567/game")
    socket.onmessage
    socket
}

val scoreboardHandler = { msg: String ->
    val jsonData = JSON.parse<List<User>>(msg)

}

