package slava.puzzle.hitori.gui;

import java.awt.BorderLayout;
import javax.swing.*;
import slava.puzzle.hitori.model.*;
import slava.ui.dialog.OkCancelWizard;
import slava.ui.editor.TextFieldEditor;

public class HitoriPreferencesDialog extends OkCancelWizard {
	HitoriModel model;

	TextFieldEditor suggestionsLimit;

	public HitoriPreferencesDialog() {}

	public void setInput(Object input) {
		model = (HitoriModel)input;
		suggestionsLimit.setValue("" + HitoriPreferences.instance.getSuggestionsLimit());
	}

	protected JComponent createInputPanel() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);
		
		suggestionsLimit = new TextFieldEditor();
		suggestionsLimit.setName("Suggestions Limit at Solving/Generating");
		panel.add(suggestionsLimit.getComponent());

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
    	try {
    		String sv = suggestionsLimit.getValue();
    		int v = Integer.parseInt(sv);
    		if(v < 0) v = 0; 
    		HitoriPreferences.instance.setSuggestionsLimit(v);
    	} catch (Exception e) {
    	}
        return true;
    }

}
