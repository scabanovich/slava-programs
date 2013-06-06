package slava.puzzle.pentaletters.analysis;

import slava.puzzle.pentaletters.model.*;

public class PentaLettersGenerator {
	PentaLettersAnalyzer analyzer = new PentaLettersAnalyzer();
	PentaLettersField field = new PentaLettersField();

	PentaLettersAnalyzer analyzer2 = new PentaLettersAnalyzer();

	public PentaLettersGenerator() {
		analyzer.setField(field);
		analyzer.setIgnoreLetters(true);
		
		analyzer2.setField(field);
		analyzer2.setSolutionCountLimit(10);
	}
	
	public void setFigures(PentaFigures figures) {
		analyzer.setFigures(figures.getFigures(), figures.getFigureIndices());
		analyzer2.setFigures(figures.getFigures(), figures.getFigureIndices());
	}
	
	public void setField(PentaLettersField field) {
		this.field.setSize(field.getWidth(), field.getHeight());
	}
	
	int attemptCount;

	public void generate() {
		attemptCount = 0;
		while(true) {
			int ac = 0;
			analyzer.solve();
			if(analyzer.getSolutionCount() < 0) continue;
			int[] groups = (int[])analyzer.getSolutions().get(0);
			int solutionCount = 2;
			do {
				generateLetters(groups);
				analyzer2.solve();
				solutionCount = analyzer2.getSolutionCount();
				if(solutionCount < 10) {
///					System.out.println("solutionCount=" + solutionCount);
				}
				try { Thread.sleep(10); } catch (Exception e) {}
				attemptCount++;
				ac++;
			} while(solutionCount > 1 && ac < 50);
			if(solutionCount == 1) return;
		}
	}
	
	public PentaLettersField getField() {
		return field;
	}
	
	private void generateLetters(int[] groups) {
		int groupCount = field.getSize() / 5;
		int[][] letters = new int[groupCount][5];
		for (int i = 0; i < letters.length; i++) {
			for (int j = 0; j < letters[0].length; j++) {
				letters[i][j] = 0;
			}
		}
		for (int i = 0; i < field.getSize(); i++) {
			int g = groups[i];
			int q = 0;
			do {
				q = (int)(5 * Math.random());
			} while(letters[g][q] > 0);
			letters[g][q]++;
			field.setLetterAt(i, q);
		}
	}
	
	public int getAttemptCount() {
		return attemptCount;
	}

}
