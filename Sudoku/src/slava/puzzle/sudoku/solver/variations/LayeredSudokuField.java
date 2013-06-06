package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;

public class LayeredSudokuField extends AbstractSudokuField {
	int layerCount = 6;
	int colorCount = 6;

	int[] lastLineIndex;

	int[] spaces = {};
	
	public LayeredSudokuField() {
		init();
	}

	void init() {
		size = colorCount * colorCount * layerCount;

		lastLineIndex = new int[colorCount * layerCount];
		for (int i = 0; i < lastLineIndex.length; i++) lastLineIndex[i] = (i + 1) * colorCount - 1;
		spaces = new int[size];
		
		int flatRegions = 3 * colorCount * layerCount;
		regions = new int[flatRegions + colorCount * colorCount][];
		for (int i = 0; i < colorCount; i++) {
			for (int j = 0; j < colorCount; j++) {
				int q = i * colorCount + j;
				int ri = flatRegions + q;
				regions[ri] = new int[layerCount];
				for (int k = 0; k < layerCount; k++) {
					regions[ri][k] = k * colorCount * colorCount + q;
				}
			}
		}
		for (int i = 0; i < colorCount; i++) {
			for (int k = 0; k < layerCount; k++) {
				int ri = 3 * colorCount * k + i;
				regions[ri] = new int[colorCount];
				int q = k * colorCount * colorCount + i * colorCount;
				for (int j = 0; j < colorCount; j++) {
					regions[ri][j] = q + j;
				}
			}
		}
		for (int j = 0; j < colorCount; j++) {
			for (int k = 0; k < layerCount; k++) {
				int ri = 3 * colorCount * k + colorCount + j;
				regions[ri] = new int[colorCount];
				int q = k * colorCount * colorCount + j;				
				for (int i = 0; i < colorCount; i++) {
					regions[ri][i] = q + i * colorCount;
				}
			}
		}
		int[] firstRegion = {};
		int[][] deltas = {};

		if(colorCount == 9) {
			firstRegion = new int[]{0,1,2,9,10,11,18,19,20};
			deltas = new int[][]{{0,0},{3,0},{6,0},{0,3},{3,3},{6,3},{0,6},{3,6},{6,6}};
		} else if(colorCount % 2 == 0) {
			firstRegion = new int[colorCount];
			for (int i = 0; i < colorCount / 2; i++) {
				firstRegion[i] = i;
				firstRegion[i + (colorCount / 2)] = i + colorCount;
			}
			deltas = new int[colorCount][2];
			for (int dj = 0; dj < colorCount / 2; dj++) {
				for (int di = 0; di < 2; di++) {
					int q = dj * 2 + di;
					deltas[q][0] = di * colorCount / 2;
					deltas[q][1] = dj * 2;
				}
			}
		}

		for (int n = 0; n < deltas.length; n++) {
			for (int k = 0; k < layerCount; k++) {
				int ri = 3 * colorCount * k + 2 * colorCount + n;
				regions[ri] = new int[colorCount];
				int q = k * colorCount * colorCount + deltas[n][1] * colorCount + deltas[n][0];
				for (int i = 0; i < firstRegion.length; i++) {
					regions[ri][i] = q + firstRegion[i];
				}
			}
		}

//		for (int i = 0; i < regions.length; i++) {
//			if(regions[i] == null) {
//				System.out.println("null");
//				continue;
//			}
//			for (int j = 0; j < regions[i].length; j++) {
//				String s = " " + regions[i][j];
//				while(s.length() < 3) s = " " + s;
//				System.out.print(s);
//			}
//			System.out.println("");
//		}
		
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
			if((i + 1) % (colorCount * colorCount) == 0) {
				sb.append("---\n");
			}
		}
		return sb.toString();
	}

	boolean isProblem(int[] s) {
		for (int i = 0; i < s.length; i++) {
			if(s[i] < 0) return true;
		}
		return false;
	}

	public int getColorCount() {
		return colorCount;
	}
	
}
