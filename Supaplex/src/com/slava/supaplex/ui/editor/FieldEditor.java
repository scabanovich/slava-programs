package com.slava.supaplex.ui.editor;

import java.awt.*;
import javax.swing.*;

public abstract class FieldEditor {
	protected JComponent component;
	protected String name;
	protected JLabel label;
	protected JPanel panel;
	protected IValueListener listener;
	protected String value;
	boolean isEnabled = false;
	boolean isChanging;
	
	public FieldEditor() {}
	
	public void addValueListener(IValueListener listener) {
		this.listener = listener;
	}
	
	public void setName(String name) {
		this.name = name;
		if(label != null) label.setText(name);
	}
	
	protected abstract JComponent createComponent();
	
	public JComponent getComponent() {
		if(component == null) {
			component = createComponent();
		}
		return component;
	}
	
	public final JComponent getPanel() {
		if(panel == null) {
			panel = addLabel(getComponent(), getLabelText());
		}
		return panel;
	}
	
	protected String getLabelText() {
		return name;
	}
	
	protected final JPanel addLabel(JComponent c, String text) {
		JPanel p = new JPanel(new BorderLayout());
		JLabel label = new JLabel();
		p.add(label, BorderLayout.WEST);
		label.setPreferredSize(new Dimension(100, 20));
		label.setText(text);
		p.add(c, BorderLayout.CENTER);
		p.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		return p;
	}
	
	public final void setEnabled(boolean b) {
		if(b == isEnabled) return;
		isEnabled = b;
		doSetEnabled(b);
	}
	
	public final boolean isEnabled() {
		return isEnabled;
	}
	
	protected abstract void doSetEnabled(boolean b);
	
	public final void setValue(String value) {
		if(hasValue(value)) return;
		boolean isnull = this.value == null;
		doSetValue(value);
		this.value = value;
		if(!isnull && isEnabled()) fire();
	}
	
	protected abstract void doSetValue(String value);
	protected abstract String doGetValue();
	
	public String getValue() {
		return value;
	}

	protected boolean hasValue(String value) {
		return (this.value != null && this.value.equals(value));
	}
	
	protected void edit(String value) {
		this.value = value;
		fire();
	}
	
	protected void fire() {
		if(listener != null && isEnabled && !isChanging) {
			isChanging = true;
			try {
				listener.valueChanged(getValue());
			} finally {
				isChanging = false;
			}
		}
	}
	
}
