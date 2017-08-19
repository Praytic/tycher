import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T): String {
    val commandPair = Pair(src.command.toString(), gson.toJsonTree(src, T::class.java))
    val jsonCommand = JsonObject().plus(commandPair)
    return gson.toJson(jsonCommand)
}