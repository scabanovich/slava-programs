package slava.puzzle.template.action;

import java.awt.event.ActionEvent;

public class PuzzleActionStop extends PuzzleAction {
	
	public boolean isEnabled() {
		return manager != null && manager.model.isRunning();
	}

	public void actionPerformed(ActionEvent e) {
		if(manager.getThread() == null) return;
		manager.getThread().stop();
		manager.model.setRunning(false);
		manager.component.repaint();		
		manager.setThread(null);
	}

}
