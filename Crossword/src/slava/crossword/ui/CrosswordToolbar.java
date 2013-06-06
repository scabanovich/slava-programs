package slava.crossword.ui;

import javax.swing.*;
import slava.crossword.action.*;
import slava.ui.util.*;

public class CrosswordToolbar {
    protected JPanel panel = new JPanel();
    protected ActionManager manager;
    protected ColorModeChooser colorMode = new ColorModeChooser();

    public CrosswordToolbar() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    public JComponent getComponent() {
        return panel;
    }

    public void setActionManager(ActionManager manager) {
        this.manager = manager;
        colorMode.setModel(manager.getModel());
    }

    public void init() {
        JToolBar bar = createToolBar();
        createToolBarButton(bar, ActionManager.SAVE, "images/action/save.gif");
        panel.add(Box.createHorizontalStrut(5));
        bar = createToolBar();
        createToolBarButton(bar, ActionManager.SET_SIZE, "images/action/resize.gif");
        bar.add(colorMode.getComponent());
        createToolBarButton(bar, ActionManager.CLEAN, "images/action/delete.gif");
        panel.add(Box.createHorizontalStrut(5));
        bar = createToolBar();
        createToolBarButton(bar, ActionManager.RUN, "images/action/start.gif");
        panel.add(Box.createHorizontalStrut(5));
        bar = createToolBar();
        createToolBarButton(bar, ActionManager.EXCLUDE, "images/action/cut.gif");
    }

    protected JToolBar createToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        panel.add(bar);
        return bar;
    }

    protected void createToolBarButton(JToolBar bar, String name, String image) {
        Icon icon = Resource.getImage(image);
        JButton b = new JButton(icon);
        b.setToolTipText(name);
        b.addActionListener(manager);
        bar.add(b);
        b.setActionCommand(name);
    }

}
