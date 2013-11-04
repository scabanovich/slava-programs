package slava.puzzle.tictactoe.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

	public TicTacToeState copy() {
		TicTacToeState copy = new TicTacToeState();
		copy.field = field;
		copy.t = t;
		copy.turn = turn;
		copy.state = new int[state.length];
		System.arraycopy(state, 0, copy.state, 0, state.length);
		copy.history = new int[history.length];
		System.arraycopy(history, 0, copy.history, 0, history.length);
		copy.isCompleted = isCompleted;
		copy.fiveStart = fiveStart;
		copy.fiveEnd = fiveEnd;
		copy.longest = new int[2][field.getSize()];
		copy.sets = new SetWrapper[2];
		for (int i = 0; i < 2; i++) {
			copy.sets[i] = sets[i].copy();
			System.arraycopy(longest[i], 0, copy.longest[i], 0, longest[i].length);
		}
		return copy;
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

	public int getT() {
		return t;
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

	public int getPlaceAt(int t) {
		return history[t];
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

	public void flipTurn() {
		turn = 1 - turn;
	}

	public void move(int p) {
		if(isCompleted() || state[p] != EMPTY) {
			return;
		}
		isCompleted = getPlacesForFive(turn).contains(p);
		setValue(p, turn);
		history[t] = p;
		flipTurn();
		t++;
		if(isCompleted) {
			checkCompleted(p);
		}
		updateLongestOnChange(p);
	}

	public void back() {
		if(t == 0) return;
		t--;
		int p = history[t];
		setValue(p, EMPTY);
		flipTurn();
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
				if(state[q] == EMPTY) updateLongest(q);
				q = field.jump(q, d);
			}
		}
		recomputeCriticalFours();
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
			int newValue = countNewValue(p, turn);
			onChangeLongest(p, turn, oldValue, newValue);
		}
	}

	Map<Integer, Set<Integer>> EMPTY_MAP = new HashMap<Integer, Set<Integer>>();

	void recomputeCriticalFours() {
		for (int turn = 0; turn < 2; turn++) {
			Set<Integer> set4 = getPlacesFor(turn, 4);
			Map<Integer, Set<Integer>> map = null;
			for (Integer p: set4) {
				Set<Integer> g5 = null;
				int p5 = -1;
				for (int d = 0; d < 4; d++) {
					int s = 1;
					int q = p;
					int qr = -1, ql = -1;
					while(true) {
						q = field.jump(q, d);
						if(q >= 0 && getValue(q) != 1 - turn) {
							if(getValue(q) == turn) {
								s++;
							} else {
								qr = q;
								break;
							}
						} else {
							break;
						}
					}
					q = p;
					while(true) {
						q = field.jump(q, d + 4);
						if(q >= 0 && getValue(q) != 1 - turn) {
							if(getValue(q) == turn) {
								s++;
							} else {
								ql = q;
								break;
							}
						} else {
							break;
						}
					}
					if(s >= 4) {
						if(qr >= 0 && set4.contains(qr)) {
							if(p5 < 0) {
								p5 = qr;
							} else {
								if(g5 == null) {
									g5 = new HashSet<Integer>();
									g5.add(p5);
								}
								g5.add(qr);
								
							}
						}
						if(ql >= 0 && set4.contains(ql)) {
							if(p5 < 0) {
								p5 = ql;
							} else {
								if(g5 == null) {
									g5 = new HashSet<Integer>();
									g5.add(p5);
								}
								g5.add(ql);
							}
							
						}
					}
				}
				if(g5 != null) {
					if (map == null) {
						map = new HashMap<Integer, Set<Integer>>();
					}
					map.put(p, g5);
				}
			}
			sets[turn].criticalFours = map != null ? map : EMPTY_MAP;
		}
	}

	private int countNewValue(int p, int turn) {
		int newValue = 0;
		for (int d = 0; d < 4; d++) {
			int il = -1;
			int c = 0;
			int cm = 0;
			int g = 1;
			while(true) {
				int q = field.jump(p, d, il);
				if(q < 0 || getValue(q) == 1 - turn) {
					il++;
					break;
				}
				g++;
				if(getValue(q) == turn) {
					c++;
				}
				if(il == -4) break;
				il--;
			}
			cm = c;
			int ir = 1;
			while(true) {
				int q = field.jump(p, d, ir);
				if(q < 0 || getValue(q) == 1 - turn) {
					ir--;
					break;
				}
				if(g == 5) {
					if(getValue(field.jump(q, d, -5)) == turn) {
						c--;
					}
				} else {
					g++;
				}
				if(getValue(q) == turn) {
					c++;
				}
				if(c > cm) cm = c;
				if(ir == 4) break;
				ir++;
			}
			if(ir - il + 1 >= 5 && cm + 1 > newValue) {
				newValue = cm + 1;
			}
		}
		return newValue;
	}

	void onChangeLongest(int p, int turn, int oldValue, int newValue) {
		if(oldValue != newValue) {
			longest[turn][p] = newValue;
			sets[turn].replace(p, oldValue, newValue);
		}
	}

	public Set<Integer> getPlacesForFive(int turn) {
		return sets[turn].sets[5];
	}

	public Set<Integer> getPlacesFor(int turn, int count) {
		return sets[turn].sets[count];
	}

	public Map<Integer, Set<Integer>> getCriticalFours(int turn) {
		return sets[turn].criticalFours;
	}

	class SetWrapper {
		Set<Integer>[] sets = new Set[]{null, null, null, new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>()};
		Map<Integer, Set<Integer>> criticalFours = new HashMap<Integer, Set<Integer>>();
		
		void replace(int p, int oldValue, int newValue) {
			if(oldValue >= 3 && oldValue <= 5) {
				sets[oldValue].remove(p);
			}
			if(newValue >= 3 && newValue <= 5) {
				sets[newValue].add(p);
			}
		}

		SetWrapper copy() {
			SetWrapper copy = new SetWrapper();
			for (int i = 3; i <= 5; i++) {
				copy.sets[i].addAll(sets[i]);
			}
			copy.criticalFours.putAll(criticalFours);
			return copy;
		}
	}

	public String getCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(turn).append(":");
		int ln = 0;
		int e = 0;
		boolean f = false;
		for (int p = 0; p < state.length; p++) {
			int v = state[p];
			if(v == EMPTY) {
				e++;
			} else {
				if(!f) {
					sb.append((char)(65 + ln));
					f = true;
				}
				if(e > 0) sb.append((char)(96 + e));
				sb.append(v);
				e = 0;
			}
			if(field.isRightBorder(p)) {
				ln++;
				e = 0;
				f = false;
			}
		}
		return sb.toString();
	}
}
