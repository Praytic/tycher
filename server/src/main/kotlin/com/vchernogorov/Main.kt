@file:JvmName("Main")

package com.vchernogorov

import com.google.gson.GsonBuilder
import com.vchernogorov.Command.*
import com.vchernogorov.GameConf.LOG_REFRESH_PER_SEC
import com.vchernogorov.GameConf.SCOREBOARD_REFRESH_PER_SEC
import com.vchernogorov.GameConf.SECOND_TO_MILLIS
import com.vchernogorov.GameConf.TYCH_REFRESH_PER_SEC
import com.vchernogorov.PlayerConf.SCOREBOARD_LIMIT
import com.vchernogorov.adapter.scoreboardRequestAdapter
import com.vchernogorov.adapter.tychAdapter
import com.vchernogorov.adapter.userAdapter
import com.vchernogorov.handler.LoginHandler
import com.vchernogorov.handler.LogoutHandler
import com.vchernogorov.handler.ScoreboardHandler
import com.vchernogorov.handler.TychHandler
import com.vchernogorov.task.LogTask
import com.vchernogorov.task.SendScoreboardTask
import com.vchernogorov.task.SendTychsTask
import com.vchernogorov.websocket.MainWebSocket
import mu.KotlinLogging
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler
import org.eclipse.jetty.websocket.api.Session
import spark.Spark.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

val log = KotlinLogging.logger {}

/**
 * Serialization library.
 */
val gson = GsonBuilder()
    .registerTypeAdapter(Tych::class.java, tychAdapter)
    .registerTypeAdapter(Scoreboard::class.java, scoreboardRequestAdapter)
    .registerTypeAdapter(User::class.java, userAdapter)
    .create()

/**
 * Map of [Session]s to [User]s. If the [User] is not presented in this map
 * then he is treated as unauthorized user.
 */
val users: MutableMap<Session, User?> = ConcurrentHashMap()

/**
 * Map of [User]s to [Tych]s. Only one [Tych] is permitted per user. [User]
 * cannot make another [Tych] if he is presented as a key in this map.
 */
val tychs: MutableMap<User, Tych> = ConcurrentHashMap()

val scoreboard = Scoreboard(SCOREBOARD_LIMIT)

/**
 * Map of [Command]s to [MessageHandler]s. Each command should have a
 * [MessageHandler].
 */
val commandHandlerMapper = mapOf(
    TYCHS to TychHandler(tychs),
    DUMMY_TYCH to TychHandler(tychs),
    SCOREBOARD to ScoreboardHandler(),
    LOGIN to LoginHandler(),
    LOGOUT to LogoutHandler()
)

val executor = ScheduledExecutorScheduler()

fun main(args: Array<String>) {
  val projectDir = System.getProperty("user.dir")
  val staticDir = "/client/build/web"
  staticFiles.externalLocation(projectDir + staticDir)

  val appPort = System.getenv("PORT")
  if (appPort != null) {
    port(appPort.toInt())
  }
  println(Tych().getDefaults())
  webSocket("/game", MainWebSocket::class.java)
  startGameLoop();
  init()
}

fun startGameLoop() {
  executor.schedule(SendTychsTask(tychs.values),
      (SECOND_TO_MILLIS/TYCH_REFRESH_PER_SEC).toLong(), TimeUnit.MILLISECONDS)
  executor.schedule(SendScoreboardTask(scoreboard),
      (SECOND_TO_MILLIS/SCOREBOARD_REFRESH_PER_SEC).toLong(), TimeUnit.MILLISECONDS)
  executor.schedule(LogTask(), (SECOND_TO_MILLIS/LOG_REFRESH_PER_SEC).toLong(), TimeUnit.MILLISECONDS)
}