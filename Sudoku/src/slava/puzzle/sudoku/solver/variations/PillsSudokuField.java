package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class PillsSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 7,15,23,31,39,47,55,63};

	static int[] spaces = {
		0,0,0,  1,  1,0,0,  1,
		  1,  1,0,0,  1,  1,0,0,
		0,0,0,  1,  1,0,0,  1,
		  1,  1,0,0,  1,  1,0,0,
		0,0,0,  1,  1,0,0,  1,
		  1,  1,0,0,  1,  1,0,0,
		0,0,0,  1,  1,0,0,  1,
		  1,  1,0,0,  1,  1,0,0,
};

	int[] fakeIndices = {};

	public PillsSudokuField() {
		init();
	}

	public int getColorCount() {
		return 8;
	}

	void init() {
		size = 64;
		regions = new int[][]{
			{ 0, 1, 2, 3, 4, 5, 6, 7},
			{ 8, 9,10,11,12,13,14,15},
			{16,17,18,19,20,21,22,23},
			{24,25,26,27,28,29,30,31},
			{32,33,34,35,36,37,38,39},
			{40,41,42,43,44,45,46,47},
			{48,49,50,51,52,53,54,55},
			{56,57,58,59,60,61,62,63},
			
			{ 0, 8,16,24,32,40,48,56},
			{ 1, 8,17,24,33,40,49,56},
			{ 2, 8,18,24,34,40,50,56},
			{ 3, 9,19,25,35,41,51,57},
			{ 3,10,19,26,35,42,51,58},
			{ 3,11,19,27,35,43,51,59},
			{ 4,12,20,28,36,44,52,60},
			{ 5,12,21,28,37,44,53,60},
			{ 6,12,22,28,38,44,54,60},
			{ 7,13,23,29,39,45,55,61},
			{ 7,14,23,30,39,46,55,62},
			{ 7,15,23,31,39,47,55,63},
			
			{ 0, 1, 2, 3, 8, 9,10,11},
			{ 4, 5, 6, 7,12,13,14,15},
			{16,17,18,19,24,25,26,27},
			{20,21,22,23,28,29,30,31},
			{32,33,34,35,40,41,42,43},
			{36,37,38,39,44,45,46,47},
			{48,49,50,51,56,57,58,59},
			{52,53,54,55,60,61,62,63}
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
