package slava.puzzle.cardnet.gui.action;

import slava.puzzle.cardnet.analysis.CardnetGenerator;
import slava.puzzle.cardnet.model.CardnetModel;
import slava.puzzle.template.action.*;

public class CardnetActionGenerate extends PuzzleActionSolve {

	protected void execute() {
		CardnetGenerator g = new CardnetGenerator();
		CardnetModel model = (CardnetModel)manager.getModel();
		model.setSettingPuzzleModeOn(false);
		model.setSolutionInfo(null);
		model.getPuzzleInfo().clearProblem();
		g.setPuzzleInfo(model.getPuzzleInfo());
		g.setGeneratorInfo(model.getGeneratorInfo());
		g.execute();
	}

}
