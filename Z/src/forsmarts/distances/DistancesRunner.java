package forsmarts.distances;

public class DistancesRunner {
	static int[] DATA = {
		0,0,0,0,1,1,1,0,0,
		0,1,1,0,1,1,0,1,1,
		0,0,0,0,0,0,0,0,0,
		0,0,0,1,1,0,1,1,0,
		0,1,0,1,0,0,1,1,1,
		0,0,1,1,0,1,1,0,0,
		1,1,0,1,0,0,0,0,0,
		0,0,0,0,0,0,1,1,0,
		0,0,0,0,0,1,1,0,0,
	};

	public static void main(String[] args) {
		DistancesField f = new DistancesField();
		f.setSize(9, 9);
		DistancesSolver solver = new DistancesSolver();
		solver.setField(f);
		solver.setData(DATA);
		solver.solve();
		System.out.println("Solution count=" + solver.getSolutionCount());
	}

}
