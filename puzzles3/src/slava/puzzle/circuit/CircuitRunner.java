package slava.puzzle.circuit;

import java.io.File;

public class CircuitRunner {
	File f = null;
	
	public void setFile(File f) {
		this.f = f;
	}

	public void run() {	
		while(true) {
			runProblem();
			System.out.print("Run one more?(y/n):");
			int i = 13;
			try {
				while((i != (int)'y') && (i != (int)'n')) i = System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(i);
			if(i != (int)'y') break;
		}
	}
	
	public void runProblem() {
		CircuitData data = new CircuitData();
		data.load(f);
		EquationSolver solver = new EquationSolver();
		solver.setVariables(data.binds);
		solver.setEquations(data.equations);
		int[] valueSet = new int[data.binds.length];
		for (int i = 0; i < valueSet.length; i++) valueSet[i] = i + 1;
		solver.setValueSet(valueSet);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("File is not specified");
			return;
		}
		String f = args[0];
		File file = new File(f);
		if(!file.isFile()) {
			System.out.println("File " + f + " is not found.");
			return;
		}
		CircuitRunner runner = new CircuitRunner();
		runner.setFile(file);
		runner.run();
	}

}
