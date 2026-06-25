package com.library.ui.common;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BorderCreator {
    //create border
    public static Border createBorder(String title){
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY),
                        title
                ),
                BorderFactory.createEmptyBorder(10,10,10,10)
        );
        return border;
    }
}
