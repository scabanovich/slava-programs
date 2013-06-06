package parensesis;

public class ParensesisIterator {
	static int PRODUCT = 0;
	static int DIVISION = 1;
	static int SUM = 2;
	static int SUBTRACTION = 3;
	// *, /, +, -
	static int[][] PRECEDENCE = { // [next][previous]
		{0,1,1,1},
		{0,1,1,1},
		{1,1,0,1},
		{1,1,0,1}
	};
	
	int[] operations;
	
	int[] used;
	int[] permutation;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	boolean finished;
	
	public void setOperations(int[] operations) {
		this.operations = operations;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		permutation = new int[operations.length];
		used = new int[operations.length];
		wayCount = new int[operations.length + 1];
		way = new int[operations.length + 1];
		ways = new int[operations.length + 1][operations.length];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		int i0 = 0;
		if(t > 0) {
			if(!canFinish()) return;
			int k = permutation[t - 1];
			i0 = k + 1;
			int kp = k - 1;
			while(kp >= 0 && used[kp] == 1) --kp;
			if(kp >= 0) {
				int o1 = operations[permutation[t - 1]];
				int o2 = operations[kp];
				if(PRECEDENCE[o1][o2] == 1) i0 = kp;
			}
		}
		for (int i = operations.length - 1; i >= i0; i--) {
			if(used[i] == 1) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	boolean canFinish() {
		if(t == operations.length) return true;
		int n = (t == 0) ? 0 : permutation[t - 1];
		boolean result = false;
		while(true) {
			int q = n;
			while(q >= 0 && used[q] > 0) --q;
			if(q < 0) {
				result = true;
				break;
			}
			if(PRECEDENCE[operations[n]][operations[q]] == 1) {
				n = q;
			} else {
				q = n;
				while(q < operations.length && used[q] > 0) ++q;
				if(q == operations.length) {
					break;
				}
				n = q;
			}
			if(n == 0) {
				result = true;
			}
			used[n] = 2;
		}
		
		for (int i = 0; i < operations.length; i++) if(used[i] == 2) used[i] = 0;
		return result;
	}
	
	void move() {
		int k = ways[t][way[t]];
		permutation[t] = k;
		used[k] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = ways[t][way[t]];
		used[k] = 0;
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
			if(t == operations.length) {
				solutionCount++;
				onSolutionFound();
				if(finished) return;
			}
		}
	}
	
	int maxValue = 0;
	int[] achieved = new int[3000];
	int achievedCount = 0;
	void onSolutionFound() {
		Calculation c = new Calculation();
		int[] numbers = new int[operations.length + 1];
		for (int i = 0; i < numbers.length; i++) numbers[i] = 1;
		c.setNumbers(numbers);
		c.setOperations(operations);
		c.setOrder(permutation);
		int res = c.compute();
		if(res < achieved.length && achieved[res] == 0) {
			achieved[res] = 1;
			achievedCount++;
			System.out.println("" + achievedCount + ":" + res);
		}
		if(res == 4007) {
			setFinished();
			printPermutation();
		}
		if(res > maxValue) {
			maxValue = res;
			System.out.println(maxValue);
// 1 4 6 7 5 9 10 8 12 11 13 15 16 14 18 19 17 3 2 21 22 20 0
		}
	}
	
	void printPermutation() {
		for (int i = 0; i < permutation.length; i++) {
			System.out.print(" " + permutation[i]);
		}
		System.out.println("");
	}
	
	public void setFinished() {
		finished = true;
	}

	public static void main(String[] args) {
		ParensesisIterator p = new ParensesisIterator();
		p.setOperations(makeTestOperations(7));
		p.solve();
		System.out.println("Permutation count=" + p.solutionCount);
	}
	
	static int[] makeTestOperations(int q) {
		int n = 3 * q + 2;
		int[] res = new int[n];
		for (int i = 0; i < n; i++) res[i] = SUM;
		for (int i = 0; i < q; i++) res[3 * i + 2] = PRODUCT;
		return res;
	}
}
