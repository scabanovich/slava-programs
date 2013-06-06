package pqrst14;

public class PqrstProblemRunner {
	
	public static void runDigitLayers() {
		DigitLayers problem = new DigitLayers();
		problem.solve(DigitLayers.FORM);
	}
	
	public static void runEquationChain() {
		EquationChain problem = new EquationChain();
		problem.solve();
	}
	
	public static void runMagnets() {
		Magnets problem = new Magnets();
		problem.setSize(12, 12);
		problem.solve();
	}

	public static void runMultiplicationTable() {
		MultiplicationTable problem = new MultiplicationTable();
		problem.setSize(7, 7);
		problem.solve();
	}

	public static void runLoopSegments() {
		LoopSegments problem = new LoopSegments();
		problem.setSize(8, 8);
		problem.solve();
	}

	public static void main(String[] args) {
		runLoopSegments();
	}
}
