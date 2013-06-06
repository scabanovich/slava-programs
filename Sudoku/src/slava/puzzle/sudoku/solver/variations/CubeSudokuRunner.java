package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class CubeSudokuRunner extends AbstractSudokuRunner {

	protected AbstractSudokuField createSudokeField() {
		return new CubeSudokuField();
	}

	public static void main(String[] args) {
		new CubeSudokuRunner().run(args);
	}

}
/**
Problem
 + + 4 8
 + + 5 +
 + + 6 +
 + + + +
 + + + 7 + + + +
 + 8 + + + 3 + +
 + 5 + + 8 + + +
 + 2 + + + + + 6
Solved logically in 16 steps:  4 1 1 3 2 1 1 1 1 1 2 2 7 5 4 1

*/