package slava.puzzle.football.gui.action;

import slava.puzzle.football.analysis.FootbalPuzzleGenerator;
import slava.puzzle.football.model.FootballModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class FootballActionGenerate extends PuzzleActionSolve {
	FootbalPuzzleGenerator generator = new FootbalPuzzleGenerator();

	public FootballActionGenerate() {		
	}

	protected void execute() {
		FootballModel model = (FootballModel)manager.getModel();
		generator.setPathLength(model.getGeneratedPathLength());
		generator.setField(model.getField());
		generator.generate();
		generator.fill(model.getField());
///		model.setSolutionInfo("Attemts count=" + generator.getAttemptCount());
		model.setState(generator.getState());
	}
	
}
