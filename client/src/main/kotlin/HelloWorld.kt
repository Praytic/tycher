import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Json
import kotlin.js.json

val canvas: HTMLCanvasElement by lazy {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = window.innerWidth
    context.canvas.height = window.innerHeight
    document.body!!.appendChild(canvas)
    canvas.onclick = { event ->
        val params = arrayOf(event.asDynamic().x, event.asDynamic().y)
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

fun drawTych(pos: Position) {
    cxt.save()
    // if you using chrome chances are good you wont see the shadow
    cxt.shadowColor = "#000000"
    cxt.shadowBlur = 5.0
    cxt.shadowOffsetX = -4.0
    cxt.shadowOffsetY = 4.0
    cxt.fillStyle = "rgb(242,160,110)"
    cxt.fillText("WOW", pos.x, pos.y)
    cxt.restore()
}

fun renderBackground() {
    cxt.save()
    cxt.fillStyle = "#FAFAFA"
    cxt.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    cxt.restore()
}

fun main(args: Array<String>) {
    initWebSockets()
    window.setInterval({
        renderBackground()
    }, 50)
}