package slava.puzzle.pentaletters.gui.action;

import slava.puzzle.pentaletters.analysis.PentaLettersAnalyzer;
import slava.puzzle.pentaletters.model.PentaFigures;
import slava.puzzle.pentaletters.model.PentaLettersModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class PentaLettersActionSolve extends PuzzleActionSolve {
	PentaLettersAnalyzer analyzer = new PentaLettersAnalyzer();
	
	public PentaLettersActionSolve() {
		PentaFigures figures = new PentaFigures();
		figures.makeFigures();
		analyzer.setFigures(figures.getFigures(), figures.getFigureIndices()); 
	}

	protected void execute() {
		PentaLettersModel model = (PentaLettersModel)manager.getModel();
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
