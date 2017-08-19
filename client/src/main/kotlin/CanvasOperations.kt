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
    tychs.forEach {
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

fun Canvas.shrinkTychs() {
    val deadTychs = mutableSetOf<Tych>()
    tychs.forEach {
        it.currentRadius -= it.shrinkSpeed
        if (it.currentRadius <= 0) {
            deadTychs.add(it)
        }
    }

    if (deadTychs.isNotEmpty()) {
        console.logWithTime("Removing dead tychs...")
        tychs.removeAll(deadTychs)
    }
}