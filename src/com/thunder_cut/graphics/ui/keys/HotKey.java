/*
 * HotKey.java
 * Author : 나상혁
 * Created Date : 2020-02-11
 */
package com.thunder_cut.graphics.ui.keys;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.awt.event.KeyEvent.*;

public enum HotKey {

    BRUSH(VK_CONTROL, VK_SHIFT, VK_B),
    ERASER(VK_CONTROL, VK_SHIFT, VK_E),
    BRUSH_SIZE_UP(VK_CONTROL, VK_SHIFT, VK_W),
    BRUSH_SIZE_DOWN(VK_CONTROL, VK_SHIFT, VK_Q),
    COLOR_SELECT(VK_CONTROL, VK_SHIFT, VK_C, VK_S),
    AREA_SELECT(VK_CONTROL, VK_SHIFT, VK_A, VK_S),

    REDO(VK_CONTROL, VK_SHIFT, VK_Z),
    UNDO(VK_CONTROL, VK_Z)

    ;

    private Set<Integer> keyCodes;
    private Runnable action;

    /**
     * Register Default Key set for execute an action.
     * @param defaultKeyCode KeyCode from {@link KeyEvent#getKeyCode()}
     *                       or
     *                       Virtual key Constants in {@link KeyEvent}
     *                       like {@link KeyEvent#VK_0}
     * @see KeyEvent
     */
    HotKey(int... defaultKeyCode){
        this.keyCodes = new HashSet<>();
        for(int event : defaultKeyCode){
            this.keyCodes.add(event);
        }
        action = ()->{
            String sb = "Hot-key Function \"" + this.name() + "\" is not registered.\n" +
                    "Registered Key set is \"" +
                    keyCodes.stream()
                            .map(KeyEvent::getKeyText)
                            .collect(Collectors.joining(" + ")) +
                    "\".";
            System.err.println(sb);
        };
    }

    /**
     * Register action for execute when pressed key set.
     * @param action An action what you execute.
     * @see Runnable
     */
    public void setAction(Runnable action){
        this.action = action;
    }

    /**
     * Change Key set for execute an action.
     * @param keyCodes KeyCode from {@link KeyEvent#getKeyCode()}
     *                 or
     *                 Virtual key Constants in {@link KeyEvent}
     *                 like {@link KeyEvent#VK_0}
     * @see KeyEvent
     */
    public void setKeyCodes(int... keyCodes){
        this.keyCodes.clear();
        for(int event : keyCodes){
            this.keyCodes.add(event);
        }
    }

    boolean isMatched(final Set<Integer> keys){
        return (keys.containsAll(this.keyCodes) && this.keyCodes.containsAll(keys));
    }

    void execute(){
        this.action.run();
    }

}
