package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class ParketSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new ParketSudokuField();
	}

	public static void main(String[] args) {
		new ParketSudokuRunner().run(args);
	}

}
