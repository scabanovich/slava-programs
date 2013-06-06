package slava.crossword.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import slava.crossword.action.*;
import slava.crossword.preference.*;
import slava.ui.dialog.*;

public class CrosswordFrame {
    static JFrame frame;
    JPanel mainpanel = new JPanel(new BorderLayout());
    CrosswordComponent component = new CrosswordComponent();
    ActionManager manager = new ActionManager();
    CrosswordMenu menu = new CrosswordMenu();
    CrosswordToolbar toolbar = new CrosswordToolbar();

    public CrosswordFrame() {
        component.getModel().setDefaultDimensions();
        manager.setModel(component.getModel());
        menu.setActionManager(manager);
        menu.init();
        toolbar.setActionManager(manager);
        toolbar.init();
    }

    public static JFrame frame() {
        return frame;
    }

    public void open() {
        frame = new JFrame();
        frame.setTitle("Crossword");
        JScrollPane sp = new JScrollPane(component);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getVerticalScrollBar().setBlockIncrement(20);
        sp.getHorizontalScrollBar().setBlockIncrement(20);
        sp.getHorizontalScrollBar().setUnitIncrement(20);
        mainpanel.add(sp, BorderLayout.CENTER);
        mainpanel.add(getToolsPanel(), BorderLayout.NORTH);
        frame.setContentPane(mainpanel);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                component.requestFocus();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                manager.actionPerformed(new ActionEvent(frame, ActionEvent.ACTION_FIRST, ActionManager.EXIT));
            }
        });
        manager.registerAction(ActionManager.EXIT, new ExitAction());
        frame.pack();
        Rectangle r = loadFrameBounds();
        if(r == null) {
            frame.setSize(400, 300);
            UIUtil.center(frame);
        } else {
            frame.setBounds(r);
        }
        frame.show();
    }

    private JComponent getToolsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(menu, BorderLayout.NORTH);
        panel.add(toolbar.getComponent(), BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
    	if(args != null && args.length > 0) {
    		CrosswordPreference.location = args[0];
    	}
        new CrosswordFrame().open();
    }

    class ExitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Frame f = frame;
            frame = null;
            if(f == null) return;
            saveFrameBounds(f);
            CrosswordPreference p = CrosswordPreference.getInstance();
            p.save();
            f.setVisible(false);
            Runtime.getRuntime().exit(0);
        }
    }

    Rectangle loadFrameBounds() {
        CrosswordPreference p = CrosswordPreference.getInstance();
        int x = p.getInt("frame.x", -1),
            y = p.getInt("frame.y", -1),
            w = p.getInt("frame.width", -1),
            h = p.getInt("frame.height", -1);
        return (x < 0 || y < 0 || w < 0 || h < 0) ? null : new Rectangle(x, y, w, h);
    }

    void saveFrameBounds(Frame f) {
        Rectangle bounds = f.getBounds();
        CrosswordPreference p = CrosswordPreference.getInstance();
        p.setInt("frame.x", bounds.x);
        p.setInt("frame.y", bounds.y);
        p.setInt("frame.width", bounds.width);
        p.setInt("frame.height", bounds.height);
    }

}