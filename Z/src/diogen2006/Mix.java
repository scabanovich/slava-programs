package diogen2006;

public class Mix {
	public static Mix EMPTY = new Mix(0, 0, 0);
	int amount;
	long water;
	long shit;
	
	public Mix(int amount, long water, long shit) {
		this.amount = amount;
		this.water = water;
		this.shit = shit;		
	}
	
	public Mix mix(Mix other) {
		if(this.amount == 0) {
			return other.copy();
		} else if(other.amount == 0) {
			return this.copy();
		}
		int a = this.amount + other.amount;
		long w = this.amount * this.water * (other.water + other.shit) + 
		        other.amount * other.water * (this.water + this.shit);
		long s = this.amount * this.shit * (other.water + other.shit) + 
		        other.amount * other.shit * (this.water + this.shit);
		long d = getMaxCommonDivisor(w, s);
		w = w / d;
		s = s / d;		
		
		return new Mix(a, w, s);
	}
	
	public Mix copy() {
		return new Mix(amount, water, shit);
	}

	public Mix take(int a) {
		return (a > amount) ? null : new Mix(a, water, shit);
	}

	public Mix deduct(int a) {
		return (a > amount) ? null : 
			(a == amount) ? EMPTY : new Mix(amount - a, water, shit);
	}

	public String toString() {
		return "amount=" + amount + "(" + water + "/" + shit + ")=" + ((shit == 0) ? "inf" : "" + (1d * water / shit));
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Mix)) return false;
		Mix other = (Mix)o;
		return this.amount == other.amount &&
		       this.water == other.water &&
		       this.shit == other.shit;		
	}
	
	public static long getMaxCommonDivisor(long a, long b) {
		if(b == 0 && a == 0) return 1;
		if(a == 0) return b;
		if(b == 0) return a;
		if(a == 1 || b == 1) return 1;
		if(a > b) return getMaxCommonDivisor(a % b, b);
		if(b > a) return getMaxCommonDivisor(a, b % a);
		return a;
	}
	
	public static void computeBestRatios(double d) {
		double d0 = d;
		int a1 = 1;
		int b1 = 0;
		int a2 = (int)Math.round(Math.floor(d));
		d = d - a2;
		int b2 = 1;
		for (int i = 0; i < 10; i++) {
			d = 1 / d;
			int c = (int)Math.round(Math.floor(d));
			d = d - c;
			int a3 = c * a2 + a1;
			int b3 = c * b2 + b1;
			a1 = a2;
			b1 = b2;
			a2 = a3;
			b2 = b3;
			System.out.println("" + a3 + "/" + b3 + " delta=" + (d0 - 1d * a3 / b3));
		}
	}
	
}
