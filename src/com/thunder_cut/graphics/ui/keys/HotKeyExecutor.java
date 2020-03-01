/*
 * HotKeyExecutor.java
 * Author : 나상혁
 * Created Date : 2020-02-09
 */
package com.thunder_cut.graphics.ui.keys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class HotKeyExecutor {

    private Set<Integer> keyCodes;
    private static HotKeyExecutor executor;

    private HotKeyExecutor(){

        keyCodes = new HashSet<>();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(key -> {
            checkKeyCommand(key);
            return false;
        });
    }

    public static void initialize(){
        if(executor == null)
            executor = new HotKeyExecutor();
    }

    public void checkKeyCommand(KeyEvent key){

        if(key.getID() == KeyEvent.KEY_PRESSED){
            keyCodes.add(key.getKeyCode());
            return;
        }
        else if(key.getID() == KeyEvent.KEY_RELEASED){
            keyCodes.remove(key.getKeyCode());
            return;
        }

        for(HotKey hotKey : HotKey.values()){
            if(hotKey.isMatched(keyCodes)){
                hotKey.execute();
                break;
            }
        }
    }
}
