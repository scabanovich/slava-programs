package slava.puzzle.tictactoe.model.solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import slava.puzzle.tictactoe.model.TicTacToeState;

public class TicTacToeForceSolver {
	int maxCostLim = 200;
	TicTacToeState state;
	int tLim = 20;
	int costLim = 10;

	int t0;
	int turn0;

	int t;
	int cost;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysC;
	
	int moveCount = 0;
	
	Map<String, Integer> won = new HashMap<String, Integer>();
	Set<String> lost = new HashSet<String>();
	Set<String> uncertain = new HashSet<String>();
	

	int[] solution = null;
	boolean tLimReached;

	public TicTacToeForceSolver() {}

	public void setState(TicTacToeState state) {
		this.state = state;
		t0 = state.getT();
		turn0 = state.getTurn();
		won.clear();
		lost.clear();
		uncertain.clear();
	}

	public void setCostLimit(int n) {
		maxCostLim = n;
	}

	/**
	 * Returns succession to victory or null;
	 * @return
	 */
	public int[] solve() {
		if(state.isCompleted()) {
			return null;
		}
		if(!state.getPlacesForFive(turn0).isEmpty()) {
			return new int[]{state.getPlacesForFive(turn0).iterator().next()};
		}
		solution = null;
		moveCount = 0;
		while(solution == null && costLim < maxCostLim) {
			_solve();
			if(!tLimReached) break;
			costLim += 10;
//			System.out.println("--->" + costLim);
		}
		System.out.println(moveCount + " -->" + costLim);
		return solution;
	}

	public boolean isForceAnalysesCompleted() {
		return !tLimReached;
	}

	void _solve() {
		init();
		int r = getResultForAttacker();
		if(r == 0) tLimReached = true;
		while(state.getT() > t0) {
			state.back();
		}
	}

	void init() {
		wayCount = new int[tLim + 1];
		way = new int[tLim + 1];
		waysP = new int[tLim + 1][50];
		waysC = new int[tLim + 1][50];
		uncertain.clear();
		t = 0;
		cost = 0;
		tLimReached = false;
	}

	//result -1 - failure of attacker;
	//        0 - limit is reached
	//        n - win in n moves.

	int getResultForAttacker() {
		if(cost >= costLim) {
			return 0;
		}
		String code = getStateCode();
		if(won.containsKey(code)) {
			return won.get(code);
		} else if(lost.contains(code)) {
			return -1;
		} else if(t == tLim || uncertain.contains(code)) {
			return 0;
		}
		srch();
		int br = -1;
		for (int i = 0; i < wayCount[t]; i++) {
			way[t] = i;
			int p = waysP[t][i];
			state.move(p);
			cost += waysC[t][i];
			moveCount++;
			t++;
			int r = getResultForDefender();
			if(r > 0 && solution == null && isForce()) {
				solution = new int[t];
				for (int k = 0; k < t; k++) solution[k] = waysP[k][way[k]];
			}
			--t;
			state.back();
			cost -= waysC[t][i];
			if(r > 0) {
				won.put(code, r + 1);
				return r + 1;
			} else if(r == 0) {
				br = 0;
			}
		}
		if(br == 0) {
			uncertain.add(code);
		} else if(br < 0) {
			lost.add(code);
		}
		return br;
	}

	int getResultForDefender() {
		if(isWon()) {
			return 1;
		}
		String code = getStateCode();
		if(won.containsKey(code)) {
			return won.get(code);
		} else if(lost.contains(code)) {
			return -1;
		} else if(t == tLim || uncertain.contains(code)) {
			return 0;
		}
		srch();
		if(wayCount[t] == 0) {
			return -1;
		}
		if(cost >= costLim) {
			return 0;
		}
		int br = 0;
		boolean limit = false;
		for (int i = 0; i < wayCount[t]; i++) {
			way[t] = i;
			int p = waysP[t][i];
			cost += waysC[t][i];
			state.move(p);
			moveCount++;
			t++;
			int r = getResultForAttacker();
			--t;
			state.back();
			cost -= waysC[t][i];
			if(r < 0) {
				lost.add(code);
				return -1;
			} else if(r == 0) {
				limit = true;
			} else if(r > br) {
				br = r;
			}
		}
		int result = limit ? 0 : br + 1;
		if(limit) {
			uncertain.add(code);
		} else {
			won.put(code, result);
		}
		return result;
	}

	boolean isForce() {
		for (int i = 1; i < t; i += 2) {
			if(wayCount[i] > 1) return false;
		}
		return true;
	}

	static int[] LOG = {0, 0, 10, 16, 20, 23, 26, 28, 30, 32, 34, 35};

	void srch() {
		wayCount[t] = 0;
		if(turn0 == state.getTurn()) {
			if(state.isCompleted()) return; //lost
			if(!state.getPlacesForFive(turn0).isEmpty()) {
				waysP[t][0] = state.getPlacesForFive(turn0).iterator().next();
				waysC[t][0] = 0;
				wayCount[t] = 1;
			} else if(!state.getPlacesForFive(1 - turn0).isEmpty()) {
				if(state.getPlacesForFive(1 - turn0).size() == 1) {
					waysP[t][0] = state.getPlacesForFive(1 - turn0).iterator().next();
					waysC[t][0] = 0;
					wayCount[t] = 1;
				}
			} else {
				Map<Integer, Set<Integer>> criticalFour = state.getCriticalFours(turn0);
				if(!criticalFour.isEmpty()) {
					waysP[t][0] = criticalFour.keySet().iterator().next();
					waysC[t][0] = 0;
					wayCount[t] = 1;
					return;
				}
				Set<Integer> set4 = state.getPlacesFor(turn0, 4);
				for (Integer i: set4) {
					waysP[t][wayCount[t]] = i;
					wayCount[t]++;
				}
				int cost4 = wayCount[t] >= LOG.length ? 40 : LOG[wayCount[t]];
				for (int i = 0; i < wayCount[t]; i++) {
					waysC[t][i] = cost4;
				}
				Integer[] set3 = state.getPlacesFor(turn0, 3).toArray(new Integer[0]);
				int wc = wayCount[t];
				for (Integer p: set3) {
					state.move(p);
					Map<Integer, Set<Integer>> f = state.getCriticalFours(turn0);
					if(!f.isEmpty()) {
						waysP[t][wayCount[t]] = p;
						waysC[t][wayCount[t]] = f.size() > 2 ? 10 : 50;
						wayCount[t]++;
					}
					state.back();
				}
				int wcd = wayCount[t] - wc;
				int cost3 = cost4 + (wayCount[t] >= LOG.length ? 40 : LOG[wcd]);
				for (int i = wc; i < wayCount[t]; i++) {
					if(waysC[t][i] == 50) waysC[t][i] = cost3;
				}
			}
		} else {
			if(state.isCompleted()) return; //win of turn0
			if(!state.getPlacesForFive(1 - turn0).isEmpty()) {
				return; //turn0 is lost.
			}
			Set<Integer> set = state.getPlacesForFive(turn0);
			if(!set.isEmpty()) {
				waysP[t][0] = set.iterator().next();
				waysC[t][0] = 0;
				wayCount[t] = 1;
				return;
			}
			if(!state.getCriticalFours(1 - turn0).isEmpty()) {
				return; //turn0 is lost.
			}
			Map<Integer, Set<Integer>> criticalFour = state.getCriticalFours(turn0);
			if(!criticalFour.isEmpty()) {
				for (int p: criticalFour.keySet()) {
					if(criticalFour.get(p).size() > 2) {
						waysP[t][0] = p;
						waysC[t][0] = 0;
						wayCount[t] = 1;
						return;
					}
				}
				Set<Integer> vars = new HashSet<Integer>();
				vars.addAll(state.getPlacesFor(1 - turn0, 4));
				for (int p: criticalFour.keySet()) {
					vars.add(p);
					vars.addAll(criticalFour.get(p));
				}
				for (int p: vars) {
					waysP[t][wayCount[t]] = p;
					wayCount[t]++;
				}
				int cost4 = wayCount[t] >= LOG.length ? 40 : LOG[wayCount[t]];
				for (int i = 0; i < wayCount[t]; i++) {
					waysC[t][i] = cost4;
				}
			}
			
			//otherwize - no move, hence no way for turn0 to win.
		}
	}

	String getStateCode() {
		int[] a = new int[(t + 1) / 2];
		int[] b = new int[t / 2];
		int ai = 0, bi = 0;
		for (int i = 0; i < t; i++) {
			int p = waysP[i][way[i]];
			if(i % 2 == 0) {
				a[ai++] = p;
			} else {
				b[bi++] = p;
			}
		}
		Arrays.sort(a);
		Arrays.sort(b);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < a.length; i++) result.append(' ').append(a[i]);
		for (int i = 0; i < b.length; i++) result.append(' ').append(b[i]);
		return result.toString();
	}

	boolean isWon() {
		return state.getTurn() != turn0 && (state.isCompleted() 
			|| (state.getPlacesForFive(1 - turn0).isEmpty() && state.getPlacesForFive(turn0).size() > 1));
	}

}
