package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class SextetSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 5,11,17,23,29,35};

	static int[] spaces = {
		    2,0,0,  1,0,0,
		    2,0,0,  1,0,0,
        0,0,0,          5,0,0,
        0,0,0,          5,0,0,
            2,0,0,  1,0,0,
            2,0,0,  1,0,0,
	};

	int[] fakeIndices = {};

	public SextetSudokuField() {
		init();
	}

	public int getColorCount() {
		return 6;
	}

	void init() {
		size = 36;
		regions = new int[][]{
			{ 0, 1, 2, 3, 4, 5},
			{ 6, 7, 8, 9,10,11},
			{12,13,14,15,16,17},
			{18,19,20,21,22,23},
			{24,25,26,27,28,29},
			{30,31,32,33,34,35},

			{ 0, 1, 2, 6, 7, 8},
			{ 3, 4, 5, 9,10,11},
			{12,13,14,18,19,20},
			{15,16,17,21,22,23},
			{24,25,26,30,31,32},
			{27,28,29,33,34,35},

			{12,18,19,24,30,31},
			{13,14,20,25,26,32},
			{ 0, 6, 7,27,33,34},
			{ 1, 2, 8,28,29,35},
			{ 3, 9,10,15,21,22},
			{ 4, 5,11,16,17,23},

			{ 1, 0, 6,13,12,18},
			{ 2, 8, 7,14,20,19},
			{ 4, 3, 9,25,24,30},
			{ 5,11,10,26,32,31},
			{16,15,21,28,27,33},
			{17,23,22,29,35,34},
		};
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
