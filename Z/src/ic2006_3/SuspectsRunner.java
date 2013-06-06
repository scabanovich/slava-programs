package ic2006_3;

import com.slava.common.RectangularField;

public class SuspectsRunner {
	static int[] FORM = {
		0,0,1,0,0,1,0,0,1,0,0,1,
		0,0,1,0,0,0,0,1,0,0,0,0,
		0,0,0,0,0,1,0,0,0,0,0,0,
		0,0,1,1,0,0,0,1,0,0,0,1,
		1,0,0,0,0,0,0,0,0,1,0,0,
		0,0,0,0,1,1,0,1,0,0,0,0,
		0,0,1,0,0,1,0,0,0,1,0,1,
		0,0,0,0,0,0,0,0,0,0,0,1,
		0,1,0,0,0,0,0,1,0,0,0,0,
		0,0,0,1,0,0,1,0,0,1,0,0
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(12, 10);
		SuspectsSolver solver = new SuspectsSolver();
		solver.setField(f);
		solver.setForm(FORM);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

}

/**
SolutionCount=11
*/