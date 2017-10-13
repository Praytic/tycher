import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.js.json

/**
 * Represents structure of methods and constants for working with
 * [HTMLCanvasElement].
 */
class Canvas(width: Int, height: Int) {

  private val canvas: HTMLCanvasElement

  /**
   * Provides the 2D rendering context for the drawing surface of the
   * [HTMLCanvasElement].
   */
  val cxt: CanvasRenderingContext2D
    get() {
      return canvas.getContext("2d") as CanvasRenderingContext2D
    }

  /**
   * Width of the [HTMLCanvasElement].
   */
  val width: Int
    get() {
      return canvas.width
    }

  /**
   * Height of the [HTMLCanvasElement].
   */
  val height: Int
    get() {
      return canvas.height
    }

  /**
   * Event on click of the [HTMLCanvasElement].
   */
  val onclick = { event: Event ->
    val pos = event.asDynamic()
    console.logWithTime("Canvas click registered at (${pos.x}, ${pos.y}).")
    val params = arrayOf(pos.x, pos.y)
    val tych = json(Pair("tych", params))
    gameSocket.send(JSON.stringify(tych))
  }

  init {
    console.logWithTime("Initializing canvas...")
    canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = width
    context.canvas.height = height
    document.body!!.appendChild(canvas)
    canvas.onclick = onclick
  }
}