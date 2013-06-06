package olia;

public class ChangeableCroswordRunner {
	
	public static void generate(int width, int height) {
		ChangeableCroswordGenerator g = new ChangeableCroswordGenerator();
		g.generate(width, height);
	}

	public static void solve(String[] parts) {
		ChangeableCroswordSolver solver = new ChangeableCroswordSolver();
		
		solver.setProblem(parts, 3, 3);
		solver.solve();
		System.out.println("Solutions=" + solver.getSolutionCount());
	}
	
	/*
	 * 
	 *    1    121    21
	 *   22    221    2
	 *   11    112    221
	 * 
	 */
	
	static String[] SET_1 = {
		"12211", "121221112", "212221", 
		"112121", "222212", "11112221"
	};
	
	public static void main(String[] args) {
		//solve(SET_1);
		generate(4, 3);
	}

}
