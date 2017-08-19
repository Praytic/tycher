import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
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
 * Puts value into [MutableMap] and removes it after [storageTime].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych): Tych? {
    val result = put(key, value)

    Timer(true).schedule(value.lifeDuration()) { remove(key) }
    return result
}