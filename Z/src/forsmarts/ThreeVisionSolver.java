package forsmarts;

import java.util.Arrays;

public class ThreeVisionSolver {
	ThreeVisionField field;
	
	int solutionLimit;
	
	int[] state;
	int[] border;
	
	int t;
	int pieceIndex;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int treeCount;
	int solutionCount;
	
	int[] solution;

	public ThreeVisionSolver() {}
	
	public void setField(ThreeVisionField f) {
		field = f;
	}
	
	public void setSolutionLimit(int limit) {
		solutionLimit = limit;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		border = new int[field.getSize()];
		wayCount = new int[100];
		way = new int[100];
		ways = new int[100][600];
		t = 0;
		pieceIndex = 0;
		solutionCount = 0;
		treeCount = 0;
		solution = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(contradiction()) return;
		
		int p = getBestP();
		if(p >= 0) getWaysForPlace(p);
		
//		int kb = 0;
//		if(t > 0) kb = ways[t - 1][way[t - 1]] + 1;
//		for (int k = kb; k < field.placements.length; k++) {
//			if(!canAdd(k)) continue;
//			ways[t][wayCount[t]] = k;
//			wayCount[t]++;
//		}
	}
	
	boolean contradiction() {
		for (int p = 0; p < field.getSize(); p++) {
			if(field.dots[p] > 0) {
				if(field.dots[p] < getNumber(p, state) || field.dots[p] > getMaxPossibleNumber(p)) {
					return true;
				}
			}
		}
		return false;
	}
	
	int getBestP() {
		int[] ds = new int[field.getSize()];
		for (int k = 0; k < field.placements.length; k++) {
			int[] placement = field.placements[k];
			for (int i = 0; i < placement.length; i++) {
				int p = placement[i];
				ds[p]++;
			}
		}
		
		int pb = -1;
		int wb = 100;
		for (int p = 0; p < ds.length; p++) {
			if(state[p] >= 0 || border[p] > 0 || field.form[p] == 0) continue;
			if(field.dots[p] <= 0) ds[p]++;
			if(ds[p] < wb) {
				wb = ds[p];
				pb = p;
			}
		}
		return pb;
	}
	
	void getWaysForPlace(int p) {
		if(field.form[p] == 0) throw new RuntimeException("fuck");
		for (int k = 0; k < field.placements.length; k++) {
			if(!canAdd(k) || !contains(p, field.placements[k])) continue;
			ways[t][wayCount[t]] = k;
			wayCount[t]++;
		}
		if(field.dots[p] <= 0) {
			ways[t][wayCount[t]] = -p - 1;
			wayCount[t]++;
		}
	}
	boolean contains(int p, int[] placement) {
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p) return true;
		}
		return false;
	}
	
	boolean canAdd(int k) {
		int[] placement = field.placements[k];
		for (int i = 0; i < placement.length; i++) {
			int p = placement[i];
			if(field.form[p] == 0 || state[p] >= 0 || border[p] > 0) return false;
		}
		return true;
	}
	
	void move() {
		int k = ways[t][way[t]];
		if(k < 0) {
			border[-k - 1]++;
		} else {
			int[] placement = field.placements[k];
			for (int i = 0; i < placement.length; i++) {
				int p = placement[i];
				state[p] = pieceIndex;
				for (int d = 0; d < 6; d++) {
					int q = field.jump(p, d);
					if(q < 0 || field.form[q] == 0) continue;
					border[q]++;
				}
			}
			pieceIndex++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = ways[t][way[t]];
		if(k < 0) {
			border[-k - 1]--;
		} else {
			int[] placement = field.placements[k];
			for (int i = 0; i < placement.length; i++) {
				int p = placement[i];
				state[p] = -1;
				for (int d = 0; d < 6; d++) {
					int q = field.jump(p, d);
					if(q < 0 || field.form[q] == 0) continue;
					border[q]--;
				}
			}
			pieceIndex--;
		}
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
			if(wayCount[t] == 0) treeCount++;
			if(isSolution()) {
				solution = (int[])state.clone();
				++solutionCount;
				onSolutionFound();
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
			}
		}
	}
	
	boolean isCompleted() {
		return false;
	}
	
	boolean isSolution() {
		if(ways[t - 1][way[t - 1]] < 0) {
			// last move just marked an empty place 
			return false;
		}
		for (int p = 0; p < state.length; p++) {
			if(field.dots[p] < 0) continue;
			if(state[p] < 0) return false;
			if(getNumber(p, state) != field.dots[p]) return false;
		}
		return true;
	}
	
	protected void onSolutionFound() {
//		printSolution(state);
	}
	
	int getNumber(int p, int[] _state) {
		int c = 0;
		for (int d = 0; d < 6; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				if(_state[q] >= 0) ++c;
				q = field.jump(q, d);
			}
		}
		return c;
	}
	
	int getMaxPossibleNumber(int p) {
		int c = 0;
		for (int d = 0; d < 6; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				if(state[q] >= 0 || border[q] <= 0) ++c;
				q = field.jump(q, d);
			}
		}
		return c;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void printProblem() {
		for (int i = 0; i < field.getSize(); i++) {
			String c = field.form[i] == 0 ? "+" : (field.dots[i] <= 0) ? "." : "" + field.dots[i];
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
	
	public void printSolution(int[] _state) {
//		System.out.println("t=" + t);
		for (int i = 0; i < field.getSize(); i++) {
			String c = field.form[i] == 0 ? "+" : (_state[i] < 0) ? "." : "" + (char)(97 + _state[i]);
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		int[] ns = getCodeNumbers(_state);
		for (int i = 0; i < ns.length; i++) {
			if(i > 0) System.out.print(",");
			System.out.print(ns[i]);
		}
		System.out.println("");
	}
	
	int[] getCodeNumbers(int[] _state) {
		int[] res = new int[7];
		for (int i = 0; i < field.getSize(); i++) {
			if(field.form[i] == 0 || _state[i] < 0) continue;
			res[_state[i]] += getNumber(i, _state);
		}
		Arrays.sort(res);
		return res;
	}
	

}
