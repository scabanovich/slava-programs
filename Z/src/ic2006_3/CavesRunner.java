package ic2006_3;

import com.slava.common.RectangularField;

public class CavesRunner {
	static int[] VISUAL_DATA = {
		0,0,0,0,0,0,5,0,5,	
		0,0,2,2,0,0,0,0,0,	
		0,4,0,2,3,0,4,2,0,	
		0,0,0,0,0,0,0,0,0,	
		3,0,0,4,0,0,0,3,0,	
		0,4,0,0,0,7,0,2,0,	
		0,0,0,2,0,0,0,0,0,	
		5,3,0,0,0,0,0,0,0,	
		0,0,0,0,0,7,0,1,0,	
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(9, 9);
		CavesSolver solver = new CavesSolver();
		solver.setField(f);
		solver.setVisualData(VISUAL_DATA);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount + " treeCount=" + solver.treeCount);
	}

}

/**
Solution
 0 3 3 3 0 0 0 0 0
 0 3 0 0 0 1 0 0 0
 0 3 3 3 0 1 1 0 1
 0 0 0 0 0 0 1 1 1
 4 4 4 4 2 0 1 1 1
 2 4 4 4 2 0 0 0 1
 2 4 2 2 2 0 0 1 1
 2 2 2 0 0 0 0 0 1
 2 2 2 2 0 0 0 1 1
 
key=36,7,7,36,14,36,36,36,16
*/