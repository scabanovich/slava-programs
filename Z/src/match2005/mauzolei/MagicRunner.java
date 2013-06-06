package match2005.mauzolei;

public class MagicRunner {

	public static void main(String[] args) {
		MauzoleiField field = new MauzoleiField();
		field.setSize(8, 8, 4, MauzoleiForm.REV_FILTER);
		
		MagicForm form = new MagicForm();
		form.setField(field);
		form.load();
		
		MagicEquations equations = new MagicEquations();
		equations.setField(field);
		equations.setForm(form.form);
		equations.setLayerSums(new int[]{10,19,26,36});
		equations.build();
		
		MagicFiller filler = new MagicFiller();
		filler.setField(field);
		filler.setForm(form.form);
		filler.setSequence(equations.sequence);
		filler.setPositionToEquations(equations.positionToEquations);
		filler.solve();

	}

}

/*
 + + + + + + + +
 + + + + + + + +
 + + + + + + + +
 + + + 5 5 + + +
 + + + 5 5 + + +
 + + + + + + + +
 + + + + + + + +
 + + + + + + + +

 + + + + + + + +
 + + + + + + + +
 + + 0 2 8 9 + +
 + + 3 9 3 4 + +
 + + 9 0 7 3 + +
 + + 7 8 1 3 + +
 + + + + + + + +
 + + + + + + + +

 + + + + + + + +
 + 8 5 2 2 1 8 +
 + 6 9 5 4 2 0 +
 + 2 7 0 6 7 4 +
 + 3 4 8 3 2 6 +
 + 6 1 5 2 5 7 +
 + 1 0 6 9 9 1 +
 + + + + + + + +

 0 2 5 8 4 7 3 7
 1 6 2 3 4 6 5 9
 5 8 4 0 8 7 1 3
 9 3 8 8 0 2 0 6
 3 4 7 2 9 1 9 1
 6 8 4 3 0 6 9 0
 5 4 0 8 7 1 2 9
 7 1 6 4 4 6 7 1
*/