package slava.puzzle.sudoku.ui.action;

import slava.puzzle.sudoku.model.*;
import slava.puzzle.sudoku.solver.SudokuSolver;
import slava.puzzle.template.action.PuzzleActionSolve;

public class SudokuActionSolve extends PuzzleActionSolve {

	protected void execute() {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuRunField runField = new SudokuRunField(model);
		SudokuSolver solver = new SudokuSolver();
		solver.setField(runField);
		int[] ns = (int[])model.getProblemInfo().getNumbers().clone();
		for (int i = 0; i < ns.length; i++) ns[i]--;
		solver.setInitialValues(ns);
		int solutionLimit = SudokuPreferences.instance.getCountSolutionUpTo();
		if(solutionLimit >= 1) {
			solver.setSolutionLimit(solutionLimit);
			if(solutionLimit < 10) solver.setRandomizing(true);
		}
		solver.solve();
		int treeCount = solver.getTreeCount();
		int solutionCount = solver.getSolutionCount();
		int[] solution = solver.getSolution();
		if(solution != null) {
			solution = (int[])solution.clone();
			for (int i = 0; i < solution.length; i++) solution[i]++;
		}

		SudokuProblemInfo p = model.getProblemInfo();
		p.setSolution(solution);
		String relation = solutionCount >= solutionLimit && solutionLimit > 0 ? ">=" : "=";
		model.setSolutionInfo("Solution count " + relation + " " + solutionCount + " Tree count=" + treeCount);

	}

}
