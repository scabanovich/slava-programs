package smallpuzzles.jumpsbyarrows;

import com.slava.common.RectangularField;

public class JumpsByArrowsRunner {
	
	
	public static void solve(int[] arrows, int width) {
		JumpsByArrowsSolver solver = new JumpsByArrowsSolver();
		RectangularField f = new RectangularField();
		f.setSize(width, width);
		solver.setField(f);
		solver.setArrows(arrows);
		solver.solve();
		System.out.println("SolutionCount=" + solver.getSolutionCount());
		if(solver.getSolution() != null) {
			solver.printSolution(solver.getSolution());
		}
	}
	
	public static void generate(int width) {
		JumpsByArrowsGenerator g = new JumpsByArrowsGenerator();
		RectangularField f = new RectangularField();
		f.setSize(width, width);
		g.setField(f);
		g.generate();
	}
	
	static int[] TEST_ARROWS = {
		0,1,1,1,
		0,0,1,1,
		0,3,2,3,
		3,2,3,0
	};
	
	public static void main(String[] args) {
		//solve(TEST_ARROWS, 4);
		generate(4);
	}

}

/**
Arrows
 0 1 1 1
 1 2 2 3
 0 2 0 3
 0 3 2 3
Solution
  0  8  1 14
  4  3  2 13
 10  9 11 12
  5  7  6 15

Arrows
 1 1 1 2 1
 0 2 0 0 3
 0 0 2 3 1
 1 3 2 2 2
 3 0 3 2 2
Solution
  0  6 10  5 23
 19 18 20 21 22
  3 12 11  4 13
  1 17 16 15 14
  2  7  9  8 24

 */