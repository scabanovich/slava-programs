package slava.puzzle.pentasets.gui.action;

import java.util.*;
import slava.algebra.PaintGraphAnalysis;
import slava.puzzle.pentasets.analysis.PentaSetsGenerator;
import slava.puzzle.pentasets.model.PentaSetsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class PentaSetsActionSolve extends PuzzleActionSolve {
	PaintGraphAnalysis analyzer = new PaintGraphAnalysis();
	
	public PentaSetsActionSolve() {
	}

	protected void execute() {
		PentaSetsModel model = (PentaSetsModel)manager.getModel();
		analyzer.setGraph(PentaSetsGenerator.createGraph(model.getField()));
		analyzer.setInitialColoring((int[])model.getField().getLetters().clone());
		analyzer.setColorCount(5);
		try {
			analyzer.solve();
		} catch (Exception e) {
			model.setSolutionInfo(e.getMessage());
			model.getField().resetGroups();
			return;
		}
		model.setSolutionInfo("Solution count = " + analyzer.getSolutionCount());
		List list = new ArrayList();
		int[] solution = analyzer.getSolution();
		if(solution != null) list.add(solution);
		model.setSolutions(list);
	}

}
