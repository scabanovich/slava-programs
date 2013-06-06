package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

/**
 *         a
 *         a             d
 *     a a a a a   c *   d            a a a j c * i d a a
 *         a b *   c d d d d d        a b * j c d d d d d
 *         a b c c c c c d e *        a b c c c c c d e *
 *       b b b b b c f * d e          b b b b c f * d e b
 *           b h * c f e e e e e      e b h * c f e e e e
 *           b h f f f f f e g *      * b h f f f f f e g
 *         h h h h h f i * e g        h h h h h f i * e g
 *             h j * f i g g g g g    g g h j * f i g g g
 *             h j i i i i i g        a * h j i i i i i g
 *           j j j j j i   * g        a j j j j j i d * g 
 *               j   * i
 *               j
 */

public class CrossesSudokuField extends AbstractSudokuField {
	static int[] lastLineIndex = { 8,17,26,35,44,53,62,71,80,89};

	static int[] spaces = {
		0,0,0,0,0,  1,0,0,0,
		0,0,  1,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,  1,0,0,
		0,0,0,  1,0,0,0,0,0,
		  1,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,  1,0,
		0,0,0,0,  1,0,0,0,0,
		0,  1,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,  1,
};

	int[] fakeIndices = {};

	public CrossesSudokuField() {
		init();
	}

	public int getColorCount() {
		return 9;
	}

	void init() {
		size = 90;
		regions = new int[][]{
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9},
			{10,11,12,13,14,15,16,17,18},
			{19,20,21,22,23,24,25,26,27},
			{28,29,30,31,32,33,34,35,36},
			{37,38,39,40,41,42,43,44,45},
			{46,47,48,49,50,51,52,53,54},
			{55,56,57,58,59,60,61,62,63},
			{64,65,66,67,68,69,70,71,72},
			{73,74,75,76,77,78,79,80,81},
			{82,83,84,85,86,87,88,89,90},

			{ 1,10,19,28,37,55,64,73,82},
			{ 2,11,20,29,38,46,56,65,83},
			{ 3,21,30,39,47,57,66,74,84},
			{ 4,12,22,31,48,58,67,75,85},
			{ 5,13,23,32,40,49,59,76,86},
			{14,24,33,41,50,60,68,77,87},
			{ 6,15,25,42,51,61,69,78,88},
			{ 7,16,26,34,43,52,70,79,89},
			{ 8,17,27,35,44,53,62,71,80},
			{ 9,18,36,45,54,63,72,81,90},
			
			{73,82, 1, 2, 3, 8, 9,10,19},
			{89, 7,14,15,16,17,18,26,34},
			{ 5,13,21,22,23,24,25,32,40},
			{11,20,36,28,29,30,31,38,46},
			{27,35,42,43,44,45,37,53,62},
			{33,41,48,49,50,51,52,60,68},
			{39,47,55,56,57,58,59,66,74},
			{54,63,70,71,72,64,65,81,90},
			{61,69,76,77,78,79,80,88, 6},
			{67,75,83,84,85,86,87, 4,12},

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
