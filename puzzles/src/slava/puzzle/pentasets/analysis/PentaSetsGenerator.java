package slava.puzzle.pentasets.analysis;

import slava.algebra.PaintGraphProblemGenerator;
import slava.puzzle.pentaletters.analysis.PentaLettersAnalyzer;
import slava.puzzle.pentaletters.model.*;

public class PentaSetsGenerator {
	PentaLettersAnalyzer analyzer = new PentaLettersAnalyzer();
	PentaLettersField field = new PentaLettersField();
	PaintGraphProblemGenerator g = new PaintGraphProblemGenerator();

	public PentaSetsGenerator() {
		analyzer.setField(field);
		analyzer.setIgnoreLetters(true);
	}
	
	public void setFigures(PentaFigures figures) {
		analyzer.setFigures(figures.getFigures(), figures.getFigureIndices());
	}
	
	public void setField(PentaLettersField field) {
		this.field.setSize(field.getWidth(), field.getHeight());
		for (int i = 0; i < field.getSize(); i++) {
			this.field.setLetterAt(i, -1);
		}
	}
	
	int attemptCount;

	public void generate() {
		attemptCount = 0;
		analyzer.solve();
		int[] groups = (int[])analyzer.getSolutions().get(0);
		for (int i = 0; i < groups.length; i++) {
			field.setGroupAt(i, groups[i]);
		}
		int[][] graph = createGraph(field);
		g.setGraph(graph);
		g.setColorCount(5);
		g.generate();
		int[] c = g.getColoring();
		for (int i =0; i < field.getSize(); i++) field.setLetterAt(i, c[i]);
	}
	
	public static int[][] createGraph(PentaLettersField field) {
		int size = field.getSize();
		int[][] graph = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int d = 0; d < 4; d++) {
				int j = field.jump(i, d);
				if(j < 0) continue;
				graph[i][j] = 1;
				graph[j][i] = 1;
				//diagonal
				int d1 = d + 1;
				if(d1 >= 4) d1 = 0;
				j = field.jump(j, d1);
				if(j < 0) continue;
				graph[i][j] = 1;
				graph[j][i] = 1;
			}
		}
		int[] groups = field.getGroups();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(j == i || groups[i] != groups[j]) continue;
				graph[i][j] = 1;
				graph[j][i] = 1;
			}
		}
		return graph;
	}
	
	public PentaLettersField getField() {
		return field;
	}
	
	public int getAttemptCount() {
		return attemptCount;
	}

}
