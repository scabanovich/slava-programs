package champukr2007;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrimeBulls {
	int max = 1000000;
	int min = max / 10;
	
	int primeCount = 0;
	int[] primes = new int[min];
	
	int prime2Count = 0;
	int[] primes2 = new int[min];
	
	public PrimeBulls() {}
	
	public void process() {
		for (int i = 2; i < max; i++) {
			if(!isPrime(i)) continue;
			primes[primeCount] = i;
			primeCount++;
			if(!isAppropriate(i)) continue;
			primes2[prime2Count] = i;
			prime2Count++;
//			System.out.println(i);
		}
		System.out.println("primeCount=" + primeCount);
		System.out.println("prime2Count=" + prime2Count);
	}
	
	boolean isPrime(int p) {
		for (int i = 0; i < primeCount; i++) {
			int q = primes[i];
			if(p % q == 0) return false;
			if(q * q > p) return true;
		}
		return true;
	}
	
	boolean isAppropriate(int p) {
		if(p < min || p >= max) return false;
		int[] digits = new int[10];
		int q = p;
		while(q > 0) {
			int d = q % 10;
			if(digits[d] > 0) return false;
			digits[d] = 1;
			q = q / 10;
		}
		
		return true;
	}
	
	public boolean testRandom(int size) {
		int[] numbers = new int[size];
		for (int i = 0; i < size; i++) {
			int j = (int)(Math.random() * prime2Count);
			numbers[i] = primes2[j];
		}
		boolean b = test(numbers);
		if(b) {
			System.out.println("Numbers");
			for (int i = 0; i < numbers.length; i++) {
				System.out.println(numbers[i]);
			}
		}
		return b;
	}
	
	public boolean test(int[] numbers) {
		Set keys = new HashSet();
		for (int i = 0; i < prime2Count; i++) {
			StringBuffer v = new StringBuffer();
			for (int j = 0; j < numbers.length; j++) {
				int[] bc = getBullsAndCows(primes2[i], numbers[j]);
				v.append(" ").append(bc[0]).append(" ").append(bc[1]);
			}
			String vl = v.toString();
//			System.out.println(vl);
			if(keys.contains(vl)) {
				if(i > 4000) {
					System.out.println("--failed at " + i);
//					testAndReport(numbers);
				}
				return false;
			}
			keys.add(vl);
		}
		return true;
	}
	
	public int[] getBullsAndCows(int p1, int p2) {
		int[] digits1 = new int[10];
		int[] digits2 = new int[10];
		int bulls = 0;
		int cows = 0;
		while(p1 > 0 || p2 > 0) {
			int d1 = p1 % 10;
			int d2 = p2 % 10;
			if(d1 == d2) bulls++;
			digits1[d1]++;
			digits2[d2]++;
			p1 = p1 / 10;
			p2 = p2 / 10;
		}
		for (int i = 0; i < 10; i++) {
			if(digits1[i] > 0 && digits2[i] > 0) cows++;
		}
		cows -= bulls;
		return new int[]{bulls, cows};
	}

	public boolean testAndReport(int[] numbers) {
		Map keys = new HashMap();
		int result = 0;
		for (int i = 0; i < prime2Count; i++) {
			StringBuffer v = new StringBuffer();
			for (int j = 0; j < numbers.length; j++) {
				int[] bc = getBullsAndCows(primes2[i], numbers[j]);
				v.append(" ").append(bc[0]).append(" ").append(bc[1]);
			}
			String vl = v.toString();
			if(keys.containsKey(vl)) {
				Integer p1 = (Integer)keys.get(vl);
				System.out.println("Duplicate " + p1 + " and " + primes2[i]);
				System.out.println("With code " + vl);
				result++;
//				return result == 0;
			}
			keys.put(vl, new Integer(primes2[i]));
		}
		if(result > 0) {
			System.out.println("Total Duplicated = " + result);
		}
		return result == 0;
	}
	
	void testUKRSolution() {
		int[] numbers = new int[]{103289, 258469, 420397, 514823, 679501, 765913, 836749};
		testAndReport(numbers);
	}
		
	
	public static void main(String[] args) {
		PrimeBulls p = new PrimeBulls();
		p.process();
		p.testUKRSolution();
//		while(!p.testRandom(10)) {}
	}

}

/**
425869
827063
756421
631847
246193
465013
609173
513749
582137
456901
794203

41927
89273
63781
57829
27043
36251
12539
97613

*/