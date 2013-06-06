package forsmarts;

/**
 * Forsmarts. Season 7, Issue 20 No 23. Three/vision.
 * 
 * Place in grids some 3-cell figures, not touching one another. 
 * Digits in cells show the number of cells occupied by figures 
 * in the same row and both diagonals, not counting the cell 
 * with the digit itself. All cells with digits belong to the 
 * figures.
 */

public class ThreeVisionRunner {
	static int[] FORM = {
		1,1,1,1,0,0,0,
		1,1,1,1,1,0,0,
		1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,
		0,0,1,1,1,1,1,
		0,0,0,1,1,1,1
	};
	
	static int E = -1;
	
	static int[] DOTS_1 = {
		E,E,E,E,E,E,E,
		E,E,2,E,E,E,E,
		E,E,E,E,E,E,E,
		E,4,E,3,E,E,E,
		E,E,E,E,E,E,4,
		E,E,E,E,E,E,E,
		E,E,E,E,E,E,E
	};

	static int[] DOTS_2 = {
		E,E,E,E,E,E,E,
		E,3,E,E,E,E,E,
		E,E,E,5,E,E,E,
		E,E,E,E,E,6,E,
		E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,
		E,E,E,E,2,E,E
	};

	static int[] DOTS_EX = {
		E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,
		E,E,1,E,E,E,E,
		E,E,E,E,E,7,E,
		E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,
		E,E,E,E,E,E,E
	};
	
	public static void generate(int edgeLength, int piecesCount, int maxHintCount) {
		int width = edgeLength * 2 - 1;
		int size = width * width;
		ThreeVisionSolver solver = null;
		ThreeVisionField f = new ThreeVisionField();
		f.setSize(width, width);
		int[] form = new int[size];
		for (int p = 0; p < size; p++) {
			if(Math.abs(f.getX(p) - f.getY(p)) >= edgeLength) {
				form[p] = 0;
			} else {
				form[p] = 1;
			}
		}
		
		int attemptCount = 0;
		while(true) {
			while(true) {
				attemptCount++;
				int[] dots = new int[size];
				for (int p = 0; p < size; p++) dots[p] = E;
				f.makePlacements(form, dots);
				
				ThreeVisionFiller filler = new ThreeVisionFiller();
				filler.setField(f);
				filler.setPieceCount(piecesCount);
				filler.solve();
				f.makePlacements(form, dots);
				
				System.out.println("Solution:");
				filler.printProblem();
				
				solver = new ThreeVisionSolver();
				solver.setField(f);
				solver.solve();
//				System.out.println("SolutionCount=" + solver.solutionCount);
				if(solver.getSolution() != null && solver.solutionCount == 1) {
					break;
				} else {
					System.out.println("Filler generated state with " + solver.solutionCount + " solutions.");
				}
				
			}
			reduce(f);

			System.out.println("Problem:");
			solver.printProblem();
			int hc = getHintCount(f);
			System.out.println("Hint count = " + hc);
			System.out.println("");
			if(hc <= maxHintCount) break;
		}
//		solver.printSolution(solver.getSolution());
		System.out.println("Attempts=" + attemptCount);
	}
	
	public static void reduce(ThreeVisionField f) {
		int[] order = new int[f.getSize()];
		for (int i = 0; i < order.length; i++) order[i] = i;
		for (int i = 0; i < order.length; i++) {
			int j = (int)((order.length - i) * Math.random()) + i;
			int c = order[i];
			order[i] = order[j];
			order[j] = c;
		}
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			if(f.form[p] == 0 || f.dots[p] <= 0) continue;
			int d = f.dots[p];
			f.dots[p] = -1;
			f.makePlacements(f.form, f.dots);
			ThreeVisionSolver solver = new ThreeVisionSolver();
			solver.setField(f);
			solver.setSolutionLimit(2);
			solver.solve();
			if(solver.solutionCount != 1) {
				f.dots[p] = d;
				f.makePlacements(f.form, f.dots);
//				System.out.println("Cannot reduce " + p);
			} else {
//				System.out.println("Reduced " + p);
			}
		}
	}
	static int getHintCount(ThreeVisionField f) {
		int c = 0;
		for (int p = 0; p < f.getSize(); p++) {
			if(f.dots[p] > 0) ++c;
		}
		return c;
	}
	
	public static void solveCurrentProblem() {
		ThreeVisionField f = new ThreeVisionField();
		f.setSize(7, 7);
		f.makePlacements(FORM, DOTS_EX);
		
		ThreeVisionSolver solver = new ThreeVisionSolver();
		solver.setField(f);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
		if(solver.getSolution() != null) {
			solver.printSolution(solver.getSolution());
		}
	}

	public static void main(String[] args) {
		//solveCurrentProblem();
		
		//edgeLength, piecesCount, hintCount
		int size = 0;
		if(size == 0) {
			generate(4, 5, 4);
		} else if(size == 1) {
			generate(5, 5, 6);
		}
	}

}

//a) 9,11,12
//b) 12,13,14,16,17
