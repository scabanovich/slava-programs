package slava.crossword.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import slava.ui.util.*;

public class ColorModeChooser implements ActionListener {
    static int BLACK = 0x000000;
    static int GREY = 0x7f7f5f;
    static int WHITE = 0xffffff;
    CrosswordComponentModel model;
    JButton button = new JButton(Resource.getEmptyIcon(16, 16));

    public ColorModeChooser() {
        button.addActionListener(this);
        button.setBackground(new Color(BLACK));
        button.setOpaque(true);
        button.setToolTipText("Brush color");
        button.setFocusPainted(false);
    }

    public void setModel(CrosswordComponentModel model) {
        this.model = model;
        update();
    }

    public JComponent getComponent() {
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        model.nextColorMode();
        update();
    }

    public void update() {
        int mode = model.getColorMode();
        int c = (mode == 0) ? BLACK : (mode == 1) ? WHITE : GREY;
        String tip = (mode == 0) ? "Black/White" : (mode == 1) ? "Erase letters" : "Grey/White";
        button.setBackground(new Color(c));
        button.setToolTipText(tip);
    }

}
