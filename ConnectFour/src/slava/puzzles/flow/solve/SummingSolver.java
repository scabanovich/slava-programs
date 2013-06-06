package slava.puzzles.flow.solve;

public class SummingSolver {
	int solutionLimit = 2;

	int number;
	int moveLimit;

	int[] state;
	int stateLength;
	int[] usedNumbers;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int treeCount;
	int solutionCount;

	public SummingSolver() {
	}

	public void setNumber(int n) {
		number = n;
	}

	public void setMoveLimit(int l) {
		moveLimit = l;
	}

	public void bestSolve() {
		int n = number;
		moveLimit = 0;
		while(n > 1) {
			n = n / 2;
			moveLimit++;
		}
		solutionCount = 0;
		while(solutionCount == 0) {
			solve();
			if(solutionCount == 0) {
				moveLimit++;
			}
		}
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[moveLimit + 1];
		state[0] = 1;
		stateLength = 1;
		usedNumbers = new int[number + 1];
		usedNumbers[1] = 1;
		wayCount = new int[moveLimit + 1];
		way = new int[moveLimit + 1];
		ways = new int[moveLimit + 1][number];
		
		t = 0;
		treeCount = 0;
		solutionCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		int last = state[stateLength - 1];
		if(last == number || t >= moveLimit) return;
		if(!hasChance()) return;
		for (int i = 0; i < stateLength; i++) {
			for (int j = i; j < stateLength; j++) {
				int n = state[i] + state[j];
				if(n > number || usedNumbers[n] > 0 || n <= last) continue;
				if(n == number) {
					ways[t][0] = n;
					wayCount[t] = 1;
					return;
				}
				ways[t][wayCount[t]] = n;
				wayCount[t]++;				
			}
		}
	}

	int getMax() {
		int m = 0;
		for (int i = 0; i < stateLength; i++) {
			if(state[i] > m) m = state[i];
		}
		return m;
	}

	boolean hasChance() {
		int m = getMax();
		for (int i = t; i < moveLimit; i++) {
			m = m * 2;
			if(m >= number) return true;
		}
		return false;
	}

	void move() {
		int n = ways[t][way[t]];
		usedNumbers[n]++;
		state[stateLength] = n;
		stateLength++;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int n = ways[t][way[t]];
		usedNumbers[n] = 0;
		stateLength--;
		state[stateLength] = 0;
	}

	void anal() {
		srch();
		way[t] = -1;
		int maxT = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > maxT) {
				maxT = t;
			}
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(state[stateLength - 1] == number) {
				solutionCount++;
				if(solutionCount == 1) printSolution();
				if(solutionCount >= solutionLimit) {
					return;
				}
			}
		}
	}

	void printSolution() {
		for (int i = 0; i < stateLength; i++) {
			System.out.print(" " + state[i]);
		}
		System.out.println(" (" + (stateLength - 1) + ")");
	}

	public static void main(String[] args) {
		SummingSolver p = new SummingSolver();
		int n = 1;
		for (int i = 1871; i < 1872; i++) {
			n = i;
			p.setNumber(n);
			p.bestSolve();
			if(p.moveLimit > 15) break;
		}
	}

}
