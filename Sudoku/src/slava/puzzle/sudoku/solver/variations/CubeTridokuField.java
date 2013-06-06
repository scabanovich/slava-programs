package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

/**
 * A: Cubik i tri v raznye storony
 * B: Iny-Yany
 */
public class CubeTridokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 2,5,8, 11,14,17, 20,23,26, 29,32,35, 38,41,44, 47,50,53, 56,59,62};
	
	public static int V_RAZNYE_STORONY = 1;
	public static int INY_YANY = 2;

	static int[] spaces = {
	};

	public CubeTridokuField() {
		init(V_RAZNYE_STORONY);
	}
	
	public CubeTridokuField(int kind) {
		init(kind);
	}

	public int getColorCount() {
		return 9;
	}

	void init(int kind) {
		size = 63;
		spaces = new int[size];
		if(kind == INY_YANY) {
			setRegionsB();
		} else {
			setRegionsA();
		}
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < regions[i].length; j++) {
				regions[i][j]--;
			}
		}
		buildPlaceToRegions();
	}

	//Cubik i tri v raznye storony
	void setRegionsA() {
		regions = new int[][]{
				{1, 2, 3, 4, 5, 6, 7, 8, 9},
				{10, 11, 12, 13, 14, 15, 16, 17, 18},
				{19, 20, 21, 22, 23, 24, 25, 26, 27},
				{28, 29, 30, 31,32, 33, 34, 35, 36},
				{37, 38, 39, 40, 41, 42, 43, 44, 45},
				{46, 47, 48, 49, 50, 51, 52, 53, 54},
				{55, 56, 57, 58, 59, 60, 61, 62, 63},

				{1, 4, 7, 37, 40, 43, 28, 31, 34},
				{2, 5, 8, 38, 41, 44, 29, 32, 35},
				{3, 6, 9, 39, 42, 45, 30, 33, 36},

				{10, 11, 12, 19, 20, 21, 28, 29, 30},
				{13, 14, 15, 22, 23, 24, 31, 32, 33},
				{16, 17, 18, 25, 26, 27, 34, 35, 36},

				{19, 22, 25, 37, 38, 39, 46, 47, 48},
				{20, 23, 26, 40, 41, 42, 49, 50, 51},
				{21, 24, 27, 43, 44, 45, 52, 53, 54}

		};
	}

	//Iny-Yany
	void setRegionsB() {
		regions = new int[][]{
				
				{1, 2, 3, 4, 5, 6, 7, 8, 9},
				{10, 11, 12, 13, 14, 15, 16, 17, 18},
				{19, 20, 21, 22, 23, 24, 25, 26, 27},
				{28, 29, 30, 31,32, 33, 34, 35, 36},
				{37, 38, 39, 40, 41, 42, 43, 44, 45},
				{46, 47, 48, 49, 50, 51, 52, 53, 54},
				{55, 56, 57, 58, 59, 60, 61, 62, 63},
				{1, 4, 7, 10, 13, 16,19, 22, 25},
				{2, 5, 8, 11, 14, 17, 20, 23, 26},
				{3, 6, 9, 12, 15, 18, 21, 24, 27},

				{10, 11, 12, 28, 29, 30, 46, 47, 48},
				{13, 14, 15, 31, 32, 33, 49, 50, 51},
				{16, 17, 18, 34, 35, 36, 52, 53, 54},

				{37, 40, 43, 46, 49, 52, 55, 58, 61},
				{38, 41, 44, 47, 50, 53, 56, 59, 62},
				{39, 42, 45, 48, 51, 54, 57, 60, 63},
	
				{19, 20, 21, 28, 31, 34, 37, 38, 39},
				{22, 23, 24, 29, 32, 35, 40, 41, 42},
				{25, 26, 27, 30, 33, 36, 43, 44, 45}				
				
				
				
		};
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
