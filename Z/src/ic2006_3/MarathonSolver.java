package ic2006_3;

import com.slava.common.TwoDimField;

public class MarathonSolver {
	TwoDimField field;
	
	int[] state;
	
	int[] visited;
	int unvisitedCount;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int maxScore = - 20;
	int treeCount;

	public MarathonSolver() {}
	
	public void setField(TwoDimField field) {
		this.field = field;
	}
	
	public void setState(int[] state) {
		this.state = state;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		place = new int[500];
		wayCount = new int[500];
		way = new int[500];
		ways = new int[500][16];
		visited = new int[field.getSize()];
		unvisitedCount = field.getSize();
		t = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t > 1 && t % 6 == 1) {
			if(!isGoodEquation()) return;
		}
		if(t == 0) {
			for (int p = 0; p < field.getSize(); p++) {
				if((field.getX(p) + field.getY(p)) % 2 == 0) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		} else {
			int p = place[t - 1];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0) continue;
				if(isCrossingItself(q)) continue;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
	}
	boolean isCrossingItself(int q) {
		int w = t % 6;
		if(w == 0) w = 6;
		for (int r = t - w; r < t; r++) {
			if(place[r] == q) return true;
		}
		return false;		
	}
	
	boolean isGoodEquation() {
		return computeLast() == (t / 6) - 1;
	}
	
	int computeLast() {
		int[] ns = {state[place[t - 7]], state[place[t - 5]], state[place[t - 3]], state[place[t - 1]]};
		int[] sg = {state[place[t - 6]], state[place[t - 4]], state[place[t - 2]]};
		int res = ns[0];
		for (int k = 0; k < 3; k++) {
			res = compute(res, sg[k], ns[k + 1]);
			if(res == Integer.MIN_VALUE) return Integer.MIN_VALUE;
		}
		return res;
	}
	
	int compute(int a, int s, int b) {
		if(s == 0) {
			return a + b;
		} else if(s == 1) {
			return a - b;
		} else if(s == 2) {
			return a * b;
		} else if(a % b != 0) {
			return Integer.MIN_VALUE;
		} else {
			return a / b;
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		place[t] = p;
		visited[p]++;
		if(visited[p] == 1) unvisitedCount--;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		if(visited[p] == 1) unvisitedCount++;
		visited[p]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) {
//					if(treeCount > 100000) {
//						System.out.println(treeCount);
//					}
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
//				if(treeCount > 50000) return;
				if(treeCount > 5000) return;
			}
			if(t % 6 == 1 && t > 1 && isGoodEquation()) {
				int score = getScore();
				if(score > maxScore) {
					maxScore = score;
					System.out.println("score=" + score + " treeCount=" + treeCount);
					printSolution();
				}
			}
		}
	}
	
	int getScore() {
		int m = (t / 6 - 18) * 3;
		if(m > 0) m += unvisitedCount * 8;
		return m;
	}
	
	char[] SIGNS = {'+', '-', '*', '/'};

	void printSolution() {
		for (int p = 0; p < field.getSize(); p++) {
			boolean b = ((field.getX(p) + field.getY(p)) % 2 == 0);
			String ch = (b) ? "" + state[p] : "" + SIGNS[state[p]];
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < t; i++) {
			if(i % 2 == 1) continue;
			int p = place[i];
			System.out.print(" " + state[p]);
		}
		System.out.println("");
		for (int i = 0; i < t; i++) {
			int p = place[i];
			if(i % 2 == 1) {
				System.out.print(SIGNS[state[p]]);
			} else {
				System.out.print(state[p]);
			}
		}
		System.out.println("");
	}
	
}
