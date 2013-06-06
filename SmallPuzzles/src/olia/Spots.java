package olia;

public class Spots {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;
	
	int[] state;
	int[] prohibition;
	int[] temp;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int blackCellCount;
	
	int minNumber;
	int[] minSolution;
	int minSolutionCount;
	int maxNumber;
	int[] maxSolution;
	int maxSolutionCount;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		blackCellCount = 0;
		prohibition = new int[size];
		temp = new int[size];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][2];
		minNumber = 1000;
		maxNumber = -1;
		maxSolutionCount = 0;
		maxSolutionCount = 0;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		for (int i = 0; i < 2; i++) {
			if(i == 0 && mustPut(t)) continue;
			if(i == 1 && prohibition[t] > 0) continue;
			if(checkContinuity()) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			} 
		}
	}
	
	boolean mustPut(int p) {
		int q1 = jp[3][p];
		if(q1 >= 0 && state[q1] == 0 && prohibition[q1] == 0) return true;
		int q2 = jp[2][p];
		if(q2 >= 0 && state[q2] == 0 && jp[1][q2] < 0 && prohibition[q2] == 0) return true;
		if(p == size - 1 && prohibition[p] == 0) return true;
		return false;
	}
	
	int[] stack = new int[200];

	boolean checkContinuity(int p, int i) {
		state[p] = i;
		if(i == 1) ++blackCellCount;
		boolean check = checkContinuity();
		state[p] = -1;
		if(i == 1) --blackCellCount;
		return check;
	}
	
	boolean checkContinuity() {
		int v = 0;
		for (int i = 0; i < size; i++) temp[i] = 0;
		int b = -1;
		for (int i = 0; i < size && b < 0; i++) if(state[i] != 1) b = i;
		stack[v] = b;
		temp[b] = 1;
		v++;
		int c = 0;
		while(c < v) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = jp[d][p];
				if(q >= 0 && temp[q] == 0 && state[q] != 1) {
					stack[v] = q;
					temp[q] = 1;
					++v;
				}
			}			
			++c;
		}
		return (v + blackCellCount) == size;
	}
	
	void move() {
		int v = ways[t][way[t]];
		state[t] = v;
		if(v == 1) {
			for (int d = 0; d < 4; d++) {
				int q = jp[d][t];
				if(q >= 0) prohibition[q]++;
			}
			blackCellCount++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int v = ways[t][way[t]];
		state[t] = -1;
		if(v == 1) {
			for (int d = 0; d < 4; d++) {
				int q = jp[d][t];
				if(q >= 0) prohibition[q]--;
			}
			blackCellCount--;
		}
	}
	
	void anal() {
		int tm = 0;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == size && checkContinuity()) {
				if(blackCellCount < minNumber) {
					minNumber = blackCellCount;
					minSolution = (int[])state.clone();
					minSolutionCount = 1;
				} else if(blackCellCount == minNumber) {
					minSolutionCount++;
				}
				if(blackCellCount > maxNumber) {
					maxNumber = blackCellCount;
					maxSolution = (int[])state.clone();
					maxSolutionCount = 1;
				} else if(blackCellCount == maxNumber) {
					maxSolutionCount++;
				}
			}
		}
	}
	
	void printResults() {
		if(minSolution != null) {
			System.out.println("min=" + minNumber + "  solutions=" + minSolutionCount);
			printState(minSolution);
		}
		if(maxSolution != null) {
			System.out.println("max=" + maxNumber + " solutions=" + maxSolutionCount);
			printState(maxSolution);
		}
	}
	
	void printState(int[] s) {
		for (int i = 0; i < size; i++) {
			char c = (s[i] == 0) ? '+' : (s[i] == 1) ? 'x' : ' ';
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
	}

	public static void main(String[] args) {
		Spots s = new Spots();
		s.setSize(7, 9);
		s.solve();
		s.printResults();
	}

}

/*
min=16  solutions=2
 + + x + + + x +
 x + + + x + + +
 + + + x + + + x
 + x + + + x + +
 + + x + + + x +
 x + + + x + + +
 + + + x + + + x
 + x + + + x + +
max=21 solutions=800
 + + x + + x + +
 x + + x + + + x
 + + x + + x + +
 x + + x + + x +
 + + x + + x + x
 x + + + x + + +
 + + x + + + x +
 x + + x + x + x
 
min=21  solutions=682
 + + x + + + x + +
 x + + + x + + + x
 + + + x + + + x +
 + x + + + x + + +
 + + + + x + + + x
 x + x + + + x + +
 + + + + x + + + x
 + x + + + x + + +
 x + + x + + + x +
max=27 solutions=282
 + + x + + x + + x
 x + + x + + x + +
 + + x + + x + + x
 x + + x + + x + +
 + + x + + x + + x
 x + + x + + x + +
 + x + + + x + + x
 + + + x + + + x +
 x + x + + x + + x

*/