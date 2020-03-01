/*
 * Theme.java
 * Author : 나상혁
 * Created Date : 2020-02-11
 */
package com.thunder_cut.graphics.ui.theme;

import java.awt.*;

public enum Theme {



    DARK(Color.BLACK, Color.GRAY, Color.BLACK, Color.DARK_GRAY, Color.WHITE),
    WHITE(new Color(215,220,240), new Color(250,250,250), Color.BLACK, new Color(225,230,255), Color.DARK_GRAY),

    CURRENT(Theme.DARK),

    ;

    public final Color background;
    public final Color primary;
    public final Color secondary;
    public final Color onPrimary;
    public final Color onSecondary;
    public final Font font;

    Theme(Color background, Color primary, Color onPrimary, Color secondary, Color onSecondary){
        this.background = background;
        this.primary = primary;
        this.secondary = secondary;
        this.onPrimary = onPrimary;
        this.onSecondary = onSecondary;
        font = Font.getFont(Font.SANS_SERIF);
    }

    Theme(Theme theme){
        this.background = theme.background;
        this.primary = theme.primary;
        this.secondary = theme.secondary;
        this.onPrimary = theme.onPrimary;
        this.onSecondary = theme.onSecondary;
        font = theme.font;
    }

}
