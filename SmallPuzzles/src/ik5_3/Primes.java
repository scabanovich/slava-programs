package ik5_3;

public class Primes {
	int result = 2005;	
	int[] primes;
	
	void initPrimes() {
		primes = new int[700];
		primes[0] = 1;
		primes[1] = 2;
		int i = 2;
		int k = 3;
		while(i < primes.length) {
			while(!isPrime(k)) k += 2;
			primes[i] = k;
//			System.out.println(k);
			i++;
			k +=2;
		} 
	}
	
	boolean isPrime(int k) {
		for (int i = 1; i < primes.length; i++) {
			int p = primes[i];
			if(p * p > k) return true;
			if(k % p == 0) return false;
		}
		return true;
	}
	
	public void runPlusMinus(int q) {
		int[] ns = new int[q];
		for (int i = 0; i < primes.length - q; i++) {
			int delta = (primes[i + q - 1] - primes[i]);
			System.arraycopy(primes, i, ns, 0, q);
			runPlusMinus(ns, delta);
		}
	}
	
	void runPlusMinus(int[] ns, int delta) {
		int sum = 0;
		for (int i = 0; i < ns.length; i++) sum += ns[i];
		if(sum < result) return;
		if(sum == result) {
			print(ns, delta);
			return; 
		}
		int w = 1;
		for (int i = 0; i < ns.length; i++) w *= 2;
		for (int i = 0; i < w; i++) {
			int u = i;
			sum = 0;
			for (int j = 0; j < ns.length; j++) {
				if(u % 2 == 0) sum -= ns[j]; else sum += ns[j];
				u = u / 2;
			}
			if(sum == 2005) {
				print(ns, delta);
			}
		}		
	}
	
	public void runPlusMinusOneProduct(int q) {
		int[] ns = new int[q - 1];
		for (int i = 0; i < primes.length - q; i++) {
			int delta = (primes[i + q - 1] - primes[i]);
			for (int m = 0; m < q - 1; m++) {
				System.arraycopy(primes, i, ns, 0, m);
				ns[m] = primes[i + m] * primes[i + m + 1];
				System.arraycopy(primes, i + m + 2, ns, m + 1, q - m - 2);
				runPlusMinus(ns, delta);
			}
		}
	}
	
	public void getAllSets(int maxDelta) {
		for (int i = 1; i < primes.length - 6; i++) {
			int min = primes[i];
			int j = i;
			while(j < primes.length && primes[j] - min <= maxDelta) ++j;
			int k = j - i;
			if(k < 4) continue;
			int[] ns = new int[k];
			System.arraycopy(primes, i, ns, 0, k);
			//int delta = ns[ns.length - 1] - ns[0];
//			if(ns.length > 8) continue;
			compute(ns, ns, new int[0]);
		}
	}
	
	void print(int[] ns, int delta) {
		System.out.print(delta + " : ");
		for (int i = 0; i < ns.length; i++) {
			System.out.print(" " + ns[i]);
		}
		System.out.println("");
	}
	
	
	void compute(int[] ns0, int[] ns, int[] operations) {
		if(ns.length == 1 && (ns[0] == result || ns[0] == -result)) {
			int delta = ns0[ns0.length - 1] - ns0[0];
			print(ns0, delta);
			for (int i = 0; i < operations.length; i++) {
				System.out.print(" " + operations[i]);
			}
			System.out.println("");
			return;
		}
		for (int i = 0; i < ns.length - 1; i++) {
			for (int s = 0; s < 4; s++) {
				if(s == 3 && (ns[i + 1] == 0 || ns[i] % ns[i + 1] != 0)) continue;
				int[] ns1 = new int[ns.length - 1];
				System.arraycopy(ns, 0, ns1, 0, i);
				ns1[i] = (s == 0) ? ns[i] + ns[i + 1] :
				(s == 1) ? ns[i] - ns[i + 1] :
				(s == 2) ? ns[i] * ns[i + 1] :
				ns[i] / ns[i + 1];
				System.arraycopy(ns, i + 2, ns1, i + 1, ns.length - i - 2);
				int[] op1 = new int[operations.length + 2];
				System.arraycopy(operations, 0, op1, 0, operations.length);
				op1[operations.length] = i;
				op1[operations.length + 1] = s;
				compute(ns0, ns1, op1);
			}
		}
	}

	public static void main(String[] args) {
		Primes p = new Primes();
		p.initPrimes();
//		p.runPlusMinus(5);
//		p.runPlusMinusOneProduct(4);
		p.getAllSets(12);
	}

}

//653 - 659 + 661 + 673 + 677
//-5 * (7 - (11 + 13) * 17)
