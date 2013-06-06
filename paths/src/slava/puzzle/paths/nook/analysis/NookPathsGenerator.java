package slava.puzzle.paths.nook.analysis;

import slava.puzzle.paths.nook.model.NookPathsPuzzle;

import com.slava.common.RectangularField;

public class NookPathsGenerator {
	NookPathsPuzzle puzzle;
	RectangularField field;
	int[] filter;
	NookPathsSimpleSolver solver = new NookPathsSimpleSolver();
	int[] solution;
	int[] problem;
	
	int treeLimit = 1;
	
	public NookPathsGenerator() {
		solver.setRandomizing(true);
		solver.setSolutionLimit(2);
	}
	
	public void setTreeLimit(int s) {
		treeLimit = s;
	}

	public void setField(RectangularField f) {
		field = f;
		solver.setField(f);
	}
	
	public void setFilter(int[] filter) {
		this.filter = filter;
		solver.setFilter(filter);
	}
	
	public void setPuzzle(NookPathsPuzzle puzzle) {
		this.puzzle = puzzle;
		setFilter(puzzle.getFilter());
	}
	
	public int[] generateSimple() {
		solution = generatePath();
		return reduceSimple();
	}
	
	public int[] generatePath() {
		NookPathsSimpleSolver solver = new NookPathsSimpleSolver();
		solver.setField(field);
		if(filter != null) solver.setFilter(filter);

		int[] data = new int[field.getSize()];
		for (int i = 0; i < data.length; i++) data[i] = -1;
		if(puzzle != null) {
			NookPathsPuzzleCheck.setStartAndEndValues(puzzle, data);
		} else {
			data[0] = 0;
			data[1] = solver.nook.getFilteredSize() - 1;
		}

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
			if(filter != null && filter[p] == 1) continue;
			if(problem[p] == 0 || problem[p] == solver.nook.getFilteredSize() - 1) continue;
			int v = problem[p];
			problem[p] = -1;
			solver.setData(problem);
			solver.solve();
			if(solver.getSolutionCount() > 1 || (treeLimit > 0 && solver.treeCount > treeLimit)) {
				problem[p] = v;
			} else {
				System.out.println("TreeCount=" + solver.treeCount);
				solver.print(problem);
				if(solver.getSolutionCount() == 0) {
					solver.solve();
					throw new RuntimeException("Error");
				}
			}
		}
		solver.print(problem);
		int n = 0;
		for (int i = 0; i < problem.length; i++) if(problem[i] >= 0) ++n;
		System.out.println("Given values=" + n);
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
