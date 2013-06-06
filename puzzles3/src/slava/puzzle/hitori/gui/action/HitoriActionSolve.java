package slava.puzzle.hitori.gui.action;

import com.slava.common.RectangularField;

import slava.puzzle.hitori.analysis.HitoriLogicalSolver;
import slava.puzzle.hitori.model.HitoriModel;
import slava.puzzle.hitori.model.HitoriPreferences;
import slava.puzzle.template.action.PuzzleActionSolve;

public class HitoriActionSolve extends PuzzleActionSolve {

	public HitoriActionSolve() {}
	
	protected void execute() {
		HitoriModel model = (HitoriModel)manager.getModel();
		RectangularField field = model.getField();
		HitoriLogicalSolver solver = new HitoriLogicalSolver();
		solver.setField(field);
		solver.setNumbers(model.getProblemInfo().getNumbers());
		solver.setSuggestionsLimit(HitoriPreferences.instance.getSuggestionsLimit());
		solver.solve();
		int[] solution = solver.getSolution();
		String contradiction = solver.getContradiction();
		String status = "No logical solution";
		if(contradiction != null) {
			status = "Contradiction at " + contradiction;
		} else if(solver.isSolved()) {
			status = "Logical solution found";
			int sc = solver.getSuggestionCount();
			if(sc > 0) status += " with " + sc + " suggestion";
			if(sc > 1) status += "s";
		}
		model.getProblemInfo().setSolution(solution);
		model.setSolutionInfo(status);
	}
	
}
