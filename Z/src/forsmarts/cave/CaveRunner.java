package forsmarts.cave;

public class CaveRunner {
	static int E = -1;
	
	static int[] DATA = {
		E,2,E,6,E,E,E,E,E,
		E,E,3,E,E,7,E,E,E,
		E,E,6,3,E,E,E,4,E,
		E,3,E,E,E,E,3,E,E,
		E,E,E,3,E,8,E,E,E,
		4,E,5,E,7,E,E,E,E,
		E,E,E,3,E,E,E,4,E,
		E,E,E,4,E,E,3,E,E,
		E,E,E,E,E,E,E,E,E,
	};

	public static void main(String[] args) {
		CaveField f = new CaveField();
		f.setSize(9, 9);
		CaveSolver solver = new CaveSolver();
		solver.setField(f);
		solver.setData(DATA);
		solver.solve();
		System.out.println(solver.getSolutionCount());

	}

}

/*

 0 0 0 0 0 0 0 0 0
 0 0 1 1 1 1 0 0 0
 0 1 1 1 0 1 1 0 0
 0 0 1 0 0 1 0 0 0
 1 0 1 1 0 1 1 1 0
 1 1 1 1 0 1 1 1 0
 1 0 1 1 0 1 0 0 0
 1 0 0 0 0 1 1 0 0
 1 1 1 0 0 0 0 0 0
 
Solution found
 0 0 0 0 0 0 0 0 0
 0 0 1 1 1 1 0 1 0
 0 1 1 1 0 1 0 1 0
 0 0 1 0 0 1 0 1 0
 1 0 1 1 0 1 1 1 0
 1 1 1 1 0 1 1 1 0
 1 0 1 1 0 1 0 0 0
 1 0 0 0 0 1 1 0 0
 1 1 1 0 0 0 0 0 0

*/