package slava.puzzle.scyscripers.analysis;

import com.slava.common.RectangularField;

public class ScyscripersGenerator {
	RectangularField field;
	ScyscripersSolver solver1 = new ScyscripersSolver();
	ScyscripersSolver2 solver2 = new ScyscripersSolver2();
	int[][] problemVisible;
	int[] solution;
	
	public ScyscripersGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
		solver1.setField(f);
		solver1.setSolutionLimit(1);
		solver1.setRandomizing(true);
		solver2.setField(f);
		solver2.setSolutionLimit(50);
		solver2.setTreeCountLimit(1000);
	}
	
	public void generate() {
		solution = null;
		int minSolutionCount = solver2.solutionLimit;
		int attemptCount = 0;
		while(solution == null) {
			attemptCount++;
			if(attemptCount % 1000 == 0) System.out.println("ac=" + attemptCount);
			solver1.solve();
			extractVisibles(solver1.getSolution());
			solver2.setVisibles(problemVisible);
			solver2.solve();
			if(minSolutionCount > solver2.getSolutionCount()) {
				minSolutionCount = solver2.getSolutionCount();
				if(minSolutionCount > 1) solver2.setSolutionLimit(minSolutionCount);
				System.out.println(minSolutionCount + " " + solver2.treeCount);
			}
			if(solver2.getSolutionCount() == 1) {
				solution = solver2.getSolution();
			}
		}
		System.out.println("attemptCount=" + attemptCount);
		solver2.printSolution();
		reduce();
	}
	
	void extractVisibles(int[] state) {
		problemVisible = new int[4][field.getWidth()];
		for (int d = 0; d < 4; d++) {
			for (int i1 = 0; i1 < field.getWidth(); i1++) {
				int ix = (d == 0) ? 0 : (d == 2) ? field.getWidth() - 1 : i1; 
				int iy = (d == 1) ? 0 : (d == 3) ? field.getHeight() - 1 : i1;
				int p = field.getIndex(ix, iy);
				int h = state[p];
				problemVisible[d][i1] = 1;
				while(true) {
					p = field.jump(p, d);
					if(p < 0) break;
					if(state[p] > h) {
						h = state[p];
						problemVisible[d][i1]++;
					}
				}
			}
		}
	}
	
	void reduce() {
		int[] r = new int[4 * field.getWidth()];
		for (int i = 0; i < r.length; i++) r[i] = i;
		for (int i = 0; i < r.length; i++) {
			int j = i + (int)((r.length - i) * Math.random());
			int c = r[i];
			r[i] = r[j];
			r[j] = c;
		}
		for (int i = 0; i < r.length; i++) {
			int d = r[i] % 4;
			int k = r[i] / 4;
			int h = problemVisible[d][k];
			problemVisible[d][k] = 0;
			solver2.solve();
//			System.out.println(solver2.getSolutionCount());
			if(solver2.getSolutionCount() != 1) {
				problemVisible[d][k] = h;
			}
		}
		solver2.solve();
		System.out.println("\nTreeCount=" + solver2.treeCount);
		solver2.printSolution();		
	}
	
	public void generateMany() {
		while(true) {
			generate();
			System.out.print("Generate one more?(y/n):");
			int i = 13;
			try {
				while((i != (int)'y') && (i != (int)'n')) i = System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(i != (int)'y') break;
		}
	}
	
	public static void main(String[] args) {
		ScyscripersGenerator g = new ScyscripersGenerator();
		RectangularField f = new RectangularField();
		f.setSize(6, 6);
		g.setField(f);
		g.generateMany();
	}

}

/**
TreeCount=168

    3 - 3 - - 6 - 2

-   5 7 6 8 2 1 4 3  3
-   4 6 3 7 5 2 8 1  2
6   2 4 5 6 7 3 1 8  -
4   3 1 4 5 8 6 2 7  -
2   7 8 1 4 6 5 3 2  5
-   8 2 7 3 1 4 6 5  -
3   1 3 8 2 4 7 5 6  3
2   6 5 2 1 3 8 7 4  -

    - 2 - 8 4 - 2 4

TreeCount=791

    - 5 - - 3 - 3 -

7   1 2 3 4 5 7 6 8  -
4   2 4 1 6 3 8 5 7  2
-   4 1 7 8 6 5 3 2  5
2   6 5 2 3 8 1 7 4  -
-   8 7 6 5 1 2 4 3  6
-   7 3 5 1 2 4 8 6  -
-   5 6 8 7 4 3 2 1  6
-   3 8 4 2 7 6 1 5  -

    4 - 2 - 2 2 3 -

TreeCount=379

    2 - 3 3 4 - - -

4   4 5 2 6 3 8 1 7  2
4   3 4 1 7 2 5 6 8  -
-   8 7 3 5 4 6 2 1  5
-   7 1 8 3 6 2 5 4  4
4   5 6 7 8 1 4 3 2  4
4   1 2 6 4 8 3 7 5  -
-   6 8 5 2 7 1 4 3  4
6   2 3 4 1 5 7 8 6  -

    4 - 5 4 - - 1 -
    
    - - 3 - - 3

-   5 2 3 6 1 4  -
-   1 4 2 5 6 3  -
-   3 6 5 4 2 1  5
1   6 5 1 3 4 2  4
-   4 1 6 2 3 5  2
-   2 3 4 1 5 6  -

    - 3 - 6 - -

    - - - - 4 -

5   1 2 4 5 3 6  -
4   2 3 5 6 4 1  3
-   6 1 3 4 5 2  3
3   3 5 6 1 2 4  -
-   4 6 2 3 1 5  -
2   5 4 1 2 6 3  -

    - 2 3 4 - -

*/