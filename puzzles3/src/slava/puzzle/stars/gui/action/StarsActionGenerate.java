package slava.puzzle.stars.gui.action;

import slava.puzzle.stars.analysis.StarRegionsGenerator;
import slava.puzzle.stars.model.StarsModel;
import slava.puzzle.stars.model.StarsSetsField;
import slava.puzzle.template.action.PuzzleActionSolve;

public class StarsActionGenerate extends PuzzleActionSolve {
	StarRegionsGenerator generator = new StarRegionsGenerator();

  public StarsActionGenerate() {
  }

  protected void execute() {
	  StarsModel model = (StarsModel)manager.getModel();
	  generator.setField(model.getField());
	  generator.generate();
	  int[] colors = generator.getColors();
	  StarsSetsField f = model.getField();
		
	  for (int i = 0; i < f.getSize(); i++) {
		  f.setSetAt(i, colors[i]);
	  }
	  model.setSolutions(generator.getSolutions());
	  model.setSolutionInfo(generator.getMessage());
  }

}

