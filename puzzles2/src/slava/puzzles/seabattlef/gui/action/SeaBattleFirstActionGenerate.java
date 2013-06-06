package slava.puzzles.seabattlef.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattlef.analysis.SeaBattleFirstGenerator;
import slava.puzzles.seabattlef.model.SeaBattleFirstModel;

public class SeaBattleFirstActionGenerate extends PuzzleActionSolve {
	SeaBattleFirstGenerator generator;
	
	public SeaBattleFirstActionGenerate() {}
	
	protected void execute() {
		generator = new SeaBattleFirstGenerator();
		SeaBattleFirstModel model = (SeaBattleFirstModel)manager.getModel();
		generator.setField(model.getField());
		generator.setPuzzle(model.getPuzzleInfo());
		generator.generate();
	}
	
}
