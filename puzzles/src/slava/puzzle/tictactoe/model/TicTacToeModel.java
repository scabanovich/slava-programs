package slava.puzzle.tictactoe.model;

import com.slava.common.RectangularField;

import slava.puzzle.template.model.PuzzleModel;

public class TicTacToeModel extends PuzzleModel {
	public static int EMPTY = -1;
	public static int CROSS = 0;
	public static int ZERO = 1;

	RectangularField field;

	int t;
	int turn = CROSS;
	int[] state;
	int[] history;

	boolean isCompleted = false;
	int fiveStart;
	int fiveEnd;

	public TicTacToeModel() {
		field = new RectangularField();
		field.setOrts(RectangularField.DIAGONAL_ORTS);
		field.setSize(21, 21);
		setLoader(new TicTacToeLoader());

		startNewGame();
	}

	public RectangularField getField() {
		return field;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int i) {
		turn = i;
	}

	public int getMovesMade() {
		return t;
	}

	public int getValue(int p) {
		return state[p];
	}

	public void setValue(int p, int v) {
		state[p] = v;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public int getFiveStart() {
		return fiveStart;
	}

	public int getFiveEnd() {
		return fiveEnd;
	}

	public void startNewGame() {
		turn = CROSS;
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) {
			state[p] = EMPTY;
		}

		t = 0;
		history = new int[field.getSize() + 1];
		isCompleted = false;
		fiveStart = -1;
		fiveEnd = -1;
	}

	public void move(int p) {
		if(isCompleted() || state[p] != EMPTY) {
			return;
		}
		setValue(p, turn);
		history[t] = p;
		turn = 1 - turn;
		t++;
		checkCompleted(p);
	}

	public void back() {
		if(t == 0) return;
		t--;
		int p = history[t];
		setValue(p, EMPTY);
		turn = 1 - turn;
		isCompleted = false;
		fiveEnd = -1;
		fiveStart = -1;
	}

	void checkCompleted(int p) {
		for (int d = 0; d < 4; d++) {
			int s = 1;
			int q = p;
			while(true) {
				int q1 = field.jump(q, d);
				if(q1 < 0 || state[q1] != state[p]) {
					break;
				}
				s++;
				q = q1;
			}
			int e = p;
			while(true) {
				int q1 = field.jump(e, d + 4);
				if(q1 < 0 || state[q1] != state[p]) {
					break;
				}
				s++;
				e = q1;
			}
			if(s >= 5) {
				isCompleted = true;
				fiveStart = q;
				fiveEnd = e;
				return;
			}			
		}
	}
}
