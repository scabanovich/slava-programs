package slava.puzzle.pentaletters.analysis;

import slava.puzzle.pentaletters.model.*;

public class PentaLettersLogicalGenerator {
	PentaLettersAnalyzer analyzer = new PentaLettersAnalyzer();
	PentaLettersField field = new PentaLettersField();

	PentaLettersLogicalAnalyzer analyzer2 = new PentaLettersLogicalAnalyzer();

	public PentaLettersLogicalGenerator() {
		analyzer.setField(field);
		analyzer.setIgnoreLetters(true);
		
		analyzer2.setField(field);
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
			boolean logicallySolvable = false;
			do {
				generateLetters(groups);
				analyzer2.solve();
				logicallySolvable = analyzer2.isLogicallySolvable();
				try { Thread.sleep(10); } catch (Exception e) {}
				attemptCount++;
				ac++;
			} while(!logicallySolvable && ac < 50);
			System.out.println("x");
			if(logicallySolvable) {
				System.out.println(analyzer2.getSolution());
				return;
			} 
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
