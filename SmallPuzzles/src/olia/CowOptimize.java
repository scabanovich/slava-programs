package olia;

import java.util.*;

public class CowOptimize {
	static int[][] CODE = new int[][]{
		{0, -1, -1, -1, -1},
		{1,  2, -1, -1, -1},
		{3,  4,  5, -1, -1},
		{6,  7,  8,  9, -1},
		{10, 11, 12, 13, 14}	
	};
	
	Map map;
	
	void findBest(int q) {
		int best = 40;
		int deg = 10000;
		while(best > 1) {
			int[] ns = generateNumbers(q);
			int max = check(ns);
			if(max < best) {
				best = max;
				deg = getDegeneracy(max);
				System.out.println("Max size=" + best+ " Degeneracy=" + deg);
				for (int i = 0; i < q; i++) System.out.print(" " + ns[i]);
				System.out.println("");
			} else if(max == best) {
				int dg = getDegeneracy(max);
				if(dg < deg) {
					deg = dg;
					System.out.println("Max size=" + best+ " Degeneracy=" + deg);
					for (int i = 0; i < q; i++) System.out.print(" " + ns[i]);
					System.out.println("");
				}
			}
		}
	}
	
	int getDegeneracy(int v) {
		int k = 0;
		Iterator it = map.values().iterator();
		while(it.hasNext()) {
			Set s = (Set)it.next();
			if(s.size() == v) ++k;
		}
		return k;
	}
	
	int[] generateNumbers(int q) {
		int[] ns = new int[q];
		for (int i = 0; i < q; i++) {
			ns[i] = -1;
			while(ns[i] < 0) ns[i] = generateNumber();
		}
		return ns;
	}
	
	int generateNumber() {
		int n = (int)(10000*Math.random());
		if(n < 1234 || n > 9876) return -1;
		int[] d = getDigits(n);
		for (int k1 = 0; k1 < 3; k1++) {
			for (int k2 = k1 + 1; k2 < 4; k2++) {
				if(d[k1] == d[k2]) return -1;
			}
		}
		
		return n;
	}
	
	int check(int[] ns) {
		map = new HashMap();
		int[][] nsd = new int[ns.length][];
		int max = 0;
		for (int i = 0; i < ns.length; i++) nsd[i] = getDigits(ns[i]);
		for (int n = 1234; n <= 9876; n++) {
			int[] d = getDigits(n);
			int code = 0;
			for (int i = 0; i < ns.length; i++) {
				int c = getCows(d, nsd[i]);
				int b = getBulls(d, nsd[i]);
				code = code * 15 + CODE[c][b];
			}
			Integer ic = new Integer(code);
			Set s = (Set)map.get(ic);
			if(s == null) {
				s = new HashSet();
				map.put(ic, s);
			}
			s.add(new Integer(n));
			if(s.size() > max) max = s.size();
		}
		///System.out.println("MaxSize=" + max);
		return max;
	}
	
	int[] getDigits(int n) {
		int[] d = new int[4];
		for (int k = 0; k < 4; k++) {
			d[k] = n % 10;
			n = n / 10;
		}
		return d;
	}
	
	int getBulls(int[] d1, int[] d2) {
		int c = 0;
		for (int k = 0; k < 4; k++) {
			if(d1[k] == d2[k]) ++c;
		}
		return c;
	}
	
	int getCows(int[] d1, int[] d2) {
		int c = 0;
		for (int k1 = 0; k1 < 4; k1++) {
			for (int k2 = 0; k2 < 4; k2++) {
				if(d1[k1] == d2[k2]) {
					++c;
					continue;
				}
			}
		}
		return c;
	}

	public static void main(String[] args) {
		CowOptimize co = new CowOptimize();
//		int[] ns = new int[]{
//			7214,1826,5479,3016,2691,8260
//		};
		co.findBest(4);
	}

}

/*
Max size=6 Degeneracy=8
 7592 5704 6023 9648 8425
Max size=21 Degeneracy=2
 6782 9564 4891 7319
*/
