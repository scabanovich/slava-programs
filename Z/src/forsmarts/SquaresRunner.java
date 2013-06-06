package forsmarts;

import com.slava.common.RectangularField;

public class SquaresRunner {
	static int E = -1;
	
	static int[] SUMS_A = {
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,8,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,10,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
	};

	static int[] SUMS_B = {
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,14,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,12,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,E,E,
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(11, 11);
		SquaresSolver solver = new SquaresSolver();
		solver.setField(f);
		solver.setSquaresCount(7);
		solver.setSums(SUMS_B);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount);

	}

}

//a) 0 4 11 17 13 18 18 5 6 5
//b) 1 0 10 14 17 17 11 7 2 0