package slava.puzzle.paths.walker.gui;

import java.awt.BorderLayout;
import javax.swing.*;

import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.paths.walker.model.WalkerPathsPreferences;
import slava.ui.dialog.OkCancelWizard;

public class WalkerPathsPreferencesDialog extends OkCancelWizard {
	WalkerPathsModel model;
	static String[] KIND_OPTIONS = new String[]{WalkerPathsPreferences.STANDARD_KIND, WalkerPathsPreferences.MASUI_KIND};

	JComboBox puzzleKind;
	JTextField solutionLimitField;
	JTextField treeLimitField;
	

	public WalkerPathsPreferencesDialog() {}
	
	public void setInput(Object input) {
		model = (WalkerPathsModel)input;
		puzzleKind.setSelectedItem("" + model.getPreferences().getPuzzleKind());
		solutionLimitField.setText("" + model.getPreferences().getSolutionLimit());
		treeLimitField.setText("" + model.getPreferences().getTreeLimit());
	}

	protected JComponent createInputPanel() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);

		JLabel label = new JLabel();
		label.setText("Puzzle Kind");
		wrap(panel, label);
		puzzleKind = new JComboBox(KIND_OPTIONS);
		wrap(panel, puzzleKind);

		label = new JLabel();
		label.setText("Solution limit");
		wrap(panel, label);
		solutionLimitField = new JTextField();
		solutionLimitField.setText("Solution limit");
		wrap(panel, solutionLimitField);
		
		label = new JLabel();
		label.setText("Generator tree limit");
		wrap(panel, label);
		treeLimitField = new JTextField();
		wrap(panel, treeLimitField);

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
    	String kind = (String)puzzleKind.getSelectedItem();
    	model.getPreferences().setPuzzleKind(kind);
    	int s = 10;
    	try {
    		s = Integer.parseInt(solutionLimitField.getText());
    	} catch (Exception e) {
    		//ignore
    	}
    	model.getPreferences().setSolutionLimit(s);
    	s = 1;
    	try {
    		s = Integer.parseInt(treeLimitField.getText());
    	} catch (Exception e) {
    		//ignore
    	}
    	model.getPreferences().setTreeLimit(s);
        return true;
    }

}
