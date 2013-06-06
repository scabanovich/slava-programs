package pqrst14;

public class MultiplicationTable {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] jp;
	
	int[][] groups = new int[][]{
		{3,4,10,11},
		{5,6,12,13},
		{9,10,16,17},
		{14,15,21,22},
		{16,17,23,24},
		{18,19,25,26},
		{24,25,31,32},
		{28,29,35,36},
		{36,37,43,44},
		{38,39,45,46},
	};
	int[] products = new int[]{168,24,120,192,60,105,120,36,20,84};
	
	int[] state;
	int[][] usage;

	int t;
	int[] place;	
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[] temp;
	
	public MultiplicationTable() {
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
	 * @param groups - [groupIndex][cellIndex] - sets of cells contributing to products
	 * @param products - [groupIndex] - product of values over set of cells
	 */
	public void setProblem(int[][] groups, int[] products) {
		this.groups = groups;
		this.products = products;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		place = new int[size + 1];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][width];
		
		state = new int[size];
		usage = new int[size][width + 1];
		temp = new int[width];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		place[t] = -1;
		int wcm = 1000;
		for (int p = 0; p < size; p++) {
			if(state[p] > 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcm) {
				wcm = wc;
				place[t] = p;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
			}
		}
		if(place[t] != -1) {
			wayCount[t] = wcm;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int c = 1; c <= width; c++) {
			if(!canPlace(p, c)) continue;
			temp[wc] = c;
			wc++;
		}
		return wc;
	}
	
	boolean canPlace(int p, int c) {
		if(state[p] != 0) return false;
		if(usage[p][c] > 0) return false;
		state[p] = c;
		boolean res = checkEquations();
		state[p] = 0;
		return res;
	}
	
	boolean checkEquations() {
		for (int i = 0; i < groups.length; i++) {
			if(!checkEquation(i)) return false;
		}
		return true;
	}
	
	boolean checkEquation(int index) {
		int unused = 0;
		int v = products[index];
		for (int i = 0; i < groups[index].length; i++) {
			int p = groups[index][i];
			int c = state[p];
			if(c == 0) {
				unused++;
			} else if(v % c != 0) {
				return false;
			} else {
				v = v / c;
			}
		}
		if(unused == 0 && v != 1) return false;
		if(unused == 1 && v > width) return false;
		return true;
	}
	
	void move() {
		int c = ways[t][way[t]];
		int p = place[t];
		state[p] = c;
		for (int ix = 0; ix < height; ix++) {
			int q = ix + y[p] * width;
			usage[q][c]++;
		}
		for (int iy = 0; iy < width; iy++) {
			int q = x[p] + iy * width;
			usage[q][c]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int c = ways[t][way[t]];
		int p = place[t];
		state[p] = 0;
		for (int ix = 0; ix < height; ix++) {
			int q = ix + y[p] * width;
			usage[q][c]--;
		}
		for (int iy = 0; iy < width; iy++) {
			int q = x[p] + iy * width;
			usage[q][c]--;
		}
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
			System.out.print(" " + state[i]);
			if(x[i] == width - 1) System.out.println();
		}
		System.out.println("");
		for (int i = 0; i < width; i++) {
			int p = i * width + i;
			System.out.print(state[p]);
		}
		System.out.println("");
	}

}

/*
5562236

 5 7 3 4 6 2 1
 3 5 4 1 7 6 2
 2 4 6 5 3 1 7
 4 6 1 2 5 7 3
 1 3 7 6 2 4 5
 6 2 5 7 1 3 4
 7 1 2 3 4 5 6

*/