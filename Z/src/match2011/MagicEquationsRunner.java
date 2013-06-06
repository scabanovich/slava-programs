package match2011;

import com.slava.common.RectangularField;
import com.slava.dominopack.DominoPackingRunner;
import com.slava.dominopack.DominoPackingSolver;

public class MagicEquationsRunner {

	static void runMagicSquare(int n) {
		int sum = (n * n + 1) * n / 2;
		Equation[] eqs = new Equation[2 * n + 2];
		for (int i = 0; i < n; i++) {
			int[] pos = new int[n];
			for (int j = 0; j < n; j++) {
				pos[j] = i * n + j;
			}
			eqs[i] = new Equation(pos, sum);
		}
		for (int i = 0; i < n; i++) {
			int[] pos = new int[n];
			for (int j = 0; j < n; j++) {
				pos[j] = i + j * n;
			}
			eqs[i + n] = new Equation(pos, sum);
		}
		int[] pos = new int[n];
		for (int j = 0; j < n; j++) {
			pos[j] = j * (n + 1);
		}
		eqs[2 * n] = new Equation(pos, sum);
		pos = new int[n];
		for (int j = 0; j < n; j++) {
			pos[j] = (j + 1) * (n - 1);
		}
		eqs[2 * n + 1] = new Equation(pos, sum);

		int maxValue = n * n;
		int[] valueLimits = new int[maxValue + 1];
		for (int i = 1; i <= maxValue; i++) {
			valueLimits[i] = 1;
		}

		MagicEquationsSolver solver = new MagicEquationsSolver();
		solver.setEquations(eqs);		
		solver.setValueLimits(valueLimits);
		solver.addInitialValue(0, 24);
		solver.addInitialValue(24, 25);
		solver.solve();
		System.out.println("Solutions=" + solver.getSolutionCount());
	}

	public static int runMagicDominoSet() {
		//Equations for rectangular 8 * 7
		int width = 8;
		int height = 7;
		int[] valueLimits = new int[7];
		for (int i = 0; i < valueLimits.length; i++) {
			valueLimits[i] = 8;
		}
		
		Equation[] eqs = new Equation[width + height + 5];
		for (int i = 0; i < height; i++) {
			int[] pos = new int[width];
			for (int j = 0; j < width; j++) {
				pos[j] = i * width + j;
			}
			eqs[i] = new Equation(pos, 24);
		}
		for (int i = 0; i < width; i++) {
			int[] pos = new int[height];
			for (int j = 0; j < height; j++) {
				pos[j] = i + j * width;
			}
			eqs[i + height] = new Equation(pos, 21);
		}
		if(eqs.length > 15) {
			eqs[15] = new Equation(new int[]{2,9,16}, 8);
			eqs[16] = new Equation(new int[]{5,14,23}, 8);
			eqs[17] = new Equation(new int[]{32,41,50}, 8);
			eqs[18] = new Equation(new int[]{39,46,53}, 8);
		}
		if(eqs.length > 19) {
			eqs[19] = new Equation(new int[]{3,10,17,24}, 8);
		}
		
		//Equations solver
		MagicEquationsSolver solver = new MagicEquationsSolver() {
			public void onSolutionFound(int[] state) {
				DominoPackingSolver solver = DominoPackingRunner.run(state);
				if(solver.getSolutionCount() > 0) {
					int[] s = solver.getSolution();
//					printSolution();
					System.out.println("Breaks=" + getBreakCount(solver.getField(), s));
					if(getBreakCount(solver.getField(), s) == 0) { 
						throw new RuntimeException("Finita");
					}
				}
			}
		};
		solver.setEquations(eqs);		
		solver.setValueLimits(valueLimits);

		//Generating diagonals
		MagicDiagonalsGenerator g = new MagicDiagonalsGenerator();
		int[] initial = g.generate();
		for (int p = 0; p < initial.length; p++) {
			if(initial[p] >= 0) {
				solver.addInitialValue(p, initial[p]);
			}
		}
		int eq = 0;
		if(initial[1] > 1) {
			eq++;
			solver.addInitialValue(8, 8 - initial[1]);
		}
		if(initial[6] > 1) {
			eq++;
			solver.addInitialValue(15, 8 - initial[6]);
		}
		if(initial[49] > 1) {
			eq++;
			solver.addInitialValue(40, 8 - initial[49]);
		}
		if(initial[54] > 1) {
			eq++;
			solver.addInitialValue(47, 8 - initial[54]);
		}
		if(eq < 4) {
			return 0;
		}
		solver.solve();
		return solver.getSolutionCount();
	}

	static int getBreakCount(RectangularField f, int[] dominoPartition) {
		int c = 0;
		int[] h = new int[f.getHeight()];
		int[] w = new int[f.getWidth()];
		for (int p = 0; p < f.getSize(); p++) {
			int v = dominoPartition[p];
			if(v == 0) {
				w[f.getX(p)]++;
			} else if(v == 1) {
				h[f.getY(p)]++;
			}
		}
		for (int y = 0; y < h.length - 1; y++) if(h[y] == 0) c++;
		for (int x = 0; x < w.length - 1; x++) if(w[x] == 0) c++;
		return c;
	}

	public static void main(String[] args) {
		while(true) {
			int s = runMagicDominoSet();
			if(s > 0) {
				System.out.println("-m-->" + s);
			}
		}
	}

}

/**

MinSum=8, Diagonals=12, Breaks=0
Solution:
 0 2 0 2 1 0 2 1
 1 0 2 1 3 0 2 3
 3 1 1 3 0 2 1 1
 1 3 3 0 2 1 3 3
 3 1 1 0 2 3 1 1
 1 3 3 1 1 1 3 3
 3 0 2 3 3 3 0 2
Tree count=7
Solution count=1
 0 2 4 5 6 5 2 0
 6 1 1 4 5 1 0 6
 3 6 3 0 0 3 6 3
 2 4 5 0 0 4 6 3
 3 5 3 2 2 3 4 2
 6 1 1 5 4 0 1 6
 1 2 4 5 4 5 2 1

MaxSum=34 Breaks=0 Solution=1
Solution:
 1 1 1 1 0 2 0 2
 3 3 3 3 1 0 2 1
 1 1 0 2 3 0 2 3
 3 3 1 1 0 2 0 2
 0 2 3 3 0 2 1 1
 1 0 2 0 2 1 3 3
 3 0 2 0 2 3 0 2
Tree count=19
Solution count=1
 6 5 3 0 0 1 3 6
 2 4 4 0 0 6 6 2
 3 0 4 4 6 5 0 2
 2 2 3 6 6 1 0 4
 2 1 3 4 6 5 2 1
 1 5 3 4 2 3 5 1
 5 4 1 3 1 0 5 5


MinSum=8, Diagonals=8, Breaks=1
Solution:
 1 0 2 0 2 1 0 2
 3 0 2 0 2 3 0 2
 0 2 0 2 1 0 2 1
 1 0 2 1 3 0 2 3
 3 0 2 3 0 2 1 1
 1 1 1 0 2 1 3 3
 3 3 3 0 2 3 0 2
Tree count=103
Solution count=12
 1 2 4 5 4 5 3 0
 6 1 1 5 5 1 0 5
 3 6 2 2 0 2 6 3
 2 4 4 0 0 5 6 3
 3 5 3 1 1 3 4 4
 6 1 3 2 5 1 0 6
 0 2 4 6 6 4 2 0


Diagonals=7, Breaks=1
Solution:
 0 2 0 2 0 2 1 1
 1 0 2 1 1 1 3 3
 3 0 2 3 3 3 1 1
 1 0 2 0 2 1 3 3
 3 0 2 0 2 3 1 1
 1 0 2 1 0 2 3 3
 3 0 2 3 0 2 0 2
Tree count=9
Solution count=3
 0 3 4 6 4 4 3 0
 5 1 3 4 3 1 2 5
 3 6 2 1 3 2 5 2
 3 3 6 0 0 5 5 2
 4 6 1 1 1 1 4 6
 6 0 1 4 5 2 0 6
 0 2 4 5 5 6 2 0


*/