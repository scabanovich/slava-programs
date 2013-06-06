package diogen2006;

public class RelationsSearcer {
	
	public static boolean isReducedToFactorsLessThan(int n, long a) {
		for (int i = 2; i <= n; i++) {
			if(a <= n) return true;
			while(a >= i && a % i == 0) a = a / i;
		}
		return a <= n;
	}

	public void search(double d, int n, double delta) {
		for (int b = 2; b < 100000000; b++) {
			int a = (int)(Math.round(d * b));
			double del = 1d * a / b - d;
			if(Math.abs(del) < delta && isReducedToFactorsLessThan(n, a + b)) {
				System.out.println(a + "/" + b + " delta=" + del);
			}
		}
	}

}

/**

 5(3/4 1/4) 3(11/15 4/15) 

*/