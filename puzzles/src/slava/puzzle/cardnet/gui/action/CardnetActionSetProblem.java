package slava.puzzle.cardnet.gui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.cardnet.model.CardnetModel;
import slava.puzzle.template.action.PuzzleAction;

public class CardnetActionSetProblem extends PuzzleAction {

	public boolean isEnabled() {
		CardnetModel cm = (CardnetModel)manager.getModel();
		return !cm.isRunning() && !cm.isSettingPuzzleModeOn();
	}

	public void actionPerformed(ActionEvent e) {
		CardnetModel cm = (CardnetModel)manager.getModel();
		if(cm.isRunning()) return;
		manager.getComponent().grabFocus();
		if(cm.isSettingPuzzleModeOn()) return;
		cm.setSettingPuzzleModeOn(true);
		manager.getComponent().repaint();
	}

}
