package champ2013;

import java.util.Random;

import com.slava.common.RectangularField;

public class ThreeDigitNumbers {
	int[] lims = { 0,  0,  1,  2,  3,  4,  5,  6,
			       7, 11, 16, 21, 26, 31, 36, 40,
			      42, 47, 53, 59, 65, 71, 77, 82,
			      84, 89, 95,101,107,113,119,124,
			     126,131,137,143,149,155,161,166,
			     168,173 - 1,179 - 2,185 - 2,191 - 3,197 - 3,203 - 4,208 - 4};
	RectangularField field;

	int[] transformation = new int[]{0,2,5,7,1,3,4,8,9,6};

	int[] numberUsage = new int[999];
	int differentNumbers = 0;

	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int treeCount = 0;
	int solutionCount = 0;
	
	public ThreeDigitNumbers() {
		transformation = new int[]{0,1,2,3,4,5,6,7,8,9};
		for (int i = 1; i < transformation.length - 1; i++) {
			int j = i + seed.nextInt(transformation.length - i);
			int c = transformation[i];
			transformation[i] = transformation[j];
			transformation[j] = c;
		}
		System.out.println("Transformation: ");
		for (int i = 1; i < transformation.length; i++) {
			System.out.print(" " + transformation[i]);
		}
		System.out.println("");
	}

	public void setField(RectangularField f) {
		field = f;
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			state[i] = -1;
		}
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][10];

		t = 0;
		treeCount = 0;
	}

	int[] ws = new int[10];
	int[] cs = new int[10];

	void srch() {
		wayCount[t] = 0;
		if(t == state.length || isSolution() || t > 80) return;
		if(t > 0 && t < 38 && t < lims.length && differentNumbers < lims[t - 1]) return;
		if(t == 16 && differentNumbers < 118) {
			return;
		}
		if(t == 20 && differentNumbers < 154) {
			return;
		}
		if(t == 50 && differentNumbers < 190) {
			return;
		}
		if(t == 60 && differentNumbers < 220) {
			return;
		}
		int p = t; // use form and compute next p;
		int c = 0;
		for (int v = 1; v <= 9; v++) {
			if(t > 50 || !hasNeighbour(p, v)) {
				cs[c] = count(p, v);
//				if(t < 38 && cs[c] < countWays(p)) continue;
				ws[c] = v;
				c++;
			}
		}
		randomize0(c);
		sort(c);
		if(t < 12) {
			if(c > 2) c = 2;
		} else if(t < 25) {
			if(c > 3) c = 3;
		} else {
			if(c > 4) c = 4;
		}
		for (int i = 0; i < c; i++) {
			ways[t][i] = ws[i];
		}
		wayCount[t] = c;
		randomize();
		if(wayCount[t] > 4) wayCount[t] = 4;
	}

	void sort(int c) {
		for (int i = 1; i < c; i++) {
			int j = i;
			while(j > 0 && cs[j] > cs[j - 1]) {
				int q = cs[j]; cs[j] = cs[j - 1]; cs[j - 1] = q;
				q = ws[j]; ws[j] = ws[j - 1]; ws[j - 1] = q;
				j--;
			}
		}
	}

	Random seed = new Random();

	void randomize0(int c) {
		for (int i = 0; i < c - 1; i++) {
			int j = i + seed.nextInt(c - i);
			int q = cs[j]; cs[j] = cs[i]; cs[i] = q;
			q = ws[j]; ws[j] = ws[i]; ws[i] = q;
		}
	}

	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t] - 1; i++) {
			int j = i + seed.nextInt(wayCount[t] - i);
			int c = ways[t][i]; ways[t][i] = ways[t][j]; ways[t][j] = c;
		}
	}

	boolean hasNeighbour(int p, int v) {
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && state[q] == v) {
				return true;
			}
			for (int d1 = 0; d1 < 4; d1++) {
				int r = field.jump(q, d1);
				if(r != p && r > 0 && state[r] == v) return true;
			}
		}
		return false;
	}

	int countWays(int p) {
		int result = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || state[q] < 0) {
				continue;
			}
			for (int d1 = 0; d1 < 4; d1++) {
				int r = field.jump(q, d1);
				if(r >= 0 && state[r] >= 0 && r != p) result++;
			}
		}
		return result;
		
	}

	class Counter {
		public final void run(int p, int v) {
			start();
				if(p > 0 && p < field.getWidth()) {
					int q = field.jump(p, 2);
					int vc = state[q];
					int v1 = transform(v);
					if(v != v1 && v != vc && v1 != vc) {
						int n = getNumber(vc, v, v1);
						found(n);
					}
					v1 = transform(vc);
					if(v != v1 && v != vc && v1 != vc) {
						int n = getNumber(v, vc, v1);
						found(n);
					}
				} else if(field.getY(p) == 1) {
					int q = field.jump(p, 3);
					int vc = state[q];
					int v1 = transform(vc);
					if(v != v1 && v != vc && v1 != vc) {
						int n = getNumber(v, vc, v1);
						found(n);
					}
				}			
			
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(wrong(q, v)) continue;
				for (int d1 = 0; d1 < 4; d1++) {
					int r = field.jump(q, d1);
					if(wrong(r, v) || r == p || state[q] == state[r]) continue;
					int n = getNumber(v, state[q], state[r]);
					found(n);
				}
			}
			int q = field.jump(p, 2);
			int r = field.jump(p, 3);
			if(wrong(q, v) || wrong(r, v) || state[q] == state[r]) {
			} else {
				int n = getNumber(state[q], v, state[r]);
				found(n);
			}
		}

		int getNumber(int v0, int v1, int v2) {
			return v1 * 10 + ((v0 < v2) ? v0 * 100 + v2 : v2 * 100 + v0);
		}

		private boolean wrong(int q, int v) {
			return q < 0 || state[q] < 0 || state[q] == v;
		}

		protected void start() {			
		}
	
		protected void found(int n) {
		}
	}

	VariantCounter variantCounter = new VariantCounter();

    class VariantCounter extends Counter {
    	int result = 0;
		protected void start() {
			result = 0;
		}
	
		protected void found(int n) {
			if(numberUsage[n] == 0) {
				result += 1;
			}
			int m = transform(n);
			if(numberUsage[m] == 0) {
				result += 1;
			}

//			result++;
		}
    }

	int count(int p, int v) {
		variantCounter.run(p, v);
		return variantCounter.result;
	}

	void move() {
		int p = t;
		int v = ways[t][way[t]];
		state[p] = v;
		add.run(p, v);
		++t;
		srch();
		way[t] = -1;
	}

	int transform(int n) {
		int i1 = transformation[n / 100];
		int i2 = transformation[n % 100 / 10];
		int i3 = transformation[n % 10];
		if(i1 > i3) {
			int c = i1;
			i1 = i3;
			i3 = c;
		}
		return i1 * 100 + i2 * 10 + i3;
	}

	Counter add = new Counter() {	
		protected void found(int n) {
			if(numberUsage[n] == 0) {
				differentNumbers++;
			}
			numberUsage[n]++;

			int m = transform(n);
			if(numberUsage[m] == 0) {
				differentNumbers++;
			}
			numberUsage[m]++;
		}
    };

	void back() {
		--t;
		int p = t;
		int v = ways[t][way[t]];
		remove.run(p, v);
		state[p] = -1;
	}

	Counter remove = new Counter() {	
		protected void found(int n) {
			int m = transform(n);
			if(numberUsage[m] == 1) {
				differentNumbers--;
			}
			numberUsage[m]--;

			if(numberUsage[n] == 1) {
				differentNumbers--;
			}
			numberUsage[n]--;
		}
    };

	void anal() {
		srch();
		way[t] = -1;
		int mdiff = 150;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
//				if(treeCount > 500000 && mdiff < 155) {
//					return;
//				}
				if(treeCount > 500000 && mdiff < 200) {
					return;
				}
				if(treeCount > 1000000 && mdiff < 225) {
					return;
				}
				if(treeCount > 240000000) {
					return;
				}
			}
			if(differentNumbers > mdiff) {
				mdiff = differentNumbers;
				System.out.println("-->" + mdiff + " " + t);
			}
			if(isSolution()) {
				printSolution();
				solutionCount++;
			}
		}
	}

	boolean isSolution() {
		return differentNumbers >= 252;
	}

	void printSolution() {
		for (int p = 0; p < state.length; p++) {
			String s = (state[p] < 0) ? "+" : "" + state[p];
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(8, 4);

		while(true) {
			ThreeDigitNumbers p = new ThreeDigitNumbers();
			p.setField(f);
			p.solve();
			if(p.solutionCount > 0) break;
		}
	}

}

/**

 3 4 6 5 9 4 6 9
 1 7 3 2 7 8 1 7
 5 8 9 4 5 6 9 5
 4 3 6 7 3 1 2 8
 8 5 2 9 8 4 7 6
 2 1 8 3 6 2 1 4
 5 4 9 2 4 3 9 7
 7 3 1 8 9 5 4 6
 2 6 5 3 1 8 7 5
 3 1 8 9 5 2 6 1

Transformation: 
 4 8 6 1 9 3 7 2 5
234 in 64

*/