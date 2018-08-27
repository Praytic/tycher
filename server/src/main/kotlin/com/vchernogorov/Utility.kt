package com.vchernogorov

import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.eclipse.jetty.websocket.api.Session
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This method wraps [Message] to another json object providing it
 * the [Command] name and then converts it to json.
 */
inline fun <reified T : Message> Gson.toJsonMessage(src: T, command: Command = src.command): String {
  val commandPair = Pair(command.toString(), toJsonTree(src, T::class.java))
  val jsonCommand = JsonObject().plus(commandPair)
  return toJson(jsonCommand)
}

inline fun <reified T : Message> Gson.toJsonMessage(src: List<T>, command: Command): String {
  val srcType = object : TypeToken<List<T>>(){}.type
  val commandPair = Pair(command.toString(), toJsonTree(src, srcType))
  val jsonCommand = JsonObject().plus(commandPair)
  return toJson(jsonCommand)
}

/**
 * Puts a [Tych] into [MutableMap] and removes it after [Tych.lifeDurationMillis].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych, now: Date = Date()) {
  put(key, value)
  val lifeDuration = value.getLifeDurationMillis(now)

  Timer().schedule(object : TimerTask() {
    override fun run() {
      val currentValue = get(key)
      if (value == currentValue) {
        if (remove(key) != null) {
          val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(Date().time)
          log.info {
            "$value was removed after ${lifeDuration / GameConf.SECOND_TO_MILLIS} seconds. " +
                "Timestamp in seconds: $nowSeconds."
          }
        }
      } else {
        log.warn { "$value has already been removed from the map." }
      }
    }
  }, lifeDuration)
  val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(now.time)
  log.info { "$value will be removed after ${lifeDuration / GameConf.SECOND_TO_MILLIS} seconds. " +
      "Timestamp in seconds: $nowSeconds." }
}

/**
 * Returns default values for each field in a [Tych].
 * Should be manually updated after adding/removing/renaming a field.
 */
fun Tych.getDefaults() = mutableMapOf(
      "ScoreReductionPerMillis" to getScoreReductionPerMillis(),
      "Radius" to getRadius(),
      "ShrinkSpeedRadius" to getShrinkSpeedRadius(),
      "LifeDurationMillis" to getLifeDurationMillis(),
      "CurrentRadius" to getCurrentRadius(),
      "LifetimeMillis" to getLifeDurationMillis(),
      "tycher" to tycher,
      "position" to position,
      "spawnTime" to spawnTime
)

fun getActiveUsers(): Map<Session, User> = users
    .filter { it.key.isOpen }
    .filter { it.value != null }
    .map { it.key to it.value!! }
    .toMap()
