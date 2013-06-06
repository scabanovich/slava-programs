package ic2006_3;

public class CyclicExpressionRunner {
	static int[] INIT_STATE = {
		-1, -1,-1, -1,9, 
		-1,-1, -1,-1, -1,-1, 0,-1,
		-1,-1, -1,-1, 2,-1, -1};
	
	static class FirstChecker implements CyclicExpressionSolver.Checker {

		public boolean finalCheck(CyclicExpressionSolver solver) {
			for (int i = 0; i < 4; i++) {
				if(solver.usedSigns[i] < 2) return false; 
			}
			return solver.sum1 == solver.state[0] && solver.sum2 == solver.state[0];
		}

		public boolean partialCheck(CyclicExpressionSolver solver) {
			if(solver.t == 13) {
				int c = solver.state[12];
				int b = solver.state[10];
				int d = solver.state[8];
				if(b > c || c > d) return false;
				if(c + c != b + d) return false;
			}
			return true;
		}
		
	}
	
	static class FirstListener implements CyclicExpressionSolver.SolutionListener, CyclicExpressionSolver.Checker {
		int[] firstState;

		public void solutionFound(CyclicExpressionSolver solver) {
			firstState = solver.state;
			CyclicExpressionSolver solver2 = new CyclicExpressionSolver();
			solver2.setInitialState(prepareInitial(solver.state));
			solver2.setChecker(this);
			solver2.setListener(new SecondListener(firstState));
			solver2.solve();
			System.out.println("-->" + solver2.solutionCount);
		}
		
		private int[] prepareInitial(int[] state) {
			int[] initialState = new int[state.length];
			for (int i = 0; i < initialState.length; i++) initialState[i] = -1;
			initialState[0] = state[10];
			initialState[1] = 3;
			initialState[7] = state[1];
			initialState[10] = state[12];
			initialState[14] = state[2];
			return initialState;
		}

		public boolean finalCheck(CyclicExpressionSolver solver) {
			for (int i = 0; i < 4; i++) {
				if(solver.usedSigns[i] < 2) return false; 
			}
			return solver.sum1 == firstState[8] && solver.sum2 == firstState[8];
		}

		public boolean partialCheck(CyclicExpressionSolver solver) {
			return true;
		}
		
	}
	
	static class SecondListener implements CyclicExpressionSolver.SolutionListener {
		int[] firstState;
		
		public SecondListener(int[] firstState) {
			this.firstState = firstState;
		}
		
		public void solutionFound(CyclicExpressionSolver solver) {
			System.out.println("Solution Found!!!");
			CyclicExpressionSolver.printState(firstState);
			CyclicExpressionSolver.printState(solver.state);
		}
		
	}

	public static void main(String[] args) {
		CyclicExpressionSolver solver = new CyclicExpressionSolver();
		solver.setInitialState(INIT_STATE);
		solver.setChecker(new FirstChecker());
		solver.setListener(new FirstListener());
		solver.solve();
		System.out.println("solutionCount " + solver.solutionCount);
	}

}

/**
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 - 10 / 1 * 2 -
 6 / 1 + 10 - 9 * 4 / 7 + 2 / 3 * 5 - 8 +
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 - 10 / 1 * 2 -
 6 / 2 + 5 - 8 * 1 + 7 * 9 + 3 - 10 / 4 -
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 - 10 / 1 * 2 -
 6 / 2 + 5 - 8 * 1 + 7 * 9 - 3 / 10 - 4 +
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 - 10 / 1 * 2 -
 6 / 2 + 9 - 10 * 1 + 7 - 5 + 3 * 8 / 4 -
-->4
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 / 1 - 10 * 2 -
 6 / 1 + 10 - 9 * 4 / 7 + 2 / 3 * 5 - 8 +
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 / 1 - 10 * 2 -
 6 / 2 + 5 - 8 * 1 + 7 * 9 + 3 - 10 / 4 -
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 / 1 - 10 * 2 -
 6 / 2 + 5 - 8 * 1 + 7 * 9 - 3 / 10 - 4 +
Solution Found!!!
 5 * 3 + 9 / 4 + 8 - 6 + 7 / 1 - 10 * 2 -
 6 / 2 + 9 - 10 * 1 + 7 - 5 + 3 * 8 / 4 -
-->4
-->0
-->0
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 - 8 / 1 * 7 +
 4 / 2 + 7 / 9 * 1 + 5 * 6 / 3 - 10 + 8 -
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 - 8 / 1 * 7 +
 4 / 2 + 8 - 10 * 1 + 5 * 6 - 3 / 9 + 7 -
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 - 8 / 1 * 7 +
 4 / 2 + 8 / 10 * 1 + 5 * 6 / 3 - 9 + 7 -
-->3
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 / 1 - 8 * 7 +
 4 / 2 + 7 / 9 * 1 + 5 * 6 / 3 - 10 + 8 -
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 / 1 - 8 * 7 +
 4 / 2 + 8 - 10 * 1 + 5 * 6 - 3 / 9 + 7 -
Solution Found!!!
 10 * 3 - 9 * 2 / 6 - 4 + 5 / 1 - 8 * 7 +
 4 / 2 + 8 / 10 * 1 + 5 * 6 / 3 - 9 + 7 -
-->3
solutionCount 6

*/