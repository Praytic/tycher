import kotlin.js.json

fun updateScoreboard() {
  val scoreboard = json("scoreboard" to SCOREBOARD_LIMIT)
  gameSocket.send(JSON.stringify(scoreboard))
}

fun handleTych(tych: dynamic) {
  console.logWithTime("Handle tych: (${tych})")
  val pos = Position(tych[0], tych[1])
  val radius = tych[2]
  val shrinkSpeed = tych[3]
  tychs.put(pos, Tych(pos, radius, shrinkSpeed))
}

fun handleScoreboard(sb: dynamic) {
  //console.logWithTime("Scoreboard update: (${sb})")
  scoreboard.clear()
  sb.forEach { pair: dynamic ->
    scoreboard.put(pair[0], pair[1])
  }
}

fun handleRemoveTych(dtych: dynamic) {
  console.logWithTime("Remove tych: (${dtych})")
  val pos = Position(dtych[0], dtych[1])
  val radius = dtych[2]
  val shrinkSpeed = dtych[3]
  tychs.remove(pos)
}
