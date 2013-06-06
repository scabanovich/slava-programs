package slava.puzzles.connectfour.ui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.connectfour.model.ConnectFourModel;
import slava.puzzles.connectfour.solve.ConnectFourSolver;
import slava.puzzles.connectfour.solve.ConnectFourState;
import slava.puzzles.connectfour.solve.ResultStorage;

public class ConnectFourActionSolve extends PuzzleActionSolve {

	protected void execute() {
		ConnectFourModel model = (ConnectFourModel)manager.getModel();
		if(model.isCompleted()) {
			return;
		}
		ConnectFourState state = model.getState().copy();
		ConnectFourSolver solver = ConnectFourSolver.getInstance();
		int depth = 100;
		int loosingAtComplex = -1;
		while(depth < 610) {
			long t0 = System.currentTimeMillis();
			int storedDepth = depth > 400 ? 6 : depth > 350 ? 5 : 4;
			byte[] results = solver.getResults(state, storedDepth, depth, loosingAtComplex);
			for (int i = 0; i < results.length; i++) {
				if(results[i] == ConnectFourSolver.COMPLEX) results[i] = ConnectFourSolver.UNKNOWN;
			}
			model.setAnalysisResults(results);
			manager.getComponent().repaint();
			long dt = System.currentTimeMillis() - t0;
			System.out.println("Depth=" + depth + " in " + dt);
			System.out.println("Cache size=" + ResultStorage.getInstance().getStorageSize());
			int u = 0, w = 0;
			for (int i = 0; i < results.length; i++) {
				if(ConnectFourSolver.isUncertain(results[i])) {
					u++;
				} else if(results[i] == model.getState().getTurn()) {
					w++;
				}
			}
			if(u < 2 || w > 0) {
				break;
			}
			if(loosingAtComplex != state.getTurn()) {
				loosingAtComplex = state.getTurn();
			} else {
				loosingAtComplex = ConnectFourState.NEXT_TURN[state.getTurn()];
			}
			depth += 25;
		}
	}

}
