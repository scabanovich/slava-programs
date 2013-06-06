package slava.puzzle.paths.walker.gui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.paths.walker.gui.WalkerPathsPreferencesDialog;
import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.template.action.*;

public class WalkerPathsPreferencesAction extends PuzzleAction {
	
	public WalkerPathsPreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		WalkerPathsModel model = (WalkerPathsModel)manager.getModel();
		WalkerPathsPreferencesDialog d = new WalkerPathsPreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
