package ic2006_1;

public class Brakets {
//                      3/1+4/1-5/9+2/6-5/3+5/8
	double[] NUMBERS = {3d,1d,4d,1d,5d,9d,2d,6d,5d,3d,5d,8d};
	int[] SIGNS      =  {3,0,3,1,3,0,3,1,3,0,3};
	
	double maximum = -1E+09;
	double minimum = 1E+09;
	int[] maximumSequence;
	int[] minimumSequence;
	int[] sequence;
	
	public void compute() {
		maximum = -1E+09;
		minimum = 1E+09;
		sequence = new int[SIGNS.length];
		compute(NUMBERS, SIGNS);
	}
	
	public void compute(double[] numbers, int[] signs) {
		if(numbers.length == 1) {
			double q = numbers[0];
				//Math.abs(numbers[0]);
			if(q > maximum) {
				maximum = q;
				maximumSequence = (int[])sequence.clone();
				System.out.println("Maximum=" + maximum);
				for (int i = sequence.length - 1; i >= 0; i--) {
					System.out.print(" " + sequence[i]);
				}
				System.out.println("");
			}
			if(q < minimum) {
				minimum = q;
				minimumSequence = (int[])sequence.clone();
				System.out.println("Minimum=" + minimum);
				for (int i = sequence.length - 1; i >= 0; i--) {
					System.out.print(" " + sequence[i]);
				}
				System.out.println("");
			}
		} else {
			for (int i = 0; i < signs.length; i++) {
				sequence[signs.length - 1] = i;
				double[] ns = new double[numbers.length - 1];
				if(i > 0) System.arraycopy(numbers, 0, ns, 0, i);
				if(i <= signs.length - 1) System.arraycopy(numbers, i + 2, ns, i + 1, ns.length - i - 1);
				int[] ss = new int[signs.length - 1];
				if(i > 0) System.arraycopy(signs, 0, ss, 0, i);
				if(i <= signs.length - 1) System.arraycopy(signs, i + 1, ss, i, ss.length - i);
				double a = numbers[i];
				double b = numbers[i + 1];
				int s = signs[i];
				if(s == 3 && Math.abs(b) < 1E-10) continue;
				double rs = (s == 0) ? a + b :
							(s == 1) ? a - b :
							(s == 2) ? a * b :
							(s == 3) ? ((b == 0) ? 0 : a / b) : 0;
				ns[i] = rs;
//				if(numbers.length == 2 && signs[0] == 1) { 
//					System.out.println("i=" + i);
//					for (int k = 0; k < ns.length; k++) {
//						System.out.print(" " + ns[k]);
//					}
//					System.out.println("");
//					for (int k = 0; k < ss.length; k++) {
//						System.out.print(" " + ss[k]);
//					}
//					System.out.println("");
//				}
				compute(ns, ss);
			}
		}
	}
	
	public static void main(String[] args) {
		double d1 = 3d/((1d+4d/(1d-5d/(9d+2d)/6d-5d))/(3d+5d)/8d);
		System.out.println("max=" + d1);
		double d2 = 3d/1d+4d/((1d-(5d/9d+2d))/(6d-5d/3d+5d))/8d;
		System.out.println("min=" + d2);
//		Brakets p = new Brakets();
//		p.compute();
	}
	
}

// 3/((1+4/(1-5/(9+2)/6-5))/(3+5)/8)
// 3/1+4/((1-(5/9+2))/(6-5/3+5))/8
