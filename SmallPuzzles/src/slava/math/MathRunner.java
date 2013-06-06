package slava.math;

public class MathRunner {

	static void run(String expression) {
		Parser p = new Parser();
		Expression x = p.parse(expression);
		SustitutionsEnumerator e = new SustitutionsEnumerator();
		e.setLength(p.letterIndices.size());
		e.setCharacters(p.characters);
		e.setExpression(x);
		try {
			e.solve();
		} catch (RuntimeException exc) {
			System.out.println("Error: " + exc.getMessage());
		}
	}

	public static void main(String[] args) {
		String expression="man*voman=ma??????";
		run(expression);
	}

}

//car*road=problem
//fat*fat=weight
//fog*fog=cloud+cloud+cloud+cloud
