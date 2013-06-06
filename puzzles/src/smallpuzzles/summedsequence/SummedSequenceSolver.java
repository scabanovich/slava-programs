package smallpuzzles.summedsequence;

import com.slava.common.RectangularField;

public class SummedSequenceSolver {
	RectangularField field;
	int solutionLimit = -1;
	boolean randomize;

	int[] initialState;

	int[] numbersToSet;
	int[] setNumbers;
	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	int[] solution;
	int treeCount;

	public SummedSequenceSolver() {}

	public void setField(RectangularField f) {
		field = f;
	}

	public void setInitialState(int[] s) {
		initialState = s;
	}

    public void setRandomising(boolean b) {
        randomize = b;
    }

    public void setSolutionLimit(int k) {
        solutionLimit = k;
    }

	public void solve() {
		init();
		anal();
	}

	void init() {
		setNumbers = new int[field.getSize() + 1];
		int setNumbersCount = 0;
		for (int i = 0; i < setNumbers.length; i++) setNumbers[i] = -1;
		state = new int[field.getSize()];
		if(initialState != null) {
			for (int p = 0; p < initialState.length; p++) {
				if(initialState[p] == 0) continue;
				int ii = initialState[p];
				state[p] = ii;
				if(setNumbers[ii] >= 0) {
					throw new IllegalArgumentException("Repeated number " + ii);
				}
				setNumbers[ii] = p;
				setNumbersCount++;
			}
		}
		numbersToSet = new int[field.getSize() - setNumbersCount];
		int k = 0;
		for (int n = 1; n <= field.getSize(); n++) {
			if(setNumbers[n] < 0) {
				numbersToSet[k] = n;
				k++;
			}
		}
	
		way = new int[numbersToSet.length + 1];
		wayCount = new int[numbersToSet.length + 1];
		ways = new int[numbersToSet.length + 1][field.getSize()];

		t = 0;
		solutionCount = 0;
		solution = null;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t >= numbersToSet.length) return;
		int n = numbersToSet[t];
		int np = t == 0 ? 0 : numbersToSet[t - 1];
		for (int nn = np + 1; nn < n; nn++) {
			int pp = setNumbers[nn];
			if(!canPut(nn, pp)) return;
		}
		for (int p = 0; p < state.length; p++) {
			if(state[p] > 0) continue;
			if(canPut(n, p)) {
				ways[t][wayCount[t]] = p;
				wayCount[t]++;
			}
		}
        if(randomize) randomizeWays();
    }

    private void randomizeWays() {
        if(wayCount[t] > 1) {
            for (int i = wayCount[t] - 1; i >= 1; i--) {
                int j = (int)((i + 1) * Math.random());
                if(j == i) continue;
                int b = ways[t][i];
                ways[t][i] = ways[t][j];
                ways[t][j] = b;
            }
        }
    }

	boolean canPut(int n, int p) {
		if(n < 3) return true;
		int[] ns = new int[(n + 1) / 2];
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q < 0 || state[q] == 0) continue;
			int v = state[q];
			if(v >= ns.length) v = n - v;
			if(v <= 0 || v >= ns.length) continue;
			if(ns[v] > 0) return true;
			ns[v] = 1;
			
		}
		return false;
	}

	void move() {
		int n = numbersToSet[t];
		int p = ways[t][way[t]];
		state[p] = n;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = 0;
	}

	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(t >= numbersToSet.length && checkTail()) {
				
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) {
					return;
				}
			}
		}
	}

	boolean checkTail() {
		int np = numbersToSet[t - 1];
		for (int nn = np + 1; nn <= field.getSize(); nn++) {
			int pp = setNumbers[nn];
			if(!canPut(nn, pp)) return false;
		}
		return true;
	}

	public void printSolution(int[] solution) {
		for (int p = 0; p < solution.length; p++) {
			String s = "" + solution[p];
			while(s.length() < 3) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

	public int getSolutionCount() {
		return solutionCount;
	}

	public int[] getSolution() {
		return solution;
	}

	public int getTreeCount() {
		return treeCount;
	}

	static int[] INITIAL_STATE = {
	  0,  0,  0,  0,  0, 
	  0,  0,  0,  0,  0, 
	  0,  0,  2,  0,  0, 
	  0,  0,  0,  0,  0, 
	  0,  0,  0,  0,  0, 
	};

	public static void main(String[] args) {
		SummedSequenceSolver solver = new SummedSequenceSolver();
		RectangularField f = new RectangularField();
		f.setOrts(RectangularField.DIAGONAL_ORTS);
		f.setSize(5, 5);
		solver.setField(f);
		solver.setInitialState(INITIAL_STATE);
		solver.setRandomising(true);
//		solver.setSolutionLimit(1);
		solver.solve();
		System.out.println("Solution count=" + solver.getSolutionCount());
		int[] solution = solver.getSolution();
		if(solution != null) {
			solver.printSolution(solution);
		}
		System.out.println("Tree count=" + solver.getTreeCount());
	}

}
