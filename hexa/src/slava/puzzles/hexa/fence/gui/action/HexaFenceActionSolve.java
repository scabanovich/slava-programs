package slava.puzzles.hexa.fence.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.hexa.fence.analysis.HexaFenceSolver;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFenceActionSolve extends PuzzleActionSolve {

	public HexaFenceActionSolve() {}

	protected void execute() {
		HexaFenceModel model = (HexaFenceModel)manager.getModel();
		int solutionLimit = 100; //model.getPreferences().getSolutionLimit();
		HexaFenceSolver solver = new HexaFenceSolver();
		solver.setField(model.getField());
		solver.setForm(model.getPuzzleInfo().getForm());
		solver.setData(model.getPuzzleInfo().getData());
		solver.setSolutionLimit(solutionLimit);
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
	}

}
