package forsmarts.csudoku;

import com.slava.sudoku.SudokuField;

/**
 * Place given shapes into the grid, without overlapping, 
 * to construct a classic Sudoku puzzle with a unique solution, 
 * then solve it. Shapes can be rotated, but not reflected. 
 * Digits in shapes do not overlap digits given in grid.
 * 
 * @author glory
 *
 */
public class ConstructedSudokuRunner {

	
	public static void main(String[] args) {
		ConstructedSudokuProblem1 problem = new ConstructedSudokuProblem1(9);
		Piece[] pieces = problem.getPieces();
		
		SudokuField f = new SudokuField();
		f.setWidth(9, false);

		ConstructedSudokuSolver solver = new ConstructedSudokuSolver();
		solver.setField(f);
		solver.setPieces(pieces);
		solver.setInitialData(problem.getInitialData());
		
		solver.solve();		
		
	}

}
