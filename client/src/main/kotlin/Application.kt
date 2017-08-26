import kotlin.browser.window

val tychs: MutableSet<Tych> = mutableSetOf()
val canvas = Canvas(window.innerWidth, window.innerHeight)
// It is inversely proportional to the Tych.SCORE_TO_SHRINK_SPEED constant.
val RENDERING_RATIO = 100

fun main(args: Array<String>) {
    initWebSockets()
    console.logWithTime("Starting game loop...")
    window.setInterval({
        canvas.renderBackground()
        canvas.renderTychs()
        canvas.shrinkTychs()
    }, RENDERING_RATIO)
}