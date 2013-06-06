package com.slava.supaplex.ui;

import javax.swing.*;
import com.slava.supaplex.model.*;
import com.slava.supaplex.ui.editor.*;

public class SupaplexLevelOptions {
	JPanel panel;
	SupaplexLevel level;
	
	FieldEditor nameField = new TextFieldEditor("Name");
	FieldEditor countField = new TextFieldEditor("Count");
	FieldEditor jumpingBox = new CheckBoxEditor("Gravity");
	FieldEditor param2Field = new TextFieldEditor("Param 2");
	FieldEditor param4Field = new TextFieldEditor("Param 4");
	FieldEditor paramLastField = new TextFieldEditor("Param Last");
	
	FieldEditor[] editors = new FieldEditor[]{
		nameField, countField, jumpingBox, param2Field, param4Field, paramLastField
	};

	JPanel createPanel() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (int i = 0; i < editors.length; i++) {
			panel.add(editors[i].getPanel());
		}
		panel.add(Box.createVerticalGlue());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		initListeners();
		return panel;
	}

	public void setLevel(SupaplexLevel level) {
		if(this.level == level) return;
		this.level = level;
		for (int i = 0; i < editors.length; i++) {
			editors[i].setEnabled(false);
			editors[i].setValue("");
		}
		if(level != null) {
			nameField.setValue(level.getName());
			countField.setValue("" + level.getCount());
			param2Field.setValue("" + level.getParameter2());
			param4Field.setValue("" + level.getParameter4());
			paramLastField.setValue("" + level.getParameterLast());
			jumpingBox.setValue(level.isJumping() ? "true" : "false");
			for (int i = 0; i < editors.length; i++) {
				editors[i].setEnabled(true);
			}
		}
	}
	
	void initListeners() {
		nameField.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editName(value);
			}
		});
		countField.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editCount(value);
			}
		});
		jumpingBox.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editGravity(value);
			}
		});
		param2Field.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editParameter2(value);
			}
		});
		param4Field.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editParameter4(value);
			}
		});
		paramLastField.addValueListener(new IValueListener() {
			public void valueChanged(String value) {
				level.editParameterLast(value);
			}
		});
	}
	
}
