package slava.puzzle.domino.gui.action;

import slava.puzzle.domino.analysis.LineRestrictedDominoGenerator;
import slava.puzzle.domino.model.DominoModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class DominoActionGenerate extends PuzzleActionSolve {
	LineRestrictedDominoGenerator runner = new LineRestrictedDominoGenerator();

	protected void execute() {
		runner.setDebugging(false);
		DominoModel model = (DominoModel)manager.getModel();
		runner.setData(6, 10, 11, model.getFieldCopy());
		if(!runner.generateRandomStateWithUniqueSolution()) {
			throw new RuntimeException("Cannot generate problem.");
		} else {
			runner.restrict();
		}
		int[] values = runner.getSolution();
		if(values == null) return;
		model.setProblem(values, runner.getHRestrictions(), runner.getVRestrictions());			
	}

}
