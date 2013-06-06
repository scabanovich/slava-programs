package slava.puzzle.sudoku.ui.action;

import javax.swing.JOptionPane;

import slava.puzzle.sudoku.model.*;
import slava.puzzle.sudoku.solver.*;
import slava.puzzle.template.action.PuzzleActionSolve;

public class SudokuActionGenerate extends PuzzleActionSolve {

	public SudokuActionGenerate() {}
	
	protected void execute() {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuRunField runField = new SudokuRunField(model);
		SudokuGenerator g = new SudokuGenerator();
		g.setField(runField);
		int maxTreeCount = SudokuPreferences.instance.getGenerateTreeCountLimit();
		boolean useTemplate = model.getProblemInfo().isGenerateByTemplate();
		int[] template = model.getProblemInfo().getNumbers();
		if(useTemplate) {
			int q = checkUseTemplate(template);
			if(q == JOptionPane.NO_OPTION) useTemplate = false;
			else if(q == JOptionPane.CANCEL_OPTION) return;
		}
		//TODO we can set options (int stepsLimit, int narrowest) when maxTreeCount = 1.
		int[] numbers = (!useTemplate)
			? g.generate(maxTreeCount, -1, -1)
			: g.processForm(model.getProblemInfo().getNumbers(), maxTreeCount)
			  //g.generate(model.getProblemInfo().getNumbers(), maxTreeCount);
				;
		SudokuProblemInfo p = model.getProblemInfo();
		for (int i = 0; i < numbers.length; i++) p.setNumberAt(i, numbers[i] + 1);
		model.setSolutionInfo("Tree count=" + g.getTreeCount());
	}
	
	int checkUseTemplate(int[] numbers) {
		int k = 0;
		for (int i = 0; i < numbers.length; i++) if(numbers[i] > 0) ++k;
		if(k * 5 > numbers.length) return JOptionPane.OK_OPTION;
		return JOptionPane.showConfirmDialog(manager.getComponent(), "Template has too few points. Do you really want to use it?");
	}

}
