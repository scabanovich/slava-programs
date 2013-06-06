package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class CrossesSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new CrossesSudokuField();
	}

	public static void main(String[] args) {
		new CrossesSudokuRunner().run(args);
	}

}
