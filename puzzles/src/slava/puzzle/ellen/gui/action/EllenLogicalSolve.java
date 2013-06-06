package slava.puzzle.ellen.gui.action;

import slava.puzzle.ellen.analysis.EllenLogicalAnalyzer;
import slava.puzzle.ellen.model.EllenFigures;
import slava.puzzle.ellen.model.EllenModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class EllenLogicalSolve extends PuzzleActionSolve {
	EllenLogicalAnalyzer analyzer = new EllenLogicalAnalyzer();
	
	public EllenLogicalSolve() {
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
