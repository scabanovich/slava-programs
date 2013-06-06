package com.slava.kropki;

import com.slava.common.RectangularField;

public class KropkiRunner {
	
	static int N = 0; //nothing
	static int D = 1; //difference one
	static int R = 2; //ratio two
	
	static int[][] V_DATA = {
		{0,0,0,0,0,0,0,0,0},
		{D,0,R,R,0,0,0,0,0},
		{0,0,D,0,0,R,0,0,0},
		{R,0,D,0,0,0,D,D,R},
		{R,0,0,0,0,R,0,0,0},
		{0,D,0,0,0,0,D,0,0},
		{0,D,0,D,0,0,0,0,0},
		{0,0,D,R,0,0,D,0,D},
	};
	static int[][] H_DATA = {
		{0,0,0,0,D,0,0,0},
		{0,0,0,0,0,0,0,0},
		{D,0,D,0,D,0,0,0},
		{0,0,D,0,D,0,0,R},
		{0,0,0,D,0,D,0,D},
		{0,0,0,0,R,D,0,R},
		{D,0,0,D,0,0,D,0},
		{0,0,0,0,0,0,0,0},
		{D,D,0,R,0,0,D,R},
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(9, 9);
		KropkiSolver solver = new KropkiSolver();
		solver.setField(f);
		solver.setData(V_DATA, H_DATA);
		solver.solve();
		System.out.println("Solution count=" + solver.getSolutionCount());
	}

}
