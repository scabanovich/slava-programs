package slava.puzzles.sudokuxy.model;

import slava.puzzle.template.model.PuzzleLoader;

public class SudokuXYLoader extends PuzzleLoader {

	public SudokuXYLoader() {
		setRoot("/data/sudokuxy");
		initName("sudoku_");
	}

}
