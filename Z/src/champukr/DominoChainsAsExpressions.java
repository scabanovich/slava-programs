package champukr;

import java.util.*;
import com.slava.math.EquationMaker;

public class DominoChainsAsExpressions {
	DominoSet dominoes;
	int[] dominoUsage;
	
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] base;
	
	int tMax = 4;
	
	int successCount;
	int failureCount;
	Map map = new HashMap();
	
	public void setDominoes(DominoSet dominoes) {
		this.dominoes = dominoes;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[dominoes.size + 2];
		base = new int[dominoes.size + 2];
		dominoUsage = new int[dominoes.size];
		wayCount = new int[dominoes.size + 2];
		way = new int[dominoes.size + 2];
		ways = new int[dominoes.size + 2][dominoes.dimension + 1];
		t = 0;
		successCount = 0;
		failureCount = 0;
		base[0] = 0;
		
		dominoUsage[dominoes.dominoIndex[0][0]] = 1;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(getEstimation() < 11) return;
//				if(t == 24 && getExpressionCount() < 6) return;
///		if(!check()) return;
//				if(t > 0 && t < 44 && t - base[t - 1] > 6) return;
//		if(t >= tMax) return;
		for (int c = 0; c < dominoes.dimension; c++) {
			if(t > 0) {
				int d = dominoes.dominoIndex[ways[t - 1][way[t - 1]]][c];
				if(dominoUsage[d] > 0) continue;
			}
			ways[t][wayCount[t]] = c;
			wayCount[t]++;
		}
		if(t < 15) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i>= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(j == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}
	
	int getEstimation() {
		int c = getExpressionCount();
		int b = t - base[t];
		if(b >= 3) {
			c += 1 + (45 - t - 1) / 4;
		} else if(b > 0) {
			c += 1 + (45 - t - 1) / 4;  
		} else {
			c += (45 - t + 2) / 4;
		}
		return c;
	}
	
	boolean check() {
///		if(t == 46) return makeEquation(getLastNumbers(3));
		if(t == 4) return checkLast(3);
		if(t > 4 && t % 4 == 0) return checkLast(4);
		return true;
	}
	
	boolean checkLast(int q) {
		String s = "";
		for (int i = 0; i <= q; i++) {
			int k = t - q - 1 + i;
			s += state[k];
		}
		Boolean b = (Boolean)map.get(s);
		if(b != null) return b.booleanValue();
		boolean bn = makeEquation(getLastNumbers(q));
		map.put(s, new Boolean(bn));
		return bn;
	}
	
	void move() {
		state[t] = ways[t][way[t]];
		if(t > 0) {
			int d = dominoes.dominoIndex[state[t - 1]][state[t]];
			dominoUsage[d] = 1;
		}
		++t;
		if(t - base[t - 1] == 4 && checkLast(3)) {
			base[t] = t - 1;
		} else if(t - base[t - 1] == 5 && (checkLast(4) || checkLast(3))) {
			base[t] = t - 1;
		} else if(/*t > 43 && */ t - base[t - 1] == 6 &&(checkLast(5) || checkLast(4) || checkLast(3))) {
			base[t] = t - 1;
		} else if(t == 45 && t - base[t - 1] == 7 && (checkLast(6) || checkLast(5) || checkLast(4) || checkLast(4))) {
			base[t] = t - 1;
		} else {
			base[t] = base[t - 1];
		}
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		if(t > 0) {
			int d = dominoes.dominoIndex[state[t - 1]][state[t]];
			dominoUsage[d] = 0;
		}
		state[t] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == tMax) {
//				makeEquation();
			}
			if(t > tm) {
				tm = t;
				System.out.println("-->" + tm);				
			}
			if(t == 45) {
				System.out.println("Solution!!! " + getExpressionCount());
				printSolution();
				if(getExpressionCount() > 10) {
					printSolution();
				}
			}
		}
	}
	
	int getExpressionCount() {
		int c = 0;
		for (int i = 1; i <= t; i++) {
			if(base[i] != base[i - 1]) ++c;
		}
		return c;
	}

	void makeEquation() {
		double[] ns = getNumbers();
		if(makeEquation(ns)) successCount++; else failureCount++;
	}

	boolean makeEquation(double[] ns) {
		EquationMaker p = new EquationMaker();
		p.init(ns, 2006.d);
		p.solve();
		if(p.isSolutionFound()) return true; else {
			for (int i = 0; i < 1/*ns.length*/; i++) {
				ns[i] = -ns[i];
				p.init(ns, 2006d);
				p.solve();
				if(p.isSolutionFound()) return true;
				ns[i] = -ns[i];
			}
			return false;
		}
	}
	
	double[] getNumbers() {
		double[] res = new double[t - 1];
		for (int i = 0; i < res.length; i++) {
			int c1 = ways[i][way[i]];
			int c2 = ways[i + 1][way[i + 1]];
			res[i] = c1 * 10d + c2;
		}
		return res;
	}
	
	double[] getLastNumbers(int q) {
		double[] res = new double[q];
		for (int i = 0; i < res.length; i++) {
			int i1 = t - q - 1 + i;
			int c1 = ways[i1][way[i1]];
			int c2 = ways[i1 + 1][way[i1 + 1]];
			res[i] = c1 * 10d + c2;
		}
		return res;
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			System.out.print(" " + state[i]);
		}
		System.out.println();
		for (int i = 1; i <= t; i++) {
			int d = base[i] - base[i - 1];
			if(d > 0) System.out.print(" " + d);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		DominoSet dominoes = new DominoSet();
		dominoes.setSize(9);
		DominoChainsAsExpressions p = new DominoChainsAsExpressions();
		p.setDominoes(dominoes);
		p.solve();
		System.out.println("successCount=" + p.successCount);
		System.out.println("failureCount=" + p.failureCount);		
	}

}

/*
Solution!!! 10
 8 2 3 4 4 1 6 6 0 1 2 2 7 7 3 0 7 6 4 2 5 6 2 0 8 8 6 3 5 8 7 4 0 5 4 8 1 1 7 5 5 1 3 3 8
 3 4 4 3 4 4 4 4 4 4          75 55 51 13 33 38 x
Solution!!! 10
 8 2 0 6 1 1 5 7 6 8 3 1 7 4 0 5 4 2 5 6 2 7 7 3 4 1 0 8 8 1 2 2 3 5 5 8 4 4 6 6 3 3 0 7 8
 4 4 4 4 4 3 4 4 4 4          63 33 30 7 78 x
Solution!!! 10
 4 4 7 0 8 4 5 2 2 7 7 3 3 0 2 6 3 2 1 1 5 5 0 6 8 3 1 7 8 2 4 0 1 6 5 3 4 1 8 8 5 7 6 6 4
 4 4 3 4 4 4 4 4 4 3          88 85 57 76 66 64 x

Solution!!! 11
 3 3 4 6 8 0 2 5 6 0 1 2 2 7 7 3 8 2 4 0 3 6 2 3 5 5 8 7 5 1 6 6 7 0 5 4 1 7 4 4 8 8 1 1 3
 4 4 4 3 3 4 4 5 5 3 5
Solution!!! 11
 6 0 1 4 8 8 1 2 2 7 7 3 8 2 4 0 3 3 4 5 0 7 1 5 7 8 6 7 4 4 6 3 1 1 6 6 2 3 5 5 8 0 2 5 6
 4 4 3 3 4 4 5 4 5 4 4
Solution!!! 11
 6 0 1 4 8 8 1 2 2 7 7 3 8 2 4 5 0 7 1 5 7 8 6 7 4 4 6 3 4 0 3 3 1 1 6 6 2 3 5 5 8 0 2 5 6
 4 4 3 3 4 5 4 4 5 4 4
Solution!!! 11
 6 0 1 4 8 8 1 2 2 7 7 3 4 0 3 3 8 2 4 5 0 7 1 5 7 8 6 7 4 4 6 3 1 1 6 6 2 3 5 5 8 0 2 5 6
 4 4 3 4 3 4 5 4 5 4 4

*/