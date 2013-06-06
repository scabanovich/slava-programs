package com.slava.dice;

public class DiceRunner {
	static int[] TEST_DICE_SEQUENCE = new int[]{
		3,5,3,4,2,2,4,2,1,4,3,6,3
	};
	
	static void generateDiceSequence(int[] s) {
		for (int i = 0; i < s.length; i++) s[i] = 1 + (int)(6 * Math.random()); 
	}
	
	static void generate(DiceOptions options) {
		int[] diceSequence = new int[options.throwsCount];
		DiceSolver solver = new DiceSolver();
		solver.setTransitionCountOption(options.arrowCount);
		solver.setDiceSequence(diceSequence);
		solver.setFieldLength(options.distance);
		solver.canJumpToFinish = options.canJumpToFinish;
		solver.canJumpToStart = options.canJumpToStart;
		solver.setSoltionLimit(2);
		int attemptCount = 0;
		while(true) {
			attemptCount++;
			generateDiceSequence(diceSequence);
			solver.solve();
			if(solver.solutionCount == 1) break;
		}
		if(solver.solutionCount == 1) {
			System.out.println("Attempts=" + attemptCount);
			System.out.print("Dice sequence=");
			for (int i = 0; i < diceSequence.length; i++) System.out.print(" " + diceSequence[i]);
			System.out.println("");
			solver.printSolutions();
		}
		
	}
	
	static void generate(String[] args) {
		DiceOptions options = new DiceOptions();
		options.load(args);
		if(options.errorMessage != null) {
			System.out.println(options.errorMessage);
			System.out.println("Usage:");
			System.out.println(DiceOptions.USAGE);
		} else {
			generate(options);
		}
	}
	
	static void solve(int[] diceSequence, int length) {
		DiceSolver solver = new DiceSolver();
		solver.setDiceSequence(diceSequence);
		solver.setFieldLength(length);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

	public static void main(String[] args) {
		generate(args);
	}

}
