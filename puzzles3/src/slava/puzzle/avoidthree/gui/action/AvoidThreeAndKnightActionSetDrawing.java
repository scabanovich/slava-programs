package slava.puzzle.avoidthree.gui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.avoidthree.model.AvoidThreeAndKnightModel;
import slava.puzzle.template.action.PuzzleAction;

public class AvoidThreeAndKnightActionSetDrawing extends PuzzleAction {
	
	public boolean isEnabled() {
		AvoidThreeAndKnightModel model = (AvoidThreeAndKnightModel)manager.getModel(); 
		return !model.isDrawingFieldMode();		
	}

	public void actionPerformed(ActionEvent e) {
		if(!isEnabled()) return; 
		AvoidThreeAndKnightModel model = (AvoidThreeAndKnightModel)manager.getModel();
		model.setDrawingFieldMode();
		manager.getComponent().repaint();
	}

}
