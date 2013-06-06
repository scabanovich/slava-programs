package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class DavidsSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 0, 6,17,29,41,53,65,76,82,83};

	static int[] spaces = {
		          6,
		      4,0,0,0,0,    2,
		  2,  1,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,  1,0,  1,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,  1,0,
          2,0,  1,0,0,0,0,0,0,0,0,0,
        1,0,0,0,0,  1,0,  1,0,0,0,0,
        1,0,0,0,0,0,0,0,0,0,  1,
            3,    2,0,0,0,0,
                      8
	};

	int[] fakeIndices = {};

	public DavidsSudokuField() {
		init();
	}

	public int getColorCount() {
		return 12;
	}

	void init() {
		size = 84;
		regions = new int[][]{
			{ 2, 3, 4, 5, 6, 7},
			{ 8, 9,10,11,12,13,14,15,16,17,18},
			{19,20,21,22,23,24,25,26,27,28,29,30},
			{31,32,33,34,35,36,37,38,39,40,41,42},
			{43,44,45,46,47,48,49,50,51,52,53,54},
			{55,56,57,58,59,60,61,62,63,64,65,66},
			{67,68,69,70,71,72,73,74,75,76,77},
			{78,79,80,81,82,83},

			{ 1, 2, 3, 4, 5, 6, 9,10,11,12,13,24},
			{ 7,14,15,16,17,18,26,27,28,29,30,41},
			{ 8,19,20,21,22,23,31,32,33,34,35,43},
			{25,36,37,38,39,40,45,46,47,48,49,60},
			{42,50,51,52,53,54,62,63,64,65,66,77},
			{44,55,56,57,58,59,67,68,69,70,71,78},
			{61,72,73,74,75,76,79,80,81,82,83,84},

			{31,55,56,68,69,78},
			{19,20,32,33,43,44,57,58,70,71,79},
			{ 8,21,22,34,35,45,59,72,73,80,81,84},
			{ 9,23,36,37,46,47,60,61,74,75,82,83},
			{ 2, 3,10,11,24,25,38,39,48,49,62,76},
			{ 1, 4, 5,12,13,26,40,50,51,63,64,77},
			{ 6,14,15,27,28,41,42,52,53,65,66},
			{ 7,16,17,29,30,54},

			{ 2, 8,21,20,32,31},
			{ 1, 4, 3,10, 9,23,22,34,33,43,55},
			{ 6, 5,12,11,24,36,35,44,57,56,68,67},
			{14,13,25,38,37,46,45,59,58,70,69,78},
			{ 7,16,15,27,26,40,39,48,47,60,72,71},
			{18,17,29,28,41,50,49,61,74,73,80,79},
			{30,42,52,51,63,62,76,75,82,81,84},
			{54,53,65,64,77,83}
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
		for (int k = 0; k < fakeIndices.length; k++) if(fakeIndices[k] == i) return true;
		return false;
	}

}
