package forsmarts;

import com.slava.common.TwoDimField;

public class WorldWarRunner {
	static int[] FORM = {
		1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1
	};
	
	static int E = -1;

	static int[] DATA_0 = {
		E,1,E,E,E,E,E,E,E,
		E,1,E,2,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,3,E,E,E,E,
		E,E,E,4,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,5,E,E,
		E,E,E,E,E,E,E,E,E
	};

	static int[] DATA_1 = {
		E,E,E,E,E,E,E,E,E,
		E,E,3,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		3,E,E,E,3,E,3,E,E,
		E,E,E,E,5,E,E,E,E,
		E,E,3,E,3,E,3,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E
	};

	static int[] DATA_2 = {
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,2,E,E,E,
		E,E,2,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,6,E,2,E,E,E,
		E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,6,E,E,E,
		E,E,E,E,E,E,E,4,E,
		E,E,E,E,E,E,E,E,E
	};

	
	static void solve() {
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(9, 9);
		WorldWarSolver solver = new WorldWarSolver();
		solver.setField(f);
		solver.setForm(FORM);
		WorldWarPlacements ps = new WorldWarPlacements();
		ps.createPlacements(f, FORM);
		solver.setPlacements(ps);
		solver.setData(DATA_2);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}
	
	public static void main(String[] args) {
		solve();
	}

}

/**

 . a . . . . . . .
 a a . . . . . . .
 . . a . . . d . .
 . b . c . . d . .
 b b c c d d d . .
 . . b . c . . d .
 . . . . . . . . d
 . . . . . . . . .
 . . . . . . . . .
 0 3 1 0 0

 . . a . . . . . .
 . . . a . . . . .
 . . . b a a a . .
 . . . b a . . . .
 . b b b a . . . .
 . . . c b . . d .
 . . c c e b d d .
 . . . . c e e . d
 . . . . . e . . .
 0 3 2 0 0

*/