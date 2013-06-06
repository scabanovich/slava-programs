package balance;

public class BalanceRunner {
	
	public static void generate(int size) {
		BalanceGenerator g = new BalanceGenerator();
		g.setSize(size);
		g.generate();		
	}
	
	public static void solve(Weight root) {
		BalanceSolver solver = new BalanceSolver();
		solver.setRoot(root);
		solver.solve();
		System.out.println("Solution count=" + solver.solutionCount);
	}
	
	public static void test() {
		solve(BalanceGenerator.generateTest());
	}

	public static void main(String[] args) {
		generate(9);
	}

}

/**

*/