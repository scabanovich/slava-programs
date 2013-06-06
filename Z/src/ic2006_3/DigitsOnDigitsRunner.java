package ic2006_3;

import com.slava.common.RectangularField;

public class DigitsOnDigitsRunner {
	
	static int[] FILTER = {
		1,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,1,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,1,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
	};
	
	static int[][] sums = {
		//[p][d][s]
		{19, 3, 14},
		{60, 7, 12},
		{69, 5, 14},
		{80, 7, 15},
		{94, 6, 25},
		{96, 5, 16}
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(10, 10);
		DigitsOnDigitsFiguresBuilder b = new DigitsOnDigitsFiguresBuilder();
		b.createPlacements(DigitsOnDigitsFigures.FIGURES, f);
		DigitsOnDigitsSolver solver = new DigitsOnDigitsSolver();
		solver.setField(f);
		solver.setPlacements(b.placements);
		solver.setFilter(FILTER);
		solver.setSums(sums);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount);
	}

}

/**
 + 1 2 3 4 5 + 1 2 3
 3 2 1 5 + 5 4 3 + 4
 4 + + 4 + 6 + 2 + 1
 5 1 2 3 2 1 + 1 2 +
 4 1 2 3 5 1 + 2 3 1
 3 2 1 4 4 2 + 1 + 2
 + + + 1 3 3 4 5 4 3
 + + 2 1 2 1 2 3 + +
 + + 3 2 1 6 + 4 + +
 + + + 3 4 5 + 5 + +
 
 -2-35243--

 + 3 3 3 3 3 + 2 2 2
 0 0 0 1 + 1 1 1 + 2
 0 + + 1 + 1 + 1 + 2
 0 0 0 1 1 1 + 1 2 +
 0 2 2 2 3 0 + 0 2 0
 0 0 0 2 3 0 + 0 + 0
 + + + 2 3 0 0 0 0 0
 + + 2 1 3 1 1 1 + +
 + + 2 1 3 1 + 1 + +
 + + + 1 1 1 + 1 + +

*/