package slava.puzzle.shopping.model;

import slava.puzzle.template.model.PuzzleModel;

public class ShoppingModel extends PuzzleModel {
	ShoppingField field = new ShoppingField();
	
	public ShoppingModel() {
		field.setSize(7, 9);
		setLoader(new ShoppingLoader());
	}
	
	public ShoppingField getField() {
		return field;
	}
	
}
