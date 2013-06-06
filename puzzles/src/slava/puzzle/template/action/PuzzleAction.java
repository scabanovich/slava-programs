package slava.puzzle.template.action;

import java.awt.event.ActionListener;

public abstract class PuzzleAction implements ActionListener {
	protected PuzzleActionManager manager;
	
	public void setManager(PuzzleActionManager manager) {
		this.manager = manager;
	}
	
	public boolean isEnabled() {
		return true;
	}

}
