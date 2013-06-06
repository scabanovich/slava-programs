package slava.puzzles.seabattlef.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattlef.analysis.SeaBattleFirstSolver;
import slava.puzzles.seabattlef.model.SeaBattleFirstModel;

public class SeaBattleFirstActionSolve extends PuzzleActionSolve {
	SeaBattleFirstSolver solver = new SeaBattleFirstSolver();

	public SeaBattleFirstActionSolve() {}
	
	protected void execute() {
		SeaBattleFirstModel model = (SeaBattleFirstModel)manager.getModel();
		solver.setPuzzle(model.getPuzzleInfo());
		solver.setRandomizing(true);
		//TODO
		int solutionLimit = 10;//model.getPreferences().getSolutionLimit();
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
