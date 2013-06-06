package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class DoubleSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new DoubleSudokuField();
	}
	
	public static void main(String[] args) {
		DoubleSudokuRunner runner = new DoubleSudokuRunner();
//			runner.setCurrenProblem(PROBLEM);
//			runner.solve();
		runner.run(args);
	}

}
