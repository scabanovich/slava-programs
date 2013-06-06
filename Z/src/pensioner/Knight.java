package pensioner;

public class Knight {
	int[][] moves = new int[][]{{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	int[] form;
	
	int[] state;
	int[] freeWays;
	int[] position;
	
	int[] wayCount;
	int[] way;
	int[][] ways;
	int t;
	
	int solutionCount;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[moves.length][size];
		for (int i = 0; i < size; i++) {
			for (int d = 0; d < moves.length; d++) {
				int xd = x[i] + moves[d][0];
				int yd = y[i] + moves[d][1];
				jp[d][i] = (xd < 0 || xd >= width || yd < 0 || yd >= height) ? -1 : xy[xd][yd];
			}
		}
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		freeWays = new int[size];
		for (int i = 0; i < size; i++) {
			freeWays[t] = 0;
			for (int d = 0; d < moves.length; d++) {
				if(jp[d][i] >= 0) ++freeWays[i];
			}
		}
		position = new int[size + 1];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][size];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		if(t == 0) {
			wayCount[t] = size;
			for (int i = 0; i < size; i++) ways[t][i] = i;
			return;
		}
		int f = getForm();
		boolean fc = false;
		for (int d = 0; d < moves.length; d++) {
			int i = jp[d][position[t - 1]];
			if(i < 0 || state[i] >= 0) continue;
			if(freeWays[i] == 1 && t < size - 10) {
				if(fc || !checkForm(f, i)) {
					wayCount[t] = 0;
					return;
				}
				wayCount[t] = 1;
				ways[t][0] = i;
				fc = true;
			} else if(!fc && checkForm(f, i)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	int getForm() {
		if(form == null || t == 0) return -1;
		int f = form[position[t - 1]];
		for (int i = 0; i < size; i++) {
			if(state[i] < 0 && form[i] == f) return f;
		}
		return -1;
	}
	
	boolean checkForm(int f, int i) {
		return (f < 0 || form[i] == f);
	}
	
	void move() {
		int i = ways[t][way[t]];
		position[t] = i;
		state[i] = t;
		for (int d = 0; d < moves.length; d++) {
			int j = jp[d][i];
			if(j >= 0 && state[j] < 0) freeWays[j]--;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		state[i] = -1;
		for (int d = 0; d < moves.length; d++) {
			int j = jp[d][i];
			if(j >= 0 && state[j] < 0) freeWays[j]++;
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
			if(t == size 
					&& isCycle()
			   ) {
				++solutionCount;
				if(solutionCount == 1) printSolution();
			}
		}
	}
	
	boolean isCycle() {
		int i = position[0];
		int j = position[t - 1];
		for (int d = 0; d < moves.length; d++) {
			if(jp[d][i] == j) return true;
		}
		return false;
	}
	
	void printSolution() {
		for (int i = 0; i < size; i++) {
			String s = "" + state[i];
			while(s.length() < 3) s = " " + s;
			System.out.print(s);
			if(x[i] == width - 1) System.out.println("");
		}
	}
	
	static int[] FORM = new int[]{
			0,0,0,0,1,1,1,1,
			0,0,0,0,1,1,1,1,
			0,0,0,0,1,1,1,1,
			3,3,3,3,2,2,2,2,
			3,3,3,3,2,2,2,2,
			3,3,3,3,2,2,2,2,
			3,3,3,3,2,2,2,2,
			3,3,3,3,2,2,2,2
	};
	
	public static void main(String[] args) {
		Knight k = new Knight();
		k.setSize(8, 8);
		k.setForm(FORM);
		k.solve();
		System.out.println("solutionCount=" + k.solutionCount);
	}
}

/*
  9  6  3  0 61 58 53 56
  4  1  8 11 52 55 62 59
  7 10  5  2 63 60 57 54
 40 35 46 51 12 17 30 25
 45 50 41 36 31 24 13 18
 34 39 44 47 16 21 26 29
 49 42 37 32 23 28 19 14
 38 33 48 43 20 15 22 27
*/