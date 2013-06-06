package ic2006_3;

public class CyclicExpressionSolver {
	public interface Checker {
		public boolean partialCheck(CyclicExpressionSolver solver);
		public boolean finalCheck(CyclicExpressionSolver solver);
	}
	public interface SolutionListener {
		public void solutionFound(CyclicExpressionSolver solver);
	}
	int numberCount = 10;
	
	int[] initialState;
	Checker checker;
	SolutionListener listener;
	
	int[] state;
	int[] usedSigns;
	int[] usedNumbers;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int sum1;
	int sum2;
	
	int solutionCount;

	public CyclicExpressionSolver() {}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void setChecker(Checker c) {
		checker = c;
	}
	
	public void setListener(SolutionListener l) {
		listener = l;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[2 * numberCount];
		for (int p = 0; p < state.length; p++) state[p] = -1;
		
		usedSigns = new int[4];
		usedNumbers = new int[numberCount + 1];

		wayCount = new int[2 * numberCount + 1];
		way = new int[500];
		ways = new int[500][16];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 2 * numberCount) return;
		if(!partialCheck()) return;
		if(!checker.partialCheck(this)) return;
		if(t % 2 == 0) {
			for (int i = 1; i <= numberCount; i++) {
				if(usedNumbers[i] > 0) continue;
				if(initialState[t] >= 0 && initialState[t] != i) continue;
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		} else {
			for (int i = 0; i < 4; i++) {
				if(usedSigns[i] >= 3) continue;

				// same operators cannot follow one another.
				if(t > 2 && i == ways[t - 2][way[t - 2]]) continue;
				if(t == 2 * numberCount - 1 && i == ways[1][way[1]]) continue;
				
				// take a value from initial state, if it is set.
				if(initialState[t] >= 0 && initialState[t] != i) continue;

				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	boolean partialCheck() {
		if(t % 2 == 0) return true; // no last number
		int a = state[0];
		int i = 2;
		while(i < t) {
			int sign = state[i - 1];
			int b = state[i];
			a = compute(a, sign, b);
			i += 2;
			if(a < 0) return false;
		}
		return true;
	}

	int compute(int a, int sign, int b) {
		return (sign == 0) ? a + b :
			   (sign == 1) ? a - b :
			   (sign == 2) ? a * b :
			   (a % b == 0) ? a / b :
				   -1;
	}
	
	void move() {
		int i = ways[t][way[t]];
		state[t] = i;
		if(t % 2 == 0) {
			usedNumbers[i]++;
		} else {
			usedSigns[i]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		state[t] = -1;
		if(t % 2 == 0) {
			usedNumbers[i]--;
		} else {
			usedSigns[i]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) {
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(t == 2 * numberCount && check()) {
				solutionCount++;
				listener.solutionFound(this);
				//printSolution();
			}
		}
	}
	
	void computeSums() {
		sum1 = state[0];
		for (int i = 0; i < numberCount; i++) {
			int s = state[2 * i + 1];
			int b = state[(2 * i + 2) % (2 * numberCount)];
			sum1 = compute(sum1, s, b);
			if(sum1 < 0) break;
		}
		sum2 = state[numberCount];
		for (int i = 0; i < numberCount; i++) {
			int s = state[(numberCount + 2 * i + 1) % (2 * numberCount)];
			int b = state[(numberCount + 2 * i + 2) % (2 * numberCount)];
			sum2 = compute(sum2, s, b);
			if(sum2 < 0) break;
		}
	}
	
	boolean check() {
		computeSums();
		return checker.finalCheck(this);
	}
	
	static char[] SIGNS = {'+', '-', '*', '/'};
	
	static void printState(int[] state) {
		for (int i = 0; i < state.length; i++) {
			String ch = "-";
			if(i % 2 == 0) {
				ch = "" + state[i];
			} else {
				ch = "" + SIGNS[state[i]];
			}
			System.out.print(" " + ch);
		}
		System.out.println("");
	}

}
