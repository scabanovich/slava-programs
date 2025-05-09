package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;
import slava.puzzle.sudoku.solver.SudokuField;

public class Sudoku10ColorsRunner extends AbstractSudokuRunner {

	@Override
	protected AbstractSudokuField createSudokeField() {
		SudokuField f = new SudokuField() {
			@Override
			public int getColorCount() {
				return 10;
			}
		};
		f.setWidth(9, false);
		return f;
	}

	public static void main(String[] args) {
		Sudoku10ColorsRunner runner = new Sudoku10ColorsRunner();
		runner.run(args);
	}

}
