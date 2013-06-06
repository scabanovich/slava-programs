package slava.puzzle.sudoku.solver;

public class TriangularSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 1,10,19,29,39,48,57,59};
//	static int[] spaces = {
//		    3,      3,
//		1,0,0,0,0,0,0,0,0,
//		1,0,0,0,0,0,0,0,0,
//	  0,0,0,0,0,  1,0,0,0,0,
//	  0,0,0,0,0,  1,0,0,0,0,
//		1,0,0,0,0,0,0,0,0,
//		1,0,0,0,0,0,0,0,0,
//	        3,      3,
//	};
	static int[] spaces = {
		4,      3,
	2,0,0,0,0,0,0,0,0,
	2,0,0,0,0,0,0,0,0,
  0,0,0,0,0,  3,0,0,0,0,
  0,0,0,0,0,  3,0,0,0,0,
	2,0,0,0,0,0,0,0,0,
	2,0,0,0,0,0,0,0,0,
        4,      3,
};
	int[] fakeIndices = { 1, 2,29,30,57,58};
	
	public TriangularSudokuField() {
		init();
	}
	
	void init() {
		size = 60;
		regions = new int[][]{
			{ 0, 3, 4, 5,11,12,13,14,15},
			{ 6, 7, 8, 9,10,16,17,18,25},
			{20,21,22,23,24,31,32,33,40},
			{19,26,27,28,35,36,37,38,39},
			{34,41,42,43,49,50,51,52,53},
			{44,45,46,47,48,54,55,56,59},
			
			{ 0, 4, 3,12,11,22,21,31,30},
			{ 6, 5,14,13,24,23,33,32,40},
			{ 8, 7,16,15,34,42,41,50,49},
			{10, 9,18,17,25,44,43,52,51},
			{19,27,26,36,35,46,45,54,53},
			{29,28,38,37,48,47,56,55,59},
			
			{ 1, 8, 9,18,19,27,28,38,39},
			{ 6, 7,16,17,25,26,36,37,48},
			{ 0, 4, 5,14,15,35,46,47,56},
			{ 3,12,13,24,44,45,54,55,59},
			{11,22,23,33,34,42,43,52,53},
			{20,21,31,32,40,41,50,51,58},
			
			{ 2, 3, 4, 5, 6, 7, 8, 9,10},
			{11,12,13,14,15,16,17,18,19},
			{20,21,22,23,24,25,26,27,28},
			{31,32,33,34,35,36,37,38,39},
			{40,41,42,43,44,45,46,47,48},
			{49,50,51,52,53,54,55,56,57},
		};
		buildPlaceToRegions();
	}

	public int getColorCount() {
		return 9;
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		int line = 0;
		for (int i = 0; i < size; i++) {
			int q = spaces[i];
			for (int k = 0; k < q; k++) sb.append(" .");
			char c = (isFake(i)) ? '.' : 
			         (solution[i] < 0) ? '+' : 
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
