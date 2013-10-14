package slava.puzzle.tictactoe.ui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.template.action.PuzzleAction;
import slava.puzzle.tictactoe.model.TicTacToeModel;

public class TicTacToeBackAction extends PuzzleAction {

	public TicTacToeBackAction() {}

	public void actionPerformed(ActionEvent e) {
		TicTacToeModel model = (TicTacToeModel)manager.getModel();
		model.back();
		manager.getComponent().repaint();
	}
}
