package ic2005_1;

public class PathForDomino {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;
	int[] restrictions;

	int[] state;
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int pathLength;

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
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1; 
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width; 
			jp[2][i] = (x[i] == 0) ? -1 : i - 1; 
			jp[3][i] = (x[i] == 0) ? -1 : i - width; 
			jp[4][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width; 
			jp[5][i] = (x[i] == 0 || y[i] == height - 1) ? -1 : i - 1 + width; 
			jp[6][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width; 
			jp[7][i] = (x[i] == width - 1 || y[i] == 0) ? -1 : i + 1 - width; 
		}
	}
	
	public void setRestrictions(int[] v) {
		restrictions = v;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[size];
		way = new int[size];
		ways = new int[size][height];
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		t = 0;
		pathLength = 39;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 0) {
			ways[t][wayCount[t]] = 0;
			wayCount[t]++;
		} else {
			int p = ways[t - 1][way[t - 1]];
			for (int d = 0; d < 4; d++) {
				int q = jp[d][p];
				if(q < 0 || state[q] >= 0 || restrictions[q] != 1 || !check(q)) continue;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
	}
	
	boolean check(int p) {
		for (int d = 0; d < 8; d++) {
			int q = jp[d][p];
			if(q >= 0 && state[q] >= 0 && state[q] < t - 2) return false;
		}
		return true;
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = t;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			int p = ways[t - 1][way[t - 1]];
			if(x[p] == width - 1 && y[p] == 0) {
				if(pathLength <= t) {
					pathLength = t;
					System.out.println("pathLength=" + pathLength);
					printSolution();
				}
			}
		}
	}
	void printSolution() {
		System.out.println("");
		for (int i = 0; i < size; i++) {
			char c = '+';
			if(restrictions[i] == 0) {
				c = '-';
			} else if(state[i] >= 0) {
				int k = state[i] % 26;
				c = (char)(97 + k);
			}
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	static int[] RESTRICTIONS = new int[]{
		1,1,1,0,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,1,0,
		0,0,1,1,1,1,1,1,1,1,1,0,0,
		0,0,0,1,1,1,1,1,1,1,0,0,0,
		0,0,0,0,1,1,1,1,1,0,0,0,0,
	};

	public static void main(String[] args) {
		System.out.println("Start");
		PathForDomino p = new PathForDomino();
		p.setSize(13, 7);
		p.setRestrictions(RESTRICTIONS);
		p.solve();
	}
}

/*
 - - - - + + + + + - - - -
 - - - + + + + + + + - - -
 - - + + + + + + + + + - -
 - + + + + + + + + x x x - 
 x x x x x x x x x x + x x
 x + + + + + + + + + + + x
 x x x x x x x x x + + + x
 + + + + + + + + x + + + x
 + + x x x x + x x + x x x
 - + x + + x x x + + x + -
 - - x x + + + + + x x - -
 - - - x x + + + x x - - -
 - - - - x x x x x - - - -
*/ 
