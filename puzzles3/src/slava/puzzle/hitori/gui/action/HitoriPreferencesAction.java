package slava.puzzle.hitori.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.hitori.gui.HitoriPreferencesDialog;
import slava.puzzle.hitori.model.HitoriModel;
import slava.puzzle.template.action.PuzzleAction;

public class HitoriPreferencesAction extends PuzzleAction {
	
	public HitoriPreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		HitoriModel model = (HitoriModel)manager.getModel();
		HitoriPreferencesDialog d = new HitoriPreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
