package com.slava.patience.piramida;

import com.slava.common.RectangularField;

public class IogaRunner {
	static int[] FORM_77 = {
		0,0,1,1,1,0,0,
		0,0,1,1,1,0,0,
		1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,
		0,0,1,1,1,0,0,
		0,0,1,1,1,0,0,
	};
	static int[] FORM_99 = {
		0,0,0,1,1,1,0,0,0,
		0,0,0,1,1,1,0,0,0,
		0,0,0,1,1,1,0,0,0,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		0,0,0,1,1,1,0,0,0,
		0,0,0,1,1,1,0,0,0,
		0,0,0,1,1,1,0,0,0,
	};
	
	static int E = 0;
	
	static int[] INIT = {
		E,E,1,1,1,E,E,
		E,E,1,1,1,E,E,
		1,1,1,1,1,1,1,
		1,1,1,0,1,1,1,
		1,1,1,1,1,1,1,
		E,E,1,1,1,E,E,
		E,E,1,1,1,E,E,
	};

	static int[] INIT_99 = {
		E,E,E,1,1,1,E,E,E,
		E,E,E,1,1,1,E,E,E,
		E,E,E,1,1,1,E,E,E,
		1,1,1,1,1,1,1,1,1,
		1,1,1,1,0,1,1,1,1,
		1,1,1,1,1,1,1,1,1,
		E,E,E,1,1,1,E,E,E,
		E,E,E,1,1,1,E,E,E,
		E,E,E,1,1,1,E,E,E,
	};
	
	static void run77() {
		RectangularField f = new RectangularField();
		f.setSize(7, 7);
		IogaSolver solver = new IogaSolver();
		solver.setField(f);
		solver.setData(FORM_77, INIT);
		solver.solve();
	}
	
	static void run99() {
		RectangularField f = new RectangularField();
		f.setSize(9, 9);
		IogaSolver solver = new IogaSolver();
		solver.setField(f);
		solver.setData(FORM_99, INIT_99);
		solver.solve();
	}
	
	public static void main(String[] args) {
		run99();
	}

}
