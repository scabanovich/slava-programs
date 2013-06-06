package slava.crossword.dialog;

import java.awt.*;
import javax.swing.*;
import slava.ui.editor.*;
import slava.ui.dialog.*;

public class SetSizeDialog extends OkCancelWizard {
    TextFieldEditor widthEditor;
    TextFieldEditor heightEditor;

    public SetSizeDialog() {
        title = "Set Size";
    }

    public Point execute(Component parent, Point size) {
        widthEditor.setValue("" + size.x);
        heightEditor.setValue("" + size.y);
        int i = execute(parent);
        return (i != 0) ? null : new Point(widthEditor.getIntValue(), heightEditor.getIntValue());
    }

    protected JComponent createInputPanel() {
        widthEditor = new TextFieldEditor();
        heightEditor = new TextFieldEditor();
        widthEditor.setName("Width");
        heightEditor.setName("Height");
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(widthEditor.getComponent());
        p.add(Box.createVerticalStrut(5));
        p.add(heightEditor.getComponent());
        p.add(Box.createVerticalBox());
        return p;
    }

    protected boolean ok() {
        return (widthEditor.getIntValue() > -1 && heightEditor.getIntValue() > -1);
    }

    protected boolean cancel() {
        return true;
    }

}
