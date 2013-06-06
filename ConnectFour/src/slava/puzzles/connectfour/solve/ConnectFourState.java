package slava.puzzles.connectfour.solve;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.slava.common.RectangularField;

public class ConnectFourState {
	    public static boolean DRAW_IS_WIN_OF_SECOND = true;
	    
	public static final int EMPTY = -1;
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final byte[] NEXT_TURN  = {SECOND, FIRST};
	RectangularField field;
	//-1 empty; 0 - first; 1 - second
	int[] state;
	int[] heights;

//	int turn = FIRST;
	int[] history;
	int filled = 0;

	ConnectFourSmartState smartState;
	
	public ConnectFourState(RectangularField field) {
		this.field = field;
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			state[i] = EMPTY;
		}
		heights = new int[field.getWidth()];
		history = new int[state.length];
		smartState = new ConnectFourSmartState(this);
	}

	/**
	 * Used by ui.
	 * @return
	 */
	public ConnectFourState copy() {
		ConnectFourState copy = new ConnectFourState(field);
		System.arraycopy(state, 0, copy.state, 0, state.length);
		System.arraycopy(heights, 0, copy.heights, 0, heights.length);
		System.arraycopy(history, 0, copy.history, 0, history.length);
		copy.filled = filled;
		smartState.copyTo(copy.smartState);
		return copy;
	}

	public RectangularField getField() {
		return field;
	}

	public ConnectFourSmartState getSmartState() {
		return smartState;
	}

	public int getValue(int p) {
		return state[p];
	}

	public byte getTurn() {
		return filled % 2 == 0 ? (byte)FIRST : (byte)SECOND;
	}

	public String code() {
		StringBuilder sb = new StringBuilder(10);
		for (int ix = 0; ix < heights.length; ix++) {
			sb.append(heights[ix]);
			int z = 0;
			for (int iy = 0; iy < heights[ix]; iy++) {
				int p = field.getIndex(ix, iy);
				z = z * 2 + state[p];
			}
			char zc = (char)(65 + z);
			sb.append(zc);
		}
		return sb.toString();
	}

	/**
	 * Move at column x
	 * @param x
	 */
	public void move(int x) {
		int p = field.getIndex(x, heights[x]);
		state[p] = getTurn();
		heights[x]++;
		history[filled++] = x;
		smartState.compute();
	}

	public int getLastMove() {
		return filled == 0 ? -1 : history[filled - 1];
	}

	public void move(int[] xs) {
		for (int i = 0; i < xs.length; i++) move(xs[i]);
	}

	public void back() {
		int x = history[--filled];
		heights[x]--;
		int p = field.getIndex(x, heights[x]);
		state[p] = EMPTY;
		smartState.compute();
	}

	public boolean canMove(int x) {
		return heights[x] < field.getHeight();
	}

	public int getFilled() {
		return filled;
	}
	
	public void print() {
		for (int p = 0; p < state.length; p++) {
			char c = state[p] == EMPTY ? '-' : state[p] == FIRST ? 'x' : 'o';
			System.out.print(" " + c);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

	// Trivial analysis
	///////////////////////////

	public boolean isDraw() {
		//TODO compute
		return filled == field.getSize();
	}

	public boolean isLast(int p, int player) {
		for (int d = 0; d < 4; d++) {
			int m = 1;
			int q = field.jump(p, d);
			while(q >= 0 && state[q] == player) {
				m++;
				q = field.jump(q, d);
			}
			int d1 = Runner.OPPOSITE_DIRECTION[d];
			q = field.jump(p, d1);
			while(q >= 0 && state[q] == player) {
				m++;
				q = field.jump(q, d1);
			}
			if(m >= 4) {
				return true;
			}
		}
		return false;
	}

//	public int getFirstForced() {
//		for (int x = 0; x < heights.length; x++) {
//			if(isWinning(x, NEXT_TURN[getTurn()])) {
//				return x;
//			}
//		}
//		return -1;
//	}
//
//	public int getLastForced() {
//		for (int x = heights.length -1; x >= 0; x--) {
//			if(isWinning(x, NEXT_TURN[getTurn()])) {
//				return x;
//			}
//		}
//		return -1;
//	}

	/**
	 * Returns true only if opponent can win by moving in the same column.
	 * @param x
	 * @return
	 */
	public boolean isLosing(int x) {
		return smartState.getResult(x) == NEXT_TURN[getTurn()];
	}

	public boolean isLostByNextMove() {
		return smartState.getResult() == NEXT_TURN[getTurn()];
	}

	public int getWinningIn2Moves() {
		for (int x = 0; x < heights.length; x++) {
			if(!canMove(x) || isLosing(x)) {
				continue;
			}
			move(x);
			boolean b = isLostByNextMove();
			back();
			if(b) {
				return x;
			}
		}
		return -1;
	}

	public boolean isLostIn2Moves() {
		if(filled == field.getSize()) {
			return false;
		}
		for (int x = 0; x < heights.length; x++) {
			if(!canMove(x) || isLosing(x)) {
				continue;
			}
			move(x);
			int x1 = smartState.isWinning() ? smartState.getForcedMove() : getWinningIn2Moves();
			back();
			if(x1 < 0) {
				return false;
			}
		}
		return true;
	}

	public int getWinningIn3Moves() {
		for (int x = 0; x < heights.length; x++) {
			if(!canMove(x) || isLosing(x)) {
				continue;
			}
			move(x);
			boolean b = (!smartState.isWinning()) && (isLostByNextMove() || isLostIn2Moves());
			back();
			if(b) {
				return x;
			}
		}
		return -1;
	}

	// Complex analysis
	/////////////////////////

	/**
	 * Returns 0 if first wins, 1 if second wins, 2 - if draw, -1 if not applyable
	 * @return
	 */
	class S {
		int[] deltas;
		int winner;
		S(int[] deltas, int winner) {
			this.deltas = deltas;
			this.winner = winner;
		}
		boolean isLower(S s) {
			for (int i = 0; i < deltas.length; i++) {
				if(s.deltas[i] > deltas[i]) return false;
			}
			return true;
		}
	}

	public byte canSecondGoAsFirst() {
		if(getTurn() == FIRST) {
			return -1;
		}
		int odd = 0;
		for (int x = 0; x < heights.length; x++) {
			if(heights[x] % 2 == 1) odd++;
		}
		if(odd > 3) return -1;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == EMPTY) {
				state[p] = (field.getY(p) % 2 == 0) ? FIRST : SECOND;
			}
		}
		byte result = -1;
		if(odd == 1) {
			result = canSecondGoAsFirst(collectFours());
		} else if(odd == 3) {
			int pp = -1;
			int wins = 0, draws = 0;
			for (int x = 0; x < heights.length; x++) {
				if(heights[x] % 2 == 1) {
					int p = field.getIndex(x, heights[x]);
					if(pp >= 0) {
						state[pp] = NEXT_TURN[state[pp]];
					}
					pp = p;
					state[pp] = NEXT_TURN[state[pp]];
					byte r = canSecondGoAsFirst(collectFours());
					if(r == ConnectFourSolver.DRAW_OR_SECOND) {
						draws++;
					} else if(r == SECOND) {
						wins++;
					}
				}
			}
			if(wins >= 2) {
				result = SECOND;
			} else if(wins + draws >= 2) {
				result = ConnectFourSolver.DRAW_OR_SECOND;
			} else {
				result = -1;
			}
		}
		for (int p = 0; p < state.length; p++) {
			if(field.getY(p) >= heights[field.getX(p)]) {
				state[p] = EMPTY;
			}
		}
		return result;
	}

	private byte canSecondGoAsFirst(Set<S> ss) {
		boolean sw = false;
		for (S s: ss) {
			if(s.winner == FIRST) return -1;
			if(s.winner == SECOND) sw = true;
		}
		if(DRAW_IS_WIN_OF_SECOND) return SECOND;
		return sw ? SECOND : ConnectFourSolver.DRAW_OR_SECOND;
	}

	private Set<S> collectFours() {
		Set<S> ss = new HashSet<S>();
		for (int p = 0; p < state.length; p++) {
			int w = state[p];
			for (int d = 0; d < 4; d++) {
				int q3 = field.jump(p, d, 3);
				if(q3 >= 0 && state[q3] == w) {
					int q1 = field.jump(p, d);
					int q2 = field.jump(q1, d);
					if(state[q1] == w && state[q2] == w) {
						int[] ds = new int[heights.length];
						ds[field.getX(p)] = getDS(p);
						ds[field.getX(q1)] = getDS(q1);
						ds[field.getX(q2)] = getDS(q2);
						ds[field.getX(q3)] = getDS(q3);
						S s = new S(ds, w);
						Iterator<S> it = ss.iterator();
						boolean drop = false;
						while(it.hasNext()) {
							S s1 = it.next();
							if(s1.isLower(s)) {
								it.remove();
							} else if(s.isLower(s1)) {
								drop = true;
							}
						}
						if(!drop) {
							ss.add(s);
						}
					}						
				}
			}
		}
		return ss;
	}

	private int getDS(int p) {
		int ds = field.getY(p) + 1 - heights[field.getX(p)];
		return ds < 0 ? 0 : ds;
	}

	/**
	 * There is an odd column 'x0' where second cannot go.
	 * There is only one more odd column 'xodd'.
	 * First always covers odd column that is not 'x0'.
	 * Returns 'xodd' if first wins in this way.
	 * @return
	 */
	public int canFirstGoAsSecond() {
		if(getTurn() == SECOND) {
			return -1;
		}

		int odd = 0;
		int x0 = -1;
		int xodd = -1;
		for (int x = 0; x < heights.length; x++) {
			int p = field.getIndex(x, heights[x] + 1);
			if(x0 < 0 && p >= 0 && heights[x] % 2 == 1 && smartState.isLastFor(p, FIRST)) {
				x0 = x;
			} else {
				if(heights[x] % 2 == 1) {
					odd++;
					xodd = x;
				}
			}
		}
		if(x0 < 0) return -1;
		if(odd > 1) return -1; // try 3, too!
		for (int p = 0; p < state.length; p++) {
			if(state[p] == EMPTY) {
				if(field.getX(p) != x0) {
					state[p] = (field.getY(p) % 2 == 1) ? FIRST : SECOND;
				} else {
					state[p] = (field.getY(p) % 2 == 0) ? FIRST : SECOND;
				}
			}
		}
		Set<S> ss = collectFours();
		for (int p = 0; p < state.length; p++) {
			if(field.getY(p) >= heights[field.getX(p)]) {
				state[p] = EMPTY;
			}
		}
		for (S s: ss) {
			if(s.winner == SECOND) return -1;
		}
		return xodd;
	}

}
