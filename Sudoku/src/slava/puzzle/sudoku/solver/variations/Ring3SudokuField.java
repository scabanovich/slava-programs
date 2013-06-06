package slava.puzzle.sudoku.solver.variations;

import java.util.*;
import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class Ring3SudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = {}; //generate

	static int[] spaces = {
		//generate
	};

	int[] fakeIndices = {};

	public Ring3SudokuField() {
		init();
	}

	public int getColorCount() {
		return 9;
	}

	void init() {
		size = 3 * 81;
		lastLineIndex = new int[9];
		for (int iy = 0; iy < 9; iy++) lastLineIndex[iy] = (iy + 1) * 27 - 1;
		spaces = new int[size];
		
		ArrayList list = new ArrayList();
		//vertical
		for (int i = 0; i < 27; i++) {
			int[] r = new int[9];
			for (int j = 0; j < 9; j++) r[j] = (i + j * 27);
			list.add(r);
		}
		//squares
		for (int ix = 0; ix < 9; ix++) {
			for (int iy = 0; iy < 3; iy++) {
				int[] r = new int[9];
				for (int jx = 0; jx < 3; jx++) {
					for (int jy = 0; jy < 3; jy++) {
						int j = jy * 3 + jx;
						r[j] = iy * 81 + jy * 27 + ix * 3 + jx;
					}
				}
				list.add(r);
			}			
		}
		//horizontal
		for (int ix = 0; ix < 9; ix++) {
			for (int iy = 0; iy < 9; iy++) {
				int[] r = new int[9];
				for (int j = 0; j < 9; j++) {
					int i = (ix * 3 + j) % 27;
					r[j] = (i + iy * 27);
				}
				list.add(r);
			}			
		}
		regions = (int[][])list.toArray(new int[0][]);
		buildPlaceToRegions();
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		int line = 0;
		for (int i = 0; i < size; i++) {
			int q = spaces[i];
			for (int k = 0; k < q; k++) sb.append(" .");
			char c = (isFake(i)) ? '*' :
				(solution[i] < 0) ? '+' :
					(solution[i] == 9) ? 'a' :
			         ("" + (solution[i] + 1)).charAt(0);
			sb.append(" " + c);
			if(i == lastLineIndex[line]) {
				sb.append("\n");
				line++;
			}
		}
		return sb.toString();
	}

	boolean isFake(int i) {
		for (int k = 0; k < fakeIndices.length; k++) if(fakeIndices[k] == i) return true;
		return false;
	}

}
