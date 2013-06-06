package slava.puzzle.ellen.gui.action;

import slava.puzzle.ellen.analysis.*;
import slava.puzzle.ellen.model.*;
import slava.puzzle.template.action.PuzzleActionSolve;

public class EllenActionSolve extends PuzzleActionSolve {
	EllenAnalyzer analyzer = new EllenAnalyzer();
	
	public EllenActionSolve() {
		EllenFigures figures = new EllenFigures();
		figures.makeFigures();
		analyzer.setFigures(figures.getFigures(), figures.getFigureIndices()); 
	}

	protected void execute() {
		EllenModel model = (EllenModel)manager.getModel();
		analyzer.setField(model.getField());
		try {
			analyzer.solve();
		} catch (Exception e) {
			model.setSolutionInfo(e.getMessage());
			model.getField().resetGroups();
			return;
		}
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
		model.setSolutions(analyzer.getSolutions());
	}

}
