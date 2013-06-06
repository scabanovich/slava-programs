package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class SquaresSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new SquaresSudokuField();
	}

	public static void main(String[] args) {
		SquaresSudokuRunner runner = new SquaresSudokuRunner();
		runner.run(args);
	}

}
