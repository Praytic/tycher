import com.google.gson.Gson
import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

/**
 * Template for all game handlers. Contains [handle] method for handling
 * received request via stringified [JSONObject] called [message].
 */
abstract class MessageHandler {

    /**
     * Executes any logic the handler implements with the
     * provided [message] and [session].
     */
    abstract fun handle(session: Session, message: JsonElement)

    inline fun <reified T> fromJson(message: JsonElement) = Gson().fromJson(message, T::class.java)
}