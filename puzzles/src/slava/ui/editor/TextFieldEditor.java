package slava.ui.editor;

import java.awt.*;
import javax.swing.*;

public class TextFieldEditor extends FieldEditor {
    protected JTextField text;

    public TextFieldEditor() {}

    protected void build() {
        text = new JTextField();
        panel.add(label, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        text.setText(value);
    }

    public String getValue() {
        return text.getText();
    }

    public void requestFocus() {
        text.requestFocus();
    }
    
}
