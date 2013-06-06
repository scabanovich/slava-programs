package ic2006_3;

import com.slava.common.RectangularField;

public class OpticomizationRunner {
	
	static int[] NUMBERS = {
		1,2,3,4,1,2,3,4,1,
		2,3,4,1,2,3,4,1,2,
		3,4,1,2,3,4,1,2,3,
		4,1,2,3,4,1,2,3,4,
		1,2,3,4,1,2,3,4,1,
		2,3,4,1,2,3,4,1,2,
		3,4,1,2,3,4,1,2,3,
		4,1,2,3,4,1,2,3,4,
		1,2,3,4,1,2,3,4,1
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(9, 9);
		OpticomizationSolver solver = new OpticomizationSolver();
		solver.setField(f);
		solver.setNumbers(NUMBERS);
		solver.solve();
	}

}

/**
maxGoodShipCount=7
 + + + x x x x + x  9
 x x + + + + + + x  8
 + + + x x + + + +  7
 + + + + + + x x x  6
 x x x x x + + + +  5
 + + + + + + + + +  4
 x + x x x x + x +  3
 x + + + + + + x +  2
 x + + + x x + x +  1
 
 a b c d e f g h i 
 
 e1H, d7H, a8H, i9V, a3V, h3V, g6H, c3H, d9H, a5H
 
maxGoodRayCount=7
 + + + x + x + + x  9
 x x + x + x + + x  8
 + + + x + + + + +  7
 + + + + + + x x +  6
 x x x x x + + + +  5
 + + + + + + + + +  4
 x x x + + x x x x  3
 + + + + + + + + +  2
 x x x + x x x x +  1
 
 a b c d e f g h i 

 g6H, a8H, f9V, i9V, a1H, a3H, d9V, e1H, f3H, a5H

6 - 3 + 3 - 4 - 3 - 2 + 3
*/