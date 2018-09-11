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
 * Renders player's [Tych] on the [Canvas].
 */
fun Canvas.renderPlayerTych() {
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
 * Renders all [Tych]s on the [Canvas].
 */
fun Canvas.renderTychs() {
  tychs.values.forEach {
    renderTych(it)
  }
  if (playerTych != null) {
    renderTych(playerTych!!)
  }
}

fun Canvas.renderTych(tych: Tych) {
  with(cxt) {
    save()
    beginPath()
    arc(tych.position.x,
        tych.position.y,
        tych.currentRadius,
        0.0,
        2.0 * PI)
    fillStyle = "rgba(80,160,250, 0.4)"
    fill()
    stroke()
    restore()
  }
}

/**
 * Renders the scoreboard.
 */
fun Canvas.renderScoreboard() {
  with(cxt) {
    var y = 15.0
    fillStyle = "rgba(80,80,80, 0.2)"
    fillRect(8.0, 13.0, 30.0, scoreboard.size * 15.0 + 2)
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