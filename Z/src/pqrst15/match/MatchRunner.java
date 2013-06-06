package pqrst15.match;

public class MatchRunner {

	public static void main(String[] args) {
		MatchField f = new MatchField();
		f.setSize(7);
		MatchSolver solver = new MatchSolver();
		solver.setField(f);
		solver.solve();
	}

}
