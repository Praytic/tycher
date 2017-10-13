import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T): String {
  val commandPair = Pair(src.command.toString(),
                         gson.toJsonTree(src, T::class.java))
  val jsonCommand = JsonObject().plus(commandPair)
  return gson.toJson(jsonCommand)
}

/**
 * Puts a [Tych] into [MutableMap] and removes it after [Tych.lifeDurationMillis].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych) {
  put(key, value)
  val lifeDuration = value.getLifeDurationMillis()

  Timer(true).scheduleAtFixedRate(object : TimerTask() {
    override fun run() {
      remove(key)
    }
  }, 0L, lifeDuration)
  log.info { "$value will be removed after ${lifeDuration / 1000} seconds." }
}