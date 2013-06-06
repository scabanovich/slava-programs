package olia;

import java.util.*;

public class PrimeSequence {
	
	public boolean isPrime(long k) {
		if(k < 1) return false;
		if(k < 4) return true;
		if(k % 2 == 0) return false;
		long max = (long)Math.sqrt(k);
		for (int i = 3; i <= max; i += 2) {
			if(k % i == 0) return false;
		}
		return true;
	}
	
	public long[] step(long[] vs, int n, int dn) {
		Set set = new TreeSet();
		for (int i = 0; i < vs.length; i++) {
			long m = vs[i] * n;
			for (int j = 0; j <= dn; j++) {
				long k = m + j;
				if(isPrime(k)) set.add(new Long(k));
			}
		}
		
		Long[] is = (Long[])set.toArray(new Long[0]);
		long[] rs = new long[is.length];
		for (int i = 0; i < rs.length; i++) rs[i] = is[i].longValue();
		return rs;
	}
	
	public void solve() {
		int n = 1;
		long[] vs = new long[]{1};
		while(true) {
			vs = step(vs, n, n + n);
			print(vs, n);
			++n;
			if(vs.length == 0) break;
		}
	}

	public void solveFixed(int n) {
		long[] vs = new long[]{9};
		while(true) {
			vs = step(vs, n, n - 1);
			print(vs, n);
			if(vs.length == 0) break;
		}
	}
	
	void print(long[] vs, int n) {
		System.out.print(n + ": " + vs.length + " :");
		for (int i = 0; i < vs.length && i < 10; i++) System.out.print(" " + vs[i]);
		System.out.println("");
	}


	public static void main(String[] args) {
		PrimeSequence p = new PrimeSequence();
		p.solveFixed(10);
	}
}
