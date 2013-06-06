package ik6_1;

public class SecondHexRunner {
	static int[] FIELD_FORM = {
		1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1
	};
	
	static int[][] XYD_CONDITIONS = {
		//{starting x, starting y, direction, value, index}
		{0,0,0,1,2},
		{0,2,0,1,2},
		{0,4,4,1,2},
		{0,0,1,2,2},
		{4,0,4,2,2},
		{0,3,0,3,3},
		{3,0,4,4,3},
	};

	public static void main(String[] args) {
		SecondHexField f = new SecondHexField();
		f.setSize(9);
		f.setForm(FIELD_FORM);
		f.setConditions(XYD_CONDITIONS);
		SecondHexSolver solver = new SecondHexSolver();
		solver.setField(f);
		solver.setConditions(f.row_conditions);
		solver.solve();
	}

}
