package ic2006_2;

public class LogodromSolver {
	LogodromWords words;
	LogodromField field;
	
	int[] reqState;
	
	int[] state;
	
	int[] place;
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int bestScore;
	
	public LogodromSolver() {}
	
	public void setWords(LogodromWords w) {
		words = w;
	}
	
	public void setField(LogodromField field) {
		this.field = field;
	}
	
	public void setReqState(int[] rs) {
		reqState = rs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		place = new int[field.getSize()];
		
		wayCount = new int[state.length];
		way = new int[state.length];
		ways = new int[state.length][state.length];
		
		t = 0;
		bestScore = 20;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.sequenceSize) return;
		if(words.wordBeginnings[t] > 0 && !checkContinuouseness()) return;

//		int ec = getEstimatedCompleteness();
//		if(t > 50 && ec < 34) return;
//		if(t < 50) {
//			if(t > 23 && ec < 28) return;
//			if(t > 30 && ec < 34) return;
//			if(t > 35 && ec < 42) return;
//		}

		if(!checkReq()) return;
///		if(getEstimatedScore() < bestScore) return;
		
		if(words.wordBeginnings[t] > 0) {
			for (int p = 0; p < field.getSize(); p++) {
				if(field.filter[p] > 0) continue;
				if(state[p] >= 0) continue;
				add(p);
			}
		} else {
			int p = place[t - 1];
			for (int di = 0; di < field.hvDirections.length; di++) {
				int d = field.hvDirections[di];
				int q = field.jump(p, d);
				if(q < 0 || field.filter[q] > 0 || state[q] >= 0) continue;
				add(q);
			}
		}
		if(t < 40) randomize();
	}
	
	void add(int p) {
		if(reqState != null && reqState[p] >= 0 && reqState[p] != words.numericSequence[t]) return;
		ways[t][wayCount[t]] = p;
		wayCount[t]++;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i != j) {
				int k = ways[t][i];
				ways[t][i] = ways[t][j];
				ways[t][j] = k;
			}
		}
//		if(t < 10 && wayCount[t] > 3) wayCount[t] = 3;
	}
	
	void move() {
		int p = ways[t][way[t]];
		place[t] = p;
		state[p] = words.numericSequence[t];
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		int q = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			++q;
			if(q == 100000000) {
				q = 0;
				System.out.println("-->" + getPercent());
			}
			if(t > tm) {
				tm = t;
				System.out.println(tm);
			}
			if(t == words.sequenceSize) {
				int score = getScore();
				if(score > bestScore) {
					bestScore = score;
					printSolution();
				}
			}
		}		
	}
	
	int getEstimatedScore() {
		int c = 0;
		int stack = 0;
		int[] ls = new int[words.letters.length];
		for (int i = 0; i < field.rows.length; i++) {
			int lt = getIncompleteRowLetter(field.rows[i]);
			if(lt < -1) continue;
			if(lt < 0) stack += field.rows[i].length;
			else ls[lt] += field.rows[i].length;
		}
		for (int i = 0; i < ls.length; i++) {
			if(words.statistics[i] < 4) continue;
			if(ls[i] > words.statistics[i]) ls[i] = words.statistics[i];
			c += ls[i];
			int left = words.statistics[i] - ls[i];
			if(left > stack) left = stack;
			c += left;
			stack -= left;			
		}
		return c;
	}
	int getIncompleteRowLetter(int[] row) {
		int c = -1;
		for (int i = 0; i < row.length; i++) {
			int s = state[row[i]];
			if(s < 0) continue;
			if(c < 0) {
				c = s;
				if(words.statistics[c] < row.length) return -2;
			} else {
				if(s != c) return -2;
			}
		}
		return c;
	}

	int getEstimatedCompleteness() {
		int c = 0;
		int[] ls = new int[words.letters.length];
		int[] cs = new int[words.letters.length];
		for (int i = 0; i < field.rows.length; i++) {
			int ci = getRowCompleteness(field.rows[i]);
			if(ci < 3) continue;
			int lt = getIncompleteRowLetter(field.rows[i]);
			ls[lt] += field.rows[i].length;
			cs[lt] += ci;
		}
		for (int i = 0; i < ls.length; i++) {
			if(ls[i] > words.statistics[i]) {
				c += (ls[i] + cs[i]) * words.statistics[i] / ls[i];
			} else {
				c += ls[i] + cs[i];
			}
		}
		return c;
	}
	int getRowCompleteness(int[] row) {
		int c = -1;
		int v = 0;
		for (int i = 0; i < row.length; i++) {
			int s = state[row[i]];
			if(s < 0) continue;
			if(c < 0) {
				c = s;
				v = 1;
				if(words.statistics[c] < row.length) return 0;
			} else {
				if(s != c) return 0;
				v++;
			}
		}
		return v;
	}

	int getScore() {
		int c = 0;
		for (int i = 0; i < field.rows.length; i++) {
			c += getCompletedRowInput(field.rows[i]);
		}
		return c;
	}
	
	int getCompletedRowInput(int[] row) {
		int c = state[row[0]];
		if(c < 0) return 0;
		for (int i = 1; i < row.length; i++) {
			if(state[row[i]] != c) return 0;
		}
		return row.length;
	}
	
	void printSolution() {
		System.out.println("Solution " + bestScore);
		for (int p = 0; p < state.length; p++) {
			char c = state[p] < 0 || field.filter[p] > 0
			? '-' : words.letters[state[p]]; 
			System.out.print(" " + c);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	double getPercent() {
		double s = 0;
		double p = 1;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}
		return s;
	}
	
	int[] vector = new int[100];
	
	boolean checkContinuouseness() {
		int[] s = (int[])state.clone();
		for (int i = 0; i < s.length; i++) {
			if(field.filter[i] > 0 || s[i] >= 0) continue;
			int v = 1;
			int c = 0;
			vector[0] = i;
			s[i] = 100;
			while(c < v) {
				int p = vector[c];
				for (int di = 0; di < field.hvDirections.length; di++) {
					int d = field.hvDirections[di];
					int q = field.jump(p, d);
					if(q < 0 || field.filter[q] > 0 || s[q] >= 0) continue;
					s[q] = 100;
					vector[v] = q;
					v++;
				}
				c++;
			}
			if(v < 5) return false;			
		}
		return true;
	}
	
	boolean checkReq() {
		if(reqState == null) return true;
		int[] h = new int[words.letters.length];
		for (int p = 0; p < state.length; p++) {
			if(reqState[p] >= 0) {
				h[reqState[p]]++;
			} else if(state[p] >= 0) {
				h[state[p]]++;
			}
		}
		for (int i = 0; i < h.length; i++) {
			if(h[i] > words.statistics[i]) return false;
		}
		return true;
	}

}
