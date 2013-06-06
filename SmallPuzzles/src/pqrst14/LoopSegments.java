package pqrst14;

public class LoopSegments {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] jp;

	int[] points = new int[]{
		0,0,0,0,0,0,0,0,
		0,0,2,0,0,0,7,0,
		0,0,0,0,0,0,0,0,
		0,0,0,4,0,0,0,0,
		0,0,3,0,2,0,4,0,
		3,5,2,0,2,0,0,0,
		0,0,0,0,0,4,0,0,
		0,0,6,0,0,0,0,0,
	};
	
	int[] state;
	int[] visible;
	int[] place;	

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public LoopSegments() {
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
		visible = new int[size];
		state = new int[size];
		
		place[0] = width * (height - 1);
		state[place[0]] = 1;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t > 0 && place[t] == place[0]) return;
		if(t > 0 && (!check() || !check1())) return;
		int md = t == 0 ? 2 : 4;
		for (int d = 0; d < md; d++) {
			int n = jp[d][place[t]];
			if(n < 0 || (state[n] > 0 && (n != place[0] || t < 6))) continue;
			ways[t][wayCount[t]] = d;
			wayCount[t]++;
		}
	}
	
	boolean check1() {
		for (int i = 0; i < size; i++) {
			if(points[i] != 0 && state[i] == 0) {
				int h = 0;
				for (int d = 0; d < 4; d++) {
					int q = jp[d][i];
					if(q >= 0 && (state[q] == 0 || q == place[t]) || q == place[0]) ++h;
				}
				if(h < 2) return false;
			}
		}
		return true;
	}
	
	boolean check() {
		int t1 = t;
		int d = (t1 == 0) ? -1 : ways[t1 - 1][way[t1 - 1]];
		boolean direct = true;
		while(true) {
			int p = place[t1];
			if(direct) {
				if(points[p] != 0 && points[p] < visible[p]) return false;
			} else {
				if(points[p] != 0 && points[p] != visible[p]) return false;
			}
			if(d < 0) return true;
			t1--;
			if(t1 < 0) return true;
			int d1 = (t1 <= 0) ? -1 : ways[t1][way[t1]];
			if(d != d1) {
				if(!direct) return true;
				direct = false;
				d = d1;
			}
		}
	}
	
	boolean finalCheck() {
		for (int i = 0; i < size; i++) {
			if(points[i] != 0 && points[i] != visible[i]) return false;
		}
		return true;
	}
	
	void move() {
		int d = ways[t][way[t]];
		int p = jp[d][place[t]];
		place[t + 1] = p;
		if(p != place[0]) state[p] = t + 2;
		int direct = 0;
		int t1 = t;
		while(true) {
			int q = place[t1];
			visible[q]++;
			direct++;
			--t1;
			int d1 = (t1 < 0) ? -1 : ways[t1][way[t1]];
			if(d1 != d) break;
		}
		visible[p] += direct;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		int p = place[t + 1];
		if(p != place[0]) state[p] = 0;
		int t1 = t;
		int direct = 0;
		while(true) {
			int q = place[t1];
			visible[q]--;
			direct++;
			--t1;
			int d1 = (t1 < 0) ? -1 : ways[t1][way[t1]];
			if(d1 != d) break;
		}
		visible[p] -= direct;
	}
	
	void anal() {
		//int tm = 2;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(place[t] == place[0] && finalCheck()) {
				System.out.println("solution");
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("");
		for (int i = 0; i < size; i++) {
			char c = (char)(97 + ((state[i] - 1) % 26));
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < size; i++) {
			System.out.print(" " + visible[i]);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");

		int q = 0;		
		for (int i = 0; i < size; i++) {
			if(state[i] == 0) ++q;
			if(x[i] == width - 1) {
				if(y[i] > 0) System.out.print(",");
				System.out.print(q);
				q = 0;
			}
		}
		System.out.println("");
		
	}

}
/*
solution

2,1,4,2,1,1,2,1

 ` ` q p o n m l
 ` s r y z a b k
 ` t ` x ` ` c j
 ` u ` w v u d i
 ` v o p s t e h
 x w n q r ` f g
 a ` m l k j i `
 b c d e f g h `

 0 0 6 5 5 5 5 10
 0 5 2 5 3 3 7 5
 0 4 0 2 0 0 4 5
 0 4 0 4 2 3 4 5
 0 4 3 2 2 2 4 5
 2 5 2 2 2 0 5 6
 1 0 6 4 4 4 5 0
 7 6 6 6 6 6 7 0


*/