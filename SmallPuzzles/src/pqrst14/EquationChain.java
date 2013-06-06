package pqrst14;

public class EquationChain {
	
	int[] digits = {1,2,3,4,5,6,7,8,9};
	Operation[] operations = new Operation[]{
		new ProductOperation(2),
		new DivisionOperation(5),
		new DivisionOperation(2),
		new SumOperation(5),
		new SumOperation(4),
		new SumOperation(3),
		new SumOperation(1),
		new SumOperation(-7),
		new SumOperation(-3)
	};
	
	int[] usedOperations;
	int[] usedNumbers;
	
	int[] state;

	int t;	
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[operations.length + 1];
		way = new int[operations.length + 1];
		ways = new int[operations.length + 1][operations.length];
		
		state = new int[operations.length + 1];
		usedNumbers = new int[operations.length];
		usedOperations = new int[operations.length];
		state[0] = 1;
		usedNumbers[0] = 1;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == operations.length) return;
		for (int i = 0; i < operations.length; i++) {
			if(usedOperations[i] > 0) continue;
			int next = operations[i].compute(state[t]);
			int index = getIndex(next);
			if(index < 0) continue;
			if(t == operations.length - 1) {
				if(next != state[0]) continue;
			} else {
				if(usedNumbers[index] > 0) continue;
			}
			ways[t][wayCount[t]] = i;
			wayCount[t]++;			
		}
	}
	
	int getIndex(int c) {
		for (int i = 0; i < digits.length; i++) {
			if(digits[i] == c) return i;
		}
		return -1;
	}
	
	void move() {
		int op = ways[t][way[t]];
		usedOperations[op] = 1;
		int next = operations[op].compute(state[t]);
		int index = getIndex(next);
		if(t == operations.length - 1) {
		} else {
			usedNumbers[index] = 1;
			state[t + 1] = next;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int op = ways[t][way[t]];
		usedOperations[op] = 0;
		int next = operations[op].compute(state[t]);
		int index = getIndex(next);
		if(t == operations.length - 1) {
		} else {
			usedNumbers[index] = 0;
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
			if(t == operations.length) {
				printSolution();
			}
		}
	}
	
	void printSolution() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < t; i++) {
			int op = ways[i][way[i]];
			sb.append(operations[op].toString()).append(',');
		}
		System.out.println(sb.toString());
		sb.setLength(0);
		for (int i = 0; i < t; i++) {
			sb.append(state[i]);
			if(i < t - 1) sb.append(',');
		}
		System.out.println(sb.toString());
	}

}

abstract class Operation {
	public abstract int compute(int c);
}
class SumOperation extends Operation {
	int d;
	public SumOperation(int d) {
		this.d = d;
	}
	public int compute(int c) {
		return c + d;
	}
	
	public String toString() {
		return d >= 0 ? "+" + d : "" + d;
	}
}
class ProductOperation extends Operation {
	int d;
	public ProductOperation(int d) {
		this.d = d;
	}
	public int compute(int c) {
		return c * d;
	}
	public String toString() {
		return "*" + d;
	}
}
class DivisionOperation extends Operation {
	int d;
	public DivisionOperation(int d) {
		this.d = d;
	}
	public int compute(int c) {
		if(c % d != 0) return Integer.MIN_VALUE;
		return c / d;
	}
	public String toString() {
		return "/" + d;
	}
}

//+5,/2,+4,-3,*2,+1,-7,+3,/5,
//1,6,3,7,4,8,9,2,5
