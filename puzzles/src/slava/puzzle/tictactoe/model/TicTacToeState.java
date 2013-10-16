package slava.puzzle.tictactoe.model;

import java.util.HashSet;
import java.util.Set;

import com.slava.common.RectangularField;

public class TicTacToeState {
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

	int[][] longest;
	SetWrapper[] sets;


	public TicTacToeState() {}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public RectangularField getField() {
		return field;
	}

	public void clean() {
		turn = CROSS;
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) {
			state[p] = EMPTY;
		}
		longest = new int[2][field.getSize()];
		sets = new SetWrapper[2];
		for (int i = 0; i < 2; i++) {
			sets[i] = new SetWrapper();
			for (int p = 0; p < state.length; p++) {
				longest[i][p] = 1;
			}
		}
		

		t = 0;
		history = new int[field.getSize() + 1];
		isCompleted = false;
		fiveStart = -1;
		fiveEnd = -1;
	}

	public int getTurn() {
		return turn;
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

	public void move(int p) {
		if(isCompleted() || state[p] != EMPTY) {
			return;
		}
		setValue(p, turn);
		history[t] = p;
		turn = 1 - turn;
		t++;
		checkCompleted(p);
		updateLongestOnChange(p);
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
		updateLongestOnChange(p);
	}

	public int getLongest(int p, int turn) {
		return longest[turn][p];
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

	void updateLongestOnChange(int p) {
		updateLongest(p);
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			for (int i = 0; i < 4 && q >= 0; i++) {
				updateLongest(q);
				q = field.jump(q, d);
			}
		}
	}

	void updateLongest(int p) {
		for (int turn = 0; turn < 2; turn++) {
			updateLongest(p, turn);
		}
	}

	void updateLongest(int p, int turn) {
		int oldValue = longest[turn][p];
		if(state[p] != EMPTY) {
			onChangeLongest(p, turn, oldValue, 0);
		} else {
			int newValue = 0;
			for (int d = 0; d < 4; d++) {
				int q = p;
				for (int i = 0; i < 5 && q >= 0; i++) {
					int k = count(q, d, turn);
					if(k >= 0 && k + 1 > newValue) {
						newValue = k + 1;
					}
					q = field.jump(q, d + 4);
				}
			}
			onChangeLongest(p, turn, oldValue, newValue);
		}
	}

	int count(int p, int d, int turn) {
		int r = field.jump(p, d, 5);
		if(r >= 0 && getValue(r) == turn) return -1;
		r = field.jump(p, d + 4);
		if(r >= 0 && getValue(r) == turn) return -1;
		int c = 0;
		for (int i = 0; i < 5; i++) {
			if(p < 0) return -1;
			if(getValue(p) == turn) {
				c++;
			} else if(getValue(p) != EMPTY) {
				return -1;
			}
			p = field.jump(p, d);
		}
		return c;
	}

	void onChangeLongest(int p, int turn, int oldValue, int newValue) {
		if(oldValue != newValue) {
			longest[turn][p] = newValue;
			sets[turn].replace(p, oldValue, newValue);
		}
	}

	public Set<Integer> getPlacesForFive(int turn) {
		return sets[turn].set5;
	}

	public Set<Integer> getPlacesForFour(int turn) {
		return sets[turn].set4;
	}

	class SetWrapper {
		Set<Integer> set4 = new HashSet<Integer>();
		Set<Integer> set5 = new HashSet<Integer>();
		
		void replace(int p, int oldValue, int newValue) {
			if(oldValue == 5) {
				set5.remove(p);
			} else if(oldValue == 4) {
				set4.remove(p);
			}
			if(newValue == 5) {
				set5.add(p);
			} else if(newValue == 4) {
				set4.add(p);
			}
		}
	}
}
