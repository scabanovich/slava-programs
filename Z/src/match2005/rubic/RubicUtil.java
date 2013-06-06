package match2005.rubic;

public class RubicUtil implements RubicConstants {
	public static int[] DEFAULT_SQUARE_COLORS = new int[SQUARE_COUNT];
	
	/**
	 * These four arrays map cube state to its index and orientation
	 */
	public static int[] CORNER_INDEX = new int[CUBE_STATE_COUNT]; 
	public static int[] CORNER_ORIENTATION = new int[CUBE_STATE_COUNT]; 
	public static int[] EDGE_INDEX = new int[CUBE_STATE_COUNT]; 
	public static int[] EDGE_ORIENTATION = new int[CUBE_STATE_COUNT]; 
	
	static {
		for (int i = 0; i < SQUARE_COUNT; i++) {
			DEFAULT_SQUARE_COLORS[i] = (i / 8);
		}
		for (int s = 0; s < CUBE_STATE_COUNT; s++) {
			CORNER_INDEX[s] = s % 8;
			CORNER_ORIENTATION[s] = s / 8;
			EDGE_INDEX[s] = s % 12;
			EDGE_ORIENTATION[s] = s / 12;
		}
	}
	
	public static int getCornerSquareIndex(int state, int k) {
		int index = RubicUtil.CORNER_INDEX[state];
		int orientation = RubicUtil.CORNER_ORIENTATION[state];
		return CORNERS[index][CORNER_ROTATION[orientation][k]];
	}

	public static int getEdgeSquareIndex(int state, int k) {
		int index = RubicUtil.EDGE_INDEX[state];
		int orientation = RubicUtil.EDGE_ORIENTATION[state];
		return EDGES[index][EDGES_ROTATION[orientation][k]];
	}
	
	static int[][] NEIGHBOURS; // [s1][s2] {0,1}
	
	private static int[][] FACE_PAIRS = {
		{0,1},{1,2},{1,4},{2,4},{4,7},{4,6},{7,6},{6,5},{6,3},{5,3},{3,0},{3,1}
	};
	private static int[][] EDGE_PAIRS = {
		{1,37},{1,39},{3,40},{3,42},{4,16},{4,18},{6,8},{6,10},
		{9,5},{9,7},{11,42},{11,47},{12,16},{12,21},{14,24},{14,26},
		{17,2},{17,7},{19,10},{19,15},{20,34},{20,39},{22,26},{22,31},
		{25,13},{25,15},{27,45},{27,47},{28,21},{28,23},{30,32},{30,34},
		{33,29},{33,31},{35,40},{35,45},{36,18},{36,23},{38,0},{38,2},
		{41,0},{41,5},{43,32},{43,37},{44,8},{44,13},{46,24},{46,29}
	};
	static {
		int[][] q = new int[SQUARE_COUNT][SQUARE_COUNT];
		for (int i = 0; i < FACE_COUNT; i++) {
			for (int j = 0; j < FACE_PAIRS.length; j++) {
				int i1 = i * 8 + FACE_PAIRS[j][0];
				int i2 = i * 8 + FACE_PAIRS[j][1];
				q[i1][i2] = 1;
				q[i2][i1] = 1;
			}
		}
		for (int i = 0; i < EDGE_PAIRS.length; i++) {
			q[EDGE_PAIRS[i][0]][EDGE_PAIRS[i][1]] = 1;
			q[EDGE_PAIRS[i][1]][EDGE_PAIRS[i][0]] = 1;
		}
		NEIGHBOURS = new int[SQUARE_COUNT][];
		for (int s = 0; s < SQUARE_COUNT; s++) {
			int n = 0;
			for (int i = 0; i < SQUARE_COUNT; i++) if(q[s][i] == 1) ++n;
			NEIGHBOURS[s] = new int[n];
			n = 0;
			for (int i = 0; i < SQUARE_COUNT; i++) if(q[s][i] == 1) {
				NEIGHBOURS[s][n] = i;
				++n;
			}
		}
//		for (int s = 0; s < SQUARE_COUNT; s++) {
//			for (int j = 0; j < NEIGHBOURS[s].length; j++) {
//				System.out.print(" " + NEIGHBOURS[s][j]);
//			}
//			System.out.println("");
//		}
	}

	public static boolean isTotalCornerOrientationZero(int[] state) {
		int q = 0;
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			q += RubicUtil.CORNER_ORIENTATION[state[i]];
		}
		return q % 3 == 0;
	}
	
	public static boolean isTotalEdgeOrientationZero(int[] state) {
		int q = 0;
		for (int i = CORNER_CUBE_COUNT; i < CUBE_COUNT; i++) {
			q += EDGE_ORIENTATION[state[i]];
		}
		return q % 2 == 0;
	}

	public static boolean isEvenCubePermutation(int[] state) {
		return isEvenCornerPermutation(state) == isEvenEdgePermutation(state);
	}

	public static boolean isEvenCornerPermutation(int[] state) {
		int[] st = new int[CORNER_CUBE_COUNT];
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			st[i] = CORNER_INDEX[state[i]];
		}
		return isPermutationEven(st);
	}
	
	public static boolean isEvenEdgePermutation(int[] state) {
		int[] st = new int[EDGE_CUBE_COUNT];
		for (int i = CORNER_CUBE_COUNT; i < CUBE_COUNT; i++) {
			st[i - CORNER_CUBE_COUNT] = EDGE_INDEX[state[i]];
		}
		return isPermutationEven(st);
	}
	
	static boolean isPermutationEven(int[] state) {
		boolean q = true;
		for (int i = 1; i < state.length; i++) {
			int j = i;
			while(j > 0 && state[j] < state[j - 1]) {
				q = !q;
				int c = state[j];
				state[j] = state[j - 1];
				state[j - 1] = c;
				--j;
			}
		}
		return q;
	}
	
}
