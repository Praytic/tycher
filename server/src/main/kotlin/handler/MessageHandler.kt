package handler

import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session
import users
import Message
import User

/**
 * Template for all handlers. Contains [handle] method for handling
 * received request from [Session] via stringified [JSONObject] called [message].
 */
abstract class MessageHandler<V : Message> {

    /**
     * Method-wrapper for [handle] which extracts current [User] from [Session].
     *
     * @throws IllegalStateException when [User] is not authorized.
     */
    fun handle(session: Session, message: JsonElement) {
        val user = users[session] ?:
                throw IllegalStateException("Tycher can't be an unauthorized user.")
        handle(user, session, parse(message))
    }

    abstract fun parse(message: JsonElement): V

    /**
     * Handles a generic [Message] provided by [User].
     */
    abstract fun handle(user: User, session: Session, message: V)
}