package slava.puzzle.doublepath.gui.action;

import slava.puzzle.doublepath.analysis.DoublePathAnalysis;
import slava.puzzle.doublepath.model.DoublePathModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class DoublePathActionSolve extends PuzzleActionSolve {
	DoublePathAnalysis analyzer = new DoublePathAnalysis();

	protected void execute() {
		DoublePathModel model = (DoublePathModel)manager.getModel();
///		analyzer.setRandomizing(true);
		analyzer.setPrintSolutionLimit(100);
		analyzer.setField(model.getField());
		analyzer.solve();
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
		model.setSolutions(analyzer.getSolutions());
	}

}
