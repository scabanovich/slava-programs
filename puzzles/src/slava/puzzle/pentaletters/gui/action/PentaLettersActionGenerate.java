package slava.puzzle.pentaletters.gui.action;

import slava.puzzle.pentaletters.analysis.*;
import slava.puzzle.pentaletters.model.*;
import slava.puzzle.template.action.PuzzleActionSolve;

public class PentaLettersActionGenerate extends PuzzleActionSolve {
	PentaLettersGenerator generator = new PentaLettersGenerator();

	public PentaLettersActionGenerate() {
		PentaFigures figures = new PentaFigures();
		figures.makeFigures();
		generator.setFigures(figures);
	}

	protected void execute() {
		PentaLettersModel model = (PentaLettersModel)manager.getModel();
		generator.setField(model.getField());
		generator.generate();
		PentaLettersField gfield = generator.getField();
		PentaLettersField field = model.getField();
		for (int i = 0; i < field.getSize(); i++) {
			field.setLetterAt(i, gfield.getLetterAt(i));
		}
		model.setSolutionInfo("Attemts count=" + generator.getAttemptCount());
		model.setSolutions(new java.util.ArrayList());
	}
	
}
