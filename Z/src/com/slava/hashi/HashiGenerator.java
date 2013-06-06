package com.slava.hashi;

import com.slava.common.RectangularField;

public class HashiGenerator {
	RectangularField field;
	int linesAmount;
	int[] problem;

	public HashiGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setLinesAmount(int s) {
		linesAmount = s;
	}
	
	public void generate() {
		int attempts = 0;
		while(true) {
			attempts++;
			int[] problem = createRandomProblem();
			HashiSolver solver = new HashiSolver();
			solver.setField(field);
			solver.setProblem(problem);
			solver.solve();
			if(attempts % 1000 == 0) System.out.println("attempts=" + attempts);
			if(solver.solutionCount == 1) {
				this.problem = problem;
				System.out.println("Attempts=" + attempts);
				System.out.println("Tree size=" + solver.treeCount);
				printProblem();
				solver.printSolution();
				break;			
			}
		}
	}
	
	int[] createRandomProblem() {
		int[] pm = null;
		int q = 0;
		while(pm == null) {
			q++;
			HashiRandomGenerator g = new HashiRandomGenerator();
			g.setField(field);
			g.setLinesAmount(linesAmount);
			g.generate();
			pm = g.problem;
			if(q % 1000 == 0) System.out.println(q);
		}
		
		return pm;
	}

	void printProblem() {
		System.out.println("Problem");
		for (int i = 0; i < problem.length; i++) {
			String s = "" + problem[i];
			if(problem[i] == 0) {
				if(HashiRandomGenerator.isNode(field, i)) {
					s = "+";
				} else {
					s = ".";
				}
			}
			System.out.print(" " + s);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		HashiGenerator g = new HashiGenerator();
		RectangularField f = new RectangularField();
		f.setSize(7, 7);
		g.setField(f);
		//7x7 : 25; 8x8 : 32;  9x9 : 39; 10x10 : 44; 11x11 : 54;
		g.setLinesAmount(22);
		g.generate();
	}

}
