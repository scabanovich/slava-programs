package forsmarts;

import com.slava.common.RectangularField;

public class ManifoldDividingRunner {
	static int[] DATA = {
		1,1,1,1,1,1,1,1,
		1,0,0,0,1,1,1,0,
		1,0,0,1,0,0,0,1,
		1,1,1,1,0,0,0,0,
		1,0,0,0,1,1,1,0,
		1,0,0,0,0,0,0,1,
		1,0,0,1,0,0,0,1,
		1,0,1,1,0,1,0,0,
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		ManifoldDividingPlacements ps = new ManifoldDividingPlacements();
		ps.createPlacements(f, DATA);
		ManifoldDividingSolver solver = new ManifoldDividingSolver();
		solver.setField(f);
		solver.setPlacements(ps);
		solver.solve();
		System.out.println("Solution Count=" + solver.solutionCount + " Tree Count=" + solver.treeCount);

	}

}

//B1, B2, D2, E2, F7, F8
