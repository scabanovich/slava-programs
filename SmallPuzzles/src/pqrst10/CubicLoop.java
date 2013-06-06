package pqrst10;
/**
 * How many different ways are there to cover a cube with two different tiles 
 * so that there appears one closed loop passing around all six faces of it? 
 * Cube’s position is fixed, so two covers are different from each other 
 * if they have at least one face different, considering the position of 
 * the straight line on it.
 * 
 * This program finds single loops (16 - they are answer to the puzzle) 
 * and double loops (4). 
 */

public class CubicLoop {
	int[][] sets = new int[][]{
		{0,1,2,3},
		{0,4,5,8},
		{1,5,6,9},
		{2,6,7,10},
		{3,4,7,11},
		{8,9,10,11}
	};
	
	int[] first = new int[]{0,0,0,1,1,2};
	int[] second = new int[]{1,2,3,2,3,3};
	int[] state = new int[6];
	
	public void execute() {
		for (int i = 0; i < 6; i++)  state[i] = 0;
		while(true) {
			if(checkState()) {
				printState();
			}
			if(!nextState()) break;
		}
	}
	
	boolean nextState() {
		for (int i = 5; i >= 0; i--) {
			if(state[i] < 5) {
				state[i]++;
				return true;
			} else {
				state[i] = 0;
			}
		}
		return false;
	}
	
	boolean checkState() {
		int[] q = new int[12];
		for (int i = 0; i < 6; i++) {
			q[sets[i][first[state[i]]]]++;
			q[sets[i][second[state[i]]]]++;
		}
		for (int i = 0; i < 12; i++) if(q[i] == 1) return false;
		return true;
	}
	
	void printState() {
		for (int i = 0; i < 6; i++) {
			System.out.print("(" + sets[i][first[state[i]]] + "," + sets[i][second[state[i]]] + ") ");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		CubicLoop g = new CubicLoop();
		g.execute();
	}
}
