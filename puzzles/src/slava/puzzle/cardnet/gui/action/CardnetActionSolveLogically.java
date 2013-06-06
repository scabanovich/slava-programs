package slava.puzzle.cardnet.gui.action;

import slava.puzzle.cardnet.analysis.CardnetLogicalAnalyzer;
import slava.puzzle.cardnet.model.*;
import slava.puzzle.template.action.PuzzleActionSolve;

public class CardnetActionSolveLogically extends PuzzleActionSolve {

	protected void execute() {
		CardnetModel model = (CardnetModel)manager.getModel();
		model.setSettingPuzzleModeOn(false);
		model.setSettingGeneratorInfoModeOn(false);
		CardnetLogicalAnalyzer analyzer = new CardnetLogicalAnalyzer();
		model.getPuzzleInfo().clearSolution();
		analyzer.setPuzzleInfo(model.getPuzzleInfo());
		analyzer.solve();
		int[] values = analyzer.getSolution();
		if(analyzer.isUnsolvable()) {
			model.setSolutionInfo("No solution.");
		} else if(!analyzer.isSolutionFound()) {
			model.getPuzzleInfo().setSolution(values);
			model.setSolutionInfo("Cannot solve logically.");
		} else {
			model.getPuzzleInfo().setSolution(values);
			model.setSolutionInfo("Is logically solved (" + analyzer.getComplexity() + ").");
		}
	}

}
