package slava.puzzle.hitori.analysis;

import com.slava.common.ContinuityCheck;
import com.slava.common.RectangularField;

public class HitoriLogicalSolver {
	RectangularField field;
	ContinuityCheck check = new ContinuityCheck();
	int[] numbers;
	
	int suggestionsLimit = -1;
	
	int[] solution;
	int unresolvedCount;
	int contradiction = -1;
	int suggestionCount;
	
	public void setField(RectangularField f) {
		field = f;
		check.setField(field);
		check.setAcceptor(new ContinuityCheck.Acceptor() {
			public boolean accept(int p) {
				return solution[p] < 1;
			}
		});
	}
	
	public void setNumbers(int[] ns) {
		numbers = ns;		
	}
	
	public void setSuggestionsLimit(int s) {
		suggestionsLimit = s;
	}
	
	public void solve() {
		solution = new int[numbers.length];
		unresolvedCount = numbers.length;
		contradiction = -1;
		suggestionCount = 0;
		for (int i = 0; i < numbers.length; i++) solution[i] = -1;
		checkNumbers();
		applyInitialRules();
		iterare();
	}
	
	public void checkNumbers() {
		for (int i = 0; i < numbers.length; i++) {
			if(numbers[i] <= 0) throw new RuntimeException("Not all numbers are set.");
		}
	}
	
	void iterare() {
		int count = unresolvedCount;
		while(!isSolved() && contradiction < 0) {
			tryContinuity();
			if(count == unresolvedCount && suggestionCount < suggestionsLimit) {
				makeSuggestion();
			}			
			if(count == unresolvedCount) break;
			count = unresolvedCount;
		}
	}
	
	void applyInitialRules() {
		for (int d = 0; d < 2; d++) {
			for (int p = 0; p < solution.length; p++) {
				int q = field.jump(p, d, 2);
				if(q >= 0 && numbers[q] == numbers[p]) {
					int u = field.jump(p, d);
					setState(u, 0);
				}
			}
		}
		for (int p = 0; p < solution.length; p++) {
			int q = field.jump(p, 0);
			if(q < 0 || numbers[q] != numbers[p]) continue;
			int iy = field.getY(p);
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int u = field.getIndex(ix, iy);
				if(u == p || u == q || numbers[u] != numbers[p]) continue;
				setState(u, 1);
			}
		}
		for (int p = 0; p < solution.length; p++) {
			int q = field.jump(p, 1);
			if(q < 0 || numbers[q] != numbers[p]) continue;
			int ix = field.getX(p);
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int u = field.getIndex(ix, iy);
				if(u == p || u == q || numbers[u] != numbers[p]) continue;
				setState(u, 1);
			}
		}
	}
	
	void setState(int p, int c) {
		if(solution[p] == c) return;
		if(solution[p] >= 0) {
			contradiction = p;
			return;
		}
		solution[p] = c;
		unresolvedCount--;
		if(!check.isContinuous()) {
			contradiction = p;
			return;
		}
		if(c == 0) {
			for (int q = 0; q < solution.length; q++) {
				if(q == p || numbers[q] != numbers[p]) continue;
				if(field.getX(q) != field.getX(p) && field.getY(q) != field.getY(p)) continue;
				setState(q, 1);
				if(contradiction >= 0) return;
			}
			checkOnlyWayOut(p);
		} else {
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) {
					setState(q, 0);
				}
				checkOnlyWayOut(q);
				if(contradiction >= 0) return;
			}
		}
	}
	
	void checkOnlyWayOut(int p) {
		int wc = getWayOutCount(p);
		if(wc == 0) {
			contradiction = p;
		} else if(wc == 1) {
			int q = getWayOut(p);
			setState(q, 0);
		}
	}
	
	int getWayOutCount(int p) {
		int c = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && solution[q] < 1) ++c;
		}
		return c;
	}
	
	int getWayOut(int p) {
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && solution[q] < 1) return q;
		}
		return -1;
	}
	
	void tryContinuity() {
		for (int p = 0; p < solution.length; p++) {
			if(solution[p] >= 0) continue;
			solution[p] = 1;
			boolean b = check.isContinuous();
			solution[p] = -1;
			if(!b) setState(p, 0);
		}
	}

	void makeSuggestion() {
		for (int p = 0; p < solution.length; p++) {
			if(contradiction >= 0) return;
			if(solution[p] >= 0) continue;
			if(makeSuggestion(p)) {
				suggestionCount++;
				return;			
			}
		}
	}

	boolean makeSuggestion(int p) {
		if(contradiction > 0) return false;
		int[] ss = solution;
		int uc = unresolvedCount;

		solution = (int[])ss.clone();
		setState(p, 1);
		solution = ss;
		unresolvedCount = uc;
		if(contradiction >= 0) {
			contradiction = -1;
			setState(p, 0);
			return true;
		}

		solution = (int[])ss.clone();
		setState(p, 0);
		solution = ss;
		unresolvedCount = uc;
		if(contradiction >= 0) {
			contradiction = -1;
			setState(p, 1);
			return true;
		}
		return false;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public boolean isSolved() {
		return unresolvedCount == 0;
	}
	
	public int getSuggestionCount() {
		return suggestionCount;
	}
	
	public String getContradiction() {
		if(contradiction < 0) return null;
		char yc = (char)(97 + field.getX(contradiction));
		return "" + yc + "" + (field.getHeight() - field.getY(contradiction));
	}

}
