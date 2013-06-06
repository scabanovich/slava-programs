package slava.puzzle.sudoku.ui;

import java.awt.BorderLayout;

import javax.swing.*;
import slava.puzzle.sudoku.model.SudokuModel;
import slava.puzzle.sudoku.model.SudokuPreferences;
import slava.ui.dialog.OkCancelWizard;
import slava.ui.editor.TextFieldEditor;

public class SudokuPreferencesDialog extends OkCancelWizard {
	SudokuModel model;
	
	static String[] diagonalOptions = {
		"No diagonals", "Main diagonals", "All diagonals"
	};

	JComboBox addDiagonalsBox;
	JCheckBox generateByTemplate;
	JCheckBox notTouchingDiagonals;
	TextFieldEditor neighboursMaxDifference;
	JCheckBox usingDifferenceOneRestriction;
	TextFieldEditor diffValue;

	JCheckBox usingLessOrGreaterRestriction;
	JCheckBox usingXVRestriction;
	JCheckBox usingPartitioningSumRestriction;

	TextFieldEditor treeCount;
	TextFieldEditor solutionLimit;
	
	public SudokuPreferencesDialog() {}
	
	public void setInput(Object input) {
		model = (SudokuModel)input;
		int diagonals = model.getProblemInfo().getDiagonalsOption();
		addDiagonalsBox.setSelectedIndex(diagonals);
		generateByTemplate.setSelected(model.getProblemInfo().isGenerateByTemplate());
		notTouchingDiagonals.setSelected(model.getProblemInfo().isNotTouchingDiagonally());
		int n = model.getProblemInfo().getNeighboursDifferNotMoreThan();
		String s = n == 0 ? "" : "" + n;
		neighboursMaxDifference.setValue(s);
		usingDifferenceOneRestriction.setSelected(model.getProblemInfo().isUsingDifferenceOneRestriction());
		diffValue.setValue("" + model.getProblemInfo().getDifferenceValue());
		usingLessOrGreaterRestriction.setSelected(model.getProblemInfo().isUsingLessOrGreaterRestriction());
		usingXVRestriction.setSelected(model.getProblemInfo().isUsingXVRestriction());
		usingPartitioningSumRestriction.setSelected(model.getProblemInfo().isUsingPartitioningSumRestriction());

		treeCount.setValue("" + SudokuPreferences.instance.getGenerateTreeCountLimit());
		solutionLimit.setValue("" + SudokuPreferences.instance.getCountSolutionUpTo());
	}

	protected JComponent createInputPanel() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);
		addDiagonalsBox = new JComboBox(diagonalOptions);
///		addDiagonalsBox.setText("Add Diagonals To Regions");
		wrap(panel, addDiagonalsBox);

		notTouchingDiagonals = new JCheckBox();
		notTouchingDiagonals.setText("Equal values may not touch diagonally");
		wrap(panel, notTouchingDiagonals);
		
		neighboursMaxDifference = new TextFieldEditor();
		neighboursMaxDifference.setName("      Neighbour Values Max Difference");
		panel.add(neighboursMaxDifference.getComponent());
		
		usingDifferenceOneRestriction = new JCheckBox();
		usingDifferenceOneRestriction.setText("Use Difference-One Restrictions");
		wrap(panel, usingDifferenceOneRestriction);

		diffValue = new TextFieldEditor();
		diffValue.setName("      Difference/Sum");
		panel.add(diffValue.getComponent());
		
		usingLessOrGreaterRestriction = new JCheckBox();
		usingLessOrGreaterRestriction.setText("Use Less-Greater Restrictions");
		wrap(panel, usingLessOrGreaterRestriction);
		
		usingXVRestriction = new JCheckBox();
		usingXVRestriction.setText("Use X-V Restriction");
		wrap(panel, usingXVRestriction);

		usingPartitioningSumRestriction = new JCheckBox();
		usingPartitioningSumRestriction.setText("Use Sums Restrictions");
		wrap(panel, usingPartitioningSumRestriction);
		
		JSeparator s = new JSeparator(SwingConstants.HORIZONTAL);
		panel.add(s);

		treeCount = new TextFieldEditor();
		treeCount.setName("Generate with Tree Count not More Than");
		panel.add(treeCount.getComponent());
		generateByTemplate = new JCheckBox();
		generateByTemplate.setText("Generate By Template");
		wrap(panel, generateByTemplate);

		s = new JSeparator(SwingConstants.HORIZONTAL);
		panel.add(s);
		
		solutionLimit = new TextFieldEditor();
		solutionLimit.setName("Count Solutions Up To");
		panel.add(solutionLimit.getComponent());
		
		s = new JSeparator(SwingConstants.HORIZONTAL);
		panel.add(s);

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
    	model.getProblemInfo().setDiagonalsOption(addDiagonalsBox.getSelectedIndex());
    	model.getProblemInfo().setGenerateByTemplate(generateByTemplate.isSelected());
    	model.getProblemInfo().setNotTouchingDiagonally(notTouchingDiagonals.isSelected());
    	try {
    		String sv = neighboursMaxDifference.getValue();
    		int v = sv.length() == 0 ? 0 : Integer.parseInt(sv);
    		if(v <= 0) v = 0;
    		model.getProblemInfo().setNeighboursDifferNotMoreThan(v);
    	} catch (NumberFormatException e) {
    	}

    	model.getProblemInfo().setUsingDifferenceOneRestriction(usingDifferenceOneRestriction.isSelected());
    	try {
    		String sv = diffValue.getValue();
    		int v = Integer.parseInt(sv);
    		if(v <= 0) v = 1;
    		model.getProblemInfo().setDifferenceValue(v);
    	} catch (NumberFormatException e) {
    	}
    	model.getProblemInfo().setUsingLessOrGreaterRestriction(usingLessOrGreaterRestriction.isSelected());
    	model.getProblemInfo().setUsingXVRestriction(usingXVRestriction.isSelected());
    	model.getProblemInfo().setUsingPartitioningSumRestriction(usingPartitioningSumRestriction.isSelected());
    	try {
    		String sv = treeCount.getValue();
    		int v = Integer.parseInt(sv);
    		if(v <= 0) v = -1; 
    		SudokuPreferences.instance.setGenerateTreeCountLimit(v);
    	} catch (Exception e) {
    	}
    	try {
    		String sv = solutionLimit.getValue();
    		int v = Integer.parseInt(sv);
    		if(v <= 0) v = -1; 
    		SudokuPreferences.instance.setCountSolutionUpTo(v);
    	} catch (Exception e) {
    	}
        return true;
    }

}
