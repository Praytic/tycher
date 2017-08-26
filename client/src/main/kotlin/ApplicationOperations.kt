import kotlin.js.json

fun updateScoreboard() {
  val scoreboard = json("scoreboard" to SCOREBOARD_LIMIT)
  gameSocket.send(JSON.stringify(scoreboard))
}

fun handleTych(tych: dynamic) {
  console.logWithTime("Message game socket with data: (${tych})")
  val pos = Position(tych[0], tych[1])
  val radius = tych[2]
  val shrinkSpeed = tych[3]
  tychs.add(Tych(pos, radius, shrinkSpeed))
}

fun handleScoreboard(sb: dynamic) {
  console.logWithTime("Message game socket with data: (${sb})")
  scoreboard.clear()
  sb.forEach { pair: dynamic ->
    scoreboard.put(pair[0], pair[1])
  }
}