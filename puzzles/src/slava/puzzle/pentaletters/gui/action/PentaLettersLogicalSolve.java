package slava.puzzle.pentaletters.gui.action;

import slava.puzzle.pentaletters.analysis.PentaLettersLogicalAnalyzer;
import slava.puzzle.pentaletters.model.PentaFigures;
import slava.puzzle.pentaletters.model.PentaLettersModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class PentaLettersLogicalSolve extends PuzzleActionSolve {
	PentaLettersLogicalAnalyzer analyzer = new PentaLettersLogicalAnalyzer();
	
	public PentaLettersLogicalSolve() {
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
			e.printStackTrace();
			model.setSolutionInfo(e.getMessage());
			model.getField().resetGroups();
			return;
		}
		boolean isLogicallySolvable = analyzer.isLogicallySolvable();
		if(isLogicallySolvable) {
			model.setSolutionInfo("Problem has logycal solution");
		} else { 
			model.setSolutionInfo("Logical solution has not been found");
		}
		System.out.println(analyzer.getSolution());

		model.setSolutions(analyzer.getSolutions());
	}


}
