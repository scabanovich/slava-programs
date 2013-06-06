package slava.puzzle.ellen.analysis;

import slava.puzzle.pentaletters.analysis.PentaFieldChecker;
import slava.puzzle.pentaletters.model.PentaLettersField;

public class EllenFieldChecker extends PentaFieldChecker {

	protected void check(PentaLettersField field) {
		int valueCount = 2;
		int[] c = new int[valueCount];
		for (int i = 0; i < field.getSize(); i++) {
			c[field.getLetterAt(i)]++;
		}
		boolean b = true;
		for (int i = 1; i < valueCount && b; i++) {
			if(c[i] != c[i - 1]) b = false;
		}
		if(b) return;
		String message = "distribution:";
		for (int i = 0; i < valueCount; i++) {
			message += " " + c[i];
		}
		throw new RuntimeException(message);
	}

}
