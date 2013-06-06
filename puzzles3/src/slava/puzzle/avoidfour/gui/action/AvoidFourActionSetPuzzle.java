package slava.puzzle.avoidfour.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.avoidfour.model.*;
import slava.puzzle.template.action.*;

public class AvoidFourActionSetPuzzle extends PuzzleAction {
	
	public boolean isEnabled() {
		AvoidFourModel model = (AvoidFourModel)manager.getModel(); 
		return !model.isSettingPuzzleMode();		
	}

	public void actionPerformed(ActionEvent e) {
		if(!isEnabled()) return; 
		AvoidFourModel model = (AvoidFourModel)manager.getModel();
		model.setSettingPuzzleMode();
		manager.getComponent().grabFocus();
		manager.getComponent().repaint();
	}

}
