package com.thunder_cut.netio;

public enum Command {
    NAME("/NAME "),
    IGNORE("/IGNORE "),
    KICK("/KICK "),
    OP("/OP ")
    ;

    public final String command;

    Command(String command){
        this.command = command;
    }
}
