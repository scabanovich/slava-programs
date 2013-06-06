package slava.puzzle.avoidfour.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.avoidfour.model.AvoidFourModel;
import slava.puzzle.template.action.PuzzleAction;

public class AvoidFourActionSetDrawing extends PuzzleAction {
	
	public boolean isEnabled() {
		AvoidFourModel model = (AvoidFourModel)manager.getModel(); 
		return !model.isDrawingFieldMode();		
	}

	public void actionPerformed(ActionEvent e) {
		if(!isEnabled()) return; 
		AvoidFourModel model = (AvoidFourModel)manager.getModel();
		model.setDrawingFieldMode();
		manager.getComponent().repaint();
	}

}
