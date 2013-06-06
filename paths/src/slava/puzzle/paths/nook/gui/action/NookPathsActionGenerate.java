package slava.puzzle.paths.nook.gui.action;

import slava.puzzle.paths.nook.analysis.NookPathsGenerator;
import slava.puzzle.paths.nook.analysis.NookPathsPuzzleCheck;
import slava.puzzle.paths.nook.model.NookPathsModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class NookPathsActionGenerate extends PuzzleActionSolve {
	NookPathsGenerator generator;
	
	public NookPathsActionGenerate() {}
	
	protected void execute() {
		generator = new NookPathsGenerator();
		NookPathsModel model = (NookPathsModel)manager.getModel();
		String error = NookPathsPuzzleCheck.getGenerationError(model.getPuzzleInfo());
		if(error != null) {
			throw new RuntimeException(error);
		}
		generator.setTreeLimit(model.getPreferences().getTreeLimit());
		generator.setField(model.getField());
		generator.setPuzzle(model.getPuzzleInfo());
		//set treeCount
		int[] gData = generator.generateSimple();
		int[] pData = model.getPuzzleInfo().getData();
		for (int i = 0; i < pData.length; i++) {
			pData[i] = gData[i] + 1;
		}
	}
	
}
