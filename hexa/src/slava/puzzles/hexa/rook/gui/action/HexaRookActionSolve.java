package slava.puzzles.hexa.rook.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.hexa.rook.analysis.HexaRookSolver;
import slava.puzzles.hexa.rook.model.HexaRookModel;

public class HexaRookActionSolve extends PuzzleActionSolve {
	HexaRookSolver solver = new HexaRookSolver();

	public HexaRookActionSolve() {}
	
	protected void execute() {
		HexaRookModel model = (HexaRookModel)manager.getModel();
		solver.setPuzzle(model.getPuzzleInfo());
		//solver.setRandomizing(true);
		int solutionLimit = 100; //model.getPreferences().getSolutionLimit();
		solver.setSolutionLimit(solutionLimit + 1);
		solver.setField(model.getField());
		solver.solve();
		if(solver.getSolutionCount() > 0) {
			model.setSolutions(solver.getSolutions());
		}
		String sc = "";
		if(solver.getSolutionCount() >= solutionLimit) {
			sc = "> " + solutionLimit;
		} else {
			sc = "= " + solver.getSolutionCount();
		}
		model.setSolutionInfo("Solution count " + sc);
//		model.setDistribution(analyzer.getCardDistribution(), analyzer.getSumDistribution());
	}


}
