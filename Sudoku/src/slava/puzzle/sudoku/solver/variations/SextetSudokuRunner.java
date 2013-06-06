package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class SextetSudokuRunner extends AbstractSudokuRunner {
	static int[] PROBLEM = {
	    1,2,3,  0,0,0,
	    4,5,6,  0,0,0,
    0,0,0,          0,0,0,
    0,0,0,          0,0,0,
        0,0,0,  0,0,0,
        0,0,0,  0,0,0,
};


	protected AbstractSudokuField createSudokeField() {
		return new SextetSudokuField();
	}

	public static void main(String[] args) {
		SextetSudokuRunner runner = new SextetSudokuRunner();
//			runner.setCurrenProblem(PROBLEM);
//			runner.solve();
		runner.run(args);
	}

}
