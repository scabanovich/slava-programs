package ic2006_2;

public class LaserRunner implements LaserConstants {

	static int[] INITIAL = {
		0,0,0,0,0,0,0,0,A,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,B,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,C,0,
		0,0,0,0,0,0,0,0,0,D,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,E,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,E,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,F,0,0,0,0,0,0,
	};
	
	static int[] H_MIRRORS = {
		-1,-1,5,-1,4,2,-1,-1,-1,3,2,-1,-1
	};

	static int[] V_MIRRORS = {
		-1,-1,-1,-1,2,5,-1,-1,0,-1,3,-1,-1
//		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
	};
	
	static int[] H_CROSSINGS = {
		7,-1,4,-1,-1,-1,3,-1,-1,-1,-1,-1,-1,
//		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
	};

	static int[] V_CROSSINGS = {
		-1,-1,-1,-1,-1,-1,4,4,-1,-1,3,-1,-1
//	-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,-1,-1
	};

	public static void main(String[] args) {
		LaserField f = new LaserField();
		f.setSize(13);
		LaserSolver solver = new LaserSolver();
		solver.setField(f);
		solver.setInitialState(INITIAL);
		solver.setMirrorsRestriction(H_MIRRORS, V_MIRRORS);
		solver.setCrossingRestriction(H_CROSSINGS, V_CROSSINGS);
		solver.solve();
	}

}

//Key=14,9,16