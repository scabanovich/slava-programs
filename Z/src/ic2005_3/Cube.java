package ic2005_3;

public class Cube {
	CubeStates cubeStates = new CubeStates();
	CubeField field = new CubeField();
	
	int[] fieldSettings = {
		 0, 1, 2, 3, 4, 5,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1, 2, 2, 2,-1,-1, 3,-1,
		 1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1, 4,-1,-1, 5,-1,
		-1,-1,-1,-1,-1,-1,-1, 0,-1,
		-1,-1, 2,-1,-1,-1,-1,-1,-1
	};

	int size;

	int[] place;
	int[] visited;
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public Cube() {
		field.setSize(9, 7);
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		size = field.getSize();
		place = new int[size];
		visited = new int[size];
		state = new int[size];
		for (int i = 0; i < size; i++) {
			visited[i] = -1;
			state[i] = -1;
		}
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][4];
		t = 0;
		place[0] = 0;
		visited[0] = 0;
		state[0] = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		int df = getForceDirection();
		int dmin = (df >= 0) ? df : 0;
		int dmax = (df >= 0) ? df + 1 : 4;
		for (int d = dmin; d < dmax; d++) {
			int q = field.jump(place[t], d);
			if(q < 0 || state[q] >= 0) continue;
			int s = cubeStates.roll(state[place[t]], d);
			if(check(q, s)) {
				ways[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
	}
	
	int getForceDirection() {
		for (int d = 0; d < 4; d++) {
			int q = field.jump(place[t], d);
			if(q < 0 || state[q] >= 0 || q == size - 1) continue;
			int z = 0;
			for (int d1 = 0; d1 < 4; d1++) {
				int q1 = field.jump(q, d1);
				if(q1 >= 0 && state[q1] < 0) ++z;
			}
			if(z < 2) return d;
		}
		return -1;
	}
	
	boolean check(int q, int s) {
		if(fieldSettings[q] < 0) return true;
		for (int i = 0; i < size; i++) {
			if(state[i] < 0 || fieldSettings[i] < 0) continue;
			if ((fieldSettings[i] == fieldSettings[q]) != 
								(cubeStates.getUpperFace(state[i]) == cubeStates.getUpperFace(s))) {
				return false;
			}
		}		
		return true;
	}
	
	void move() {
		int d = ways[t][way[t]];
		int p = field.jump(place[t], d);
		place[t + 1] = p;
		visited[p] = t + 1;
		int s = cubeStates.roll(state[place[t]], d);
		state[p] = s;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		int p = field.jump(place[t], d);
		visited[p] = -1;
		state[p] = -1;
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
//				System.out.println(t);
			}
			if(t == size - 1) {
				System.out.println("solution");
				printSolution();
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < size; i++) {
			int u = cubeStates.getUpperFace(state[i]);
			System.out.print(" " + u);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < size; i++) {
			char c = (char) ((97 + (visited[i] % 26)));
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		int turnCount = 0;
		for (int i = 1; i < t; i++) {
			if(ways[i][way[i]] != ways[i - 1][way[i - 1]]) ++turnCount;
		}
		System.out.println("turns = " + turnCount);
	}

	public static void main(String[] args) {
		Cube p = new Cube();
		p.solve();
	}

}

//38