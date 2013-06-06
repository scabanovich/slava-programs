package pqrst10;

public class Tripath {
	int width;
	int size;
	
	int[] x;
	int[] y;
	int[][] jp;

	int[] field;
	
	int[] occupation;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysA; // main direction
	int[][] waysB; // auxiliary direction
	
	int pathLength;
	
	public void solve() {
		init();
		anal();
	}
	
	public void setSize(int width) {
		this.width = width;
		this.size = width * width;
		x = new int[size];
		y = new int[size];
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] == width - 1) ? -1 : i + 1 + width;
			jp[2][i] = (y[i] == width - 1) ? -1 : i + width;
			jp[3][i] = (x[i] == 0) ? -1 : i - 1;
			jp[4][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
			jp[5][i] = (y[i] == 0) ? -1 : i - width;
		}
	}
	
	public void setField(int[] field) {
		this.field = field;
		for (int i = 0; i < size; i++) {
			if(field[i] == 0) continue;
			for (int d = 0; d < 6; d++) {
				int j = jp[d][i];
				if(j < 0) continue;
				if(field[j] == 0) jp[d][i] = -1;
			}
		}		
	}
	
	void init() {
		occupation = new int[size];
		for (int i = 0; i < size; i++) {
			occupation[i] = 0;
		}
		place = new int[size + 1];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		waysA = new int[size + 1][6];
		waysB = new int[size + 1][6];
		t = 0;
		place[0] = 6 * width + 6;
		occupation[place[0]] = 1;
		pathLength = 50;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t < 56) {
			if(!isConnected()) return;
			if(!allReachable()) return;
		}
		int p = place[t];
		int q = (t == 0) ? 1 : 6;
		for (int d = 0; d < q; d++) {
			int p1 = jp[d][p];
			if(p1 < 0 || occupation[p1] == 1) continue;
			int d2 = (d == 5) ? 0 : d + 1;
			int p2 = jp[d2][p];
			if(p2 < 0 || occupation[p2] == 1) continue;
			waysA[t][wayCount[t]] = d;
			waysB[t][wayCount[t]] = d2;
			wayCount[t]++;
			waysA[t][wayCount[t]] = d2;
			waysB[t][wayCount[t]] = d;
			wayCount[t]++;
		}
		if(t < 10) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			int c = waysA[t][i];
			waysA[t][i] = waysA[t][j];
			waysA[t][j] = c;
			c = waysB[t][i];
			waysB[t][i] = waysB[t][j];
			waysB[t][j] = c;
		}
	}
	
	void move() {
		int d1 = waysA[t][way[t]];
		int d2 = waysB[t][way[t]];
		int p = place[t];
		int p1 = jp[d1][p];
		int p2 = jp[d2][p];
		occupation[p1] = 1;
		occupation[p2] = 1;
		place[t + 1] = p1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d1 = waysA[t][way[t]];
		int d2 = waysB[t][way[t]];
		int p = place[t];
		int p1 = jp[d1][p];
		int p2 = jp[d2][p];
		occupation[p1] = 0;
		occupation[p2] = 0;
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
			if(t > pathLength) {
				pathLength = t;
				System.out.println(pathLength);
				printSolution();
			}
		}
	}
	
	int[] temp = new int[200];
	boolean isConnected() {
		int v = 1;
		int c = 0;
		temp[0] = place[t];
		while(c < v) {
			int p = temp[c];
			for (int d = 0; d < 6; d++) {
				int pd = jp[d][p];
				if(pd < 0 || occupation[pd] != 0) continue;
				occupation[pd] = 2;
				temp[v] = pd;
				++v;
			}
			c++;
		}
		for (int i = 1; i < v; i++) {
			if(occupation[temp[i]] == 2) occupation[temp[i]] = 0;
		}
		return 1 + 2 * t + v >= 127;		
	}
	
	boolean allReachable() {
		for (int i = 0; i < size; i++) {
			if(field[i] != 1 || occupation[i] != 0) continue;
			if(!isReachablePoint(i)) return false;
		}
		return true;
	}
	
	boolean isReachablePoint(int p) {
		for (int d = 0; d < 6; d++) {
			int p1 = jp[d][p];
			if(p1 < 0 || (occupation[p1] == 1 && p1 != place[t])) continue;
			int d2 = (d == 5) ? 0 : d + 1;
			int p2 = jp[d2][p];
			if(p2 < 0 || (occupation[p2] == 1 && p2 != place[t])) continue;
			return true;
		}
		return false;
	}
	
	private void printSolution() {
		for (int i = 0; i < t; i++) {
			System.out.print("" + waysA[i][way[i]] + ":" + waysB[i][way[i]] + ",");
		}
		System.out.println("");
	}
	
	static int[] field_7 = new int[]{
		1,1,1,1,1,1,1,0,0,0,0,0,0,
		1,1,1,1,1,1,1,1,0,0,0,0,0,
		1,1,1,1,1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1,1,1,1,1,
		0,0,0,0,0,1,1,1,1,1,1,1,1,
		0,0,0,0,0,0,1,1,1,1,1,1,1
	};

	public static void main(String[] args) {
		Tripath t = new Tripath();
		t.setSize(13);
		t.setField(field_7);
		t.solve();
	}
	
	/*
	 * 0:1,1:0,0:1,5:0,5:0,
	 * 5:4,5:0,4:3,3:4,4:3,
	 * 4:5,2:3,2:3,1:2,1:0,
	 * 2:1,3:4,3:4,2:3,2:1,
	 * 3:2,3:2,5:4,5:0,4:3,
	 * 0:5,5:0,5:0,5:0,4:5,
	 * 2:3,3:2,3:4,2:1,2:1,
	 * 2:1,2:1,1:2,1:2,1:2,
	 * 1:2,1:0,2:3,0:1,0:5,
	 * 1:2,5:0,4:5,5:4,5:4,
	 * 1:0,1:0,1:2,1:2,5:0,
	 * 5:0,4:3,0:5,0:1,5:4,
	 * 5:4,4:5,4:5,
	 */

}
