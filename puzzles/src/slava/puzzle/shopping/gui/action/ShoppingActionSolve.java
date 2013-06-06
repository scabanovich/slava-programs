package slava.puzzle.shopping.gui.action;

import slava.puzzle.shopping.analysis.ShoppingAnalyzer;
import slava.puzzle.shopping.model.ShoppingModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class ShoppingActionSolve extends PuzzleActionSolve {
	ShoppingAnalyzer analyzer = new ShoppingAnalyzer(); 
	
	protected void execute() {
		ShoppingModel model = (ShoppingModel)manager.getModel();
		analyzer.setNodes(model.getField().getNodes());
		analyzer.solve();
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
	}

}
