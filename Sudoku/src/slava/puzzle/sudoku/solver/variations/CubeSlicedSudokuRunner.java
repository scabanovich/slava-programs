package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class CubeSlicedSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new CubeSlicedSudokuField();
	}

	public static void main(String[] args) {
		new CubeSlicedSudokuRunner().run(args);
	}

}
/**

*/