import kotlin.js.Console
import kotlin.js.Date

/**
 * Extended logging method which prints current time.
 */
fun Console.logWithTime(vararg o: Any?) {
    console.log("${Date().getTime()} > ${o}")
}

