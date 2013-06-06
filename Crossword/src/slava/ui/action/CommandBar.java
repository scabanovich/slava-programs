package slava.ui.action;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;

public class CommandBar implements ActionListener {
    private HashMap buttons = new HashMap();
    protected JPanel panel = new JPanel(new BorderLayout());
    protected CommandBarListener listener;
    private boolean shownames = true;
    private int orientation = BoxLayout.X_AXIS;

    public CommandBar() {
        setOrientation(BoxLayout.X_AXIS);
    }

    public void setOrientation(int o) {
        orientation = o;
        panel.setLayout(new BoxLayout(panel, orientation));
        if(isHorisontal()) {
            panel.setBorder(BorderFactory.createEmptyBorder(9, 0, 0, 0));
        } else {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }
    }

    public boolean isHorisontal() {
        return orientation == BoxLayout.X_AXIS;
    }

    public static CommandBar getOkCancelInstance() {
        CommandBar c = new CommandBar();
        c.setCommands(new String[]{"OK", "Cancel"});
        c.registerKeyboardAction("OK", KeyEvent.VK_ENTER);
        c.registerKeyboardAction("Cancel", KeyEvent.VK_ESCAPE);
        return c;
    }

    public void setShowNames(boolean b) {
        if(shownames == b) return;
        shownames = b;
        Iterator it = buttons.keySet().iterator();
        while(it.hasNext()) {
            String command = (String)it.next();
            JButton button = (JButton)buttons.get(command);
            button.setText((shownames) ? command : "");
            button.setToolTipText((!shownames) ? command : "");
        }
        setButtonsSize();
    }

    public void setCommands(String[] names) {
        panel.removeAll();
        panel.add(Box.createHorizontalGlue());
        for (int i = 0; i < names.length; i++) {
            JButton b = new XButton("");
            if(shownames) b.setText(names[i]); else b.setToolTipText(names[i]);
            b.setActionCommand(names[i]);
            b.addActionListener(this);
            b.setMargin(new InsetsUIResource(0, 6, 0, 6));
            panel.add(b);
            if (i < names.length - 1) {
                if(isHorisontal()) {
                    panel.add(Box.createHorizontalStrut(9));
                } else {
                    panel.add(Box.createVerticalStrut(5));
                }
            }
            buttons.put(names[i], b);
        }
        setButtonsSize();
        panel.validate();
    }

    public void addCommandBarListener(CommandBarListener listener) {
        this.listener = listener; 
    }

    public JComponent getComponent() {
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        listener.action(e.getActionCommand());
    }

    public void setEnabled(String command, boolean enabled) {
        JButton b = (JButton)buttons.get(command);
        if(b != null) b.setEnabled(enabled);
    }

    public void setIcon(String command, ImageIcon icon) {
        JButton b = (JButton)buttons.get(command);
        if(b != null) {
            b.setIcon(icon);
            b.revalidate();
            setButtonsSize();
        }
    }

    public void registerKeyboardAction(String command, int key) {
        panel.registerKeyboardAction(new ControlKeyAction(command),
            KeyStroke.getKeyStroke(key, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    Dimension d = null;

    protected void setButtonsSize() {
		    int nMaxWidth = 0, nMaxHeight = 0;
		    Dimension objSize;
		    Iterator objIterator = buttons.values().iterator();
		    while (objIterator.hasNext()) {
			      JButton objButton = (JButton)objIterator.next();
            objButton.setPreferredSize(null);
			      objSize = objButton.getPreferredSize();
			      nMaxWidth = Math.max(nMaxWidth, objSize.width);
			      nMaxHeight = Math.max(nMaxHeight, objSize.height);
		    }
		    objSize = new Dimension(nMaxWidth, nMaxHeight);
        d = objSize;
		    objIterator = buttons.values().iterator();
		    while (objIterator.hasNext()) {
            ((JButton)objIterator.next()).setPreferredSize(objSize);
        }
	  }

  class XButton extends JButton {
      public XButton(String text) {
          super(text);
      }
      
      public void setBounds(int x, int y, int width, int height) {
          if(d != null) {
              if(width < d.width) width = d.width;
              if(height < d.height) height = d.height;
          }
          super.setBounds(x, y, width, height);
      }
  }

    class ControlKeyAction implements ActionListener {
        String command;

        ControlKeyAction(String command) {
            this.command = command;
        }

        public void actionPerformed(ActionEvent event) {
            CommandBar.this.actionPerformed(new ActionEvent(CommandBar.this, 0, command));
        }
    }


}
