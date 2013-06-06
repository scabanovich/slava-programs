package com.slava.math;

public class EuclidEquation {
	
	public static int getMaxCommonDivisor(int a, int b) {
		if(a == 0) return b;
		if(b == 0) return a;
		if(Math.abs(a) >= Math.abs(b)) {
			return getMaxCommonDivisor(a % b, b);
		} else {
			return getMaxCommonDivisor(a, b % a);
		}
	}
	
	public static int[] solve(int a, int b, int c) {
		if(a == 0 && b == 0) {
			return null;
		}
		if(a == 0) {
			return (c % b == 0) ? new int[]{0, c / b} : null; 
		} else if(b == 0) {
			return (c % a == 0) ? new int[]{c / a, 0} : null;
		} else if(a < 0) {
			int[] sol = solve(-a, b, c);
			if(sol != null) sol[0] = -sol[0];
			return sol;
		} else if(b < 0) {
			int[] sol = solve(a, -b, c);
			if(sol != null) sol[1] = -sol[1];
			return sol;
		} else if(a >= b) {
			int k = a / b;
			int a2 = a - k * b;
			int[] sol = solve(a2, b, c);
			if(sol != null) sol[1] = sol[1] - k * sol[0];
			return sol;
		} else {
			int k = b / a;
			int b2 = b - k * a;
			int[] sol = solve(a, b2, c);
			if(sol != null) sol[0] = sol[0] - k * sol[1];
			return sol;
		}
	}
	
	public static void normalizeSolution(int[] solution, int dx, int dy) {
		if(dx != 0 && Math.abs(solution[0] / dx) > 0) {
			int k = solution[0] / dx;
			solution[0] -= k * dx;
			solution[1] -= k * dy;
		}
		if(dx != 0 && solution[0] < 0) {
			int k = (dx > 0) ? 1 : -1;
			solution[0] += k * dx;
			solution[1] += k * dy;
		}
		if(dy != 0 && solution[1] < 0) {
			int k = (dy > 0) ? 1 : -1;
			while(solution[1] < 0 && solution[0] + k * dx >= 0) {
				solution[0] += k * dx;
				solution[1] += k * dy;
			}
		}
	}
	
	public static String equationToString(int a, int b, int c) {
		return "" + a + "x + " + b + "y = " + c;
	}
	
	public static void solveAndReport(int a, int b, int c) {
		System.out.println(equationToString(a, b, c));
		int[] sol = solve(a, b, c);
		int mc = getMaxCommonDivisor(a, b);
		int dy = mc == 0 ? -a : -a / mc;
		int dx = mc == 0 ? b : b / mc;
		if(sol != null) normalizeSolution(sol, dx, dy);
		if(sol == null) {
			System.out.println("No solution");
		} else {
			System.out.println("x=" + sol[0] + " y=" + sol[1]);
		}
		System.out.println("dx=" + dx + " dy=" + dy);
	}
	
	
	
	static void beerProblem() {
		int vm = 100000;
		int basis = 7;
		for (int ia1 = 1; ia1 < basis; ia1++) {
			int ib1 = basis - ia1;
			int a1 = 4011 * ia1;
			int b1 = 4011 * ib1;
			for (int d1 = 1; d1 < basis; d1++) {
				if(((a1 * d1) % basis) != 0) continue;
				int da1 = (a1 * d1) / basis;
				int db1 = (b1 * d1) / basis;
				int a2_1 = da1;
				int b2_1 = db1;
				int a1_1 = a1 - da1;
				int b1_1 = b1 - db1;
				for (int ia2 = 1; ia2 < 190 - basis + d1; ia2++) {
					int ib2 = 191 - basis + d1 - ia2;
					int a1_2 = a1_1 + 4011 * ia2;
					int b1_2 = b1_1 + 4011 * ib2;
					if(a1_2 + b1_2 != 4011 * 191) throw new RuntimeException("fuck");
					for (int d2 = 1; d2 < 170; d2++) {
						int da2 = a1_2 / 191 * d2;
						int db2 = b1_2 / 191 * d2;
						int a2_3 = a2_1 + da2;
						int b2_3 = b2_1 + db2;
//						int a1_3 = a1_2 - da2;
//						int b1_3 = b1_2 - db2;
						if((a2_3 + b2_3) % 4011 != 0) throw new RuntimeException("fuck 2");
						int s = (a2_3 + b2_3) / 4011;
						int p = s * 2005 - a2_3;
						if(p < 0) continue;
						int x = p % 2005;
						int y = x - p / 2005;
						if(y < 0) continue;
						int v = s + x + y;
						if(v > 95 && v < vm && ib1 * ib2 + y <= v && ia1 + ia2 + x <= v) {
							vm = v;
							System.out.println("v=" + v);
							System.out.println("ia1=" + ia1 + " d1=" + d1 + " ia2=" + ia2 + " ib2=" + ib2 + " d2=" + d2 + " x=" + x + " y=" + y);
						}
					}					
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		solveAndReport(55, 89, 88 * 54);
//		solveAndReport(9 * 191, 91, 200600);
//		beerProblem();
	}

}
