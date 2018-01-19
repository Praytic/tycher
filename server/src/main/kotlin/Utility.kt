import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T, command: Command = src.command): String {
  val commandPair = Pair(command.toString(),
                         gson.toJsonTree(src, T::class.java))
  val jsonCommand = JsonObject().plus(commandPair)
  return gson.toJson(jsonCommand)
}

/**
 * Puts a [Tych] into [MutableMap] and removes it after [Tych.lifeDurationMillis].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych, now: Date = Date()) {
  put(key, value)
  val lifeDuration = value.getLifeDurationMillis(now)

  Timer().scheduleAtFixedRate(object : TimerTask() {
    override fun run() {
      if (remove(key) != null) {
        val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(Date().time)
        log.info { "$value was removed after ${lifeDuration / 1000} seconds. " +
            "Timestamp in seconds: $nowSeconds." }
      }
    }
  }, 0L, lifeDuration)
  val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(now.time)
  log.info { "$value will be removed after ${lifeDuration / 1000} seconds. " +
      "Timestamp in seconds: $nowSeconds." }
}