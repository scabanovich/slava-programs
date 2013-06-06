package slava.puzzle.sudoku.solver;

import slava.puzzle.sudoku.solver.variations.AbstractSudokuRunner;

public class TriangularSudokuRunner extends AbstractSudokuRunner {
	static int[] PROBLEM = {
		      4,      0,
		  0,0,0,0,6,0,2,0,5,
		  0,1,0,0,0,9,0,0,0,
		2,0,0,0,0,  8,0,0,0,	0,
	0,    5,0,0,0,  0,1,0,0,6,
		  8,0,0,0,0,0,0,0,0,
		  0,7,0,0,9,0,4,0,0,
	          0,      0,
	};

	static int[] PROBLEM_1 = {
	          0,      0,
	      0,3,4,5,1,0,0,0,0,
	  	  0,0,0,0,1,0,0,0,0,
	    0,0,0,0,0,  0,0,0,0,	0,
	0,    0,0,0,0,  0,0,0,0,0,
	      0,0,0,0,2,0,0,0,0,
	      0,0,0,0,2,0,0,0,0,
              0,      0,
};

	public void solve() {
		setCurrenProblem(PROBLEM);
		super.solve();		
	}

	protected AbstractSudokuField createSudokeField() {
		return new TriangularSudokuField();
	}
	
	protected void setUpGenerator(AbstractSudokuField field, SudokuGenerator g) {
		TriangularSudokuField f = (TriangularSudokuField)field;
		g.setForceRemoveNumbersFrom(f.fakeIndices);
	}

	public static void main(String[] args) {
		new TriangularSudokuRunner().run(args);
	}

}
