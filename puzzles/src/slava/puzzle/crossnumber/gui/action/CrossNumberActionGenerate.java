package slava.puzzle.crossnumber.gui.action;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.analysis.logic.CrossNumberGenerator;
import slava.puzzle.crossnumber.undo.*;
import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzle.template.undo.UndoManager;

public class CrossNumberActionGenerate extends PuzzleActionSolve {
	CrossNumberGenerator g = new CrossNumberGenerator(); 

	protected void execute() {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		g.init(model.getField());
		
		for (int i = 0; i < model.getField().size(); i++) {
			model.getField().setValue(i, 0);
		}
		
		g.generate();
		if(!g.isChanged()) {
			model.setSolutionInfo("Cannot generate");
			model.setDistribution(null, null);
			return;			
		}
		model.setSolutionInfo("generated");
		model.setDistribution(null, null);
		GenerationChange c = new GenerationChange(model, g.getNewHSum(), g.getNewVSum());
		c.redo();
		UndoManager.getInstance().addChange(c);
	}

}
