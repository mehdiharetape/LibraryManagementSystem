package com.library.ui.reportUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

    public TextAreaRenderer(){
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        setText(value == null ? "" : value.toString());
        setSize(table.getColumnModel().getColumn(column).getWidth(),
                getPreferredSize().height);
        int preferredHeight = getPreferredSize().height;
        if(table.getRowHeight(row) != preferredHeight)
            table.setRowHeight(row, preferredHeight);
        return this;
    }
}
