import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Console
import kotlin.js.Date
import kotlin.js.Math
import kotlin.js.Math.PI
import kotlin.js.json

val tychs: MutableSet<Tych> = mutableSetOf()

fun Console.logWithTime(vararg o: Any?) {
    console.log("${Date().getTime()} > ${o}")
}

val canvas: HTMLCanvasElement by lazy {
    console.logWithTime("Initializing canvas...")
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D

    context.canvas.width = window.innerWidth
    context.canvas.height = window.innerHeight
    document.body!!.appendChild(canvas)

    canvas.onclick = { event ->
        val pos = event.asDynamic()
        console.logWithTime("Canvas click registered at (${pos.x}, ${pos.y}).")
        val params = arrayOf(pos.x, pos.y)
        val tych = json(Pair("tych", params))
        gameSocket.send(JSON.stringify(tych))
    }
    canvas
}

val cxt: CanvasRenderingContext2D
    get() {
        return canvas.getContext("2d") as CanvasRenderingContext2D
    }

val width: Int
    get() {
        return canvas.width
    }

val height: Int
    get() {
        return canvas.height
    }

fun renderBackground() {
    cxt.save()
    cxt.fillStyle = "#FAFAFA"
    cxt.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    cxt.restore()
}

fun renderTychs() {
    tychs.forEach {
        cxt.save()
        cxt.beginPath()
        cxt.arc(it.position.x,
                it.position.y,
                it.currentRadius,
                0.0,
                2.0 * PI)
        cxt.fillStyle = "rgb(242,160,111)"
        cxt.fill()
        cxt.stroke()
        cxt.restore()
    }
}

fun shrinkTychs() {
    tychs.forEach { it.currentRadius -= it.shrinkSpeed }
    tychs.filter { it.currentRadius <= 0  }
}

fun main(args: Array<String>) {
    initWebSockets()
    console.logWithTime("Starting game loop...")
    window.setInterval({
        renderBackground()
        renderTychs()
//        shrinkTychs()
    }, 50)
}