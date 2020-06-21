package miner;

import com.slava.common.RectangularField;

public class MinerSolver implements MinerConstants {
	RectangularField f = new RectangularField();
	boolean reportProgress = true;

	int[] condition; // HIDDEN or >= 0
	int[] state;     //UNKNOWN  SAFE  MINE
	
	public MinerSolver() {
		f.setOrts(RectangularField.DIAGONAL_ORTS);
	}

	public void setReportProgress(boolean b) {
		this.reportProgress = b;
	}

	public void setSize(int width, int height) {
		f.setSize(width, height);
	}

	public void setCondition(int[] condition) {
		if (condition.length != f.getSize()) {
			throw new RuntimeException();
		}
		this.condition = condition;
		state = new int[condition.length];
		for (int p = 0; p < state.length; p++) {
			if (condition[p] > HIDDEN) {
				state[p] = SAFE;
			} else if (condition[p] < HIDDEN) {
				throw new RuntimeException();
			} else {
				state[p] = UNKNOWN;
			}
		}
	}

	public int getConditionSize() {
		int res = 0;
		for (int p = 0; p < condition.length; p++) {
			if (condition[p] > HIDDEN) {
				res++;
			}
		}
		return res;
	}
	
	int countAround(int p, int v) {
		int res = 0;
		for (int d = 0; d < 8; d++) {
			int q = f.jump(p, d);
			if (q >= 0 && state[q] == v) {
				res++;
			}
		}
		return res;
	}

	int setValueAround(int p, int v) {
		int res = 0;
		for (int d = 0; d < 8; d++) {
			int q = f.jump(p, d);
			if (q >= 0 && state[q] == UNKNOWN) {
				res++;
				state[q] = v;
			}
		}
		return res;
	}

	//returns changes
	int solveAt(int p) {
		if (condition[p] > HIDDEN) {
			int actualMines = condition[p];
			int unknown = countAround(p, UNKNOWN);
			int mines = countAround(p, MINE);
			if (actualMines < mines) {
				MinerGenerator.print(state, f);
				throw new RuntimeException("Error at " + p);
			}
			if (unknown == 0) {
				if (actualMines != mines) {
					MinerGenerator.print(state, f);
					throw new RuntimeException("Error at " + p + " found mines=" + mines + " actual mines=" + actualMines);
				}
				return 0;
			}
			if (actualMines == mines + unknown) {
				return setValueAround(p, MINE);
			}
			if (actualMines == mines) {
				return setValueAround(p, SAFE);
			}
		}
		return 0;
	}

	public void solve() {
		while (true) {
			int changes = 0;
			for (int p = 0; p < state.length; p++) {
				changes += solveAt(p);
			}
			if (reportProgress) {
				System.out.println(changes);
			}
			if (changes == 0) {
				break;
			}
		}
	}

	public boolean isSolved() {
		for (int p = 0; p < state.length; p++) {
			if (state[p] == UNKNOWN) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		int width = 6;
		int height = 6;
		int mines = 5;

		int attempt = 0;
		while (true) {
			attempt++;
			System.out.println("Attempt " + attempt);
			int[] condition = MinerGenerator.generateAndReduce(width, height, mines);
			MinerSolver s = new MinerSolver();
			s.setSize(width, height);
			s.setCondition(condition);
			if (s.getConditionSize() > 170) continue;
			System.out.println("Numbers = " + s.getConditionSize());
			s.solve();
			if (s.isSolved()) {
				MinerGenerator.print(s.state, s.f);
				break;
			}
		}
	}

}
