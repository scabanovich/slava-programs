package slava.puzzles.seabattle.gui.action;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.seabattle.analysis.SeaBattleGenerator;
import slava.puzzles.seabattle.model.SeaBattleModel;

public class SeaBattleActionGenerate extends PuzzleActionSolve {
	SeaBattleGenerator generator;
	
	public SeaBattleActionGenerate() {}
	
	protected void execute() {
		generator = new SeaBattleGenerator();
		SeaBattleModel model = (SeaBattleModel)manager.getModel();
		generator.setField(model.getField());
		generator.setPuzzle(model.getPuzzleInfo());
		generator.setMode(model.getPreferences().getGeneratorMode());
		generator.generate();
	}
	
}
