package forsmarts;

/**
 * Forsmarts. Season 8, Issue 27 No 14. Three-O.
 * 
 * Fill some cells with letters "O", so that each "circle" 
 * consisting of 12 cells contain exactly three "O"s. 
 * Grey cells must stay empty. 
 */

public class ThreeORunner {
	static int[] FORM = {
		1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1,
	};
	
	/**
	 * Hear dots are cells where letter 'O' cannot be inserted. 
	 */
	static int[] DOTS_1 = {
		0,1,0,0,1,0,0,0,0,
		1,0,1,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		1,0,0,0,1,0,0,0,0,
		0,0,0,0,0,0,0,0,1,
		0,0,1,0,0,0,0,0,1,
		0,0,0,0,0,0,1,0,0,
		0,0,0,0,0,1,1,0,0,
		0,0,0,0,1,0,0,0,0,
	};
	
	static int[] DOTS_EX = {
		1,0,1,0,0,0,0,0,0,
		0,0,0,1,1,0,0,0,0,
		0,1,0,0,1,0,0,0,0,
		0,1,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,1,
		0,0,0,1,0,0,0,1,0,
		0,0,0,0,1,0,0,1,0,
		0,0,0,0,0,0,0,0,0,
	};
	

	public static void main(String[] args) {
		ThreeOField f = new ThreeOField();
		f.setSize(9, 9);
		f.makePlacements(FORM, DOTS_1);
		ThreeOSolver solver = new ThreeOSolver();
		solver.setField(f);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount + " TreeCount=" + solver.treeCount);
	}

}

/*
 . * . . * + + + +
 * O * . . . + + +
 O . O . . . O + +
 * . . . * O O O +
 . O O O . . . . *
 + . * . . . . O *
 + + . O . . * . O
 + + + O O * * . .
 + + + + * . . . O
-OO-----O,-OOO-----,-O-------

*/