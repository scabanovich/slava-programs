package com.slava.circuit;

public class CircuitRunner {

	public static void main(String[] args) {
		CircuitData data = new CircuitData();
		data.load();
		EquationSolver solver = new EquationSolver();
		solver.setVariables(data.binds);
		solver.setEquations(data.equations);
		int[] valueSet = new int[data.binds.length];
		for (int i = 0; i < valueSet.length; i++) valueSet[i] = i + 1;
		solver.setValueSet(valueSet);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

}
