package slava.puzzle.paths.nook.gui.action;

import slava.puzzle.paths.nook.analysis.NookPathsPuzzleCheck;
import slava.puzzle.paths.nook.analysis.NookPathsSimpleSolver;
import slava.puzzle.paths.nook.model.NookPathsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class NookPathsActionSolve extends PuzzleActionSolve {
	NookPathsSimpleSolver solver = new NookPathsSimpleSolver();

	public NookPathsActionSolve() {}
	
	protected void execute() {
		NookPathsModel model = (NookPathsModel)manager.getModel();
		String error = NookPathsPuzzleCheck.getPuzzleError(model.getPuzzleInfo());
		if(error != null) {
			throw new RuntimeException(error);
		}
		solver.setField(model.getField());
		solver.setFilter(model.getPuzzleInfo().getFilter());
		solver.setRandomizing(true);
		int solutionLimit = model.getPreferences().getSolutionLimit();
		solver.setSolutionLimit(solutionLimit + 1);
		int[] data = (int[])model.getPuzzleInfo().getData().clone();
		for (int i = 0; i < data.length; i++) data[i]--;
		solver.setData(data);
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
