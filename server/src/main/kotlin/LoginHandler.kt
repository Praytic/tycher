import com.google.gson.JsonElement
import org.eclipse.jetty.websocket.api.Session

class LoginHandler : MessageHandler() {

    override fun handle(session: Session, message: JsonElement) {
        users[session] = fromJson<User>(message)
    }
}