package slava.puzzle.tictactoe.model;

import com.slava.common.RectangularField;

import slava.puzzle.template.model.PuzzleModel;

public class TicTacToeModel extends PuzzleModel {
	RectangularField field;
	TicTacToeState state = new TicTacToeState();

	public TicTacToeModel() {
		field = new RectangularField();
		field.setOrts(RectangularField.DIAGONAL_ORTS);
		field.setSize(21, 21);
		state.setField(field);
		setLoader(new TicTacToeLoader());

		startNewGame();
	}

	public RectangularField getField() {
		return field;
	}

	public TicTacToeState getState() {
		return state;
	}

	public int getTurn() {
		return state.getTurn();
	}

	public int getMovesMade() {
		return state.getMovesMade();
	}

	public int getValue(int p) {
		return state.getValue(p);
	}

	public void setValue(int p, int v) {
		state.setValue(p, v);
	}

	public boolean isCompleted() {
		return state.isCompleted();
	}

	public int getFiveStart() {
		return state.getFiveStart();
	}

	public int getFiveEnd() {
		return state.getFiveEnd();
	}

	public void startNewGame() {
		state.clean();
	}

}
