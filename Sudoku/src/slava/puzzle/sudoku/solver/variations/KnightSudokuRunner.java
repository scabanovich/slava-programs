package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class KnightSudokuRunner extends AbstractSudokuRunner {
	int[][] data;
	
	public KnightSudokuRunner(int[][] data) {
		this.data = data;
	}

	protected AbstractSudokuField createSudokeField() {
		return new KnightSudokuField(data);
	}
	
	/**
	 * used to generate knight-sudoku without pairs of equal digits.
	 */
	public static int[][] EMPTY_DATA = new int[81][8];
	
	/**
	 * used to generate knight-sudoku with random pairs.
	 */
	static int[][] NULL_DATA = null; // use

	public static void main(String[] args) {
		KnightSudokuRunner runner = new KnightSudokuRunner(EMPTY_DATA);
		runner.run(args);
	}

}
