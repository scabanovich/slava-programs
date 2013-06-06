package forsmarts;

public class ArithmeticSpiralSolver {
	int width = 7;
	int[] data;
	int[] order;
	
	int[] state;
	int[] used;
	
	int t;
	int[] value;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public ArithmeticSpiralSolver() {}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void setOrder(int[] order) {
		this.order = order;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[data.length];
		value = new int[data.length];
		used = new int[(data.length + 1) / 2];
		wayCount = new int[data.length + 1];
		way = new int[data.length + 1];
		ways = new int[data.length + 1][10];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == data.length) return;
		int p = order[t];
		if(data[p] >= 0) {
			if(t == 0) {
				ways[t][wayCount[t]] = data[p];
				wayCount[t]++;
			} else if(t % 2 == 0) {
				int r = compute(data[p]);
				if(r >= 0 && r < used.length && used[r] == 0) {
					ways[t][wayCount[t]] = data[p];
					wayCount[t]++;
				}
			} else {
				ways[t][wayCount[t]] = data[p];
				wayCount[t]++;
			}
		} else {
			if(t == 0) {
				for (int i = 1; i < 10; i++) {
					ways[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			} else if(t % 2 == 0) {
				for (int i = 1; i < 10; i++) {
					int r = compute(i);
					if(r >= 0 && r < used.length && used[r] == 0) {
						ways[t][wayCount[t]] = i;
						wayCount[t]++;
					}
				}
			} else {
				for (int i = 0; i < 4; i++) {
					ways[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			}
		}
	}
	
	int compute(int v2) {
		int v1 = value[t];
		int s = state[order[t - 1]];
		if(s == 0) {
			return v1 + v2; 
		} else if(s == 1) {
			return v1 - v2;
		} else if(s == 2) {
			return v1 * v2;
		} else if(s == 3) {
			return (v1 % v2 == 0) ? v1 / v2 : -1;
		} else {
			return -1;
		}
	}
	
	void move() {
		int p = order[t];
		int w = ways[t][way[t]];
		state[p] = w;
		if(t == 0) {
			value[0] = w;
			value[t + 1] = w;
		} else if(t % 2 == 0) {
			int r = compute(w);
			if(t + 1 < value.length) value[t + 1] = r;
			used[r] = 1;
		} else {
			if(t < value.length) value[t + 1] = value[t];
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = order[t];
		int w = ways[t][way[t]];
		state[p] = w;
		if(t == 0) {
			//
		} else if(t % 2 == 0) {
			int r = compute(w);
			used[r] = 0;
		} else {
			//
		}
	}
	
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
				System.out.println(tm);
			}
			if(t == data.length) {
				solutionCount++;
				printSolution();
			}
		}
	}
	
	char[] SIGN = {'+', '-', '*', '/'};
	
	void printSolution() {
		System.out.println("solution");
		for (int i = 0; i < data.length; i++) {
			int x = i % width, y = i / width;
			boolean isDigit = ((x + y) % 2 == 0);
			String s = isDigit ? "" + state[i] : "" + SIGN[state[i]];
			System.out.print(" " + s);
			if(x == width - 1) System.out.println("");			
		}
		System.out.println("");
	}
	
}
