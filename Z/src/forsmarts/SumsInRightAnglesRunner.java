package forsmarts;

public class SumsInRightAnglesRunner {
	
	static int[] VALUES = {
		0,1,3,0,0,3,0,0,
		4,0,1,4,1,2,1,1,
		2,3,2,0,0,1,1,5,
		0,6,5,1,1,3,0,0,
		0,4,0,4,2,2,3,0,
		5,2,4,0,6,2,3,5,
		0,3,3,2,1,5,1,0,
		1,0,2,4,2,4,3,0,
	};

	public static void main(String[] args) {
		SumsInRightAnglesSolver solver = new SumsInRightAnglesSolver();
		SumsInRightAnglesField f = new SumsInRightAnglesField();
		f.setSize(8, 8);
		solver.setField(f);
		solver.setValues(VALUES);
		solver.solve();
		System.out.println("Solution count=" + solver.solutionCount);		
	}

}

//5,4,3,3,4,6,4,2