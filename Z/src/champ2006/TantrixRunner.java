package champ2006;

public class TantrixRunner {
	static int[] FORM_1 = {
		1,1,1,1,1,0,
		1,1,1,1,1,1,
		0,1,1,1,1,1
	};
	
	static int[] FORM_2 = {
		1,1,1,1,
		1,1,1,1,
		1,1,1,1,
		1,1,1,1
	};

	static int[] FORM_3 = {
		1,1,1,1,0,
		1,1,1,1,1,
		0,1,1,1,1,
		0,0,1,1,1
	};

	static class LongestPathFinder implements TantrixSolver.PackingListener {
		int path = 0;
		
		public void packingFound(TantrixSolver solver) {
			int cpath = solver.getLongestPath();
			if(cpath >= path) {
				path = cpath;
				System.out.println("Solution " + path);
				solver.printSolution();
			}
		}
		
	}
	
	static class ExtremumProductsFinder implements TantrixSolver.PackingListener {
		int minProduct = 1000000000;
		int maxProduct = 0;
		
		public void packingFound(TantrixSolver solver) {
			int product = solver.getProduct();
			if(product > maxProduct) {
				maxProduct = product;
				System.out.println("Maximum product = " + product);
				solver.printSolution();
			}
			if(product < minProduct) {
				minProduct = product;
				System.out.println("Minimum product = " + product);
				solver.printSolution();
			}
		}
		
	}
	
	static void runPuzzle1() {
		TantrixField f = new TantrixField();
		f.setSize(6, 3);
		TantrixSolver solver = new TantrixSolver();
		solver.setField(f);
		solver.setForm(FORM_1);
		solver.setListener(new LongestPathFinder());
		solver.solve();
	}

	static void runPuzzle2() {
		TantrixField f = new TantrixField();
		f.setSize(4, 4);
		TantrixSolver solver = new TantrixSolver();
		solver.setField(f);
		solver.setForm(FORM_2);
		solver.setListener(new LongestPathFinder());
		solver.solve();
	}

	static void runPuzzle3() {
		TantrixField f = new TantrixField();
		f.setSize(5, 4);
		TantrixSolver solver = new TantrixSolver();
		solver.setField(f);
		solver.setForm(FORM_3);
		solver.setListener(new ExtremumProductsFinder());
		solver.solve();
	}

	public static void main(String[] args) {
		//runPuzzle1();
		//runPuzzle2();
		runPuzzle3();
	}

}

//2
//Solution 16
//(2,3) (1,1) (0,1) (7,0) (14,1) (11,0) (6,2) (4,3) (10,1) (9,5) (15,1) (13,0) (8,3) (3,2) (12,2) (5,4)

/**
Maximum product = 18662400
 (0,0) (1,2) (8,4) (2,0) (13,2) (15,2) (14,5) (6,0) (5,2) (7,5) (12,3) (11,4) (10,2) (3,3) (4,2) (9,0)
Minimum product = 1764
 (3,0) (7,2) (14,0) (10,5) (12,0) (4,4) (1,1) (8,0) (5,0) (6,0) (9,3) (0,0) (11,4) (2,4) (13,2) (15,2)

*/