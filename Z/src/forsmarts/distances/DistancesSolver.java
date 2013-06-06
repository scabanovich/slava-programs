package forsmarts.distances;

public class DistancesSolver {
	DistancesField field;
	int[] data;
	
	int[] state;
	int valueToPut;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] distances;
	
	int solutionCount;

	public DistancesSolver() {}
	
	public void setField(DistancesField field) {
		this.field = field;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void solve() {
		init();
		anal();
	}

	void init() {
		valueToPut = 19;
		int size = field.getSize();
		state = new int[size];
//		for (int i = 0; i < size; i++) state[i] = -1;
		wayCount = new int[valueToPut + 1];
		way = new int[valueToPut + 1];
		distances = new int[valueToPut + 1];
		ways = new int[size + 1][valueToPut];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(valueToPut == 0) return;
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] > 0 || data[p] != 1) continue;
			if(valueToPut >= 10) {
				int pp = field.jump(p, 2);
				if(pp < 0 || state[pp] > 0 || data[pp] != 1) continue;
			}
			if(t < 2) {
				ways[t][wayCount[t]] = p;
				wayCount[t]++;
			} else {
				int q = ways[t - 1][way[t - 1]];
				int d = field.getDistance(p, valueToPut >= 10, q, valueToPut >= 9);
				if(d < distances[t - 1]) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = valueToPut;
		if(valueToPut >= 10) {
			int pp = field.jump(p, 2);
			state[pp] = 1;
		}
		if(valueToPut == 9) {
			System.currentTimeMillis();
		}
		if(t > 0) {
			int q = ways[t - 1][way[t - 1]];
			int d = field.getDistance(p, valueToPut >= 10, q, valueToPut >= 9);
			distances[t] = d;
		}
		valueToPut--;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		valueToPut++;
		int p = ways[t][way[t]];
		state[p] = 0;
		if(valueToPut >= 10) {
			int pp = field.jump(p, 2);
			state[pp] = 0;
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
				System.out.println(t);
			}
			if(valueToPut == 0) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution found");
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + (state[i]));
			if(field.isRightBorder(i)) System.out.println("");
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
