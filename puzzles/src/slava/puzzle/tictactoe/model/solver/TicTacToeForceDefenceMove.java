package slava.puzzle.tictactoe.model.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import slava.puzzle.tictactoe.model.TicTacToeState;

public class TicTacToeForceDefenceMove {
	TicTacToeState state;

	int t0;
	int turn0;
	boolean isForceDefence = false;

	public TicTacToeForceDefenceMove() {
	}

	public void setState(TicTacToeState state) {
		this.state = state;
		t0 = state.getT();
		turn0 = state.getTurn();
	}

	public boolean isForceDefence() {
		return isForceDefence;
	}

	public Map<Integer, Integer> find() {
		isForceDefence = false;
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		Set<Integer> set5 = state.getPlacesForFive(1 - turn0);
		if(set5.size() > 1) {
			for (Integer i: set5) result.put(i, 1);
			isForceDefence = true;
		} else if(set5.size() == 1) {
			result.put(set5.iterator().next(), -1);
			isForceDefence = true;
		} else if(!state.getPlacesFor(1 - turn0, 4).isEmpty()) {
			state.flipTurn();
			TicTacToeForceSolver s = new TicTacToeForceSolver();
			s.setState(state);
			s.setCostLimit(120);
			int[] force = s.solve();
			if(force != null) {
				Set<Integer> ps = new HashSet<Integer>();
				ps.addAll(state.getPlacesForFive(1 - turn0));
				ps.addAll(state.getPlacesFor(turn0, 4));
				if(!state.getCriticalFours(1 - turn0).isEmpty()) {
					isForceDefence = true;
					state.flipTurn();
					Map<Integer, Set<Integer>> critical = new HashMap<Integer, Set<Integer>>(state.getCriticalFours(1 - turn0));
					for (int i: critical.keySet()) {
						if(!ps.contains(i)) {
							state.move(i);
							if(state.getCriticalFours(1 - turn0).isEmpty()) {
								ps.add(i);
							}
							state.back();
						}
						for (int j: critical.get(i)) {
							if(!ps.contains(j)) {
								state.move(j);
								if(state.getCriticalFours(1 - turn0).isEmpty()) {
									ps.add(j);
								}
								state.back();
							}
						}
					}
					state.flipTurn();
				} else {
					for (int i = 0; i < force.length; i++) {
						ps.add(force[i]);
						state.move(force[i]);
						ps.addAll(state.getPlacesFor(turn0, 4));
						ps.addAll(state.getPlacesForFive(1 - turn0));
					}
					for (int i = 0; i < force.length; i++) {
						state.back();
					}
				}
				state.flipTurn();
				for (Integer p: ps) {
					state.move(p);
					s = new TicTacToeForceSolver();
					s.setCostLimit(120);
					s.setState(state);
					int[] force1 = s.solve();
					state.back();
					if(force1 != null) {
						result.put(p, force1.length);
					} else {
						result.put(p, -1);
					}
				}
			} else {
				state.flipTurn();
			}
		}
		return result;
	}

}
