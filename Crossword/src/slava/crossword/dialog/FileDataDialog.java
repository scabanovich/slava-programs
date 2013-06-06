package slava.crossword.dialog;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import slava.ui.editor.*;
import slava.ui.dialog.*;

public class FileDataDialog extends OkCancelWizard {
    TextFieldEditor pathEditor;
    TextFieldEditor nameEditor;
    protected Properties p;

    public FileDataDialog() {}

    public int execute(Component parent, Properties p) {
        this.p = p;
        title = "" + p.getProperty("title");
        pathEditor.setValue(p.getProperty("path"));
        nameEditor.setValue(p.getProperty("name"));
        return execute(parent);
    }

    protected void onWindowOpened() {
        nameEditor.requestFocus();
    }

    protected JComponent createInputPanel() {
        pathEditor = new TextFieldEditor();
        nameEditor = new TextFieldEditor();
        pathEditor.setName("Path");
        nameEditor.setName("Name");
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(pathEditor.getComponent());
        p.add(Box.createVerticalStrut(5));
        p.add(nameEditor.getComponent());
        p.add(Box.createVerticalBox());
        p.add(Box.createVerticalGlue());
        return p;
    }

    protected boolean ok() {
        p.setProperty("path", pathEditor.getValue());
        p.setProperty("name", nameEditor.getValue());
        return true;
    }

}
