package match2005.rubic;

public class RubicState implements RubicConstants {
	int[] corners = new int[CORNER_CUBE_COUNT];
	int[] edges = new int[EDGE_CUBE_COUNT];
	
	public RubicState() {}
	
	public RubicState copy() {
		RubicState copy = new RubicState();
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) copy.corners[i] = corners[i];
		for (int i = 0; i < EDGE_CUBE_COUNT; i++) copy.edges[i] = edges[i];
		return copy;
	}
	
	public int[] getSquares() {
		int[] squares = new int[SQUARE_COUNT];
		for (int i = 0; i < corners.length; i++) {
			for (int k = 0; k < 3; k++) {
				int f1 = RubicUtil.getCornerSquareIndex(i, k);
				int f2 = RubicUtil.getCornerSquareIndex(corners[i], k);
				squares[f1] = f2;
			}			
		}
		for (int i = 0; i < edges.length; i++) {
			for (int k = 0; k < 2; k++) {
				int f1 = RubicUtil.getEdgeSquareIndex(i, k);
				int f2 = RubicUtil.getEdgeSquareIndex(edges[i], k);
				squares[f1] = f2;
			}			
		}
		return squares;
	}
	
	public boolean equals(RubicState state) {
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			if(state.corners[i] != corners[i]) return false;
		}
		for (int i = 0; i < EDGE_CUBE_COUNT; i++) {
			if(state.edges[i] != edges[i]) return false;
		}
		return true;
	}
	
	public int code(int module) {
		int q = 0;
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			q = (24 * q + corners[i]) % module;
		}
		for (int i = 0; i < EDGE_CUBE_COUNT; i++) {
			q = (24 * q + edges[i]) % module;
		}
		return q;
	}
	
	public int partialCode() {
		return ((corners[0] * 24 + edges[0]) * 24 + edges[3]);// * 24 + edges[8];
	}
	
	public int rotationCode() {
		int q = 0;
		for (int i = 0; i < CORNER_CUBE_COUNT - 1; i++) {
			q = 3 * q + RubicUtil.CORNER_ORIENTATION[corners[i]];
		}
		for (int i = 0; i < EDGE_CUBE_COUNT - 1; i++) {
			q = 2 * q + RubicUtil.EDGE_ORIENTATION[edges[i]];
		}
		return q;
	}
	
	static int[] FACTORIALS = new int[13];
	
	static {
		FACTORIALS[0] = 1;
		for (int i = 1; i < FACTORIALS.length; i++) FACTORIALS[i] = FACTORIALS [i - 1] * i;
	}
	
	public int cornerCode() {
		int q = 0;
		for (int i = 0; i < CORNER_CUBE_COUNT - 1; i++) {
			int v = RubicUtil.CORNER_INDEX[corners[i]];
			int m = v;
			for (int j = 0; j < i; j++) {
				if(RubicUtil.CORNER_INDEX[corners[i]] < v) --m;
			}
			q += m * FACTORIALS[CORNER_CUBE_COUNT - i - 1];
		}		
		for (int i = 0; i < CORNER_CUBE_COUNT - 1; i++) {
			int r = RubicUtil.CORNER_ORIENTATION[corners[i]];
			q = 3 * q + r;
		}		
		return q;
	}
	
	public int edgeCode(int module) {
		int q = 0;
		for (int i = 0; i < EDGE_CUBE_COUNT - 1; i++) {
			int v = RubicUtil.EDGE_INDEX[edges[i]];
			int m = v;
			for (int j = 0; j < i; j++) {
				if(RubicUtil.EDGE_INDEX[edges[i]] < v) --m;
			}
			q += m * FACTORIALS[EDGE_CUBE_COUNT - i - 1];
		}		
		return q % module;
	}
	
	static int[] distr = new int[6];
	public int coloringCode() {
		for (int j = 0; j < distr.length; j++) distr[j] = 0;
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			for (int k = 0; k < 3; k++) {
				int f1 = RubicUtil.getCornerSquareIndex(i, k);
				int f2 = RubicUtil.getCornerSquareIndex(corners[i], k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				int dc = (c1 > c2) ? c1 - c2 : c2 - c1;
				distr[dc]++;
			}
		}
		for (int i = 0; i < EDGE_CUBE_COUNT; i++) {
			for (int k = 0; k < 2; k++) {
				int f1 = RubicUtil.getEdgeSquareIndex(i, k);
				int f2 = RubicUtil.getEdgeSquareIndex(edges[i], k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				int dc = c1 + 6 - c2;
				if(dc >= 6) dc -= 6;
				distr[dc]++;
			}
		}
		int q = 0;
		for (int i = 0; i < 5; i++) {
			q = q * 20 + distr[i];
		}
		return q;
	}
	
	public void print() {
		System.out.println(getStates());
	}
	
	public String getStates() {
		StringBuffer sb = new StringBuffer();
		sb.append("Corners:");
		for (int i = 0; i < corners.length; i++) sb.append(" " + corners[i]);
		sb.append(" Edges:");
		for (int i = 0; i < edges.length; i++) sb.append(" " + edges[i]);
		return sb.toString();
	}
	
	public static RubicState getOrigin() {
		RubicState state = new RubicState();
		for (int i = 0; i < state.corners.length; i++) state.corners[i] = i;
		for (int i = 0; i < state.edges.length; i++) state.edges[i] = i;
		return state;
	}
	
	public boolean isEvenCornerPermutation() {
		int[] st = new int[CORNER_CUBE_COUNT];
		for (int i = 0; i < CORNER_CUBE_COUNT; i++) {
			st[i] = RubicUtil.CORNER_INDEX[corners[i]];
		}
		return RubicUtil.isPermutationEven(st);
	}

	public boolean isEvenEdgePermutation() {
		int[] st = new int[EDGE_CUBE_COUNT];
		for (int i = 0; i < EDGE_CUBE_COUNT; i++) {
			st[i] = RubicUtil.EDGE_INDEX[edges[i]];
		}
		return RubicUtil.isPermutationEven(st);
	}

}
