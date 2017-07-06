import j2html.TagCreator
import org.eclipse.jetty.websocket.api.*
import org.eclipse.jetty.websocket.api.annotations.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.scheduleAtFixedRate

class Vector2D {
    constructor(x: Int, y: Int) {
        this.x = x;
        this.y = y;
    }

    var x = 0;
    var y = 0;
}

@WebSocket
class ChatWebSocketHandler {

    var nextUserNumber = 1
    val userUsernameMap: MutableMap<Session, String> = ConcurrentHashMap();
    
    val userInputs: MutableMap<Session, JSONObject> = ConcurrentHashMap();
    var userPositions: MutableMap<Session, Vector2D> = ConcurrentHashMap();

    init {
        val timer = Timer("schedule", true);
        timer.scheduleAtFixedRate(0, 1000 / 60) {
            update();
        }
    }

    fun update() {
        // Update positions
        for ((key, value) in userInputs) {
            var keys: JSONArray = value.getJSONArray("keys");

            fun contains(array: JSONArray, value: Int): Boolean {
                for (e in array) {
                    if (e == value) {
                        return true;
                    }
                }
                return false;
            }

            var LEFT = contains(keys, Key.A);
            var RIGHT = contains(keys, Key.D);
            var UP = contains(keys, Key.W);
            var DOWN = contains(keys, Key.S);
            var x = userPositions[key]!!.x;
            var y = userPositions[key]!!.y;
            if (LEFT && !RIGHT) {
                x--;
            } else if (!LEFT && RIGHT) {
                x++;
            } else if (UP && !DOWN) {
                y--;
            } else if (!UP && DOWN) {
                y++;
            }
            userPositions[key]!!.x = x;
            userPositions[key]!!.y = y;
        }

        var positions = mutableListOf<Int>();
        for (entry in userPositions) {
            positions.add(entry.value.x);
            positions.add(entry.value.y);
        }

        // Send data to users
        userUsernameMap.keys.stream().filter({ it.isOpen() }).forEach { session ->
            try {
                session.remote.sendString(JSONObject().put("positions", JSONArray(positions)).toString());
            } catch (e: Exception) {
                session.close();
//                e.printStackTrace();
            }
        }
    }

    @OnWebSocketConnect
    fun onConnect(user: Session) {
        val username = "User" + nextUserNumber++
        userUsernameMap.put(user, username)
        broadcastMessage(sender = "Server", message = "$username joined the chat")
        userPositions.put(user, Vector2D(50, 50));
    }

    @OnWebSocketClose
    fun onClose(user: Session, statusCode: Int, reason: String) {
        val username = userUsernameMap.get(user)
        userUsernameMap.remove(user)
        broadcastMessage(sender = "Server", message = "$username left the chat")
    }

    @OnWebSocketMessage
    fun onMessage(user: Session, message: String) {
        // Receive inputs from user
        try {
            userInputs[user] = JSONObject(message);
        } catch (e: Exception) {
            e.printStackTrace();
        }

//        userUsernameMap.keys.stream().filter({ it.isOpen() && it != user }).forEach { session ->
//            try {
//                session.remote.sendString(JSONObject().put("point", JSONObject(message)).toString());
//            } catch (e: Exception) {
//                e.printStackTrace();
//            }
//        }
    }


    //Sends a message from one user to all users, along with a list of current usernames
    fun broadcastMessage(sender: String, message: String) {
        userUsernameMap.keys.stream().filter({ it.isOpen() }).forEach { session ->
            try {
                session.remote.sendString(JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist", userUsernameMap.values).toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    fun createHtmlMessageFromSender(sender: String, message: String): String {
        return TagCreator.article(
            TagCreator.b("$sender says:"),
            TagCreator.span(TagCreator.attrs(".timestamp"), SimpleDateFormat("HH:mm:ss").format(Date())),
            TagCreator.p(message)
            ).render()
    }

}


object Key {
    val NUMBER_0: Int = 48;
    val NUMBER_1: Int = 49;
    val NUMBER_2: Int = 50;
    val NUMBER_3: Int = 51;
    val NUMBER_4: Int = 52;
    val NUMBER_5: Int = 53;
    val NUMBER_6: Int = 54;
    val NUMBER_7: Int = 55;
    val NUMBER_8: Int = 56;
    val NUMBER_9: Int = 57;
    val A: Int = 65;
    val B: Int = 66;
    val C: Int = 67;
    val D: Int = 68;
    val E: Int = 69;
    val F: Int = 70;
    val G: Int = 71;
    val H: Int = 72;
    val I: Int = 73;
    val J: Int = 74;
    val K: Int = 75;
    val L: Int = 76;
    val M: Int = 77;
    val N: Int = 78;
    val O: Int = 79;
    val P: Int = 80;
    val Q: Int = 81;
    val R: Int = 82;
    val S: Int = 83;
    val T: Int = 84;
    val U: Int = 85;
    val V: Int = 86;
    val W: Int = 87;
    val X: Int = 88;
    val Y: Int = 89;
    val Z: Int = 90;
    val NUMPAD_0: Int = 96;
    val NUMPAD_1: Int = 97;
    val NUMPAD_2: Int = 98;
    val NUMPAD_3: Int = 99;
    val NUMPAD_4: Int = 100;
    val NUMPAD_5: Int = 101;
    val NUMPAD_6: Int = 102;
    val NUMPAD_7: Int = 103;
    val NUMPAD_8: Int = 104;
    val NUMPAD_9: Int = 105;
    val NUMPAD_MULTIPLY: Int = 106;
    val NUMPAD_ADD: Int = 107;
    val NUMPAD_ENTER: Int = 108;
    val NUMPAD_SUBTRACT: Int = 109;
    val NUMPAD_DECIMAL: Int = 110;
    val NUMPAD_DIVIDE: Int = 111;
    val F1: Int = 112;
    val F2: Int = 113;
    val F3: Int = 114;
    val F4: Int = 115;
    val F5: Int = 116;
    val F6: Int = 117;
    val F7: Int = 118;
    val F8: Int = 119;
    val F9: Int = 120;
    val F10: Int = 121;
    val F11: Int = 122;
    val F12: Int = 123;
    val F13: Int = 124;
    val F14: Int = 125;
    val F15: Int = 126;
    val BACKSPACE: Int = 8;
    val TAB: Int = 9;
    val ALTERNATE: Int = 18;
    val ENTER: Int = 13;
    val COMMAND: Int = 15;
    val SHIFT: Int = 16;
    val CONTROL: Int = 17;
    val BREAK: Int = 19;
    val CAPS_LOCK: Int = 20;
    val NUMPAD: Int = 21;
    val ESCAPE: Int = 27;
    val SPACE: Int = 32;
    val PAGE_UP: Int = 33;
    val PAGE_DOWN: Int = 34;
    val END: Int = 35;
    val HOME: Int = 36;
    val LEFT: Int = 37;
    val RIGHT: Int = 39;
    val UP: Int = 38;
    val DOWN: Int = 40;
    val INSERT: Int = 45;
    val DELETE: Int = 46;
    val NUMLOCK: Int = 144;
    val SEMICOLON: Int = 186;
    val EQUAL: Int = 187;
    val COMMA: Int = 188;
    val MINUS: Int = 189;
    val PERIOD: Int = 190;
    val SLASH: Int = 191;
    val BACKQUOTE: Int = 192;
    val LEFTBRACKET: Int = 219;
    val BACKSLASH: Int = 220;
    val RIGHTBRACKET: Int = 221;
    val QUOTE: Int = 222;
};