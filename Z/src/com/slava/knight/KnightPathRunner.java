package com.slava.knight;

import com.slava.common.TwoDimField;

public class KnightPathRunner implements ISolutionListener {
	TwoDimField field;
	int[] fieldRestriction;
	KnightPairsSolver solver = new KnightPairsSolver();

	int solutionCount;

	public KnightPathRunner() {}

	public void setField(TwoDimField field) {
		this.field = field;
		solver.setField(field);
	}

	public void setEnds(int start, int end) {
		int[] fieldRestriction = new int[field.getSize()];
		fieldRestriction[start] = 1;
		fieldRestriction[end] = 1;
		solver.setFieldRestriction(fieldRestriction);
	}

	@Override
	public void solutionFound(int[] state) {
		PairRestriction p = new PairRestriction();
		p.setPairs(state);
		solver.setPairRestriction(p);
		solver.solve();
		int sc = solver.getSolutionCount();
//		if(sc > 0) {
			System.out.println("sc=" + sc);
//		}
		solutionCount += sc;
		
	}
	public static void run(int width, int height, int start, int end) {
		KnightPathRunner runner = new KnightPathRunner();		
		KnightField f = new KnightField();
		f.setSize(width, height);
		runner.setField(f);
		runner.setEnds(start, end);
		
		KnightPairsSolver solver1 = new KnightPairsSolver();
		solver1.setField(f);
		solver1.setSolutionListener(runner);
		solver1.solve();
		System.out.println("Total solutions=" + runner.solutionCount);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		run(8, 6, 5, 6); //3595288 (7, 6, 0, 1) //45212000 (8, 6, 5, 6)
	}

}
