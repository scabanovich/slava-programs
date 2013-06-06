package slava.puzzle.paths.rook.analysis;

import com.slava.common.RectangularField;

public class RookPathsGenerator {
	RectangularField field;
	RookPathsSimpleSolver solver = new RookPathsSimpleSolver();
	int[] solution;
	int[] problem;
	
	public RookPathsGenerator() {
		solver.setRandomizing(true);
		solver.setSolutionLimit(2);
	}

	public void setField(RectangularField f) {
		field = f;
		solver.setField(f);
	}
	
	public int[] generateSimple() {
		solution = generatePath();
		return reduceSimple();
	}
	
	public int[] generatePath() {
		RookPathsSimpleSolver solver = new RookPathsSimpleSolver();
		int[] data = new int[field.getSize()];
		for (int i = 0; i < data.length; i++) data[i] = -1;
		data[0] = 0;
		data[data.length - 1] = data.length - 1;
		solver.setField(field);
		solver.setData(data);
		solver.setRandomizing(true);
		solver.setSolutionLimit(1);
		solver.solve();
		int[] solution = solver.getSolution();
		solver.print(solution);
		return solution;
	}
	
	public int[] reduceSimple() {
		int[] order = getRandomOrder();
		int[] problem = (int[])solution.clone();
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			if(p == 0 || p == problem.length - 1) continue;
			int v = problem[p];
			problem[p] = -1;
			solver.setData(problem);
			solver.solve();
			if(solver.getSolutionCount() > 1) {
				problem[p] = v;
			} else {
				solver.print(problem);
			}
		}
		solver.print(problem);
		return problem;
	}
	
	int[] getRandomOrder() {
		int[] q = new int[field.getSize()];
		for (int i = 0; i < q.length; i++) q[i] = i;
		for (int i = 0; i < q.length; i++) {
			int j = i + (int)((q.length - i) * Math.random());
			int c = q[j];
			q[j] = q[i];
			q[i] = c;
		}
		return q;
	}
	

}
