package slava.puzzle.paths.walker.gui.action;

import slava.puzzle.paths.walker.analysis.WalkerPathsPuzzleCheck;
import slava.puzzle.paths.walker.analysis.WalkerPathsSolver;
import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class WalkerPathsActionSolve extends PuzzleActionSolve {

	WalkerPathsSolver solver = new WalkerPathsSolver();

	public WalkerPathsActionSolve() {}
	
	protected void execute() {
		WalkerPathsModel model = (WalkerPathsModel)manager.getModel();
		String error = WalkerPathsPuzzleCheck.getPuzzleError(model.getPuzzleInfo());
		if(error != null) {
			throw new RuntimeException(error);
		}
		solver.setField(model.getField());
		solver.setFilter(model.getPuzzleInfo().getFilter());
		solver.setTurns(model.getPuzzleInfo().getTurns());
		solver.setRandomizing(true);
		int solutionLimit = model.getPreferences().getSolutionLimit();
		solver.setSolutionLimit(solutionLimit + 1);
		solver.setParts(model.getPuzzleInfo().getParts());
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
		model.setSolutionInfo("Solution count " + sc + " Tree=" + solver.getTreeCount());
//		model.setDistribution(analyzer.getCardDistribution(), analyzer.getSumDistribution());
	}

}
