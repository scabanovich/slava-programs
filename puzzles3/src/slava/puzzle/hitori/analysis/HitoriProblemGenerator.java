package slava.puzzle.hitori.analysis;

import slava.puzzle.hitori.model.HitoriPreferences;

import com.slava.common.RectangularField;

public class HitoriProblemGenerator {
	RectangularField field;
	BlackSetGenerator bsg = new BlackSetGenerator();
	NumbersInWhiteGenerator nwg = new NumbersInWhiteGenerator();
	HitoriLogicalSolver solver = new HitoriLogicalSolver();
	
	int[] solution;
	int[] numbers;
	int attemptsCount;
	
	public HitoriProblemGenerator() {}

	public void setField(RectangularField f) {
		field = f;
		bsg.setField(f);
		nwg.setField(f);
		solver.setField(field);
	}

	public int[] generate() {
		solver.setSuggestionsLimit(HitoriPreferences.instance.getSuggestionsLimit());
		attemptsCount = 0;
		while(true) {
			attemptsCount++;
			int[] ns = generateOne();
			solver.setNumbers(ns);
			solver.solve();
			if(solver.isSolved() && solver.getContradiction() == null) {
				return ns;
			}
		}
	}

	public int[] generateOne() {
		int[] set = bsg.generate();
		solution = set;
		int colors = getColorCount();
		nwg.setForm(set);
		nwg.setColorCount(colors);
		nwg.solve();
		int[] ns = nwg.getNumbers();
		generateInBlack(ns, colors);
		//TODO improve
		return ns;
	}
	
	int getColorCount() {
		int maxColorCount = field.getWidth();
		int minColorCount = computeMinColorCount();
		if(maxColorCount == minColorCount) return maxColorCount;
		int delta = maxColorCount - minColorCount;
		return minColorCount + (int)(delta * Math.random());
	}
	
	int computeMinColorCount() {
		int cc = 0;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int ci = 0;
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int p = field.getIndex(ix, iy);
				if(solution[p] < 1) ci++;
			}			
			if(ci > cc) cc = ci;
		}
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int ci = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(solution[p] < 1) ci++;
			}			
			if(ci > cc) cc = ci;
		}
		return cc;
	}
	
	void generateInBlack(int[] ns, int colorCount) {
		for (int p = 0; p < ns.length; p++) {
			if(ns[p] > 0) continue;
			ns[p] = (int)(Math.random() * colorCount) + 1;
		}
	}
	
	public int[] getNumbers() {
		return numbers;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int getAttemptCount() {
		return attemptsCount;
	}

}
