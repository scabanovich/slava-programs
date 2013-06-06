package com.slava.lots;

import com.slava.common.RectangularField;

public class LotsGenerator {
	LotsSolver g = new LotsSolver();
	RectangularField field;
	int[] problem;
	
	public LotsGenerator() {}

	public void setField(RectangularField f) {
		field = f;
		g.setField(field);
	}
	
	public void setMinArea(int s) {
		g.setMinArea(s);
	}
	
	public void setMaxArea(int s) {
		g.setMaxArea(s);
	}
	
	public void generate() {
		while(true) {
			g.setSoltionLimit(1);
			g.setTreeCountLimit(100000);
			g.setProblem(null);
			g.solve();
			if(g.getSolution() == null) {
				continue;
			}
			problem = createProblem(g.getSolution());
			g.setSoltionLimit(1000);
			g.setTreeCountLimit(-1);
			g.setProblem(problem);
			g.solve();
			if(g.getSolutionCount() == 1) {
				printProblem();
				System.out.println("Solution count=" + g.getSolutionCount() + " " + " tree count=" + g.treeCount);
				g.printSolution(g.getSolution());
				break;
			}
		}
	}
	
	int[] createProblem(int[] state) {
		int[] problem = new int[state.length];
		int max = -1;
		for (int i = 0; i < state.length; i++) if(state[i] > max) max = state[i];
		for (int i = 0; i <= max; i++) {
			int s = 0;
			for (int p = 0; p < state.length; p++) if(state[p] == i) ++s;
			int p = (int)(state.length * Math.random());
			while(state[p] != i) p = (int)(state.length * Math.random());
			problem[p] = s;
		}
		return problem;
	}
	
	void printProblem() {
		System.out.println("Problem");
		for (int i = 0; i < problem.length; i++) {
			String s = "" + problem[i];
			if(problem[i] == 0) {
				s = "+";
			}
			System.out.print(" " + s);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		LotsGenerator g = new LotsGenerator();
		RectangularField f = new RectangularField();
		f.setSize(12, 12);
		g.setField(f);
		g.setMaxArea(12);
		g.setMinArea(2);
		
		g.generate();		
	}

}
