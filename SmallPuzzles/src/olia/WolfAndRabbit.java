package olia;

public class WolfAndRabbit {
	
	static double f(double a) {
		return Math.PI * 3d / 2d - a - 1d/Math.tan(a);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double a1 = 0.1;
		double a2 = 0.5;
		while(a2 - a1 > 0.0001) {
			double f1 = f(a1), f2 = f(a2);
			double ac = (a1 + a2) / 2d, fc = f(ac);
			if(fc < 0) a1 = ac; else a2 = ac;
		}
		double ac = (a1 + a2) / 2d;
		System.out.println(ac);
		System.out.println(1d/Math.sin(ac));
		

	}

}
