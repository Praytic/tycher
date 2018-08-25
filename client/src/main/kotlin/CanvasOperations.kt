import kotlin.math.PI

/**
 * Renders background on the [Canvas].
 */
fun Canvas.renderBackground() {
  with(cxt) {
    save()
    fillStyle = "#FAFAFA"
    fillRect(0.0, 0.0, width, height)
    restore()
  }
}

/**
 * Renders all [Tych]s on the [Canvas].
 */
fun Canvas.renderTychs() {
  with(cxt) {
    tychs.values.forEach {
      save()
      beginPath()
      arc(it.position.x,
          it.position.y,
          it.currentRadius,
          0.0,
          2.0 * PI)
      fillStyle = "rgba(80,160,250, 0.4)"
      fill()
      stroke()
      restore()
    }
  }
}

/**
 * Renders the scoreboard.
 */
fun Canvas.renderScoreboard() {
  with(cxt) {
    var y = 15.0
    font = "12px Arial"
    fillText("Scoreboard:", 10.0, y)
    scoreboard.forEach {
      y += 15.0
      val placement = y / 15 - 1
      fillText("${placement}. ${it.key}: ${it.value}", 10.0, y)
    }
    restore()
  }
}