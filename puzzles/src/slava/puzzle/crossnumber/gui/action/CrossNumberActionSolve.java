package slava.puzzle.crossnumber.gui.action;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.analysis.CrossNumberAnalyzer;
import slava.puzzle.template.action.*;

public class CrossNumberActionSolve extends PuzzleActionSolve {
	CrossNumberAnalyzer analyzer = new CrossNumberAnalyzer(); 
	
	protected void execute() {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		analyzer.setRandomizing(true);
		analyzer.setSolutionLimit(10000);
		analyzer.setField(model.getField());
		analyzer.solve();
		int[] values = analyzer.getSolution();
		for (int i = 0; i < model.getField().size(); i++) {
			int v = (values == null) ? 0 : values[i];
		    model.getField().setValue(i, v);
		}
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
		model.setDistribution(analyzer.getHDistribution(), analyzer.getVDistribution());
	}

}
