package champukr;

public class DominoCubeFiller {
	interface Listener {
		public void onSolutionFound(int[] state);
	}
	DominoCubeField field;
	int magicSum = 12;
	int maxNumber = 6;
	
	Listener listener;
	
	int[] state;
	int[] numberUsage; 
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public DominoCubeFiller() {}
	
	public void setField(DominoCubeField f) {
		field = f;
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.surfaceSize];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		numberUsage = new int[maxNumber + 1];
		wayCount = new int[field.surfaceSize + 1];
		way = new int[field.surfaceSize + 1];
		ways = new int[field.surfaceSize + 1][maxNumber + 1];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.surfaceSize) return;
		int p = field.order[t];
		for (int c = 0; c <= maxNumber; c++) {
				//temp
				if(t == 0 && c != 5) continue;
			if(numberUsage[c] >= 8) continue;
			if(canAdd(p, c)) {
				ways[t][wayCount[t]] = c;
				wayCount[t]++;
			}
		}
	}
	
	boolean canAdd(int p, int c) {
		int[] eqs = field.surfaceIndexToEquations[p];
		for (int i = 0; i < eqs.length; i++) {
			if(!meets(p, c, field.equations[eqs[i]])) return false; 
		}
		return true;
	}
	
	boolean meets(int p, int c, int[] equation) {
		int sum = 0;
		int e = 0;
		for (int i = 0; i < equation.length; i++) {
			int v = state[equation[i]];
			if(v < 0) e++; else sum += v;
		}
		if(sum + c > magicSum) return false;
		if(sum + c + maxNumber * (e - 1) < magicSum) return false;
		return true;
	}
	
	void move() {
		int p = field.order[t];
		int c = ways[t][way[t]];
		numberUsage[c]++;
		state[p] = c;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = field.order[t];
		int c = ways[t][way[t]];
		numberUsage[c]--;
		state[p] = -1;
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
			if(t == state.length) {
				solutionCount++;
				onSolutionFound();
			}
		}
		
	}
	
	void onSolutionFound() {
		if(solutionCount == 1) {
			printSolution();
		} else if(solutionCount % 1000 == 0) {
			System.out.println("Magic solution count=" + solutionCount);
///			printSolution();
		}
		listener.onSolutionFound(state);
	}
	
	void printSolution() {
		for (int p = 0; p < t; p++) {
			int c = state[p];
			System.out.print(" " + c);
		}
		System.out.println("");
	}

}
