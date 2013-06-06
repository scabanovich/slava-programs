package forsmarts;

import com.slava.common.TwoDimField;

public class ArithmeticSpiralRunner {
	static int E = -1; //empty
	static int A = 0; //addition
	static int S = 1; //substruction
	static int M = 2; //multiplication
	static int D = 3; //division
	
	static int[] DATA = {
		E,D,5,E,2,M,E,
		A,E,M,9,E,8,A,
		8,A,E,D,4,E,9,
		E,7,S,E,S,3,E,
		5,E,6,A,E,A,6,
		A,4,E,6,A,E,A,
		E,D,4,E,8,D,E,		
	};
	
	static int[] generateSpiral(int startPoint, int startDirection, int width) {
		TwoDimField f = new F();
		f.setSize(width, width);
		int[] spiral = new int[f.getSize()];
		int[] state = new int[spiral.length];
		spiral[0] = startPoint;
		state[startPoint] = 1;
		int p = startPoint;
		int d = startDirection;
		for (int t = 1; t < state.length; t++) {
			int q = f.jump(p, d);
			int r = 0;
			while(q < 0 || state[q] > 0) {
				d += 1;
				if(d < 0) d += 4;
				if(d >= 4) d = d % 4;
				q = f.jump(p, d);
				r++;
				if(r > 3) throw new RuntimeException("Cannot make step " + t);
			}			
			p = q;
			spiral[t] = p;
			state[p] = 1;
			
		}
		return spiral;
	}

	public static void main(String[] args) {
		int[] spiral = generateSpiral(48, 3, 7);
		ArithmeticSpiralSolver solver = new ArithmeticSpiralSolver();
		solver.setData(DATA);
		solver.setOrder(spiral);
		solver.solve();
	}
	
	static class F extends TwoDimField {
		public F() {
			setOrts(TwoDimField.RECTANGULAR_ORTS);
		}
	}

}

/*
start = 48, dir = 3
 3 / 5 + 2 * 7
 + 3 * 9 - 8 +
 8 + 2 / 4 + 9
 + 7 - 8 - 3 -
 5 + 6 + 5 + 6
 + 4 - 6 + 7 +
 4 / 4 + 8 / 4

 3328574
*/