package ic5_1;

public class SumOfMeeted {
	int width;
	int size;
	int maxX;
	int[] x,y;
	int[][] xy;
	int[][] vSums;
	int[][] hSums;
	
	int[] state;
	int[][] vUsed;
	int[][] hUsed;
	int t;
	int[] wayCount;
	int[] way;
	int[] place;
	int[][] ways;
	
	public void setSize(int width) {
		this.width = width;
		maxX = width - 1;
		size = width * width;
		x = new int[size];
		y = new int[size];
		xy = new int[width][width];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
	}
	
	public void setSums(int[][] hSums, int[][] vSums) {
		this.hSums = hSums;
		this.vSums = vSums;
	}
	
	boolean check(int p) {
		for (int n = 0; n < 3; n++) {
			if(!checkHorizontal(y[p], n)) return false;
			if(!checkVertical(x[p], n)) return false;
		}
		return true;
	}
	
	boolean checkVertical(int ix, int n) {
		if(vSums[ix][n] < 0) return true;
		int sum = 0;
		int fi = -1;
		int iy = -1;
		while(fi < n) {
			++iy;
			if(state[xy[ix][iy]] < 0) return true;
			if(state[xy[ix][iy]] > 0) ++fi;
		}
		sum += state[xy[ix][iy]];
		fi = -1;
		iy = width;
		while(fi < n) {
			--iy;
			if(state[xy[ix][iy]] < 0) return true;
			if(state[xy[ix][iy]] > 0) ++fi;
		}
		sum += state[xy[ix][iy]];
		return sum == vSums[ix][n];
	}
	
	boolean checkHorizontal(int iy, int n) {
		if(hSums[iy][n] < 0) return true;
		int sum = 0;
		int fi = -1;
		int ix = -1;
		while(fi < n) {
			++ix;
			if(state[xy[ix][iy]] < 0) return true;
			if(state[xy[ix][iy]] > 0) ++fi;
		}
		sum += state[xy[ix][iy]];
		fi = -1;
		ix = width;
		while(fi < n) {
			--ix;
			if(state[xy[ix][iy]] < 0) return true;
			if(state[xy[ix][iy]] > 0) ++fi;
		}
		sum += state[xy[ix][iy]];
		return sum == hSums[iy][n];
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		hUsed = new int[width][width];
		vUsed = new int[width][width];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		place = new int[size + 1];
		ways = new int[size + 1][width];
		tempWays = new int[width];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		int wcm = width + 1;
		place[t] = -1;
		for (int p = 0; p < size; p++) {
			if(state[p] >= 0) continue;
			int wc = getWays(p);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = tempWays[i];
				place[t] = p;
			}
		}
		if(place[t] < 0) return;
		wayCount[t] = wcm;
	}
	
	int[] tempWays;
	
	int getWays(int p) {
		int wc = 0;
		for (int v = 0; v < width; v++) {
			if(hUsed[y[p]][v] > 0 || vUsed[x[p]][v] > 0) continue;
			state[p] = v;
			if(check(p)) {
				tempWays[wc] = v;
				++wc;
			}
			state[p] = -1;
		}
		return wc;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		hUsed[y[p]][v] = 1;
		vUsed[x[p]][v] = 1;
		state[p] = v;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		hUsed[y[p]][v] = 0;
		vUsed[x[p]][v] = 0;
		state[p] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			++way[t];
			move();
			if(t == size) {
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		printSolution();
	}
	
	void printSolution() {
		System.out.println("");
		for (int i = 0; i < size; i++) {
			System.out.print(" " + state[i]);
			if(x[i] == maxX) System.out.println("");
		}
	}

	static int[][] H_SUMS = new int[][]{
		{-1, 7, -1},
		{-1, -1, -1},
		{-1, 8, -1},
		{5, 10, 6},
		{6, -1, -1},
		{11, 5, 5},
		{3, 8, 10}
	};
	static int[][] V_SUMS = new int[][]{
		{-1, 11, -1},
		{-1, 3, -1},
		{9, -1, -1},
		{11, -1, -1},
		{3, 10, 8},
		{-1, -1, -1},
		{4, 10, 7}
	};

	public static void main(String[] args) {
		SumOfMeeted a = new SumOfMeeted();
		a.setSize(7);
		a.setSums(H_SUMS, V_SUMS);
		a.solve();
	}
}
