package slava.puzzles.seabattle.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.template.action.*;
import slava.puzzles.seabattle.gui.SeaBattlePreferencesDialog;
import slava.puzzles.seabattle.model.SeaBattleModel;

public class SeaBattlePreferencesAction extends PuzzleAction {
	
	public SeaBattlePreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		SeaBattleModel model = (SeaBattleModel)manager.getModel();
		SeaBattlePreferencesDialog d = new SeaBattlePreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
