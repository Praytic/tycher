package com.vchernogorov

import com.github.salomonbrys.kotson.plus
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
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
 * Puts a [Tych] into [MutableMap] and removes it after [Tych.lifeDurationMs].
 */
fun <K> MutableMap<K, Tych>.putTemp(key: K, value: Tych, now: Date = Date()): Job {
  put(key, value)
  val lifetime = value.getLifetimeMs()

  val job = launch {
    delay(lifetime, TimeUnit.MILLISECONDS)
    val currentValue = get(key)
    if (value == currentValue) {
      if (remove(key) != null) {
        val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(Date().time)
        log.info {
          "$value was removed after ${lifetime / GameConf.SECOND_TO_MILLIS} seconds. " +
              "Timestamp in seconds: $nowSeconds."
        }
      }
    } else {
      log.warn { "$value has already been removed from the map." }
    }
  }
  val nowSeconds = TimeUnit.MILLISECONDS.toSeconds(now.time)
  log.info { "$value will be removed after ${lifetime / GameConf.SECOND_TO_MILLIS} seconds. " +
      "Timestamp in seconds: $nowSeconds." }
  return job
}

fun getActiveUsers(): Map<Session, User> = users
    .filter { it.key.isOpen }
    .filter { it.value != null }
    .map { it.key to it.value!! }
    .toMap()
