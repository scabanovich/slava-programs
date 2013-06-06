package ik5_3;

public class Pipeline {
	int width;
	int size;
	int[] x,y;
	int[][] jp;
	int[] rev = {2,3,0,1};
	String designation = " ABCDEFGHIJKLMNO";
	
	int[][] transition = {
		{-1,-1,-1, 3, 1,0},
		{-1,-1, 0, 2,-1,1},
		{-1, 1, 3,-1,-1,2},
		{-1, 0,-1,-1, 2,3}
	};
	
	int[] initialState = {
		6,1,5,4,
		2,4,3,3,
		2,1,4,5,
		5,3,5,0
	};
	
	int t;
	int[] state;
	int[] state2 = {
		1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0
	};
	int[] vacancy;
	int[] moved;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int tMax = 23;
	int maxLength = 15;		
	
	public void setWidth() {
		width = 4;
		size = width * width;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? - 1 : i + 1;
			jp[1][i] = (y[i] == width - 1) ? - 1 : i + width;
			jp[2][i] = (x[i] == 0) ? - 1 : i - 1;
			jp[3][i] = (y[i] == 0) ? - 1 : i - width;
		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = (int[])initialState.clone();
		wayCount = new int[tMax + 1];
		way = new int[tMax + 1];
		ways = new int[tMax + 1][4];
		vacancy = new int[tMax + 1];
		moved = new int[tMax + 1];
		t = 0;
		vacancy[0] = size - 1;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tMax) return;
		for (int d = 0; d < 4; d++) {
			if(t > 0 && ways[t - 1][way[t - 1]] == rev[d]) continue;
			int q = jp[d][vacancy[t]];
			if(q < 0 || state[q] == 6) continue;
			ways[t][wayCount[t]] = d;
			wayCount[t]++;
		}
	}
	
	void move() {
		int d = ways[t][way[t]];
		int p = vacancy[t];
		int q = jp[d][vacancy[t]];
		state[p] = state[q];
		state[q] = 0;
		vacancy[t + 1] = q;
		state2[p] = state2[q];
		state2[q] = 0;
		moved[t] = state2[p];
		++t;
		srch();
		way[t] = -1;		
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		int p = vacancy[t];
		int q = jp[d][vacancy[t]];
		state[q] = state[p];
		state[p] = 0;
		state2[q] = state2[p];
		state2[p] = 0;
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
			if(t == tMax) {
				int length = getLength();
				if(length > maxLength) {
					maxLength = length;
					System.out.println(maxLength);
					printSolution();
				}
			}
		}
	}
	
	int getLength() {
		int length = 1;
		int p = 0;
		int d = 0;
		while(true) {
			int q = jp[d][p];
			if(q < 0 || state[q] == 0 || state[q] == 6) break;
			int s = state[q];
			d = transition[d][state[q]];
			if(d < 0) break;
			p = q;
			length++;
		}		
		return length;
	}
	
	void printSolution() {
		System.out.println("State");
		for (int i = 0; i < size; i++) {
			System.out.print(" " + state[i]);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
		System.out.println("Path");
		System.out.print(maxLength + ":");
		for (int i = 0; i < t; i++) {
			System.out.print(designation.charAt(moved[i]));
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		Pipeline p = new Pipeline();
		p.setWidth();
		p.solve();
	}

}

//19:OKLHGCBFJLKOHKONMIEJLMI