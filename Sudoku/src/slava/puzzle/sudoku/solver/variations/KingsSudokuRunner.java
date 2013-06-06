package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class KingsSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new KingsSudokuField();
	}

	public static void main(String[] args) {
		new KingsSudokuRunner().run(args);
	}

}
