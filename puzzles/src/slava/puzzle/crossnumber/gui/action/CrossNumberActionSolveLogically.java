package slava.puzzle.crossnumber.gui.action;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.analysis.logic.CrossNumberLogicSolver;
import slava.puzzle.template.action.*;

public class CrossNumberActionSolveLogically extends PuzzleActionSolve {
	CrossNumberLogicSolver solver = new CrossNumberLogicSolver(); 

	protected void execute() {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		solver.setField(model.getField());
		solver.solve();
		int[] values = solver.getSolution();
		for (int i = 0; i < model.getField().size(); i++) {
			int v = (values == null) ? 0 : values[i];
		    model.getField().setValue(i, v);
		}
		model.setSolutionInfo("" + solver.getResultInfo());
		model.setDistribution(null, null);
	}

}
