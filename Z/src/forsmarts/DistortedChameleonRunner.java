package forsmarts;

import com.slava.common.TwoDimField;

public class DistortedChameleonRunner {
	static int[] FILTER_7 = {
		1,1,1,1,0,0,0,
		1,1,1,1,1,0,0,
		1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,
		0,0,1,1,1,1,1,
		0,0,0,1,1,1,1,		
	};

	static int[] FILTER_9 = {
		1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1,
	};

	static int[][] SUM_RESTRICTIONS_1 = {
		{-1,-1,7,4,8,-1},
		{-1,-1,-1,7,5,-1},
		{-1,-1,8,-1,-1,-1}
	};
	
	static int[][] SUM_RESTRICTIONS_2 = {
		{-1,6,14,6,6,9,3,-1},
		{7,-1,10,15,-1,19,-1,9},
		{11,-1,-1,6,-1,14,8,-1},
	};
	
	static void runFirstExample() {
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(7, 7);
		DistortedChameleonSolver solver = new DistortedChameleonSolver();
		solver.setField(f);
		solver.setFigures(DistortedChameleonFigure.FIGURES_1_7);
		solver.setFilter(FILTER_7);
		solver.setSumRestrictions(SUM_RESTRICTIONS_1);
		solver.solve();
	}

	static void runSecondExample() {
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(9, 9);
		DistortedChameleonSolver solver = new DistortedChameleonSolver();
		solver.setField(f);
		solver.setFigures(DistortedChameleonFigure.FIGURES_0_9);
		solver.setFilter(FILTER_9);
		solver.setSumRestrictions(SUM_RESTRICTIONS_2);
		solver.solve();
	}

	public static void main(String[] args) {
		System.out.println("First Problem");
		runFirstExample();
		System.out.println("Second Problem");
		runSecondExample();
	}

}
//a) 6 4 5 7 2 3 1
//b) 5 2 8 1 0 7 9 6 4 3