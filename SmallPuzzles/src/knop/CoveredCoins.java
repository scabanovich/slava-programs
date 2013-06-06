package knop;

public class CoveredCoins {
	
	/**
	 * Compute ln[C(k,n)]
	 * @param args
	 */
	public static double getLogarithmicCombination(double k, double n) {
		return k * Math.log(n/k) + (n - k) * Math.log(n/(n - k))
		+ 0.5d * Math.log(n/k/(n - k)/2d/Math.PI);
	}

	public static double getLogarithmicCombinationSum(double k, double n) {
		double v0 = getLogarithmicCombination(k, n);
		double v1 = getLogarithmicCombination(k - 1d, n);
		double dv = - Math.log(1 - Math.exp(v1 - v0));
		return v0 + dv;
	}

	public static double getCombination(int k, int n) {
		return Math.exp(getLogarithmicCombination(k, n));
	}
	
	/**
	 * SUM{ln[C(k,n)]} - ln2 * (n - k)
	 * @param k
	 * @param n
	 * @return
	 */	
	static double f(double k, double n) {
		return 1.1 * getLogarithmicCombinationSum(k, n) - Math.log(2d) * (n - k);
	}
	
	/**
	 * Solve ln[C(k,n)] = ln2 * (n - k)
	 * @param args
	 */
	public static double solve(int n) {
		double ka = 1;
//		System.out.println(ka + " " + f(ka, (double)n));
		double kb = n * 0.5d;
//		System.out.println(kb + " " + f(kb, (double)n));
		int c = 0;
		while(true) {
			c++;
			double kc = (ka + kb) / 2;
			if(c > 100) {
				System.out.println("Cannot solve");
				return kc;
			}
			double vc = f(kc, (double)n);
//			System.out.println(kc + " " + vc);
			if(Math.abs(vc) < 0.0001) return kc;
			if(vc < 0) {
				ka = kc;
			} else {
				kb = kc;
			}
		}
	}

	public static void main(String[] args) {
		
		int n = 100;
		double k = solve(n);
		System.out.println("n = " + n + "  k = " + k);
	}
}
