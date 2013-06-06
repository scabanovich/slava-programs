package slava.puzzles.seabattle.gui;

import java.awt.BorderLayout;
import javax.swing.*;

import slava.puzzles.seabattle.model.SeaBattleModel;
import slava.ui.dialog.OkCancelWizard;

public class SeaBattlePreferencesDialog extends OkCancelWizard {
	SeaBattleModel model;

	JTextField solutionLimitField;
	
	static String[] generatorModeList = {
		"Reducing",
		"Narrowing by water only",
		"Narrowing by anything"
	};
	JComboBox generatorModeBox;

	public SeaBattlePreferencesDialog() {}
	
	public void setInput(Object input) {
		model = (SeaBattleModel)input;
		solutionLimitField.setText("" + model.getPreferences().getSolutionLimit());
		generatorModeBox.setSelectedIndex(model.getPreferences().getGeneratorMode());
	}

	protected JComponent createInputPanel() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);

		JLabel label = new JLabel();
		label.setText("Solution limit");
		wrap(panel, label);
		solutionLimitField = new JTextField();
		solutionLimitField.setText("Solution limit");
		wrap(panel, solutionLimitField);
		
		label = new JLabel();
		label.setText("Generator mode");
		wrap(panel, label);
		generatorModeBox = new JComboBox(generatorModeList);
		wrap(panel, generatorModeBox);

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
    	int s = 10;
    	try {
    		s = Integer.parseInt(solutionLimitField.getText());
    	} catch (Exception e) {
    		//ignore
    	}
    	model.getPreferences().setSolutionLimit(s);
    	model.getPreferences().setGeneratorMode(generatorModeBox.getSelectedIndex());
        return true;
    }

}
