package p;

// a=2    4  -2    (+1)
public class Problem721 {
	
	public static double pow(double a, long n) {
		if (n == 0) {
			return 1;
		}
		if (n == 1) {
			return a;
		}
		long n2 = n / 2;
		double a2 = pow(a, n2);
		double res = a2 * a2;
		if (n % 2 == 1) {
			res *= a;
		}
		return res;
	}
	
	public static long f(long a, long n) {
		double s = Math.sqrt(a);
		double s2 = Math.ceil(s) + s;
		double sn = pow(s2, n);
		return (long)Math.floor(sn);
	}

	public static void main(String[] args) {
		long a = 2;
		int nM = 20;
		long[] an = new long[nM]; 
		for (int n = 0; n < nM; n++) {
			long f = f(a, n);
			an[n] = f;
			System.out.print(f + ", ");
		}
		System.out.println("");
		
		long[] d1n = new long[nM]; 
		for (int n = 2; n < nM; n++) {
			d1n[n] = an[n] - 4 * an[n - 1] + 2 * an[n - 2];
			System.out.print(d1n[n] + ", ");
		}
		System.out.println("");
	}
}

//1,3,11,39,135,463,1583,5407,18463,63039,215231,734847,2508927,8566015,29246207
/*

    0, 2, 6, 18, 58, 194, 658, 2242, 7650, 26114, 89154,
    
    0, 1, 3,  7, 19,  59




*/