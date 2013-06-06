package com.slava.mover.model;

import com.slava.common.RectangularField;

public class MoverRunner implements MoverConstants {
	static int[] STATE_8 = {
		D,D,B,D,D,x,
		D,D,x,D,D,x,
		e,B,B,B,e,x,
		e,e,B,e,e,x,
		e,B,B,B,e,x,
		e,e,x,M,e,x,
	};
	static int[] STATE_9 = {
		x,e,M,e,x,x,
		x,e,x,B,e,e,
		e,G,D,e,D,e,
		e,e,B,B,e,x,
		x,x,e,x,D,x,
		x,x,e,e,e,x,
	};

	static int[] STATE_A = {
		e,e,e,e,e,x,
		D,x,x,D,D,x,
		e,e,e,e,e,x,
		e,D,x,x,e,x,
		e,e,e,e,e,x,
		e,e,e,e,e,x,
	};

	public static void main(String[] args) {
		RectangularField field = new RectangularField();
		field.setSize(6, 6);
		
		MoverProblem problem = new MoverProblem();
		problem.setField(field);
		problem.init(STATE_9);
		
		MoverSolver solver = new MoverSolver();
		solver.setProblem(problem);
		solver.solve();
	}

}
