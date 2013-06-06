package slava.puzzle.pentaletters.analysis;

import slava.puzzle.pentaletters.model.PentaLettersField;

public class PentaFieldChecker {

	protected void check(PentaLettersField field) {
		int[] c = new int[5];
		for (int i = 0; i < field.getSize(); i++) {
			c[field.getLetterAt(i)]++;
		}
		boolean b = true;
		for (int i = 1; i < 5 && b; i++) {
			if(c[i] != c[i - 1]) b = false;
		}
		if(b) return;
		String message = "distribution:";
		for (int i = 0; i < 5; i++) {
			message += " " + c[i];
		}
		throw new RuntimeException(message);
	}

}
