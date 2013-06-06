package slava.puzzle.avoidthree.gui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.avoidthree.model.AvoidThreeAndKnightModel;
import slava.puzzle.template.action.PuzzleAction;

public class AvoidThreeAndKnightActionSetPuzzle extends PuzzleAction {
	
	public boolean isEnabled() {
		AvoidThreeAndKnightModel model = (AvoidThreeAndKnightModel)manager.getModel(); 
		return !model.isSettingPuzzleMode();		
	}

	public void actionPerformed(ActionEvent e) {
		if(!isEnabled()) return; 
		AvoidThreeAndKnightModel model = (AvoidThreeAndKnightModel)manager.getModel();
		model.setSettingPuzzleMode();
		manager.getComponent().grabFocus();
		manager.getComponent().repaint();
	}

}
