package champ2013;

import java.math.BigInteger;
import java.util.Random;

import com.slava.common.RectangularField;

public class LFilling {
	int[][][] figures = new int[][][] {
		{{0,0}, {1,0}, {1,1}, {1,2}},
		{{0,0}, {0,1}, {1,1}, {2,1}},
		{{0,0}, {0,1}, {-1,1}, {-2,1}},
		
		{{2,1}, {2,0}, {1,0}, {0,0}},
		{{1,2}, {0,2}, {0,1}, {0,0}},
		{{-1,2}, {0,2}, {0,1}, {0,0}},

		{{1,0}, {0,0}, {0,1}, {0,2}},
		{{0,1}, {0,0}, {1,0}, {2,0}}
	};

	RectangularField field;
	int[][] colors = new int[16][4];

	int[] colorUsage = new int[16];
	int[] lstate;
	int[] cstate;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysC; //coloring 0..15
	
	int treeCount;
	int solutionCount;

	public LFilling() {
		field = new RectangularField();
		field.setSize(8, 8);
		for (int c = 0; c < 16; c++) {
			int cc = c;
			for (int i = 0; i < 4; i++) {
				colors[c][i] = (cc % 2);
				cc = cc / 2;
			}
		}
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		lstate = new int[field.getSize()];
		cstate = new int[field.getSize()];
		for (int i = 0; i < lstate.length; i++) {
			lstate[i] = -1;
			cstate[i] = -1;
		}
		colorUsage = new int[16];
		
		place = new int[17];
		wayCount = new int[17];
		way = new int[17];
		waysF = new int[17][200];
		waysC = new int[17][200];

		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t == 16) return;
		if(t < 16 && getRegions(0) > 1) return;
		
		if(t == 4 && getValues(1) < 10) return;
//		if(t < 5 && getRegions(1) > 1) return; //good
		
		
//		if(t == 7 && getRegions(1) < 4) return;
//		if(t == 8 && getRegions(1) < 8) return;

		place[t] = nextPlace();
		for (int f = 0; f < 8; f++) {
			if(canPlace(figures[f])) {
				for (int c = 0; c < 16; c++) {
					if(colorUsage[c] > 0) continue;
//							if(t == 0 && c != 15) continue;
//							if(c == 0 && t < 11) continue;
					if(t > 5 && t < 12 && wouldTouch(f, place[t], c)) continue;
					waysF[t][wayCount[t]] = f;
					waysC[t][wayCount[t]] = c;
					wayCount[t]++;
				}
			}
		}
		randomize();
		if(wayCount[t] > 30) {
			wayCount[t] = 30;
		}
	}

	Random seed = new Random();

	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t] - 1; i++) {
			int j = i + seed.nextInt(wayCount[t] - i);
			int c = waysF[t][i]; waysF[t][i] = waysF[t][j]; waysF[t][j] = c;
			c = waysC[t][i]; waysC[t][i] = waysC[t][j]; waysC[t][j] = c;
		}
	}

	int nextPlace() {
		if(t == 0) return 0;
		if(t == 16) return -1;
		int p = place[t - 1] + 1;
		while(p < lstate.length && lstate[p] >= 0) {
			p++;
		}
		return p;
	}

	boolean canPlace(int[][] figure) {
		int p = place[t];
		for (int i = 0; i < figure.length; i++) {
			int q = field.jumpXY(p, figure[i][0], figure[i][1]);
			if(q < 0 || lstate[q] >= 0) return false;
		}
		return true;
	}

	boolean wouldTouch(int f, int p, int c) {
		int[][] figure = figures[f];
		for (int i = 0; i < 4; i++) {
			if(colors[c][i] == 0) continue;
			int q = field.jumpXY(p, figure[i][0], figure[i][1]);
			for (int d = 0; d < 4; d++) {
				int r = field.jump(q, d);
				if(r >= 0 && cstate[r] == 1) {
					return true;
				}
			}
		}
		return false;
	}

	int getValues(int v) {
		int result = 0;
		for (int p = 0; p < cstate.length; p++) {
			if(cstate[p] == v) result++;
		}
		return result;
	}

	int getRegions(int s) {
		int result = 0;
		int[] stack = new int[lstate.length];
		int[] passed = new int[lstate.length];
		for (int p = 0; p < cstate.length; p++) {
			if(cstate[p] != s || passed[p] == 1) continue;
			result++;
			int vm = 1;
			passed[p] = 1;
			int vc = 0;
			stack[0] = p;
			while(vc < vm) {
				int q = stack[vc];
				for (int d = 0; d < 4; d++) {
					int r = field.jump(q, d);
					if(r >= 0 && (cstate[r] == s || cstate[r] < 0) && passed[r] == 0) {
						passed[r] = 1;
						stack[vm] = r;
						vm++;
					}
				}
				vc++;
			}
		}
		return result;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int c = waysC[t][way[t]];
		colorUsage[c]++;

		int p = place[t];
		int[][] figure = figures[f];
		for (int i = 0; i < figure.length; i++) {
			int q = field.jumpXY(p, figure[i][0], figure[i][1]);
			lstate[q] = t;
			cstate[q] = colors[c][i];
		}
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int f = waysF[t][way[t]];
		int c = waysC[t][way[t]];
		colorUsage[c]--;

		int p = place[t];
		int[][] figure = figures[f];
		for (int i = 0; i < figure.length; i++) {
			int q = field.jumpXY(p, figure[i][0], figure[i][1]);
			lstate[q] = -1;
			cstate[q] = -1;
		}
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 14;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCount >= 7000000) return;
			}
			if(t > tm) {
//				System.out.println(t);
				tm = t;
			}
			if(isSolution()) {
				printSolution();
				solutionCount++;
//				return;
			}
		}
	}

	int best = 10;

	boolean isSolution() {
		if(t < 16) return false;
		int q = getRegions(1) - getRegions(0);
		if(q <= best) return false;
		best = q;
		return true;
	}

	void printSolution() {
		System.out.println(solutionCount + " " + getRegions(1) + " " + getRegions(0));
		for (int p = 0; p < lstate.length; p++) {
			String s = " " + (char)('a' + lstate[p]);
			System.out.print(s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
		for (int p = 0; p < lstate.length; p++) {
			System.out.print(" " + cstate[p]);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}


	public static void main(String[] args) {
		BigInteger a = new BigInteger("66656533023001");
		BigInteger b = new BigInteger("66660431521201");
		System.out.println(a.multiply(b));
//		while(true) {
//			LFilling p = new LFilling();
//			p.solve();
//			if(p.best > 13) break;
//		}
	}

}

/**
14 1
 a b b b c d d d
 a a a b c c c d
 e e e f g g g h
 i i e f g h h h
 i j f f k k l l
 i j m m n k o l
 j j p m n k o l
 p p p m n n o o

 1 0 0 1 0 1 0 1
 1 0 1 0 0 0 0 0
 0 0 0 1 0 1 0 1
 1 0 1 0 0 0 1 0
 1 0 0 0 1 0 0 0
 0 0 1 0 0 1 1 1
 1 1 0 0 1 1 1 1
 1 1 1 1 1 1 1 1

1 14 1
 a a a b b b c c
 d d a e e b f c
 d g g g e h f c
 d g i i e h f f
 j j i k h h l l
 j m i k n n l o
 j m k k p n l o
 m m p p p n o o

 1 0 0 1 0 1 0 1
 0 0 1 0 0 1 0 1
 1 0 0 1 0 0 0 0
 1 0 1 0 0 1 0 1
 0 0 0 0 1 0 0 1
 1 0 1 0 0 1 1 1
 0 0 0 1 1 1 1 1
 1 1 0 1 1 1 1 1

2 14 1
 a a a b c d d d
 e e a b c f f d
 e g b b c c f h
 e g i i j j f h
 g g k i j l h h
 m m k i j l l l
 m n k k o o o p
 m n n n o p p p

 1 1 1 0 1 1 1 1
 1 1 0 0 1 1 1 1
 0 0 0 1 0 1 0 0
 1 0 1 0 0 0 0 1
 0 0 0 1 0 1 0 1
 1 0 1 0 1 0 0 0
 0 0 0 0 0 0 1 1
 1 0 1 1 1 0 1 1

13 1
 a a a b c c c d
 a e f b b b c d
 g e f f f h d d
 g e e i i h j j
 g g k i h h l j
 k k k i m m l j
 n o o o m p l l
 n n n o m p p p

 1 0 0 1 0 1 0 1
 0 1 0 0 0 0 0 1
 0 0 1 0 1 1 0 0
 1 0 0 0 0 0 1 1
 1 0 1 0 1 0 0 0
 0 1 0 0 0 1 1 1
 0 0 0 1 1 1 1 1
 1 1 1 1 1 1 1 1

14 2
 a a a b b c c c
 d d a b e c f f
 g d h b e e e f
 g d h h h i i f
 g g j k k k i l
 m m j j j k i l
 m n n n o p l l
 m n o o o p p p

 1 1 1 0 1 0 1 1
 1 1 1 0 0 1 0 0
 0 0 0 1 0 0 0 1
 0 1 0 0 1 0 1 0
 1 0 1 0 0 0 0 0
 1 0 0 1 0 1 1 1
 1 1 0 0 0 0 1 1
 1 1 0 1 1 0 1 1
*/