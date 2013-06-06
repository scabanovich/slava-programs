package slava.math;

public class SustitutionsEnumerator {
	int length;
	
	int[] state;
	int[] used = new int[10];
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int solutionCount;
	
	Expression expression;
	char[] characters;
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	public void setCharacters(char[] cs) {
		this.characters = cs;
	}
	
	public void solve() {
		if(length > 10) throw new RuntimeException("Too many letters: " + length);
		init();
		anal();
	}
	
	void init() {
		state = new int[length];
		used = new int[10]; 
		wayCount = new int[length + 1];
		way = new int[length + 1];
		ways = new int[length + 1][10];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == length) return;
		for (int i = 0; i < 10; i++) {
			if(used[i] == 1) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	void move() {
		int k = ways[t][way[t]];
		used[k] = 1;
		state[t] = k;
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
			if(t == length) {
				onNextState();
			}
		}
	}
	
	void onNextState() {
		if(expression != null) {
			int v = expression.compute(state);
			if(v != 1) return;
			for (int i = 0; i < length; i++) {
				System.out.print("" + characters[i] + "=" + state[i] + "; ");
			}
			System.out.println("");
		}
		++solutionCount;
	}

}
