package ic2005_3;

public class CubeStates {
	private int[][] states = {
		{0,1}, {0,4}, {0,3}, {0,5},
		{1,2}, {1,4}, {1,0}, {1,5}, 
		{2,3}, {2,4}, {2,1}, {2,5},
		{3,0}, {3,4}, {3,2}, {3,5},
		{4,0}, {4,1}, {4,2}, {4,3},
		{5,0}, {5,1}, {5,2}, {5,3}
	};
	private int[][] stateIndex;

	private int[] upperFace = {
		4,1,5,3, 4,1,5,3, 4,1,5,3, 4,1,5,3, 0,0,0,0, 2,2,2,2,
	};
		
	//s d
	private int[][] vectorTransition = {
		{5,0,4,0},
		{1,5,1,4},
		{4,2,5,2},
		{3,4,3,5},
		{0,1,2,3},
		{2,3,0,1}
	};
		
	private int[][] stateTransition;
	
	public CubeStates() {
		initTransitions();
	}
	
	private void initTransitions() {
		stateIndex = new int[6][6];
		for (int i1 = 0; i1 < 6; i1++) {
			for (int i2 = 0; i2 < 6; i2++) {
				stateIndex[i1][i2] = -1;
			}
		}
		for (int s = 0; s < states.length; s++) {
			stateIndex[states[s][0]][states[s][1]] = s;
		}
		stateTransition = new int[states.length][4];
		for (int s = 0; s < states.length; s++) {
			for (int d = 0; d < 4; d++) {
				int i1 = vectorTransition[states[s][0]][d];
				int i2 = vectorTransition[states[s][1]][d];
				stateTransition[s][d] = stateIndex[i1][i2];
				if(stateTransition[s][d] < 0) {
					throw new RuntimeException("Cannot build transition for state " + s + " in direction " + d);
				}
			}
		}
	}
	
	public int roll(int state, int d) {
		return stateTransition[state][d];
	}
	
	public int getUpperFace(int state) {
		return upperFace[state];
	}

}
