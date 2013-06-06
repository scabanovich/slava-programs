package smallpuzzles.summedsequence;

import com.slava.common.RectangularField;

public class SummedSequenceGenerator {
	SummedSequenceSolver solver = new SummedSequenceSolver();
	RectangularField field;
	int[] form;

	int[] problem;
	int[] solution;

	public SummedSequenceGenerator() {}

	public void setSize(int width, int height) {
		field = new RectangularField();
		field.setOrts(RectangularField.DIAGONAL_ORTS);
		field.setSize(width, height);
		solver.setField(field);
	}

	public void setForm(int[] f) {
		form = f;
	}

	
	public void generate() {
		int attemptCount = 0;
		while(!attempt()) {
			attemptCount++;
		}
		System.out.println("Problem:");
		solver.printSolution(problem);
		System.out.println("Solution:");
		solver.printSolution(solution);
	}

	boolean attempt() {
		solver.setSolutionLimit(1);
		solver.setInitialState(null);
		solver.setRandomising(true);
		solver.solve();
		if(solver.getSolutionCount() == 0) return false;
		problem = new int[field.getSize()];
		int[] ss = solver.getSolution();
		for (int p = 0; p < problem.length; p++) {
			if(form[p] == 1) problem[p] = ss[p];
		}
		solver.setInitialState(problem);
		solver.setSolutionLimit(-1);
		solver.solve();
		System.out.println(solver.getSolutionCount());
		if(solver.getSolutionCount() == 1) {
			solution = solver.getSolution();
			System.out.println("Tree count=" + solver.getTreeCount());
			return true;
		}
		return false;
	}

	static int[] FORM = {
		1, 0, 0, 0, 1,
		0, 1, 0, 0, 0,
		0, 0, 1, 0, 0,
		0, 0, 0, 1, 0,
		1, 0, 0, 0, 1,
	};

	public static void main(String[] args) {
		SummedSequenceGenerator g = new SummedSequenceGenerator();
		g.setSize(5, 5);
		g.setForm(FORM);
		g.generate();
	}
	
}
