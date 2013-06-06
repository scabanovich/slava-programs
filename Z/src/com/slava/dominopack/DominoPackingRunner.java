package com.slava.dominopack;

import java.util.Random;

import com.slava.common.RectangularField;

public class DominoPackingRunner {

	static int[] generateRandomNumbers(DominoSet set) {
		int[] result = new int[set.getSize() * 2];
		for (int i = 0; i < set.getSize(); i++) {
			result[2 * i] = set.getLessNumber(i);
			result[2 * i + 1] = set.getLargerNumber(i);
		}
		Random seed = new Random();
		for (int i = 0; i < result.length; i++) {
			int j = i + seed.nextInt(result.length - i);
			int c = result[i];
			result[i] = result[j];
			result[j] = c;
		}
		return result;
	}

	static void printNumbers(int[] numbers, RectangularField f) {
		for (int p = 0; p < f.getSize(); p++) {
			String s = numbers[p] < 0 ? "*" : "" + numbers[p];
			System.out.print(" " + s);
			if(f.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

	static int runRandom() {
		DominoSet domino = new DominoSet();
		RectangularField f = new RectangularField();
		f.setSize(8, 7);

		DominoPackingSolver solver = new DominoPackingSolver();
		solver.setDomino(domino);
		solver.setField(f);
		int[] numbers = generateRandomNumbers(domino);
		solver.setNumbers(numbers);

		solver.solve();
		if(solver.getSolutionCount() > 0) {
			System.out.println("Tree count=" + solver.getTreeCount());
			System.out.println("Solution count=" + solver.getSolutionCount());
			printNumbers(numbers, f);
		}
		return solver.getSolutionCount();
	}

	public static DominoPackingSolver run(int[] numbers) {
		DominoSet domino = new DominoSet();
		RectangularField f = new RectangularField();
		f.setSize(8, 7);

		DominoPackingSolver solver = new DominoPackingSolver();
		solver.setDomino(domino);
		solver.setField(f);
		solver.setNumbers(numbers);

		solver.solve();
		if(solver.getSolutionCount() > 0) {
			System.out.println("Tree count=" + solver.getTreeCount());
			System.out.println("Solution count=" + solver.getSolutionCount());
			printNumbers(numbers, f);
		}
		return solver;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int k = 0;
		for (int i = 0; i < 10000; i++) {
			if(runRandom() > 0) k++;
		}
		System.out.println("Problems=" + k);
	}

}
