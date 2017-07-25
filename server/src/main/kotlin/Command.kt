/**
 * [Command] entity is required to map [MessageHandler] to the
 * json message which is received from frontend.
 */
enum class Command(val handler: MessageHandler) {
    TYCH(TychHandler()),
    DUMMY_TYCH(TychHandler()),
    GREETINGS(LoginHandler()),
    SCOREBOARD(ScoreboardHandler());

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}