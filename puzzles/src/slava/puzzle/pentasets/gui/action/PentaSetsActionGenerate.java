package slava.puzzle.pentasets.gui.action;

import slava.puzzle.pentaletters.model.*;
import slava.puzzle.pentasets.analysis.PentaSetsGenerator;
import slava.puzzle.pentasets.model.PentaSetsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class PentaSetsActionGenerate extends PuzzleActionSolve {
	PentaSetsGenerator generator = new PentaSetsGenerator();

	public PentaSetsActionGenerate() {
		PentaFigures figures = new PentaFigures(){
			protected void initForms() {
				figureSize = 5;
				forms = new int[][][]{
					  {{1,1},{2,1},{3,1},{4,1},{5,1}}, //I
					  {{1,1},{2,1},{3,1},{4,1},{1,2}},
					  {{1,1},{2,1},{3,1},{1,2},{2,2}},
					  {{1,1},{2,1},{3,1},{4,1},{3,2}},
///					  {{1,1},{2,1},{3,1},{1,2},{3,2}}, //U
					  {{1,1},{2,1},{3,1},{2,2},{2,3}},
					  {{2,1},{1,2},{2,2},{3,2},{2,3}},
					  {{1,1},{2,1},{3,1},{3,2},{4,2}},
///					  {{1,1},{2,1},{3,1},{1,2},{1,3}}, //V
					  {{2,1},{1,2},{2,2},{3,2},{3,3}},
					  {{3,1},{1,2},{2,2},{3,2},{1,3}},
					  {{1,1},{2,1},{2,2},{3,2},{3,3}}
				};
			}

		};
		figures.makeFigures();
		generator.setFigures(figures);
	}

	protected void execute() {
		PentaSetsModel model = (PentaSetsModel)manager.getModel();
		generator.setField(model.getField());
		generator.generate();
		PentaLettersField gfield = generator.getField();
		
		PentaLettersField field = model.getField();
		for (int i = 0; i < field.getSize(); i++) {
			field.setLetterAt(i, gfield.getLetterAt(i));
			field.setGroupAt(i, gfield.getGroupAt(i));
		}
		model.setConditions(field.getLetters());
		model.setSolutionInfo("Attemts count=" + generator.getAttemptCount());
		model.setSolutions(new java.util.ArrayList());
	}
	
}
