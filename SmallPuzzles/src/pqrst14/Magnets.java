package pqrst14;

public class Magnets {
	static char[] CHAR_VALUE = {'+','-','N'};
	//0 '+'; 1 '-'; 2 ' '
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] jp;
	
	//This is problem
	int[] neighbours = new int[]{
		1,1,0,2,1,1,0,2,0,2,1,1,
		3,3,0,2,3,3,1,0,2,1,3,3,
		0,2,1,0,2,1,3,1,1,3,1,1,
		1,1,3,1,1,3,1,3,3,1,3,3,
		3,3,1,3,3,1,3,0,2,3,0,2,
		0,2,3,0,2,3,0,2,1,0,2,1,
		1,0,2,1,0,2,1,1,3,0,2,3,
		3,0,2,3,0,2,3,3,0,2,0,2,
		0,2,0,2,1,1,0,2,1,0,2,1,
		0,2,1,1,3,3,1,1,3,0,2,3,
		1,1,3,3,0,2,3,3,1,1,0,2,
		3,3,0,2,0,2,0,2,3,3,0,2
	};
	int[][] hLimits = new int[][]{
		{4,5,4,3,5,5,5,5,3,3,2,3},
		{3,6,1,6,4,5,5,6,3,2,3,3}
	};
	int[][] vLimits = new int[][]{
		{5,4,5,4,2,3,4,3,4,5,4,4},
		{5,4,5,3,3,3,3,5,3,4,4,5}
	};
	
	int[] state;
	int[][] hCounts;
	int[][] vCounts;

	int t;	
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public Magnets() {
	}
	
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
	
	/**
	 * 
	 * @param neighbours - directions for cell in the same domino
	 * @param hLimits - int[2][height] - numbers of '+' and '-' in h-line y 
	 * @param vLimits - int[2][width] - numbers of '+' and '-' in v-line v
	 */	
	public void setProblem(int[] neighbours, int[][] hLimits, int[][] vLimits) {
		this.neighbours = neighbours;
		this.hLimits = hLimits;
		this.vLimits = vLimits;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][3];
		
		state = new int[size];
		hCounts = new int[height][3];
		vCounts = new int[width][3];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		if(t > 0 && !check()) return;
		for (int i = 0; i < 3; i++) {
			if(!canPlace(t, i)) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;			
		}
	}
	
	boolean canPlace(int p, int c) {
		int nd = neighbours[p];
		int n = jp[nd][p];
		for (int d = 2; d < 4; d++) {
			int q = jp[d][p];
			if(q < 0) continue;
			int v = state[q];
			if(q == n) {
				if(v == 2 && c != 2) return false;
				if(v != 2 && (c == 2 || c == v)) return false;
			} else {
				if(v != 2 && c == v) return false;
			}
		}
		return true;
	}
	
	boolean check() {
		int ix = x[t - 1];
		int iy = y[t - 1];
		for (int jy = 0; jy < iy - 1; jy++) {
			if(hCounts[jy][0] != hLimits[0][jy]) return false;
			if(hCounts[jy][1] != hLimits[1][jy]) return false;
		}
		if(ix == width - 1) {
			if(hCounts[iy][0] != hLimits[0][iy]) return false;
			if(hCounts[iy][1] != hLimits[1][iy]) return false;
		} else {
			if(hCounts[iy][0] > hLimits[0][iy]) return false;
			if(hCounts[iy][1] > hLimits[1][iy]) return false;
		}
		for (int jx = 0; jx < width; jx++) {
			if(iy == height - 1 && jx <= ix) {
				if(vCounts[jx][0] != vLimits[0][jx]) return false;
				if(vCounts[jx][1] != vLimits[1][jx]) return false;
			} else {
				if(vCounts[jx][0] > vLimits[0][jx]) return false;
				if(vCounts[jx][1] > vLimits[1][jx]) return false;
			}
		}
		return true;
	}
	
	void move() {
		int c = ways[t][way[t]];
		state[t] = c;
		int ix = x[t];
		int iy = y[t];
		hCounts[iy][c]++;
		vCounts[ix][c]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int c = ways[t][way[t]];
		int ix = x[t];
		int iy = y[t];
		hCounts[iy][c]--;
		vCounts[ix][c]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(t == size) {
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution");
		for (int i = 0; i < size; i++) {
			System.out.print(CHAR_VALUE[state[i]]);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < width; i++) {
			int p = i * width + i;
			System.out.print(CHAR_VALUE[state[p]]);
		}
		System.out.println("");
	}

}
/*
+++++NN-NNN-

complete field:
+-NNN+NN-+-+
-+-+N-+-+-+-
NN+NNN-+N+N+
-+-+-N+-N-N-
+-+-+N-NN+-+
-+-+-N+-+-+N
+-+-+-N+-+-N
-+-+-+N-+-+-
+-NNN-NNN+-+
NN+NN+-+NNN-
-N-NNN+-+NNN
+N+-NNNN-N+-

*/