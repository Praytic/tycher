import kotlin.browser.window

//TODO: Tychs should be defined by all properties, not only by position
val tychs = mutableMapOf<Position, Tych>()
val scoreboard = mutableMapOf<String, Int>()
val canvas = Canvas(window.innerWidth, window.innerHeight)

// It is inversely proportional to the Tych.SCORE_TO_SHRINK_SPEED constant.
val RENDERING_RATIO = 10.0
val UPDATE_RATIO = 1000.0
val SCOREBOARD_LIMIT = 10.0

fun main(args: Array<String>) {
  initWebSockets()
  console.logWithTime("Starting game loop...")

  window.setInterval({
    canvas.renderBackground()
    canvas.renderTychs()
    canvas.renderScoreboard()
    canvas.shrinkTychs()
  }, RENDERING_RATIO.toInt())

  window.setInterval({
    if (gameSocket.readyState.toInt() == 1) {
      updateScoreboard()
    }
  }, UPDATE_RATIO.toInt())
}