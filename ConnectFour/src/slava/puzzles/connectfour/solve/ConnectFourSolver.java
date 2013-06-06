package slava.puzzles.connectfour.solve;

import java.util.HashMap;
import java.util.Map;

public class ConnectFourSolver {
	public static byte NOMOVE = -3;
	public static byte COMPLEX = -2;
	public static byte UNKNOWN = -1;
	public static byte FIRST = ConnectFourState.FIRST;
	public static byte SECOND = ConnectFourState.SECOND;
	public static byte FORCED = 2;
	public static byte DRAW = 3;
	public static byte DRAW_OR_FIRST = 4;
	public static byte DRAW_OR_SECOND = 5;

	public static boolean isUncertain(int result) {
		return result == UNKNOWN || result == DRAW_OR_FIRST || result == DRAW_OR_SECOND;
	}
	
	static int[] LOG = {0, 0, 10, 16, 20, 23, 26, 28, 30, 32};
	
	static ConnectFourSolver instance = new ConnectFourSolver();

	public static ConnectFourSolver getInstance() {
		return instance;
	}
	class Results {
		byte[] results;
		int depth;

		Results(byte[] results, int depth) {
			this.results = results;
			this.depth = depth;
		}
	}

	Map<String, Results> storedResults = new HashMap<String, Results>();

	private ConnectFourSolver() {
	}

	public byte[] getResults(ConnectFourState state, int storedDepth, int depth, int losingAtComplex) {
		return getResults(state, storedDepth, depth, false, losingAtComplex);
	}

	public byte[] getResults(ConnectFourState state, int storedDepth, int depth, boolean all, int losingAtComplex) {
//		if(state.getFilled() > 25) {
//			depth = 1000;
//		}
		String code = state.code();
		int code1 = state.getSmartState().code(ResultStorage.MOD_1);
		int code2 = state.getSmartState().code(ResultStorage.MOD_2);
		if(ResultStorage.getInstance().contains(code1, code2)) {
			byte[] results = ResultStorage.getInstance().get(code1, code2);
			if(ResultStorage.getInstance().getDepth(code1, code2) >= depth) {
				return results;
			} else {
				for (int i = 0; i < results.length; i++) {
					if(results[i] == COMPLEX) {
						if(!state.canMove(i)) {
							System.out.println("fuck");
							results[i] = NOMOVE;
						} else {
							results[i] = UNKNOWN;
						}
					}
				}
				if(areResultsResolved(state, results)) {
					return results;
				}
			}
			
		}
		boolean hasStored = storedResults.containsKey(code);
		byte[] results = hasStored ? storedResults.get(code).results : 
			ResultStorage.getInstance().contains(code1, code2) ? ResultStorage.getInstance().get(code1, code2) : null;
		if(hasStored) {
			if(storedResults.get(code).depth >= depth && areResultsResolved(state, results)) {
				return results;
			}
		} else {
			if(results == null) {
				results = new byte[state.getField().getWidth()];
				for (int i = 0; i < results.length; i++) {
					results[i] = state.canMove(i) ? UNKNOWN : NOMOVE;
				}
			} else {
			}
			boolean forced = force(state, results, all);
			if(forced) {
				return results;
			}
		}
		byte turn = state.getTurn();
		byte next = ConnectFourState.NEXT_TURN[turn];

		int u = 0;
		for (int i = 0; i < results.length; i++) {
			if(isUncertain(results[i])) {
				u++;
			}
		}
		if((u > 1 && depth >= LOG[u]) || (u == 1 && all)) {
			for (int i = 0; i < results.length; i++) {
				if(!isUncertain(results[i])) continue;
				state.move(i);
				byte[] rs = getResults(state, storedDepth - 1, depth - LOG[u], true, losingAtComplex);
				boolean hasUnknown = false, hasWin = false, hasDraw = false, hasComplex = false; 
				boolean nextCannotWin = (results[i] == DRAW_OR_FIRST + turn);
				boolean turnCannotWin = (results[i] == DRAW_OR_FIRST + next), turnCannotWin1 = false;
				for (int j = 0; j < rs.length; j++) {
					if(rs[j] == next) {
						results[i] = next;
						break;
					} else if(rs[j] == turn) {
						hasWin = true;
					} else if(rs[j] == DRAW_OR_FIRST + next) {
						if(results[i] == DRAW_OR_FIRST + turn) {
							results[i] = DRAW;
							break;
						} else {
							turnCannotWin1 = true;
							results[i] = (byte)(DRAW_OR_FIRST + next);
						}
					} else if(rs[j] == DRAW_OR_FIRST + turn) {
						nextCannotWin = true;
					} else if(rs[j] == UNKNOWN) {
						hasUnknown = true;
					} else if(rs[j] == DRAW) {
						if(results[i] == DRAW_OR_FIRST + turn) {
							results[i] = DRAW;
							break;
						} else {
							hasDraw = true;
						}
					} else if(rs[j] == COMPLEX) {
						if(storedDepth > 0 || hasStored) {
							hasUnknown = true;
						} else if(losingAtComplex == turn) {
							//complex is as bad as loss for losingAtComplex.
							results[i] = COMPLEX;
							break;
						} else if(losingAtComplex == next) {
							hasComplex = true;
						}
					}
				}
				boolean unc = isUncertain(results[i]);
				if(unc && results[i] != DRAW_OR_FIRST + next) {
					if(!hasUnknown) {
						if(hasComplex) {
							if((results[i] == DRAW_OR_FIRST + turn) && hasDraw) {
								results[i] = DRAW;
							} else {
								results[i] = COMPLEX;
							}
						} else if(hasDraw) {
							results[i] = DRAW;
						} else if(nextCannotWin) {
							results[i] = (byte)(DRAW_OR_FIRST + turn);
						} else if(hasWin) {
							results[i] = turn;
						}
					} else if(hasDraw) {
						results[i] = (byte)(DRAW_OR_FIRST + next);
					}
				} else if(unc && !turnCannotWin1) {
					if(hasUnknown || hasComplex) {
						results[i] = UNKNOWN;
					} else if(hasDraw) {
						results[i] = DRAW;
					} else if(nextCannotWin) {
						results[i] = DRAW;
					} else if(hasWin) {
						results[i] = turn;
					}
				}
				state.back();
				if(results[i] == turn || (results[i] == COMPLEX && losingAtComplex == next)) {
					break;
				}
			}
		} else if(u > 1 && depth < LOG[u]) {
			if(losingAtComplex > -1 && storedDepth <= 0 && !hasStored) {
				for (int i = 0; i < results.length; i++) {
					if(isUncertain(results[i])) results[i] = COMPLEX;
				}
			}
		}
		if(storedDepth > 0) {
			if(!hasStored) {
				storedResults.put(code, new Results(results, depth));
			} else {
				storedResults.get(code).depth = depth;
			}
		} else {
			ResultStorage.getInstance().put(code1, code2, results, depth);
		}
//		if(cache.size() % 50000 == 0) System.out.println(cache.size());
		return results;
	}

	private boolean areResultsResolved(ConnectFourState state, byte[] results) {
		boolean unc = false;
		int turn = state.getTurn();
		for (int i = 0; i < results.length; i++) {
			if(isUncertain(results[i])) {
				unc = true;
			} else if(results[i] == turn) {
				return true;
			}
		}
		return !unc;
	}

	/**
	 * Returns true, if winning move is available, 
	 * or all moves are resolved in 3 moves,
	 * or one forced move is found and all = false that is not complete analysis is not requested
	 *  
	 * @param state
	 * @param results
	 * @param all - if true, method will return false, even if one force move is found
	 * @return
	 */
	public boolean force(ConnectFourState state, byte[] results, boolean all) {
		byte turn = state.getTurn();
		int x = state.getSmartState().getWinningMove();
		if(x >= 0) {
			results[x] = turn;
			return true;
		}
		x = state.getSmartState().getForcedMove();
		if(x >= 0) {
			for (int i = 0; i < results.length; i++) {
				if(i != x && isUncertain(results[i])) results[i] = ConnectFourState.NEXT_TURN[turn];
			}
			if(!all) {
				results[x] = FORCED;
				return true;
			} else if(state.getSmartState().isLoosing()) {
				results[x] = ConnectFourState.NEXT_TURN[turn];
				return true;
			}
		}
		for (int i = 0; i < results.length; i++) {
			if(state.isLosing(i)) {
				results[i] = ConnectFourState.NEXT_TURN[turn];
			}
		}
		x = state.getWinningIn2Moves();
//		if(x < 0) x = state.getWinningIn3Moves();
		if (x < 0) x = state.canFirstGoAsSecond();
		if(x >= 0) {
			results[x] = turn;
			return true;
		}
		for (int i = 0; i < results.length; i++) {
			if(!isUncertain(results[i])) continue;
			state.move(i);
			if(state.isDraw()) {
				results[i] = ConnectFourState.DRAW_IS_WIN_OF_SECOND ? SECOND : DRAW;
			} else {
				int x1 = state.getSmartState().getWinningMove();
				if(x1 < 0) x1 = state.getWinningIn2Moves();
//				if(x1 < 0) x1 = state.getWinningIn3Moves();
				if(x1 >= 0) {
					results[i] = ConnectFourState.NEXT_TURN[turn];
				} else {
					byte r = state.canSecondGoAsFirst();
					if(r == SECOND) {
						results[i] = r;
					} else if(r == DRAW_OR_SECOND) {
						if(results[i] == DRAW_OR_FIRST) {
							results[i] = DRAW;
						} else {
							results[i] = r;
						}
					}
				}
			}
			state.back();
		}
		return false;
	}

}
