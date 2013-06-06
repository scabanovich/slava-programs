package champukr;

import java.util.*;

public class Year2006 {
	static int[] FACTORIALS = new int[9];
	
	static int[] SUMMARIALS = new int[900];
	static int[] QUADRIALS = new int[400];
	
	static {
		SUMMARIALS[0] = 0;
		for (int i = 1; i < SUMMARIALS.length; i++) SUMMARIALS[i] = SUMMARIALS[i - 1] + i;
		QUADRIALS[0] = 0;
		for (int i = 1; i < QUADRIALS.length; i++) QUADRIALS[i] = QUADRIALS[i - 1] + i * i;
	}

	boolean addSummarialsAndQuadrials = true;
	
	int number = 2006;
	int digit;
	int[][] sets = new int[20][];
	Set total = new HashSet();
	Set current = new HashSet();
	                     
	public void init(int digit, int number) {
		this.number = number;
		FACTORIALS[0] = 1;
		for (int i = 1; i < FACTORIALS.length; i++) {
			FACTORIALS[i] = FACTORIALS[i - 1] * i;
		}
		this.digit = digit;
		add(digit);
		if((digit != 0 && digit < 3) || digit >= FACTORIALS.length) {
			//sets[1] = new int[]{digit};
		} else if(digit < FACTORIALS.length) {
			add(FACTORIALS[digit]);
		}
		
		Integer[] is = (Integer[])current.toArray(new Integer[current.size()]);
		sets[1] = new int[is.length];
		for (int i = 0; i < is.length; i++) sets[1][i] = is[i].intValue();
		current.clear();
	}
	
	public void iterate() {
		int sc = 2;
		while(!compute(sc)) {
			System.out.println(" sc=" + sc + " size=" + sets[sc].length);
			++sc;
		}
	}
	
	boolean compute(int sc) {
		current.clear();
		if(sc < 6) {
			int q = digit;
			for (int i = 2; i <= sc; i++) q = q * 10 + digit;
			add(q);
		}
		boolean res = false;
		for (int i = 1; i <= sc / 2; i++) {
			if(compute(sets[i], sets[sc - i], sc)) res = true;
		}
		Integer[] is = (Integer[])current.toArray(new Integer[current.size()]);
		sets[sc] = new int[is.length];
		for (int i = 0; i < is.length; i++) sets[sc][i] = is[i].intValue();
		current.clear();
		return res;
	}
	
	boolean compute(int[] s1, int[] s2, int sc) {
		for (int i1 = 0; i1 < s1.length; i1++) {
			for (int i2 = 0; i2 < s2.length; i2++) {
				int v1 = s1[i1];
				int v2 = s2[i2];
				if(add(v1 + v2)) {
					printResult(v1, v2, "+", sc);
					return true;
				};
				if(add(v1 - v2)) {
					printResult(v1, v2, "-", sc);
					return true;
				};
				if(add(v2 - v1)) {
					printResult(v2, v1, "-", sc);
					return true;
				};
				if(add(v1 * v2)) {
					printResult(v1, v2, "*", sc);
					return true;
				};
				if(v2 != 0 && v1 % v2 == 0 && add(v1 / v2)) {
					printResult(v1, v2, "/", sc);
					return true;
				};
				if(v1 != 0 && v2 % v1 == 0 && add(v2 / v1)) {
					printResult(v2, v1, "/", sc);
					return true;
				};
				if(v1 > 1 && v1 < 15 && Math.abs(v2) < 5000) {
					int v = 1;
					boolean ok = true;
					for (int kk = 0; ok && kk < v1; kk++) {
						v = v * v2;
						if(Math.abs(v) > 10000000) ok = false;
					}
					if(ok && add(v)) {
						printResult(v2, v1, "^", sc);
						return true;
					}
				}
				if(v2 > 1 && v2 < 15 && Math.abs(v1) < 5000) {
					int v = 1;
					boolean ok = true;
					for (int kk = 0; ok && kk < v2; kk++) {
						v = v * v1;
						if(Math.abs(v) > 10000000) ok = false;
					}
					if(ok && add(v)) {
						printResult(v2, v1, "^", sc);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	void printResult(int v1, int v2, String sign, int number) {
		System.out.println("" + v1 + " " + sign + " " + v2 + " in " + number);
		System.out.println("" + v1 + " in " + findSet(v1, number) + "   " + v2 + " in " + findSet(v2, number));
	}
	
	int findSet(int v, int numberLimit) {
		for (int i = 0; i < numberLimit; i++) {
			if(sets[i] != null) for (int j = 0; j < sets[i].length; j++) {
				if(sets[i][j] == v) return i;
			}
		}
		return -1;
	}
	
	int limit = 5000000;
	boolean add(int q) {
		if(q > limit || q < -limit) return false;
		Integer i = new Integer(q);
		if(total.contains(i)) return false;
		total.add(i);
		current.add(i);
		if(q == number) return true;
		boolean result = false;
		if(q > 2 && q < FACTORIALS.length) {
			if(add(FACTORIALS[q])) result = true;
		}
		if(addSummarialsAndQuadrials && q > 1) {
			if(q < SUMMARIALS.length && add(SUMMARIALS[q])) result = true;
			if(q < QUADRIALS.length && add(QUADRIALS[q])) result = true;
		}
		
		return result;
		
	}
	
	public static void main(String[] args) {
		Year2006 p = new Year2006();
		p.addSummarialsAndQuadrials = false;
		p.init(6, 2013);
		p.iterate();
	}
	
}
// (11 - 1 - 1) * (1 + 111 * (1 + 1)) - 1    (11)
// 2 + 2 ^ (22 / 2) - 2 * 22                 (8)
// 3! + 3! / 3 + 3! * 333                    (7)
// 4! / 4 + (4! - 4) * (4 + 4 * 4!)          (7)
// (5 + 5! / (5 + 5)) * (5! - (5 + 5) / 5)   (8) 
// (6! - 6 - 6) * (6 + 66 / 6) / 6           (8)
// (7! + 7! + 7! - (77 * (7 + 7))) / 7       (8)
// 8 + 888 * (8 + (88 - 8) / 8) / 8          (10)
// 9 + ((9 + 9) * 999 - 9) / 9               (8)

//(8! / (8 + 8)) - 8 * 8 * 8 - (8 + 8) / 8
