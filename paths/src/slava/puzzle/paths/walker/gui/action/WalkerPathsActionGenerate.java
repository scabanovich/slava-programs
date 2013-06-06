package slava.puzzle.paths.walker.gui.action;

import slava.puzzle.paths.walker.analysis.WalkerPathsGenerator;
import slava.puzzle.paths.walker.analysis.WalkerPathsMasuiGenerator;
import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class WalkerPathsActionGenerate extends PuzzleActionSolve {
	WalkerPathsGenerator generator;
	
	public WalkerPathsActionGenerate() {}
	
	protected void execute() {
		WalkerPathsModel model = (WalkerPathsModel)manager.getModel();
		generator = (model.getPreferences().isMasui())
			? new WalkerPathsMasuiGenerator()
			: new WalkerPathsGenerator();
		String error = null; //NookPathsPuzzleCheck.getGenerationError(model.getPuzzleInfo());
		if(error != null) {
			throw new RuntimeException(error);
		}
		generator.setTreeLimit(model.getPreferences().getTreeLimit());
		generator.setField(model.getField());
		generator.setPuzzle(model.getPuzzleInfo());
		generator.setPreferences(model.getPreferences());
		//set treeCount
		generator.generate();
	}
	
}
