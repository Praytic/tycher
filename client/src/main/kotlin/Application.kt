import kotlin.browser.window

val tychs: MutableSet<Tych> = mutableSetOf()
val canvas = Canvas(window.innerWidth, window.innerHeight)

fun main(args: Array<String>) {
    initWebSockets()
    console.logWithTime("Starting game loop...")
    window.setInterval({
        canvas.renderBackground()
        canvas.renderTychs()
//        shrinkTychs()
    }, 50)
}