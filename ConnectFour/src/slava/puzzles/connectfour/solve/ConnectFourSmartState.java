package slava.puzzles.connectfour.solve;

import com.slava.common.RectangularField;

public class ConnectFourSmartState {
	public static final int LAST_FOR_FIRST = 2;
	public static final int LAST_FOR_SECOND = 3;
	public static final int LAST_FOR_BOTH = 4;
	public static final int EMPTY_UNREACHEABLE = 5;
	public static final int FILLED_IRRELEVANT = 6;
	public static final int SMART_EMPTY = 7;

	ConnectFourState state;

	int[] smartState;
	byte result = ConnectFourSolver.UNKNOWN;
	byte[] results;
	int forcedMove = -1;

	public ConnectFourSmartState(ConnectFourState state) {
		this.state = state;
		smartState = new int[state.getField().getSize()];
		results = new byte[state.getField().getWidth()];
		for (int x = 0; x < results.length; x++) {
			results[x] = state.canMove(x) ? ConnectFourSolver.UNKNOWN : ConnectFourSolver.NOMOVE;
		}
	}

	public int code(int mod) {
		int res = 0;
		for (int p = 0; p < smartState.length; p++) {
			int v = smartState[p];
			if(v < 0) v = SMART_EMPTY;
			res = res * 8 + v;
			if(res >= mod) res = res % mod;
		}
		return res;
	}

	public void copyTo(ConnectFourSmartState other) {
		System.arraycopy(smartState, 0, other.smartState, 0, smartState.length);
		other.result = result;
		other.forcedMove = forcedMove;
		System.arraycopy(results, 0, other.results, 0, results.length);
	}

	public int getSmartValue(int p) {
		return smartState[p];
	}

	public boolean isLastForBoth(int p) {
		return smartState[p] == LAST_FOR_BOTH;
	}

	public boolean isLastForFirstOnly(int p) {
		return smartState[p] == LAST_FOR_FIRST;
	}

	public boolean isLastForSecondOnly(int p) {
		return smartState[p] == LAST_FOR_SECOND;
	}

	public boolean isLastFor(int p, int player) {
		return isLastForBoth(p) || (player == ConnectFourState.FIRST ? isLastForFirstOnly(p) : isLastForSecondOnly(p));
	}

	public int getResult() {
		return result;
	}

	public boolean isWinning() {
		return result == state.getTurn();
	}

	public boolean isLoosing() {
		return result == ConnectFourState.NEXT_TURN[state.getTurn()];
	}

	public byte[] getResults() {
		return results;
	}

	public int getResult(int x) {
		return results[x];
	}

	public int getForcedMove() {
		return forcedMove;
	}

	public int getWinningMove() {
		return isWinning() ? forcedMove : -1;
	}

	public void compute() {
		for (int p = 0; p < smartState.length; p++) {
			smartState[p] = state.getValue(p);
		}
		computeLast();
		computeFilledIrrelevant();
		computeResult();
	}

	void computeLast() {
		for (int p = 0; p < smartState.length; p++) {
			if(smartState[p] != ConnectFourState.EMPTY) continue;
			boolean f = state.isLast(p, ConnectFourState.FIRST);
			boolean s = state.isLast(p, ConnectFourState.SECOND);
			if(f && s) {
				smartState[p] = LAST_FOR_BOTH;
				markUnreacheableAbove(p);
			} else if(f || s) {
				smartState[p] = f ? LAST_FOR_FIRST : LAST_FOR_SECOND;
				int q = state.getField().jump(p, 6);
				if(q >= 0 && smartState[q] == smartState[p]) {
					markUnreacheableAbove(p);
				}
			}
		}
	}

	void markUnreacheableAbove(int p) {
		int q = state.getField().jump(p, 2);
		while(q >= 0) {
			smartState[q] = EMPTY_UNREACHEABLE;
			q = state.getField().jump(q, 2);
		}
	}

	void computeFilledIrrelevant() {
		for (int p = 0; p < smartState.length; p++) {
			if(smartState[p] == ConnectFourState.FIRST || smartState[p] == ConnectFourState.SECOND) {
				boolean isRelevant = false;
				for (int d = 0; d < 4; d++) {
					if(isRelevant(p, d)) {
						isRelevant = true;
						break;
					}
				}
				if(!isRelevant) {
					smartState[p] = FILLED_IRRELEVANT;
				}
			}
		}
	}

	private boolean isRelevant(int p, int d) {
		int g = 1;
		int q = state.getField().jump(p, d);
		int v = (smartState[p] == ConnectFourState.FIRST) ? LAST_FOR_SECOND : LAST_FOR_FIRST;
		while(q >= 0 && (smartState[q] == smartState[p] 
		                 || smartState[q] == ConnectFourState.EMPTY
		                 || smartState[q] == v
		                 )) {
			g++;
			q = state.getField().jump(q, d);
		}
		d += 4;
		q = state.getField().jump(p, d);
		while(g < 4 && q >= 0 && (smartState[q] == smartState[p] 
		                         || smartState[q] == ConnectFourState.EMPTY
		                         || smartState[q] == v
		                         )) {
			g++;
			q = state.getField().jump(q, d);
		}
		return g >= 4;
	}

	void computeResult() {
		forcedMove = -1;
		result = ConnectFourSolver.UNKNOWN;
		for (int x = 0; x < results.length; x++) {
			results[x] = state.canMove(x) ? ConnectFourSolver.UNKNOWN : ConnectFourSolver.NOMOVE;
		}
		RectangularField field = state.getField();
		if(state.filled == field.getSize()) {
			result = ConnectFourState.DRAW_IS_WIN_OF_SECOND ? ConnectFourState.SECOND : ConnectFourSolver.DRAW;
			return;
		}
		byte turn = state.getTurn();
		byte next = ConnectFourState.NEXT_TURN[turn];
		int acceptedMoveCount = 0;
		int acceptedMove = -1;
		for (int x = 0; x < state.heights.length; x++) {
			if(results[x] != ConnectFourSolver.UNKNOWN) {
				continue;
			}
			int p = field.getIndex(x, state.heights[x]);
			if(isLastFor(p, turn)) {
				result = turn;
				results[x] = turn;
				forcedMove = x;
				return;
			} else if(isLastFor(p, next)) {
				if(forcedMove >= 0) {
					result = next; //but do not drop! continue hoping for winning move!
					continue;
				}
				forcedMove = x;
				int q = field.jump(p, 2);
				if(q >= 0 && isLastFor(q, next)) {
					//no use to defend in this column.
					result = next; //but do not drop! continue hoping for winning move!
					continue;
				}
			} else {
				int q = field.jump(p, 2);
				if(q >= 0 && isLastFor(q, next)) {
					results[x] = next;						
				} else {
					acceptedMoveCount++;
					acceptedMove = x;
				}
			}
		}
		if(forcedMove < 0) {
			if(acceptedMoveCount == 0) {
				result = next;
			} else if(acceptedMoveCount == 1) {
				forcedMove = acceptedMove;
			}
		} else {
			for (int x = 0; x < results.length; x++) {
				if(x != forcedMove && results[x] == ConnectFourSolver.UNKNOWN) {
					results[x] = next;
				}
			}
		}
		if(result == next) {
			for (int x = 0; x < results.length; x++) {
				if(results[x] == ConnectFourSolver.UNKNOWN) {
					results[x] = next;
				}
			}
		}
	}
}
