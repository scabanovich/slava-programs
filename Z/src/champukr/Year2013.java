package champukr;

import java.util.HashSet;
import java.util.Set;

public class Year2013 {

	static long getMaxDenominator(long a, long b) {
		if(a < 0) a = -a;
		if(b < 0) b = -b;
		while(a != b) {
			if(a == 1 || b == 0) return a;
			if(b == 1 || a == 0) return b;
			if(a > b) {
				a -= (a / b) * b;
			} else {
				b -= (b / a) * a; 
			}
		}
		return a;
	}
	
	static class Fraction implements Comparable<Fraction> {
		long a;
		long b;

		public Fraction(long a) {
			this(a, 1);
		}

		public Fraction(long a, long b) {
			if(b < 0) {
				a = -a;
				b = -b;
			}
			if(b != 1) {
				long c = getMaxDenominator(a, b);
				if(c > 1) {
					a = a / c;
					b = b / c;
				}
			}
			this.a = a;
			this.b = b;
		}

		public String toString() {
			return b == 1 ? "" + a : "" + a + "/" + b;
		}

		public Fraction add(Fraction f) {
			if(b == f.b) {
				return new Fraction(a + f.a, b);
			}
			long c = getMaxDenominator(b, f.b);
			if(c == 1) {
				return new Fraction(a * f.b + f.a * b, b * f.b);
			}
			return new Fraction(a * (f.b / c) + f.a * (b / c), (b / c) * f.b);
		}
		
		public Fraction substract(Fraction f) {
			if(b == f.b) {
				return new Fraction(a - f.a, b);
			}
			long c = getMaxDenominator(b, f.b);
			if(c == 1) {
				return new Fraction(a * f.b - f.a * b, b * f.b);
			}
			return new Fraction(a * (f.b / c) - f.a * (b / c), (b / c) * f.b);
		}

		public Fraction multiply(Fraction f) {
			return new Fraction(a * f.a, b * f.b);
		}
		
		public Fraction multiply(int n) {
			return new Fraction(a * n, b);
		}
		
		public Fraction divide(Fraction f) {
			return new Fraction(a * f.b, b * f.a);
		}

		@Override
		public int compareTo(Fraction o) {
			if(b == o.b) {
				return (int)(a - o.a);
			}
			return (int)substract(o).a;
		}

		public boolean equals(Object o) {
			if(o instanceof Fraction) {
				Fraction other = (Fraction)o;
				return a == other.a && b == other.b;
			}
			return false;
		}

		public int hashCode() {
			return (int)a;
		}
	
		public boolean isZero() {
			return a == 0;
		}
		
	}

	static Fraction[] FACTORIALS = new Fraction[10];

	Fraction number = new Fraction(2013, 1);
	Fraction digit;
	Fraction[][] sets = new Fraction[20][];
	Set<Fraction> total = new HashSet<Fraction>();
	Set<Fraction> current = new HashSet<Fraction>();

	public Year2013() {
	}

	public void init(int digit, Fraction number) {
		this.number = number;
		FACTORIALS[0] = new Fraction(1, 1);
		for (int i = 1; i < FACTORIALS.length; i++) {
			FACTORIALS[i] = FACTORIALS[i - 1].multiply(i);
		}
		this.digit = new Fraction(digit, 1);;
		add(this.digit);
		if((digit != 0 && digit < 3) || digit >= FACTORIALS.length) {
			//sets[1] = new int[]{digit};
		} else if(digit < FACTORIALS.length) {
			add(FACTORIALS[digit]);
		}
		
		Fraction[] is = current.toArray(new Fraction[current.size()]);
		sets[1] = new Fraction[is.length];
		for (int i = 0; i < is.length; i++) sets[1][i] = is[i];
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
			Fraction q = digit;
			for (int i = 2; i <= sc; i++) q = q.multiply(10).add(digit);
			add(q);
		}
		boolean res = false;
		for (int i = 1; i <= sc / 2; i++) {
			if(compute(sets[i], sets[sc - i], sc)) res = true;
		}
		Fraction[] is = current.toArray(new Fraction[current.size()]);
		sets[sc] = new Fraction[is.length];
		for (int i = 0; i < is.length; i++) sets[sc][i] = is[i];
		current.clear();
		return res;
	}
	
	boolean compute(Fraction[] s1, Fraction[] s2, int sc) {
		for (int i1 = 0; i1 < s1.length; i1++) {
			for (int i2 = 0; i2 < s2.length; i2++) {
				Fraction v1 = s1[i1];
				Fraction v2 = s2[i2];
				if(add(v1.add(v2))) {
					printResult(v1, v2, "+", sc);
					return true;
				};
				if(add(v1.substract(v2))) {
					printResult(v1, v2, "-", sc);
					return true;
				};
				if(add(v2.substract(v1))) {
					printResult(v2, v1, "-", sc);
					return true;
				};
				if(add(v1.multiply(v2))) {
					printResult(v1, v2, "*", sc);
					return true;
				};
				if(!v2.isZero() && add(v1.divide(v2))) {
					printResult(v1, v2, "/", sc);
					return true;
				};
				if(!v1.isZero() && add(v2.divide(v1))) {
					printResult(v2, v1, "/", sc);
					return true;
				};
				if(v1.a > 1 && v1.b == 1 && v1.a < 15 && Math.abs(v2.a) < 5000 && v2.b < 5) {
					Fraction v = new Fraction(1);
					boolean ok = true;
					for (int kk = 0; ok && kk < v1.a; kk++) {
						v = v.multiply(v2);
						if(Math.abs(v.a) > 10000000 || Math.abs(v.b) > 10000) ok = false;
					}
					if(ok && add(v)) {
						printResult(v2, v1, "^", sc);
						return true;
					}
				}
				if(v2.a > 1 && v2.b == 1 && v2.a < 15 && Math.abs(v1.a) < 5000 && v1.b < 5) {
					Fraction v = new Fraction(1);
					boolean ok = true;
					for (int kk = 0; ok && kk < v2.a; kk++) {
						v = v.multiply(v1);
						if(Math.abs(v.a) > 10000000 || Math.abs(v.b) > 10000) ok = false;
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
	
	int limit = 50000000;
	boolean add(Fraction q) {
		if(q.a > limit || q.a < -limit || q.b > 10000) return false;
		if(total.contains(q)) return false;
		total.add(q);
		current.add(q);
		if(q.equals(number)) return true;
		boolean result = false;
		if(q.a > 2 && q.a < FACTORIALS.length && q.b == 1) {
			if(add(FACTORIALS[(int)q.a])) result = true;
		}
//		if(addSummarialsAndQuadrials && q > 1) {
//			if(q < SUMMARIALS.length && add(SUMMARIALS[q])) result = true;
//			if(q < QUADRIALS.length && add(QUADRIALS[q])) result = true;
//		}
		
		return result;
		
	}
	void printResult(Fraction v1, Fraction v2, String sign, int number) {
		System.out.println("" + v1 + " " + sign + " " + v2 + " in " + number);
		System.out.println("" + v1 + " in " + findSet(v1, number) + "   " + v2 + " in " + findSet(v2, number));
	}
	
	int findSet(Fraction v, int numberLimit) {
		for (int i = 0; i < numberLimit; i++) {
			if(sets[i] != null) for (int j = 0; j < sets[i].length; j++) {
				if(sets[i][j].equals(v)) return i;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		Year2013 p = new Year2013();
//		p.addSummarialsAndQuadrials = false;
		p.init(9, new Fraction(2013,1));
		p.iterate();
	}
	

}
