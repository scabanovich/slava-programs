package slava.puzzle.ellen.analysis;

import slava.puzzle.ellen.model.*;

public class EllenGenerator {
	int figureSize = 4;

	EllenAnalyzer analyzer = new EllenAnalyzer();
	EllenField field = new EllenField();

	EllenAnalyzer analyzer2 = new EllenAnalyzer();

	public EllenGenerator() {
		analyzer.setField(field);
		analyzer.setIgnoreLetters(true);
		
		analyzer2.setField(field);
		analyzer2.setSolutionCountLimit(10);
	}
	
	public void setFigures(EllenFigures figures) {
		analyzer.setFigures(figures.getFigures(), figures.getFigureIndices());
		analyzer2.setFigures(figures.getFigures(), figures.getFigureIndices());
	}
	
	public void setField(EllenField field) {
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
	
	public EllenField getField() {
		return field;
	}
	
	private void generateLetters(int[] groups) {
		int groupCount = field.getSize() / figureSize;
		int[] letters = new int[groupCount];
		for (int i = 0; i < field.getSize(); i++) {
			field.setLetterAt(i, 0);
		}
		for (int i = 0; i < letters.length; i++) {
			letters[i] = 0;
		}
		int letterCount = 0;
		while(letterCount < groupCount * 2) {
			int i = (int)(field.getSize() * Math.random());
			if(field.getLetterAt(i) == 1) continue;
			int g = groups[i];
			if(letters[g] == 2) continue;
			letters[g]++;
			field.setLetterAt(i, 1);
			letterCount++;
		}
	}
	
	public int getAttemptCount() {
		return attemptCount;
	}
	
}
