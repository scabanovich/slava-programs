package slava.puzzle.paths.walker.analysis;

import com.slava.common.RectangularField;

public class WalkerPathsRunner {

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(10, 10);
		WalkerPathsSolver solver = new WalkerPathsSolver();
		solver.setField(f);
		int[] filter = new int[f.getSize()];
		filter[f.getIndex(0, 4)] = 1;
		filter[f.getIndex(f.getWidth() - 1, 4)] = 1;
		filter[f.getIndex(2, 4)] = 1;
		filter[f.getIndex(f.getWidth() - 3, 4)] = 1;
		solver.setFilter(filter);
		solver.solve();
		int sc = solver.getSolutionCount();
		System.out.println("Solutions=" + sc + " Tree=" + solver.getTreeCount());
	}

}
