package match2005.rubic;

public class RubicRotation implements RubicConstants {
	static int[][] CORNER_STATE_TRANSFORM = new int[CUBE_STATE_COUNT][3];
	static int[][] EDGE_STATE_TRANSFORM = new int[CUBE_STATE_COUNT][3];
	static {
		for (int i = 0; i < CUBE_STATE_COUNT; i++) {
			for (int r = 0; r < 3; r++) {
				CORNER_STATE_TRANSFORM[i][r] = (i + r * 8) % CUBE_STATE_COUNT;
			}
		}
		for (int i = 0; i < CUBE_STATE_COUNT; i++) {
			for (int r = 0; r < 2; r++) {
				EDGE_STATE_TRANSFORM[i][r] = (i + r * 12) % CUBE_STATE_COUNT;
			}
		}
	}
	
	int[][][] cornerCycles;
	int[][][] edgeCycles;
	int axe;
	int range;
	
	public RubicRotation(int axe, int range, int[][][] cornerCycles, int[][][] edgeCycles) {
		this.cornerCycles = cornerCycles;
		this.edgeCycles = edgeCycles;
		this.axe = axe;
		this.range = range;
	}
	
	public void execute(RubicState state) {
		for (int i = 0; i < cornerCycles.length; i++) {
			applyCornerCycle(cornerCycles[i], state.corners);
		}
		for (int i = 0; i < edgeCycles.length; i++) {
			applyEdgeCycle(edgeCycles[i], state.edges);
		}
	}
	
	
	void applyCornerCycle(int[][] cornerCycle, int[] corners) {
		int m = corners[cornerCycle[cornerCycle.length - 1][0]];
		for (int i = cornerCycle.length - 1; i >= 1; i--) {
			int source = corners[cornerCycle[i - 1][0]];
			int r = cornerCycle[i - 1][1];
			corners[cornerCycle[i][0]] = CORNER_STATE_TRANSFORM[source][r];
		}
		int r = cornerCycle[cornerCycle.length - 1][1];
		corners[cornerCycle[0][0]] = CORNER_STATE_TRANSFORM[m][r];
	}

	void applyEdgeCycle(int[][] edgeCycle, int[] edges) {
		int m = edges[edgeCycle[edgeCycle.length - 1][0]];
		for (int i = edgeCycle.length - 1; i >= 1; i--) {
			int source = edges[edgeCycle[i - 1][0]];
			int r = edgeCycle[i - 1][1];
			edges[edgeCycle[i][0]] = EDGE_STATE_TRANSFORM[source][r];
		}
		int r = edgeCycle[edgeCycle.length - 1][1];
		edges[edgeCycle[0][0]] = EDGE_STATE_TRANSFORM[m][r];
	}

}
