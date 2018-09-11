
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.max

//TODO: Tychs should be defined by all properties, not only by position
var playerTych: Tych? = null
val tychs = mutableMapOf<Position, Tych>()
val scoreboard = mutableMapOf<String, Int>()
val canvas by lazy {
  val width = max(document.body?.clientWidth ?: 0, window.innerWidth)
  val height = max(document.body?.clientHeight ?: 0, window.innerHeight)
  console.log("Canvas width: $width, height: $height.")
  Canvas(width, height)
}

// It is inversely proportional to the Tych.SCORE_TO_SHRINK_SPEED constant.
val RENDERING_RATIO = 10

var webSocketPort: Long = 4567;

fun main(args: Array<String>) {
  if (args.isNotEmpty()) {
    webSocketPort = args[0].toLong()
  }

  window.addEventListener("mousemove", {
    val mouseEvent = it.asDynamic()
    playerTych?.position = Position(mouseEvent.clientX as Double, mouseEvent.clientY as Double)
  }, false)

  startRenderLoop(1000, RENDERING_RATIO);
}

fun startRenderLoop(startDelayMs: Int, periodMs: Int) {
  window.setTimeout({
    console.logWithTime("Starting render loop...")
    window.setInterval({
      canvas.renderBackground()
      canvas.renderTychs()
      canvas.renderScoreboard()
    }, periodMs)
  }, startDelayMs)
}