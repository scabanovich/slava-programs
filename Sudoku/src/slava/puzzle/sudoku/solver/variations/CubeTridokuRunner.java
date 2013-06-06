package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class CubeTridokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new CubeTridokuField();
	}

	public static void main(String[] args) {
		new CubeTridokuRunner().run(args);
	}

}
/**

*/