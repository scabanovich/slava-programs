package slava.puzzle.paths.nook.gui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.paths.nook.gui.NookPathsPreferencesDialog;
import slava.puzzle.paths.nook.model.NookPathsModel;
import slava.puzzle.template.action.*;

public class NookPathsPreferencesAction extends PuzzleAction {
	
	public NookPathsPreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		NookPathsModel model = (NookPathsModel)manager.getModel();
		NookPathsPreferencesDialog d = new NookPathsPreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
