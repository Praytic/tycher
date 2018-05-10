import org.w3c.dom.TimeRanges
import kotlin.browser.window
import kotlin.js.Math

/**
 * Shortcut for [Canvas.cxt].
 */
val cxt = canvas.cxt

/**
 * Renders background on the [Canvas].
 */
fun Canvas.renderBackground() {
  cxt.save()
  cxt.fillStyle = "#FAFAFA"
  cxt.fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
  cxt.restore()
}

/**
 * Renders all [Tych]s on the [Canvas].
 */
fun Canvas.renderTychs() {
  tychs.values.forEach {
    cxt.save()
    cxt.beginPath()
    cxt.arc(it.position.x,
        it.position.y,
        it.currentRadius,
        0.0,
        2.0 * Math.PI)
    cxt.fillStyle = "rgba(242,160,111, 0.4)"
    cxt.fill()
    cxt.stroke()
    cxt.restore()
  }
}

/**
 * Reduces the size of every [Tych] and removes those which have zero or less
 * radius.
 */
fun Canvas.shrinkTychs() {
  val deadTychs = mutableSetOf<Tych>()
  tychs.values.forEach {
    it.currentRadius -= it.shrinkSpeedPerSecond / (1000.0 / RENDERING_RATIO)
    if (it.currentRadius <= 0) {
      deadTychs.add(it)
    }
  }

  if (deadTychs.isNotEmpty()) {
    console.logWithTime("Removing dead tychs...")
    deadTychs.forEach { tychs.remove(it.position) }
  }
}

/**
 * Renders the scoreboard.
 */
fun Canvas.renderScoreboard() {
  var y = 15.0
  cxt.font = "12px Arial"
  cxt.fillText("Scoreboard:", 10.0, y)
  scoreboard.forEach {
    y += 15.0
    val placement = y / 15 - 1
    cxt.fillText("${placement}. ${it.key}: ${it.value}", 10.0, y)
  }
  cxt.restore()
}