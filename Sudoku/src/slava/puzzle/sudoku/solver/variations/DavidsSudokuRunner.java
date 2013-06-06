package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class DavidsSudokuRunner extends AbstractSudokuRunner {
	static int A = 10, B = 11, C = 12;
	static int[] PROBLEM = {
        	1,
        2,3,4,5,6,    0,
    0,  7,8,9,A,B,0,0,0,0,0,
0,0,0,0,0,  C,0,  0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,  0,0,
    0,0,  0,0,0,0,0,0,0,0,0,0,
  0,0,0,0,0,  0,0,  0,0,0,0,0,
  0,0,0,0,0,0,0,0,0,0,  0,
      0,    0,0,0,0,0,
                0


};

	protected AbstractSudokuField createSudokeField() {
		return new DavidsSudokuField();
	}
	
	/**
	 * No use to generate. There exists the only solution
	 * for one filled star-region. 
	 */

	public static void main(String[] args) {
		DavidsSudokuRunner runner = new DavidsSudokuRunner();
			runner.setCurrenProblem(PROBLEM);
			runner.solve();
		//runner.run(args);
	}

}
