package slava.puzzle.ellen.gui.action;

import slava.puzzle.ellen.analysis.*;
import slava.puzzle.ellen.model.*;
import slava.puzzle.template.action.PuzzleActionSolve;

public class EllenActionGenerate extends PuzzleActionSolve {
	EllenGenerator generator = new EllenGenerator();

	public EllenActionGenerate() {
		EllenFigures figures = new EllenFigures();
		figures.makeFigures();
		generator.setFigures(figures);
	}

	protected void execute() {
		EllenModel model = (EllenModel)manager.getModel();
		generator.setField(model.getField());
		generator.generate();
		EllenField gfield = generator.getField();
		EllenField field = model.getField();
		for (int i = 0; i < field.getSize(); i++) {
			field.setLetterAt(i, gfield.getLetterAt(i));
		}
		model.setSolutionInfo("Attemts count=" + generator.getAttemptCount());
		model.setSolutions(new java.util.ArrayList());
	}
	
}
