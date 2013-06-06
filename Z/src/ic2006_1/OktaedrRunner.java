package ic2006_1;

public class OktaedrRunner {
	public static void main(String[] args) {
		TriangularField f = new TriangularField();
		f.setSize(10);
		OktaedrFigures fgs = new OktaedrFigures();
		fgs.setField(f);
		fgs.makePlacements();
		
		OktaedtNoncompactSolver solver = new OktaedtNoncompactSolver();
		solver.setField(f);
		solver.setFigures(fgs);
		solver.solve();
	}
}
