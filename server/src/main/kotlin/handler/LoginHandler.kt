package handler

import com.google.gson.JsonElement
import gson
import org.eclipse.jetty.websocket.api.Session
import users
import Login
import User

class LoginHandler : MessageHandler<Login>() {

    override fun parse(message: JsonElement) = gson.fromJson(message, Login::class.java)

    override fun handle(user: User, session: Session, message: Login) {
        users[session] = User(name = message.username)
    }
}