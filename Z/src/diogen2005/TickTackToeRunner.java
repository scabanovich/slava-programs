package diogen2005;

public class TickTackToeRunner {

	public static void run() {
		TickTackToeField field = new TickTackToeField();
		field.setSize(8, 8);
		TickTackToeGenerator g = new TickTackToeGenerator();
		g.setField(field);
		g.generateMinimum();
//		g.generateUp(16);
	}
	
	static int e = -1;
	static int O = 0;
	static int X = 1;

	static int[] PROBLEM = {
		O,O,e,e,e,O,O,e,
		O,O,O,e,e,e,e,e,
		e,O,e,e,e,e,e,e,
		e,e,e,e,e,e,e,e,
		X,e,e,e,e,e,e,O,
		e,e,e,e,e,e,e,e,
		X,e,e,e,e,e,O,O,
		e,e,X,e,e,X,X,e,
	};
	
	public static void solve(int[] problem) {
		TickTackToeField field = new TickTackToeField();
		field.setSize(8, 8);
		TickTackToeSolver solver = new TickTackToeSolver();
		solver.setCrossCount(-1);
		solver.setField(field);
		solver.setInitialValues(problem);
		solver.setMakeDistribution(true);
		solver.solve();
		System.out.println("Solution Count=" + solver.solutionCount + " Tree Count=" + solver.treeCount);
		int nc = 0;
		for (int i = 0; i < problem.length; i++) if(problem[i] != e) ++nc;
		System.out.println("Numbers=" + nc);
		int[] solution = solver.getSolution();
		if(solution != null) System.out.println(solver.solutionToString(solution));
		if(solver.solutionCount > 1 && solution != null) {
			int[] nw = solver.getNarrowestPlaceInfo();
			System.out.println("Best Place=" + field.x[nw[0]] + ":" + field.y[nw[0]] + " " + (nw[1] == 0 ? 'O' : 'X') + " count=" + nw[2]);
		}
	}

	public static void main(String[] args) {
		run();
//		solve(PROBLEM);
	}

}

/*
Problem 21 (Attempt=227229)
 + o o o + + + + o +
 + x o o + + x o + +
 + o + o + + + + o o
 + + + + + + + + + +
 + + + + + + + o + +
 + + + + + + + + + +
 o + o + o + x + + +
 o + + + + + + x + +
 + + + + o + + + + +
 + + + + + + + + + +
Solution  Complexity=8
 x o o o x o o x o x
 o x o o o x x o o x
 x o o o x o x x o o
 x o x x x o x o x x
 x x o o o x o o x o
 o x o x o x o o o x
 o x o x o o x x x o
 o o x x x o x x x o
 x x x o o x o o o x
 o x x x o x o x x x
Problem 22 (Attempt=463830)
 + o + + + + + + + o
 o o + + + + + + + o
 + o o + + + o + + +
 o + + + + + + + + o
 o o + + o + + + o o
 + + + + + + + + + o
 + + + + + + o + + +
 + + + + + + o o + +
 + + + + + o o + + +
 + + + + + o + + + +
Solution  Complexity=9
Problem 22 (Attempt=980548)
 + + o + o + x x + +
 o + + + + x x x + o
 + + + + + + + + + +
 o o + + + + + + o x
 + + + + o + + + + +
 + + + + + o o + o +
 x + + + + + + + + +
 + + o + + + + + + +
 + + + + o o o + + +
 + + + + + + + + + +
Solution  Complexity=19
Problem 22 (Attempt=447943)
 + + o + + + + + + +
 + + o o o + + o + +
 + + o + + + + + + +
 + + + + + + o + + +
 o + x + + + o o + +
 o o + + + + o o o +
 + o + o + + + + + +
 o + + + + + + + + +
 + + + o + + + + + +
 + + + + + + + o o +
Solution  Complexity=1522

Problem 15 (Attempt=5368172)
 o o + + + o + o
 o o o + + + + +
 + o + + + + + +
 + + + + + + + o
 x + + + + x o +
 + + + + + + + +
 + + + + + + o +
 x o + + + + + +
Problem 16 (Attempt=5423835)
 o o + + + + o o
 o o o + + + + o
 + o + + + + + +
 + + + + + + + +
 x + + + + + o +
 + + + + + + + x
 x + + + + + o +
 + + x + + o + +
16; a1,b1,g1,h1,a2,b2,c2,h2,b3,g5,g7,f8;a5,h6,a7,c8
Solution  Complexity=3
 o o x x o x o o
 o o o x x o o o
 x o o o x x x o
 o x o x x o o x
 x o x x o o o x
 x o o o x o x x
 x o x x o x o o
 o x x x o o o x
Problem 16 (Selfmade)
	O,O,e,e,e,O,O,e,
	O,O,O,e,e,e,e,e,
	e,O,e,e,e,e,e,e,
	e,e,e,e,e,e,e,e,
	X,e,e,e,e,e,e,O,
	e,e,e,e,e,e,e,e,
	X,e,e,e,e,e,O,O,
	e,e,X,e,e,X,X,e,
Problem 16 (Attempt=189868)
 o o + + x + + +
 o o o + + + + o
 + o + + + + + +
 + + + + + + + o
 + + + + + + + +
 + + + + + + + +
 x x + + + x + x
 + x x + o + + +
Solution  Complexity=3
 o o x o x x o x
 o o o x o o x o
 x o o o x o x o
 x x o x x o x o
 o x x x o x o x
 x o o o x x o o
 x x x o o x x x
 o x x x o o o x
Problem 16 (Attempt=2181405)
 o o + + x + + o
 o o o + + + + o
 + o + + + + + +
 + + + + + + + +
 + + + + + + + +
 + + + + + + + +
 x x + + o + o +
 x x x + + + + +
Problem 17 (Attempt=264826)
 + + + + + o + x
 + o o + + + o +
 + o o o + x + x
 + + o + + + + +
 x + + + + + + +
 + + + + o + + +
 x + + + + x + +
 + o + + + + + o

*/