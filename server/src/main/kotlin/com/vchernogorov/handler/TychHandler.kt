package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.*
import com.vchernogorov.exception.UndefinedEntityException
import org.eclipse.jetty.util.thread.Scheduler
import org.eclipse.jetty.websocket.api.Session
import java.util.concurrent.ConcurrentHashMap

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler(val tychs: MutableMap<User, Tych>,
                  val tasks: MutableMap<Tych, Scheduler.Task> = ConcurrentHashMap()) : MessageHandler<Tych>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Tych::class.java)

  override fun handle(user: User, session: Session, message: Tych) {
    log.info { "Handling $message." }
    val tych = Tych(message.position, message.spawnTime, user)
    if (user.isClickable(tychs[user])) {
      val consumedTychs = consumeTychsBy(user, tych)
      for (it in consumedTychs) {
        val timer = tasks.remove(it) ?: continue
        timer.cancel()
      }
      val timer = tychs.putTemp(user, tych)
      tasks[tych] = timer
    }
  }

  /**
   * Removes [Tych]s which were overlapped by specified [tych].
   * Returns consumed [Tych]s. They will no longer be presented in [tychs].
   */
  fun consumeTychsBy(user: User, tych: Tych): List<Tych> {
    val consumedTychs = tych.getAllConsumedTychs(tychs.values)
    val gainedScore = consumedTychs.map { it to it.calculateScore() }.toMap()
    user.score += gainedScore.values.sum()
    consumedTychs.forEach {
      val tycherScore = it.calculateScore()
      val removedTych = tychs.remove(it.tycher)
          ?: throw UndefinedEntityException(tych)
      removedTych.tycher.score -= tycherScore
      log.info { "$tych consumed $removedTych and gains $tycherScore points." }
    }
    return consumedTychs
  }
}
