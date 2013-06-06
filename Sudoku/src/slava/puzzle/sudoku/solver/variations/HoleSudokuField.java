package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class HoleSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 2, 8,17,26,35,44,53,62,71,77,80};

	static int[] spaces = {
		    2,0,0,
		    2,0,0,0,0,0,
		    2,0,0,0,0,0,0,0,0,
		  1,0,0,  1,0,0,0,0,0,
		  1,0,0,0,0,0,  1,0,0,
		  1,0,0,0,0,0,0,0,0,
		0,0,0,  1,0,0,0,0,0,
		0,0,0,0,0,0,  1,0,0,
		0,0,0,0,0,0,0,0,0,
		      3,0,0,0,0,0,
		            6,0,0,
};

	public HoleSudokuField() {
		init();
	}

	public int getColorCount() {
		return 9;
	}

	void init() {
		size = 81;
		regions = new int[][]{
			{ 0, 1, 2, 3, 4, 5, 9,10,11},
			{ 6, 7, 8,12,13,14,21,22,23},
			{15,16,17,24,25,26,33,34,35},
			{18,19,20,27,28,29,36,37,38},
			{30,31,32,39,40,41,48,49,50},
			{42,43,44,51,52,53,60,61,62},
			{45,46,47,54,55,56,63,64,65},			
			{57,58,59,66,67,68,72,73,74},
			{69,70,71,75,76,77,78,79,80},

			{ 3, 4, 5, 6, 7, 8},
			{ 9,10,11,12,13,14,15,16,17},
			{18,19,20,21,22,23,24,25,26},
			{27,28,29,30,31,32,33,34,35},
			{36,37,38,39,40,41,42,43,44},
			{45,46,47,48,49,50,51,52,53},
			{54,55,56,57,58,59,60,61,62},
			{63,64,65,66,67,68,69,70,71},
			{72,73,74,75,76,77},

			{18,27,36,46,55,64},
			{ 0, 3, 9,19,28,37,47,56,65},
			{ 1, 4,10,20,29,38,57,66,72},
			{ 2, 5,11,30,39,48,58,67,73},
			{ 6,12,21,31,40,49,59,68,74},
			{ 7,13,22,32,41,50,69,75,78},
			{ 8,14,23,42,51,60,70,76,79},
			{15,24,33,43,52,61,71,77,80},
			{16,25,34,44,53,62},
		};
		buildPlaceToRegions();
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		int line = 0;
		for (int i = 0; i < size; i++) {
			int q = spaces[i];
			for (int k = 0; k < q; k++) sb.append(" .");
			char c = (solution[i] < 0) ? '+' :
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

}
