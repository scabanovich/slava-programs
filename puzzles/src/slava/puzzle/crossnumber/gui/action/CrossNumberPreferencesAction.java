package slava.puzzle.crossnumber.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.gui.CrossNumberPreferencesDialog;
import slava.puzzle.template.action.*;

public class CrossNumberPreferencesAction extends PuzzleAction {
	
	public CrossNumberPreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		CrossNumberPreferencesDialog d = new CrossNumberPreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
