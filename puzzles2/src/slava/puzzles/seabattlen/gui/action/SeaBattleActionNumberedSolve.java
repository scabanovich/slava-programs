package slava.puzzles.seabattlen.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattlen.analysis.SeaBattleNumberedSolver;
import slava.puzzles.seabattlen.model.SeaBattleNumberedModel;

public class SeaBattleActionNumberedSolve extends PuzzleActionSolve {
	SeaBattleNumberedSolver solver = new SeaBattleNumberedSolver();

	public SeaBattleActionNumberedSolve() {}
	
	protected void execute() {
		SeaBattleNumberedModel model = (SeaBattleNumberedModel)manager.getModel();
		solver.setPuzzle(model.getPuzzleInfo());
		solver.setRandomizing(true);
		int solutionLimit = 10;
		///model.getPreferences().getSolutionLimit();
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
