package com.thunder_cut.netio;

/**
 * Enum class for identify command
 */
public enum ChatCommands {

    SET_NAME("/set_name "),
    IGNORE("/ignore "),
    BLIND("/blind "),
    KICK("/kick "),
    OP("/op ");

    public final String command;

    ChatCommands(String command) {
        this.command = command;
    }
}
