package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class PillsSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new PillsSudokuField();
	}

	public static void main(String[] args) {
		new PillsSudokuRunner().run(args);
	}

}
