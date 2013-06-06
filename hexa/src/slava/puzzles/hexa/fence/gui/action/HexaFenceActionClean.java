package slava.puzzles.hexa.fence.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.template.action.PuzzleAction;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFenceActionClean extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		HexaFenceModel model = (HexaFenceModel)manager.getModel();
		model.clean();
		manager.getComponent().repaint();
	}

}
