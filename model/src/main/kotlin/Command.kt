/**
 * [Command] entity is required to map [MessageHandler] to the json message
 * which is received from frontend.
 */
enum class Command {

    /**
     * As request: Indicates [Tych] creation from [User].
     */
    TYCH,
    DUMMY_TYCH,
    REMOVE_TYCH,
    LOGIN,
    SCOREBOARD,
    FOOD;

    override fun toString(): String = super.toString().toLowercaseCamel()
}