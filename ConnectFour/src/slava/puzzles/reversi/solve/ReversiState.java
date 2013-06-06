package slava.puzzles.reversi.solve;

import java.util.Random;

import com.slava.common.RectangularField;

public class ReversiState {
	static Random seed = new Random();

	public static final RectangularField FIELD = new RectangularField();
	static {
		FIELD.setOrts(RectangularField.DIAGONAL_ORTS);
		FIELD.setSize(8, 8);
	}

	public static final int EMPTY = -1;
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int[] NEXT_TURN  = {SECOND, FIRST};

	int[] fieldRestrictions;
	int[] state = new int[FIELD.getSize()];

	int turn = FIRST;

	int[] moves;
	ReversiState previous;
	ReversiState next;

	public ReversiState(int[] fieldRestrictions) {
		this.fieldRestrictions = fieldRestrictions;
		for (int p = 0; p < state.length; p++) state[p] = EMPTY;
		state[FIELD.getIndex(3, 3)] = FIRST;
		state[FIELD.getIndex(3, 4)] = SECOND;
		state[FIELD.getIndex(4, 3)] = SECOND;
		state[FIELD.getIndex(4, 4)] = FIRST;
		lookForMoves();
	}

	public ReversiState(ReversiState parent, int place) {
		fieldRestrictions = parent.fieldRestrictions;
		previous = parent;
		parent.next = this;
		for (int p = 0; p < state.length; p++) state[p] = parent.state[p];
		state[place] = parent.turn;
		for (int d = 0; d < 8; d++) {
			int q = jump(place, d);
			while(q >= 0 && state[q] == NEXT_TURN[parent.turn]) {
				q = jump(q, d);
			}
			if(q >= 0 && state[q] == parent.turn) {
				q = jump(place, d);
				while(q >= 0 && state[q] == NEXT_TURN[parent.turn]) {
					state[q] = parent.turn;
					q = jump(q, d);
				}
			}
		}
		turn = NEXT_TURN[parent.turn];
		lookForMoves();
	}

	int jump(int p, int d) {
		int q = FIELD.jump(p, d);
		return q < 0 || fieldRestrictions[q] == 0 ? -1 : q;
	}

	public int getDifference() {
		int result = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == FIRST) result++; else if(state[p] == SECOND) result--;
		}
		return result;
	}

	public int[] getMoves() {
		return moves;
	}

	public void print() {
		for (int p = 0; p < state.length; p++) {
			String ch = fieldRestrictions[p] == 0 ? "*" : state[p] == EMPTY ? "+" : state[p] == FIRST ? "x" : "o";
			System.out.print(" " + ch);
			if(FIELD.isRightBorder(p)) System.out.println("");
		}
	}

	void lookForMoves() {
		int n = lookForMoves(turn);
		if(n == 0) {
			n = lookForMoves(NEXT_TURN[turn]);
			if(n > 0) {
				turn = NEXT_TURN[turn];
			}
		}
	}

	int lookForMoves(int t) {
		int n = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == t) {
				for (int d = 0; d < 8; d++) {
					int q = jump(p, d);
					int f = 0;
					while(q >= 0 && state[q] == NEXT_TURN[t]) {
						q = jump(q, d);
						f++;
					}
					if(q >= 0 && f > 0 && state[q] == EMPTY) {
						state[q] = 1000;
						n++;
					}
				}
			}
		}
		moves = new int[n];
		n = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == 1000) {
				moves[n++] = p;
				state[p] = EMPTY;
			}
		}		
		return n;
	}

	public boolean canReachDifference(int turn, int diff, boolean easily) {
		if(moves.length == 0) {
			if(turn == FIRST) {
				return diff <= getDifference();
			} else {
				return diff >= getDifference();
			}
		}
		if(this.turn == turn) {
			int[] ms = null;
			if(moves.length > 2 && easily) {
				ms = new int[]{moves[seed.nextInt(moves.length)], moves[seed.nextInt(moves.length)]};
				while(ms[0] == ms[1]) {
					ms[1] = moves[seed.nextInt(moves.length)];
				}
			} else {
				ms = moves;
			}
			for (int k = 0; k < ms.length; k++) {
				ReversiState s = new ReversiState(this, ms[k]);
				if(s.canReachDifference(turn, diff, easily)) return true;
			}
			return false;
		} else {
			for (int k = 0; k < moves.length; k++) {
				ReversiState s = new ReversiState(this, moves[k]);
				if(!s.canReachDifference(turn, diff, easily)) return false;
			}
			return true;
		}
	}
	
}
