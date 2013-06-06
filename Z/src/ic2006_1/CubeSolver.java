package ic2006_1;

import forsmarts.distances.DistancesField;

public class CubeSolver {
	//[orientation][d]
	static int[][] transform = {
		{4,0,5,0},{1,4,1,5},{5,2,4,2},{3,5,3,4},{2,3,0,1},{0,1,2,3}
	};
	
	static int[] REVERSE = {2,3,0,1};
	
	DistancesField field;
	int[] painted;
	int start;
	int end;
	
	int[] state;
	int orientation;
	
	int t;
	int place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public CubeSolver() {}
	
	public void setField(DistancesField field) {
		this.field = field;
	}
	
	public void setPainted(int[] painted) {
		this.painted = painted;
	}
	
	public void setTerminals(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][4];
		t = 0;
		place = start;
		state[place] = 1;
		orientation = 4;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == state.length - 1) return;
		if(place == end) return;
		if(!isOk()) return;
		int p = place;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			int o = transform[orientation][d];
			if(q < 0 || state[q] > 0 || !canPlace(q, o)) continue;
			ways[t][wayCount[t]] = d;
			wayCount[t]++;
		}
	}
	
	boolean canPlace(int p, int o) {
		return (o == 4) == (painted[p] == 1); 
	}
	
	boolean isOk() {
		return true;
	}
	
	void move() {
		int d = ways[t][way[t]];
		place = field.jump(place, d);
		orientation = transform[orientation][d];
		state[place] = t + 2;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		state[place] = 0;
		d = REVERSE[d];
		place = field.jump(place, d);
		orientation = transform[orientation][d];
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == state.length - 1) {
				System.out.println("-->" + place);
			}
			if(t == state.length - 1 && place == end) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution found");
		int turnCount = 0;
		for (int i = 1; i < t; i++) {
			int dnew = ways[i][way[i]];
			int dold = ways[i - 1][way[i - 1]];
			System.out.print("" + dold);
			if(i % 5 == 0) System.out.print(" ");
			if(dnew != dold) turnCount++;
		}
		System.out.println("");
		System.out.println("Turns count=" + turnCount);
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	static int[] PAINTED = {
		0,0,0,0,0,0,0,0,0,
		0,1,0,1,0,0,1,1,0,
		0,0,0,0,0,0,0,0,1,
		1,0,0,0,0,1,0,0,0,
		0,0,0,0,1,0,0,0,0,
		0,0,1,0,0,0,0,0,0,
		1,0,0,0,0,0,0,1,0,
		0,0,0,0,0,0,0,0,0,
		1,0,0,0,0,0,1,1,0,
	};

	public static void main(String[] args) {
		DistancesField field = new DistancesField();
		field.setSize(9, 9);
		CubeSolver solver = new CubeSolver();
		solver.setField(field);
		solver.setPainted(PAINTED);
		solver.setTerminals(8 * 9, 8);
		solver.solve();
	}
}

//Solution found
//Turns count=43
//00322 30003 30110 12210 
//00301 03322 32332 22332 
//11101 22333 33000 00122 
//10003 30111 21010 3333

