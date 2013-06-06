package champ2013.regions;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.slava.common.RectangularField;

public class RegionBuilder2 {
	RectangularField f;
	int[] state;
	int onesCount;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int treeCount;
	int solutionCount;
	
	Set<String> results = new HashSet<String>();

	public RegionBuilder2() {
		f = new RectangularField();
		f.setSize(8, 8);
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[f.getSize()];
		onesCount = 0;

		wayCount = new int[state.length + 1];
		way = new int[state.length + 1];
		ways = new int[state.length + 1][2];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	int onesInFirstHalf = 20;

	void srch() {
		wayCount[t] = 0;
		if(t == 64) return;
		if(excludeSymmetric()) return;
		if(t < 32 && onesCount + 32 - t < onesInFirstHalf) return;
		if(onesCount < t - 32) return;
		if(computeRegions(0, 2) > 2) return;
		if(t < 63) {
			int r = computeRegions(1, 0) - computeRegions(0, 0) + 1;
			int dr1 = 32 - onesCount;
			int dr2 = (64 - t) / 2;
			r += (dr1 < dr2) ? dr1 : dr2;
			if(r < 14) return;			
		}
		
		if(onesCount == 32) {
			ways[t][0] = 0;
			wayCount[t] = 1;
		} else if(onesCount == t - 32) {
			ways[t][0] = 1;
			wayCount[t] = 1;
		} else if(t < 32 && onesCount + 32 - t == onesInFirstHalf) {
			ways[t][0] = 1;
			wayCount[t] = 1;
		} else if(t == 0 || t == 7 || t == 56) {
			//a piece can always be moved into a corner
			ways[t][0] = 1;
			wayCount[t] = 1;
//		} else if(isSquare()) {
//			//do not keep 2x2 square without ones
//			ways[t][0] = 1;
//			wayCount[t] = 1;
		} else {
			ways[t][0] = 0;
			ways[t][1] = 1;
			wayCount[t] = 2;
		}
	}

	boolean excludeSymmetric() {
		if(t == 8) {
			int a = 0;
			for (int i = 0; i < 4; i++) a = a * 2 + state[i];
			int b = 0;
			for (int i = 7; i >= 4; i--) b = b * 2 + state[i];
			return a > b; //exclude
		}
		return false;
	}

	boolean isSquare() {
		int p = f.jump(t, 2);
		if(p < 0 || state[p] == 1) return false;
		p = f.jump(t, 3);
		if(p < 0 || state[p] == 1) return false;
		p = f.jump(p, 2);
		if(p < 0 || state[p] == 1) return false;
		return true;
	}

	void move() {
		int v = ways[t][way[t]];
		state[t] = v;
		onesCount += v;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int v = ways[t][way[t]];
		state[t] = 0;
		onesCount -= v;
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 14; int tc = 0;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
//				if(treeCount >= 7000000) return;
				if(solutionCount == 0 && treeCount % 10000000 == 0) printSolution();
			}
			if(t > tm) {
				System.out.println(t);
				tm = t;
			}
			if(isSolution() && buildResult()) {
				solutionCount++;
				printSolution();
//				return;
			}
		}
	}

	public boolean isSolution() {
		return t == 64 && computeRegions(1, 0) - computeRegions(0, 0) >= 14;
	}

	void printSolution() {
		int r0 = computeRegions(0, 0);
		int r1 = computeRegions(1, 0);
		
		System.out.println(" " + r0 + " " + r1 + "   " + solutionCount);
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(f.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

	int[] stack = new int[64];

	int computeRegions(int v, int max) {
		int regions = 0;
		int[] visited = new int[state.length];
		for (int i = 0; i < t; i++) {
			if(visited[i] > 0 || state[i] != v) continue;
			visited[i] = 1;
			regions++;
			if(max > 0 && regions > max) {
				return regions;
			}
			int c = 0;
			int m = 1;
			stack[0] = i;
			while(c < m) {
				int p = stack[c];
				for (int d = 0; d < 4; d++) {
					int q = f.jump(p, d);
					if(q >= 0 && state[q] == v && visited[q] == 0) {
						stack[m] = q;
						visited[q] = 1;
						m++;
					}
				}
				c++;
			}			
		}
		return regions;
	}

	boolean buildResult() {
		StringBuilder[] sb = new StringBuilder[8];
		for (int i = 0; i < sb.length; i++) sb[i] = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				sb[0].append(state[f.getIndex(i, j)]);
				sb[1].append(state[f.getIndex(7 - i, j)]);
				sb[2].append(state[f.getIndex(i, 7 - j)]);
				sb[3].append(state[f.getIndex(7 - i, 7 - j)]);
				sb[4].append(state[f.getIndex(j, i)]);
				sb[5].append(state[f.getIndex(7 - j, i)]);
				sb[6].append(state[f.getIndex(j, 7 - i)]);
				sb[7].append(state[f.getIndex(7 - j, 7 - i)]);
			}
		}
		TreeSet<String> all = new TreeSet<String>();
		for (int i = 0; i < sb.length; i++) all.add(sb[i].toString());
		if(all.size() < 8) System.out.println("!Symmetric");
		String a = all.iterator().next();
		if(results.contains(a)) {
			return false;
		}
		results.add(a);
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RegionBuilder2().solve();
	}

}
