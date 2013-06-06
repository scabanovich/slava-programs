package slava.puzzle.paths.walker.analysis;

public class WalkerPathsMasuiGenerator extends WalkerPathsGenerator {

	public WalkerPathsMasuiGenerator() {}
	
	public void generate() {
		// generate path
		cleanPuzzle();

		solver.setSolutionLimit(1);
		solver.setTreeCountLimit(-1);

		int[] turns = puzzle.getTurns();

		solver.setField(field);
		solver.setFilter(puzzle.getFilter());
		solver.setParts(puzzle.getParts());
		solver.setTurns(turns);
		solver.solve();

		if(solver.getSolutionCount() == 0) return;

		//build complete restriction
		int[][] solution = solver.getSolution();
		for (int p = 0; p < turns.length; p++) {
			turns[p] = getTurnKind(solution[p]);
		}

		//reduce
		int[] order = getRandomOrder(field.getSize());
		
		solver.setTreeCountLimit(preferences.getTreeLimit());
		solver.setSolutionLimit(2);
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			int k = turns[p];
			turns[p] = WalkerTurnsCheck.NO_DATA;
			solver.solve();
			if(solver.getSolutionCount() != 1) {
				turns[p] = k;
			}
		}
		
	}

	int getTurnKind(int[] state) {
		for (int d = 0; d < 2; d++) {
			if(state[d] == 1 && state[WalkerPathsSolver.REVERSE[d]] == 1) return WalkerTurnsCheck.STRAIGHT;
		}
		for (int d = 0; d < 4; d++) {
			if(state[d] == 1 && state[WalkerPathsSolver.NEXT[d]] == 1) return WalkerTurnsCheck.TURN;
		}
		return WalkerTurnsCheck.NO_DATA;
	}

	int[] getRandomOrder(int size) {
		int[] q = new int[size];
		for (int i = 0; i < q.length; i++) q[i] = i;
		for (int i = 0; i < q.length; i++) {
			int j = i + (int)((q.length - i) * Math.random());
			int c = q[j];
			q[j] = q[i];
			q[i] = c;
		}
		return q;
	}
	
}
