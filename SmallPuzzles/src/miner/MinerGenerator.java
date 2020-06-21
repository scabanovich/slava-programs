package miner;

import java.util.Random;

import com.slava.common.RectangularField;

public class MinerGenerator implements MinerConstants {

	static int[] generate(RectangularField f, int mines) {
		int[] state = new int[f.getSize()];
		int[] condition = new int[f.getSize()];
		for (int p = 0; p < state.length; p++) {
			state[p] = SAFE;
		}
		Random r = new Random();
		while (mines > 0) {
			int p = r.nextInt(state.length);
			if (state[p] == SAFE) {
				mines--;
				state[p] = MINE;
				condition[p] = HIDDEN;
				for (int d = 0; d < 8; d++) {
					int q = f.jump(p, d);
					if (q >= 0 && condition[q] != HIDDEN) {
						condition[q]++;
					}
				}
			}
		}
		for (int p = 0; p < condition.length; p++) {
			if (condition[p] == 0) {
				condition[p] = HIDDEN;
			}
		}
		return condition;
	}

	static int[] generateSolvable(RectangularField f, int mines) {
		while (true) {
			int[] condition = generate(f, mines);
			MinerSolver2 s = new MinerSolver2();
			s.setReportProgress(false);
			s.setSize(f.getWidth(), f.getHeight());
			s.setCondition(condition);
			s.solve();
			if (s.isSolved()) {
				return condition;
			}
		}
	}
	
	static void reduce1(int width, int height, int[] condition) {
		Random r = new Random();

		int[] hidable = new int[condition.length];
		int hidableCount = 0;
		for (int p = 0; p < condition.length; p++) {
			if (condition[p] != HIDDEN) {
				hidable[p] = 1;
				hidableCount++;
			}
		}
		while (hidableCount > 0) {
			int p = r.nextInt(condition.length);
			while (hidable[p] == 0) {
				p = r.nextInt(condition.length);
			}
			hidable[p] = 1;
			hidableCount--;
			int c = condition[p];
			condition[p] = HIDDEN;
			MinerSolver s = new MinerSolver();
			s.setReportProgress(false);
			s.setSize(width, height);
			s.setCondition(condition);
			s.solve();
			if (!s.isSolved()) {
				condition[p] = c;
			}
		}
	}

	static void reduce2(int width, int height, int[] condition) {
		Random r = new Random();

		int[] hidable = new int[condition.length];
		int hidableCount = 0;
		for (int p = 0; p < condition.length; p++) {
			if (condition[p] != HIDDEN) {
				hidable[p] = 1;
				hidableCount++;
			}
		}
		while (hidableCount > 0) {
			int p = r.nextInt(condition.length);
			while (hidable[p] == 0) {
				p = r.nextInt(condition.length);
			}
			hidable[p] = 1;
			hidableCount--;
			int c = condition[p];
			condition[p] = HIDDEN;
			MinerSolver2 s = new MinerSolver2();
			s.setReportProgress(false);
			s.setSize(width, height);
			s.setCondition(condition);
			s.solve();
			if (!s.isSolved()) {
				condition[p] = c;
			}
		}
	}

	static int[] generateAndReduce(int width, int height, int mines) {
		RectangularField f = new RectangularField();
		f.setOrts(RectangularField.DIAGONAL_ORTS);
		f.setSize(width, height);
		
		int[] condition = generateSolvable(f, mines);
		reduce1(width, height, condition);
		reduce2(width, height, condition);
//		print(state, f);
		print(condition, f);
		return condition;
	}

	public static void print(int[] a, RectangularField f) {
		for (int p = 0; p < a.length; p++) {
			System.out.print(" " + (a[p] < 0 ? "-" : "" + a[p]));
			if (f.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}
	
}
