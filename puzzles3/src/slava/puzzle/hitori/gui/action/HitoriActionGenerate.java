package slava.puzzle.hitori.gui.action;

import com.slava.common.RectangularField;

import slava.puzzle.hitori.analysis.HitoriProblemGenerator;
import slava.puzzle.hitori.model.HitoriModel;
import slava.puzzle.template.action.PuzzleActionSolve;

public class HitoriActionGenerate extends PuzzleActionSolve {
	HitoriProblemGenerator g = new HitoriProblemGenerator();
	
	public HitoriActionGenerate() {
		
	}
	
	protected void execute() {
		HitoriModel model = (HitoriModel)manager.getModel();
		RectangularField field = model.getField();
		g.setField(field);
		//TODO
		int[] ns = g.generate();
		int[] sol = g.getSolution();
		int[] numbers = model.getProblemInfo().getNumbers();
		for (int i = 0; i < numbers.length; i++) numbers[i] = ns[i];
		model.getProblemInfo().setSolution(sol);
		int attemptCount = g.getAttemptCount();
		model.setSolutionInfo("Generated in " + attemptCount + " attempts");
		
	}

}
