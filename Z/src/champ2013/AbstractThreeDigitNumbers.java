package champ2013;

import java.util.Random;

import com.slava.common.RectangularField;

public class AbstractThreeDigitNumbers {
	protected RectangularField field;

	protected int[] state;
	protected int[] numberUsage = new int[999];
	protected int differentNumbers = 0;

	protected int t;
	protected int[] place;
	protected int[] wayCount;
	protected int[] way;
	protected int[][] ways;

	protected int bestDiff = 242;

	protected int treeCount = 0;
	protected int solutionCount = 0;

	public AbstractThreeDigitNumbers() {}

	public void solve() {
		init();
		anal();
	}

	protected void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			state[i] = -1;
		}
		place = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][10];

		t = 0;
		treeCount = 0;
	}

	protected int[] ws = new int[10];
	protected int[] cs = new int[10];

	protected void srch() {
		
	}

	protected void randomize0(int c) {
		for (int i = 0; i < c - 1; i++) {
			int j = i + seed.nextInt(c - i);
			int q = cs[j]; cs[j] = cs[i]; cs[i] = q;
			q = ws[j]; ws[j] = ws[i]; ws[i] = q;
		}
	}

	protected void sort(int c) {
		for (int i = 1; i < c; i++) {
			int j = i;
			while(j > 0 && cs[j] > cs[j - 1]) {
				int q = cs[j]; cs[j] = cs[j - 1]; cs[j - 1] = q;
				q = ws[j]; ws[j] = ws[j - 1]; ws[j - 1] = q;
				j--;
			}
		}
	}

	protected void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		addWithSimmetric(p, v);
		++t;
		srch();
		way[t] = -1;
	}

	protected void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		removeWithSimmetric(p, v);
	}

	protected void addWithSimmetric(int p, int v) {
	}
	
	protected void removeWithSimmetric(int p, int v) {
	}

	protected void add(int p, int v) {
		state[p] = v;
		for (int d1 = 0; d1 < 4; d1++) {
			int q1 = field.jump(p, d1);
			if(wrong(q1, v)) continue;
			for (int d2 = 0; d2 < 4; d2++) {
				int q2 = field.jump(q1, d2);
				if(wrong(q2, v) || q2 == p || state[q2] == state[q1]) continue;
				addNumber(getNumber(v, state[q1], state[q2]));
			}
			for (int d2 = 0; d2 < 4; d2++) {
				if(d2 == d1) continue;
				int q2 = field.jump(p, d2);
				if(wrong(q2, v) || state[q2] == state[q1]) continue;
				addNumber(getNumber(state[q1], v, state[q2]));
			}
		}
	}	

	protected void remove(int p, int v) {
		for (int d1 = 0; d1 < 4; d1++) {
			int q1 = field.jump(p, d1);
			if(wrong(q1, v)) continue;
			for (int d2 = 0; d2 < 4; d2++) {
				int q2 = field.jump(q1, d2);
				if(wrong(q2, v) || q2 == p || state[q2] == state[q1]) continue;
				removeNumber(getNumber(v, state[q1], state[q2]));
			}
			for (int d2 = 0; d2 < 4; d2++) {
				if(d2 == d1) continue;
				int q2 = field.jump(p, d2);
				if(wrong(q2, v) || state[q2] == state[q1]) continue;
				removeNumber(getNumber(state[q1], v, state[q2]));
			}
		}
		state[p] = -1;
	}

	protected void addNumber(int n) {
		if(numberUsage[n] == 0) {
			differentNumbers++;
		}
		numberUsage[n]++;
	}

	protected void removeNumber(int n) {
		if(numberUsage[n] == 1) {
			differentNumbers--;
		}
		numberUsage[n]--;
	}

	private int getNumber(int v0, int v1, int v2) {
		return v1 * 10 + ((v0 < v2) ? v0 * 100 + v2 : v2 * 100 + v0);
	}

	private boolean wrong(int q, int v) {
		return q < 0 || state[q] < 0 || state[q] == v;
	}

	Random seed = new Random();

	protected void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t] - 1; i++) {
			int j = i + seed.nextInt(wayCount[t] - i);
			int c = ways[t][i]; ways[t][i] = ways[t][j]; ways[t][j] = c;
		}
	}

	protected void anal() {
//		bestDiff = 200;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(shouldBreak()) return;

			if(differentNumbers > bestDiff) {
				System.out.println(differentNumbers);
				bestDiff = differentNumbers;
				printSolution();
				solutionCount++;
				if(isSolution()) {
					return;
				}
			}
		}
	}

	protected boolean shouldBreak() {
		if(treeCount > 100000 && bestDiff < 246) {
			System.out.println("---");
			return true;
		}
		if(treeCount > 1000000) {
			System.out.println("---");
			return true;
		}
		return false;
	}

	protected boolean isSolution() {
		return differentNumbers == 252;
	}

	protected void printSolution() {
		for (int p = 0; p < state.length; p++) {
			String s = (state[p] < 0) ? "+" : "" + state[p];
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
		if(bestDiff > 230) {
			printUnused();
		}
	}

	protected void printUnused() {
		for (int n = 123; n < numberUsage.length; n++) {
			if(numberUsage[n] > 0) continue;
			int n1 = n / 100, n2 = (n / 10) % 10, n3 = n % 10;
			if(n1 >= n3 || n1 == n2 || n2 == n3 || n1 == 0 || n2 == 0 || n3 == 0) continue;
			System.out.print(" " + n);
		}
		System.out.println("");
	}

}
