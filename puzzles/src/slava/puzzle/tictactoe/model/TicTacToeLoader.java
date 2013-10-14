package slava.puzzle.tictactoe.model;

import slava.puzzle.template.model.PuzzleLoader;

public class TicTacToeLoader extends PuzzleLoader {

	public TicTacToeLoader() {
		setRoot("/data/tictactoe");
		initName("tictactoe_");
	}

	public TicTacToeModel getModel() {
		return (TicTacToeModel)super.getModel();
	}
}
