package diogen2005;

public class OlimpicRunner {
	static int[] FORM = {
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,
		0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,
		0,1,0,1,1,0,0,1,0,0,0,0,0,0,0,
		0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,
		1,0,0,0,0,0,1,1,1,0,1,0,1,1,0,
		0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,
		1,0,0,0,1,0,0,1,0,1,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	};

	public static void main(String[] args) {
		OlimpicWords words = new OlimpicWords();
		OlimpicSolver solver = new OlimpicSolver();
		solver.setWords(words);
		OlimpicField field = new OlimpicField();
		field.setSize(15, 10);
		solver.setField(field);
		solver.setForm(FORM);
		solver.solve();
	}
}
/*
vacancies 0
 + + + + + + + + + + + + + + +
 + + + + + + M u N H E N + + +
 + + M + + + E + + + + + + + +
 S T O K G O L ' M + + + + + +
 I + S + + + ' + + + + + T + P
 D + K + + + B A R S E L O N A
 N + V + + + U + + E + + K + R
 E + A + + + R + + U + + I + I
 Y + + A F I N y + L + + O + z
 + + + + + + + + + + + + + + +
*/
