package slava.puzzles.seabattle.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattle.analysis.SeaBattleSolver;
import slava.puzzles.seabattle.model.SeaBattleModel;

public class SeaBattleActionSolve extends PuzzleActionSolve {
	SeaBattleSolver solver = new SeaBattleSolver();

	public SeaBattleActionSolve() {}
	
	protected void execute() {
		SeaBattleModel model = (SeaBattleModel)manager.getModel();
		solver.setPuzzle(model.getPuzzleInfo());
		solver.setRandomizing(true);
		int solutionLimit = model.getPreferences().getSolutionLimit();
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
