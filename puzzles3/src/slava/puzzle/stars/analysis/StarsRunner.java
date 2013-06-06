package slava.puzzle.stars.analysis;

import slava.puzzle.stars.model.StarsSetsField;

public class StarsRunner {
	static int[] COLORS = {
		0,0,0,0,1,1,1,1,1,1,	
		0,0,2,0,0,0,1,3,1,1,	
		2,0,2,0,2,0,3,3,3,1,	
		2,0,2,2,2,0,3,5,1,1,	
		2,2,2,4,4,5,5,5,1,1,	
		2,4,4,4,5,5,6,7,1,1,	
		6,6,6,6,6,6,6,7,7,7,	
		8,6,8,6,8,8,8,7,9,9,	
		8,6,8,8,8,9,8,8,8,9,	
		8,8,8,8,9,9,9,9,9,9,	
	};
	
	static void solve() {
		StarsRegionField f = new StarsRegionField();
		f.setSize(10);
		StarsSolver solver = new StarsSolver();
		f.setRegions(COLORS);
		solver.setField(f);
		solver.solve();
		if(solver.getSolutionCount() > 0) {
			solver.printSolution(solver.getSolution()[0]);
		}
		System.out.println(" solutions=" + solver.getSolutionCount() + " treeSize=" + solver.getTreeSize());
	}
	
	static void generate() {
		StarsSetsField f = new StarsSetsField();
		f.setSize(11);
		StarRegionsGenerator g = new StarRegionsGenerator();
		g.setField(f);
		g.generate();
	}

	public static void main(String[] args) {
		generate();
	}

}
