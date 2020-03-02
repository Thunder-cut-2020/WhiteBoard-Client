package com.thunder_cut.netio;

import java.util.function.Consumer;

public enum Command {
    HELP("/HELP"),
    NAME("/NAME "),
    IGNORE("/IGNORE "),
    KICK("/KICK "),
    OP("/OP ")
    ;

    public final String command;
    private Consumer<String> action;

    Command(String command){
        this.command = command;
    }

    public void run(String str) {
        this.action.accept(str);
    }

    public void addAction(Consumer<String> action) {
        this.action = action;
    }
}
