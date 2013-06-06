package com.slava.supaplex.ui.editor;

import java.awt.event.*;
import javax.swing.*;

public class CheckBoxEditor extends FieldEditor {
	JCheckBox checkBox;
	
	public CheckBoxEditor() {}
	
	public CheckBoxEditor(String name) {
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
		if(checkBox != null) checkBox.setText(name);
	}
	
	protected JComponent createComponent() {
		checkBox = new JCheckBox();
		if(name != null) checkBox.setText(name);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit(checkBox.isSelected() ? "true" : "false");
			}
		});
		return checkBox;
	}
	
	protected String getLabelText() {
		return "";
	}	
	
	protected void doSetEnabled(boolean b) {	
		if(checkBox != null) checkBox.setEnabled(b);
	}

	protected void doSetValue(String value) {
		if(checkBox != null) checkBox.setSelected("true".equals(value));
	}

	protected String doGetValue() {
		return checkBox == null ? super.getValue() : checkBox.isSelected() ? "true" : "false";
	}

}
