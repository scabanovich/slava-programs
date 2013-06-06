package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class Ring3SudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new Ring3SudokuField();
	}

	public static void main(String[] args) {
		new Ring3SudokuRunner().run(args);
	}

}
