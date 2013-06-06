package slava.puzzle.cardsum.gui.action;

import slava.puzzle.cardsum.analysis.CardSumAnalyzer;
import slava.puzzle.cardsum.model.CardSumModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class CardSumActionSolve extends PuzzleActionSolve {
	CardSumAnalyzer analyzer = new CardSumAnalyzer(); 
	
	protected void execute() {
		CardSumModel model = (CardSumModel)manager.getModel();
		analyzer.setRandomizing(true);
		analyzer.setSolutionLimit(10000);
		analyzer.setField(model.getField());
		analyzer.solve();
		if(analyzer.getSolutionCount() > 0) {
			model.setSolution(analyzer.getSolution());
		}
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
		model.setDistribution(analyzer.getCardDistribution(), analyzer.getSumDistribution());
	}

}
