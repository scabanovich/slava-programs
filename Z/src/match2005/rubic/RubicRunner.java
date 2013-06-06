package match2005.rubic;

public class RubicRunner implements RubicConstants {
	
	static void test() {
		RubicState state = RubicState.getOrigin();
		state.print();
		for (int i = 0; i < ROTATIONS.length; i++) {
			System.out.println("i=" + i);
			ROTATIONS[i].execute(state);
			state.print();
			ROTATIONS[REVERSE_ROTATION[i]].execute(state);
			state.print();
		}
	}

	static void testSolver() {
		RubicRandomStateGenerator g = new RubicRandomStateGenerator();
		RubicState state = g.generateState(12);
		System.out.println("Path=" + g.getPath());
		System.out.print("Cube state=");
		state.print();
		RubicSolver solver = new RubicSolver();
		solver.setState(state);
		solver.setMaximumTime(19);
		solver.setOutFileName("RubicSolutions.txt");
		long t = System.currentTimeMillis();
		solver.execute();
		t = System.currentTimeMillis() - t;
		System.out.println("Time=" + t);
	}
	
	static void testVicinity() {
		Vicinity v = new Vicinity(RubicVicinityMarker.RUBIC_EDGES_TASK);
		RubicRandomStateGenerator g = new RubicRandomStateGenerator();
		for (int i = 0; i < 20; i++) {
			RubicState s = g.generateState(40);
			int k = v.states[v.task.getStateCode(s)];
			System.out.println(k);
		}
	}
	
	static void testSequence() {
		RubicState s = new RubicState();
		s.corners = new int[]{6, 7, 11, 18, 21, 12, 0, 1};
		s.edges = new int[]{12, 13, 16, 15, 14, 23, 18, 19, 20, 21, 22, 17};
		s.print();
//		                     T  F' L'  B' F2  R' T  F   L  F'  L' F  T' H'  R' T2
		int[] rs = new int[]{0, 4, 16, 10, 5, 7, 0, 3, 15, 4, 16, 3, 1, 13, 7, 2};
		for (int i = 0; i < rs.length; i++) {
			ROTATIONS[rs[i]].execute(s);
			s.print();
		}
	}
	
	static void enumerateOriginVicinity() {
		RubicVicinityMarker marker = new RubicVicinityMarker();
		marker.execute();
	}
	
	static void testPermutations() {
		for (int i = 0; i < ROTATIONS.length; i++) {
			RubicState s = RubicState.getOrigin();
			ROTATIONS[i].execute(s);
			s.print();
			System.out.println(s.isEvenCornerPermutation() + " " + s.isEvenEdgePermutation());
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) testSolver();
//		testVicinity();
//		testSequence();
		
//		enumerateOriginVicinity();
	}

}
