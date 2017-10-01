import kotlin.browser.window

val tychs = mutableSetOf<Tych>()
val scoreboard = mutableMapOf<String, Int>()
val canvas = Canvas(window.innerWidth, window.innerHeight)

// It is inversely proportional to the Tych.SCORE_TO_SHRINK_SPEED constant.
val RENDERING_RATIO = 100
val UPDATE_RATIO = 1000
val SCOREBOARD_LIMIT = 10

fun main(args: Array<String>) {
  initWebSockets()
  console.logWithTime("Starting game loop...")

  window.requestAnimationFrame {
    canvas.renderBackground()
    canvas.renderTychs()
    canvas.renderScoreboard()
    canvas.shrinkTychs()
  }

  window.setInterval({
    if (gameSocket.readyState.toInt() == 1) {
      updateScoreboard()
    }
  }, UPDATE_RATIO)
}