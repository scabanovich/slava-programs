package slava.puzzles.sudokuxy.model;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.solver.ISudokuField;
import slava.puzzle.sudoku.solver.SudokuField;
import slava.puzzle.sudoku.solver.SudokuSolver;

public class SudokuHVGenerator {
	SudokuSolver solver = new SudokuSolver();
	SudokuDesignField f = new SudokuDesignField();
	int[] state;
	int[] mask;
	int[][] hRows;
	int[][] vRows;
	
	public SudokuHVGenerator() {
		f.setSize(9);
		SudokuField sf = new SudokuField();
		sf.setWidth(f.getWidth(), false);
		setField(sf);
	}
	
	public void setField(ISudokuField field) {
		solver.setField(field);
		solver.setRandomizing(true);
		solver.setSolutionLimit(1);
	}
	
	public void generate() {
		solver.setTreeCountLimit(10000);
		while(state == null) {
			solver.solve();
			state = solver.getSolution();
		}
		while(mask == null) {
			solver.solve();
			mask = solver.getSolution();
		}
		createRows();
		print();
	}
	
	void createRows() {
		hRows = new int[9][4];
		vRows = new int[9][4];
		for (int iy = 0; iy < f.getHeight(); iy++) {
			int j = 0;
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(mask[p] > 0 && mask[p] < 5) {
					hRows[iy][j] = state[p];
					j++;
				}
			}
		}
		for (int ix = 0; ix < f.getWidth(); ix++) {
			int j = 0;
			for (int iy = 0; iy < f.getHeight(); iy++) {
				int p = f.getIndex(ix, iy);
				if(mask[p] > 4) {
					vRows[ix][j] = state[p];
					j++;
				}
			}
		}
	}
	
	void print() {
		for (int iy = 0; iy < f.getHeight(); iy++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				System.out.print(" " + (state[p] + 1));
			}
			System.out.print(" ");
			for (int i = 0; i < 4; i++) {
				System.out.print(" " + (hRows[iy][i] + 1));
			}
			
			System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < 4; i++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				System.out.print(" " + (vRows[ix][i] + 1));
			}
			System.out.println("");
		}
		System.out.println("");
		//show positions that did not go to left or right.
		for (int iy = 0; iy < f.getHeight(); iy++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				String s = mask[p] == 0 ? "o" : "+";
				System.out.print(" " + s);
			}
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		SudokuHVGenerator g = new SudokuHVGenerator();
		g.generate();
	}
}
