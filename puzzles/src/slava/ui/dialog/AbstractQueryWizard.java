package slava.ui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import slava.ui.action.*;

public abstract class AbstractQueryWizard implements CommandBarListener {
    protected JPanel panel = new JPanel(new BorderLayout());
    protected CommandBar bar;
    protected int returnType = -1;
    protected JDialog dialog;
    protected String title = "";

    public AbstractQueryWizard() {
        bar = createCommandBar();
        bar.addCommandBarListener(this);
        build();
    }

    protected CommandBar createCommandBar() {
        return CommandBar.getOkCancelInstance();
    }

    public void setInput(Object object) {}

    public int execute(Component parent) {
        returnType = -1;
        dialog = new JDialog((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, parent), title, true);
        dialog.setContentPane(panel);
        dialog.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent e) {
                onWindowOpened();
            }
            public void windowClosing(WindowEvent e) {
                onWindowClosed();
            }
        });
        dialog.pack();
        Dimension sz = dialog.getSize();
        int w = getMinimumDialogWidth();
        if(w > 0 && w > sz.width) dialog.setSize(w, sz.height);
        UIUtil.center(dialog);
        dialog.setVisible(true);
        return returnType;
    }

    protected int getMinimumDialogWidth() {
        return 300;
    }

    protected void onWindowOpened() {}

    protected void onWindowClosed() {
        dispose();
    }

    protected void dispose() {
        if(dialog == null) return;
        dialog.setVisible(false);
        dialog = null;
    }

    public void action(String command) {
    }

    protected void build() {
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createInputPanel(), BorderLayout.CENTER);
        JComponent c = createButtonPanel();
        c.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(c, BorderLayout.SOUTH);
    }

    protected abstract JComponent createInputPanel();

    private JComponent createButtonPanel() {
        return bar.getComponent();
    }

}
