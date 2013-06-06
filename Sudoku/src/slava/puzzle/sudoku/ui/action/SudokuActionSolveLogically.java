package slava.puzzle.sudoku.ui.action;

import slava.puzzle.sudoku.model.*;
import slava.puzzle.sudoku.solver.SudokuLogicalSolver;
import slava.puzzle.template.action.PuzzleActionSolve;

public class SudokuActionSolveLogically extends PuzzleActionSolve {

	protected void execute() {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuRunField runField = new SudokuRunField(model);
		SudokuLogicalSolver solver = new SudokuLogicalSolver();
		solver.setField(runField);
		int[] ns = (int[])model.getProblemInfo().getNumbers().clone();
		for (int i = 0; i < ns.length; i++) ns[i]--;
		solver.setInitialValues(ns);
		solver.solve();

		int[] stepVolumes = solver.getStepVolumes();
		int[] solution = solver.getSolution();
		if(solution != null) {
			solution = (int[])solution.clone();
			for (int i = 0; i < solution.length; i++) solution[i]++;
		}

		SudokuProblemInfo p = model.getProblemInfo();
		p.setSolution(solution);
		String info = (solution == null) ? "No solution" : "Solved logically in";
		String steps = " " + stepVolumes.length + " steps=";
		for (int i = 0; i < stepVolumes.length; i++) {
			steps += " " + stepVolumes[i];
		}
		model.setSolutionInfo(info + steps);

	}

}
