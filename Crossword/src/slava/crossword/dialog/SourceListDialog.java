package slava.crossword.dialog;

import java.io.*;
import java.util.*;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import slava.ui.action.*;
import slava.crossword.ui.*;
import slava.ui.dialog.*;

public class SourceListDialog extends OkCancelWizard {
    DefaultListModel listModel;
    JList list;
    List sources;
    CommandBar modifyBar;

    public SourceListDialog() {}

    public void setData(List sources) {
        this.sources = sources;
    }

    public int execute(Component parent) {
        for (int i = 0; i < sources.size(); i++)
          listModel.addElement(sources.get(i));
        return super.execute(parent);
    }

    protected void onWindowOpened() {
        list.requestFocus();
    }

    public void action(String command) {
        if("Add".equals(command)) {
            JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
            if(listModel.size() > 0) {
                String fn = (String)listModel.getElementAt(listModel.size() - 1);
                File f = new File(fn);
                if(f.exists()) {
                    fc.setCurrentDirectory(f.getParentFile());
                }
            }
            int i = fc.showOpenDialog(panel);
            if(i != fc.APPROVE_OPTION) return;
            listModel.addElement(fc.getSelectedFile().getAbsolutePath().replace('\\', '/'));
        } else if("Delete".equals(command)) {
            Object o = list.getSelectedValue();
            if(o != null) listModel.removeElement(o);
        } else {
            super.action(command);
        }
    }

    protected boolean ok() {
        sources.clear();
        for (int i = 0; i < listModel.getSize(); i++)
          sources.add(listModel.getElementAt(i));
        return true;
    }

    protected JComponent createInputPanel() {
        modifyBar = new CommandBar();
        modifyBar.setOrientation(BoxLayout.Y_AXIS);
        modifyBar.setCommands(new String[]{"Add", "Delete"});
        modifyBar.addCommandBarListener(this);
        JPanel p = new JPanel(new BorderLayout());
        listModel = new DefaultListModel();
        list = new JList(listModel);
        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(300, 200));
        p.add(sp, BorderLayout.CENTER);
        p.add(modifyBar.getComponent(), BorderLayout.EAST);
        return p;
    }

}
