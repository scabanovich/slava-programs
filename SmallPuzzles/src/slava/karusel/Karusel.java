package slava.karusel;

public class Karusel {
	int size;
	double[] cos;
	double[] sin;
	
	int[] state;
	
	int[] used;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setSize(int s) {
		size = s;
		cos = new double[size];
		sin = new double[size];
		for (int i = 0; i < size; i++) {
			double f = 2 * Math.PI / size * i;
			cos[i] = Math.cos(f);
			sin[i] = Math.sin(f);
		}
	}
	
	private boolean isBalanced(int[] weights) {
		double d = compute(weights, cos);
		if(Math.abs(d) > 0.0001) return false;
		d = compute(weights, sin);
		if(Math.abs(d) > 0.0001) return false;
		return true;
	}
	
	private double compute(int[] weights, double[] legs) {
		double s = 0;
		for (int i = 0; i < weights.length; i++) {
			s += weights[i] * legs[i];
		}
		return s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		used = new int[size];
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
			wayCount[t] = 1;
			ways[t][0] = 0;
		} else {
			for (int i = 0; i < size; i++) {
				if(used[i] != 0) continue;
				ways[t][wayCount[t]] = i;
				wayCount[t]++; 
			}
		}
	}
	
	void move() {
		int n = ways[t][way[t]];
		state[t] = n;
		used[n] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int n = ways[t][way[t]];
		state[t] = -1;
		used[n] = 0;
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
			if(t == size) {
				onTransmutatuionFound();
			}
		}
	}
	
	void onTransmutatuionFound() {
		if(isBalanced(state)) {
			solutionCount++;
			printState();
		}
	}
	
	void printState() {
		for (int i = 0; i < size; i++) {
			System.out.print(" " + (state[i] + 1));
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Karusel k = new Karusel();
		k.setSize(14);
		k.solve();
		System.out.println("SolutionCount=" + k.solutionCount);
//		int[] w = {2,4,3,5,1,6};
//		k.isBalanced(w);
	}

}
