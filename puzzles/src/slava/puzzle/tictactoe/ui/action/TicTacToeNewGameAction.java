package slava.puzzle.tictactoe.ui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.template.action.PuzzleAction;
import slava.puzzle.tictactoe.model.TicTacToeModel;

public class TicTacToeNewGameAction extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		TicTacToeModel model = (TicTacToeModel)manager.getModel();
		model.startNewGame();
		manager.getComponent().repaint();
	}

}
