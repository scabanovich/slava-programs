package ic2006_3;

import com.slava.common.TwoDimField;

public class SixteenRunner {
	static int[] INITIAL = {
		0, 0, 0, 0,
		3, 0, 0, 0,
		0, 0, 0, 0,
		0, 0,10, 0,
	};

	public static void main(String[] args) {
		TwoDimField f =  new TwoDimField();
		f.setOrts(TwoDimField.DIAGONAL_ORTS);
		f.setSize(4, 4);
		SixteenSolver solver = new SixteenSolver();
		solver.setField(f);
		solver.setInitialState(INITIAL);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

}

/**
Solution 1
 14  9 15 11
  3  6  1  8
 12 16 13  5
  7  4 10  2

key = 14 6 13 2
SolutionCount=1
*/