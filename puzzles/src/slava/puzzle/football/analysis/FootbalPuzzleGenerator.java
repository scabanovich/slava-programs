package slava.puzzle.football.analysis;

import slava.puzzle.football.model.FootballField;

public class FootbalPuzzleGenerator {
	FootballProblemChecker checker = new FootballProblemChecker();
	FootballField field;
	int pathLength = 9;
	
	public void setField(FootballField field) {
		this.field = field;
		checker.setField(field);
	}
	
	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}
	
	public void generate() {
		FootballPathGenerator g = new FootballPathGenerator();
		g.setField(field);
		g.setPathLength(pathLength);
		g.solve();
		System.out.println("Path:");
		printField();
		fill(field);
		System.out.println("Problem:");
		printField();
	}

	public void fill(FootballField field) {
		this.field = field;
		checker.setPath(field.getValues());
		int[] s = getOrder();
		for (int i = 0; i < s.length; i++) {
			int p = s[i];
			if(p == 0 || p == field.getSize() - 1 || field.getValueAt(p) > 0) continue;
			for (int a = 0; a < 4; a++) {
				int value = (int)(5 * Math.random()) + 1;
				field.setValueAt(p, value);
				if(checker.check()) break;
				field.setValueAt(p, 0);
			}
			if(field.getValueAt(p) == 0) {
				for (int value = 1; value < 10; value++) {
					field.setValueAt(p, value);
					if(checker.check()) break;
					field.setValueAt(p, 0);
				}
			}
		}
	}
	
	int[] getOrder() {
		int[] s = new int[field.getSize()];
		for (int i = 0; i < s.length; i++) s[i] = i;
		for (int i = s.length - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			int k = s[i];
			s[i] = s[j];
			s[j] = k;
		}
		return s;
	}
	
	public int[] getState() {
		return checker.computeState();
	}

	private void printField() {
		for (int i = 0; i < field.getSize(); i++) {
			System.out.print(" " + field.getValueAt(i));
			if(field.x(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		FootballField field = new FootballField();
		field.setSize(9, 6);
		FootbalPuzzleGenerator g = new FootbalPuzzleGenerator();
		g.setField(field);
		g.generate();
	}

}
