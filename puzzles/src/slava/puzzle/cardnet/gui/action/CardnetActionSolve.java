package slava.puzzle.cardnet.gui.action;

import slava.puzzle.cardnet.analysis.*;
import slava.puzzle.cardnet.model.CardnetModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class CardnetActionSolve extends PuzzleActionSolve {

	protected void execute() {
		CardnetModel model = (CardnetModel)manager.getModel();
		model.setSettingPuzzleModeOn(false);
		model.setSettingGeneratorInfoModeOn(false);
		CardnetAnalyzer analyzer = new CardnetAnalyzer();
		analyzer.setSolutionLimit(100);
		analyzer.setRandomizing(true);
		analyzer.setConsideringLineInfo(true);
		model.getPuzzleInfo().clearSolution();
		analyzer.setPuzzleInfo(model.getPuzzleInfo());
		analyzer.solve();
		int[] values = analyzer.getSolution();
		if(values == null) {
			model.setSolutionInfo("No solution.");
			return;
		} 
		model.getPuzzleInfo().setSolution(values);
		model.setSolutionInfo("Solution count=" + analyzer.getSolutionCount());
	}

}
