package slava.puzzle.template.action;

import java.awt.event.*;

public class PuzzleActionExit extends PuzzleAction {
	
	public PuzzleActionExit() {}

	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
