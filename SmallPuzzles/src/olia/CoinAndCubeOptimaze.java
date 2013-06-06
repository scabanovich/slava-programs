package olia;

public class CoinAndCubeOptimaze {

	public double computeCoin(double x, double y) {
		double s = 0;
		s += (1d/2d) * Math.log(1d + 2d * x);
		s += (1d/2d) * Math.log(1d - 1d * x);
		return Math.exp(s);
	}
	

	public double compute(double x, double y) {
		double s = 0;
		s += (5d/12d) * Math.log(1d + 2d * x + y);
		s += (1d/12d) * Math.log(1d + 2d * x - y);
		s += (5d/12d) * Math.log(1d - 1d * x + y);
		s += (1d/12d) * Math.log(1d - 1d * x - y);
		return Math.exp(s);
	}
	
	public void optimize() {
		double best = 0;
		double x_b = 0;
		double y_b = 0;
		int iM = 10000, jM = 10000;
		for (int i = 0; i < iM; i++) {
			for (int j = 0; j < jM; j++) {
				double x = 1d * i / iM;
				double y = 1d * j / jM;
				if(x + y >= 1d) continue;
				double s = compute(x, y);
				if(s > best) {
					best = s;
					x_b = x;
					y_b = y;
				}
			}
		}
		System.out.println("x=" + x_b + " y=" + y_b + " b=" + best);
	}
	
	public static void main(String[] args) {
		CoinAndCubeOptimaze p = new CoinAndCubeOptimaze();
		double a = p.compute(1d/12d, 2d/3d);
		System.out.println(a);
		p.optimize();
	}

}
