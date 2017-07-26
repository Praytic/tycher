import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session

/**
 * Template for all game handlers. Contains [handle] method for handling
 * received request via stringified [JSONObject] called [message].
 */
abstract class MessageHandler<V : Message> {

    fun handle(session: Session, message: JsonElement) {
        val user = users[session] ?:
                throw IllegalStateException("Tycher can't be an unauthorized user.")
        handle(user, session, parse(message))
    }

    abstract fun parse(message: JsonElement): V

    /**
     * Executes any logic the handler implements with the
     * provided [message] and [session].
     */
    abstract fun handle(user: User, session: Session, message: V)
}