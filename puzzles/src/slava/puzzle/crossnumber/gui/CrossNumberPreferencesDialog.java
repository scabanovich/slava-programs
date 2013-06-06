package slava.puzzle.crossnumber.gui;

import java.awt.BorderLayout;
import javax.swing.*;
import slava.puzzle.crossnumber.CrossNumberModel;
import slava.ui.dialog.OkCancelWizard;

public class CrossNumberPreferencesDialog extends OkCancelWizard {
	CrossNumberModel model;

	JCheckBox throughRowBox;

	public CrossNumberPreferencesDialog() {}
	
	public void setInput(Object input) {
		model = (CrossNumberModel)input;
		throughRowBox.setSelected(model.isThroughRow());
	}

	protected JComponent createInputPanel() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);
		throughRowBox = new JCheckBox();
		throughRowBox.setText("Make all numbers different in a row");
		wrap(panel, throughRowBox);

		return panel;
	}
	
	void wrap(JPanel parent, JComponent child) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(child, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0));		
		parent.add(panel);
	}

    protected boolean ok() {
    	model.setThroughRow(throughRowBox.isSelected());
        return true;
    }

}
