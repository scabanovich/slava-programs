package olia.triangles;

public class TriangesRunner {

	static int[] createTriangleForm(TrianglesField f) {
		int[] form = new int[f.getSize()];
		for (int i = 0; i < form.length; i++) {
			if(f.x(i) >= f.y(i)) form[i] = 1; else form[i] = 0;
		}
		return form;
	}
	
	static int E = -1;
	
	static int[] INITIAL_STATE = new int[] {
		E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,
		E,E,E,E,0,1,E,E,
		E,E,E,E,E,2,E,E,
		E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,E,
	};

	public static void main(String[] args) {
		TrianglesField f = new TrianglesField();
		f.setSize(8, 8);
		TrianglesSolver s = new TrianglesSolver();
		s.setField(f);
		s.setForm(createTriangleForm(f));

		s.setInitialState(INITIAL_STATE);
		s.solve();
		System.out.println("SolutionCount=" + s.getSolutionCount());
	}

}
