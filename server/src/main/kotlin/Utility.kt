import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import Command
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

  Timer().schedule(object : TimerTask() {
    override fun run() {
      if (remove(key) != null) {
        val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(Date().time)
        log.info { "$value was removed after ${lifeDuration / 1000.0} seconds. " +
            "Timestamp in seconds: $nowSeconds." }
      }
    }
  }, lifeDuration)
  val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(now.time)
  log.info { "$value will be removed after ${lifeDuration / 1000.0} seconds. " +
      "Timestamp in seconds: $nowSeconds." }
}

fun Tych.getDefaults(): Map<String, Any> {
  val tychValues = mutableMapOf<String, Any>()
  tychValues["ScoreReductionPerMillis"] = this.getScoreReductionPerMillis()
  tychValues["Radius"] = this.getRadius()
  tychValues["ShrinkSpeedRadius"] = this.getShrinkSpeedRadius()
  tychValues["LifeDurationMillis"] = this.getLifeDurationMillis()
  tychValues["CurrentRadius"] = this.getCurrentRadius()
  tychValues["LifetimeMillis"] = this.getLifetimeMillis()
  tychValues["tycher"] = this.tycher
  tychValues["position"] = this.position
  tychValues["spawnTime"] = this.spawnTime
  return tychValues
}