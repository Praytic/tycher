import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T): String {
    val commandPair = Pair(src.command.toString(), gson.toJsonTree(src, T::class.java))
    val jsonCommand = JsonObject().plus(commandPair)
    return gson.toJson(jsonCommand)
}

/**
 * Puts a [Tych] into [MutableMap] and removes it after [Tych.lifeDuration].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych) {
    put(key, value)
    val lifeDuration = value.lifeDuration()
    Timer(true).schedule(lifeDuration) { remove(key) }
    log.info { "Put ($key, $value) will be removed after ${lifeDuration/1000}" +
            " seconds." }
}