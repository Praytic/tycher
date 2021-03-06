package com.vchernogorov.handler

import com.google.gson.JsonElement
import com.vchernogorov.*
import com.vchernogorov.exception.UndefinedEntityException
import kotlinx.coroutines.experimental.Job
import org.eclipse.jetty.websocket.api.Session
import java.util.concurrent.ConcurrentHashMap

/**
 * Extension of [MessageHandler] for [TychRequest] entity.
 */
class TychHandler(val tychs: MutableMap<User, Tych>,
                  val jobs: MutableMap<Tych, Job> = ConcurrentHashMap()) : MessageHandler<Tych>() {

  override fun parse(message: JsonElement) =
      gson.fromJson(message, Tych::class.java)

  override fun handle(user: User, session: Session, message: Tych): Boolean {
    log.info { "Handling $message." }
    val tych = Tych(message.position, message.spawnTime, user)
    if (user.isClickable(tychs[user])) {
      val consumedTychs = consumeTychsBy(user, tych)
      for (it in consumedTychs) {
        val job = jobs.remove(it) ?: continue
        job.cancel()
      }
      val job = tychs.putTemp(user, tych)
      jobs[tych] = job
      return true
    } else {
      return false
    }
  }

  /**
   * Removes [Tych]s which were overlapped by specified [tych].
   * Returns consumed [Tych]s. They will no longer be presented in [tychs].
   */
  fun consumeTychsBy(user: User, tych: Tych): List<Tych> {
    val consumedTychs = tych.getAllConsumedTychs(tychs.values)
    val gainedScore = consumedTychs.map { it to it.getCurrentScore() }.toMap()
    user.score += gainedScore.values.sum()
    consumedTychs.forEach {
      val tycherScore = it.getCurrentScore()
      val removedTych = tychs.remove(it.tycher)
          ?: throw UndefinedEntityException(tych)
      removedTych.tycher.score -= tycherScore
      log.info { "$tych consumed $removedTych and gains $tycherScore points." }
    }
    return consumedTychs
  }
}
