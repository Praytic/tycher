package com.vchernogorov

import com.google.common.base.CaseFormat

/**
 * [Command] entity is required to map [MessageHandler] to the json message
 * which is received from frontend.
 */
enum class Command {

    /**
     * As request: Indicates [Tych] creation from [User].
     */
    TYCHS,
    DUMMY_TYCH,
    REMOVE_TYCH,
    PLAYER_TYCH,
    LOGIN,
    LOGOUT,
    SCOREBOARD,
    FOOD;

    override fun toString(): String = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, super.toString())
}