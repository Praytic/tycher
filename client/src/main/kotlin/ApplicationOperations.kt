fun convertToTych(tych: dynamic): Tych {
  val pos = Position(tych[0], tych[1])
  val radius = tych[2]
  val shrinkSpeed = tych[3]
  return Tych(pos, radius, shrinkSpeed)
}

fun convertToTychs(tychs: Array<dynamic>): List<Tych> {
  console.logWithTime("Converting tychs: ${tychs}")
  return tychs.map { convertToTych(it) }
}

fun convertToScoreboard(sb: Array<dynamic>): Scoreboard {
  console.logWithTime("Converting scoreboard: ${sb}")
  val scoreboardValues: Map<String, Int> = sb
      .map { pair -> Pair(pair[0], pair[1]) }
      .toMap()
  return Scoreboard(scoreboardValues)
}