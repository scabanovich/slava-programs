package slava.puzzle.template.action;

import java.awt.event.ActionEvent;

import slava.puzzle.template.undo.UndoManager;

public class PuzzleRedoAction extends PuzzleAction {

	public boolean isEnabled() {
		return manager != null && !manager.model.isRunning() && UndoManager.getInstance().canRedo();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			UndoManager.getInstance().redo();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			manager.component.repaint();			
		}
	}

}
