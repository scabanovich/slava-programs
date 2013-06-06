package slava.ui.editor;

import java.awt.*;
import javax.swing.*;

public class FieldEditor {
    protected JPanel panel = new JPanel(new BorderLayout());
    protected JLabel label = new JLabel();

    public FieldEditor() {
        build();
    }

    public JComponent getComponent() {
        return panel;
    }

    public void setName(String name) {
        label.setText(name);
        label.setForeground(Color.black);
    }

    public void setValue(String value) {

    }

    public String getValue() {
        return "";
    }

    public int getIntValue() {
        try {
            return Integer.parseInt(getValue());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Value must be positive integer less than 60.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    protected void build() {}

    public void requestFocus() {}
}