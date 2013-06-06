package slava.puzzles.seabattlen.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattlen.analysis.SeaBattleNumberedGenerator;
import slava.puzzles.seabattlen.model.SeaBattleNumberedModel;

public class SeaBattleNumberedActionGenerate extends PuzzleActionSolve {
	SeaBattleNumberedGenerator generator;
	
	public SeaBattleNumberedActionGenerate() {}
	
	protected void execute() {
		generator = new SeaBattleNumberedGenerator();
		SeaBattleNumberedModel model = (SeaBattleNumberedModel)manager.getModel();
		generator.setField(model.getField());
		generator.setPuzzle(model.getPuzzleInfo());
		generator.generate();
	}
	
}
