package com.slava.supaplex.ui.editor;

import java.awt.event.*;
import javax.swing.*;

public class TextFieldEditor extends FieldEditor {
	JTextField textField;
	
	public TextFieldEditor() {}
	
	public TextFieldEditor(String name) {
		setName(name);
	}

	protected JComponent createComponent() {
		textField = new JTextField();
		doSetEnabled(isEnabled);
		doSetValue(getValue());
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				edit(textField.getText().trim());
			}
		});
		return textField;
	}
	
	protected void doSetEnabled(boolean b) {
		if(textField != null) textField.setEditable(b);
	}

	protected void doSetValue(String value) {
		if(textField != null && value != null) textField.setText(value);
	}

	protected String doGetValue() {
		return textField == null ? super.getValue() : textField.getText().trim();
	}
}
