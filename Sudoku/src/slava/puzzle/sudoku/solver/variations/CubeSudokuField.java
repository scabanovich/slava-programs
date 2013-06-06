package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

/**
 *  a a a a
 *  a a a a
 *  b b b b
 *  b b b b
 *  c c d d e e f f
 *  c c d d e e f f
 *  c c d d e e f f
 *  c c d d e e f f
 *
 */
public class CubeSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 3, 7,11,15,23,31,39,47};

	static int[] spaces = {
		0,0,0,0,
		0,0,0,0,
		0,0,0,0,
		0,0,0,0,
		0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,
	};
	
	public CubeSudokuField() {
		init();
	}

	public int getColorCount() {
		return 8;
	}

	void init() {
		size = 48;
		regions = new int[][]{
			{ 1, 2, 3, 4, 5, 6, 7, 8},
			{ 9,10,11,12,13,14,15,16},
			{17,18,25,26,33,34,41,42},
			{19,20,27,28,35,36,43,44},
			{21,22,23,24,29,30,31,32},
			{37,38,39,40,45,46,47,48},
			
			{ 1, 5, 9,13,17,25,33,41},
			{ 2, 6,10,14,18,26,34,42},
			{ 3, 7,11,15,19,27,35,43},
			{ 4, 8,12,16,20,28,36,44},

			{ 1, 2, 3, 4,24,32,40,48},
			{ 5, 6, 7, 8,23,31,39,47},
			{ 9,10,11,12,22,30,38,46},
			{13,14,15,16,21,29,37,45},
			
			{17,18,19,20,21,22,23,24},
			{25,26,27,28,29,30,31,32},
			{33,34,35,36,37,38,39,40},
			{41,42,43,44,45,46,47,48}
		};
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < regions[i].length; j++) {
				regions[i][j]--;
			}
		}
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
					(solution[i] > 8) ? (char)(solution[i] + 88) :
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
		return false;
	}

}
