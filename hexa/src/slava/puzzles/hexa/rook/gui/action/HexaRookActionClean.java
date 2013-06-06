package slava.puzzles.hexa.rook.gui.action;

import java.awt.event.ActionEvent;
import slava.puzzle.template.action.PuzzleAction;
import slava.puzzles.hexa.rook.model.HexaRookModel;

public class HexaRookActionClean extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		HexaRookModel model = (HexaRookModel)manager.getModel();
		model.clean();
		manager.getComponent().repaint();
	}
	
}