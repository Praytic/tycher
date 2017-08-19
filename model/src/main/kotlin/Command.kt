import com.google.common.base.CaseFormat.LOWER_CAMEL
import com.google.common.base.CaseFormat.UPPER_UNDERSCORE

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

    override fun toString(): String = UPPER_UNDERSCORE.to(LOWER_CAMEL, super.toString())
}