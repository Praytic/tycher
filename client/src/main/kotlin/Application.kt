import kotlin.browser.window

//TODO: Tychs should be defined by all properties, not only by position
val tychs = mutableMapOf<Position, Tych>()
val scoreboard = mutableMapOf<String, Int>()
val canvas = Canvas(window.innerWidth, window.innerHeight)

// It is inversely proportional to the Tych.SCORE_TO_SHRINK_SPEED constant.
val RENDERING_RATIO = 10
val UPDATE_RATIO = 1000.0
val SCOREBOARD_LIMIT = 100

var webSocketPort: Long = 4567;

fun main(args: Array<String>) {
  if (args.isNotEmpty()) {
    webSocketPort = args[0].toLong()
  }

  startRenderLoop(1000, RENDERING_RATIO);
}

fun startRenderLoop(startDelayMillis: Int, periodMillis: Int) {
  window.setTimeout({
    console.logWithTime("Starting render loop...")
    window.setInterval({
      canvas.renderBackground()
      canvas.renderTychs()
      canvas.renderScoreboard()
    }, periodMillis)
  }, startDelayMillis)
}