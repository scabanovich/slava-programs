package olia;

public class Equation1 {
	
	public static double X_LnX(double x) {
		return -x * Math.log(x);
	}
	
	public static double function(double t) {
		return (X_LnX(t) + X_LnX(2 * t) + X_LnX(1 - 3 * t) - (1 - 2 * t) * Math.log(2)); 
	}

	public static double function3(double t) {
		return (X_LnX(t) + X_LnX(3 * t) + X_LnX(1 - 4 * t) - (1 - 3 * t) * Math.log(3) + 4 * t * Math.log(2)); 
	}
	
	public static void solve2() {
		double t = 0.0632271d;
		double r = Equation1.function(t);
		System.out.println(r);
		double u = 1 - 3 * t;
		System.out.println(u);
	}

	public static void solve3() {
		double t = 0.06379703;
		double r = Equation1.function3(t);
		System.out.println(r);
		double u = 1 - 4 * t;
		System.out.println(u);
	}

	public static void main(String[] args) {
		solve3();
	}
}
