package slava.crossword.ui;

import java.awt.event.*;
import javax.swing.*;
import slava.crossword.action.*;

public class CrosswordMenu extends JMenuBar {
    ActionManager manager;

    public CrosswordMenu() {}

    public void setActionManager(ActionManager m) {
        manager = m;
    }

    public void init() {
        JMenu menu = new JMenu("File");
        createMenuItem(menu, ActionManager.OPEN, KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
        createMenuItem(menu, ActionManager.SAVE, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		createMenuItem(menu, ActionManager.SAVE_ANSWERS, null);
		createMenuItem(menu, ActionManager.SAVE_AS_NUMBERED, null);
        createMenuItem(menu, ActionManager.EXIT, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_MASK));
        add(menu);
        menu = new JMenu("Edit");
        createMenuItem(menu, ActionManager.SET_SIZE, KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.ALT_MASK));
        createMenuItem(menu, ActionManager.CLEAN, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
		createMenuItem(menu, ActionManager.UNDO, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
		createMenuItem(menu, ActionManager.REDO, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
        add(menu);
        menu = new JMenu("Run");
        createMenuItem(menu, ActionManager.STATISTICS);
        menu.addSeparator();
        createMenuItem(menu, ActionManager.RUN, KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.CTRL_MASK));
        add(menu);
        menu = new JMenu("Dictionary");
        createMenuItem(menu, ActionManager.EDIT_SOURCES, KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.ALT_MASK));
        menu.addSeparator();
        createMenuItem(menu, ActionManager.EXCLUDE, KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.ALT_MASK));
        createMenuItem(menu, ActionManager.CLEAN_EXCLUDED, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
        createMenuItem(menu, ActionManager.LIMIT_WORDBASE, null);
        add(menu);
    }

    protected void createMenuItem(JMenu menu, String name) {
        createMenuItem(menu, name, null);
    }

    protected void createMenuItem(JMenu menu, String name, KeyStroke accelerator) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(manager);
        menu.add(item);
        item.setActionCommand(name);
        if(accelerator != null) item.setAccelerator(accelerator);
    }

}
