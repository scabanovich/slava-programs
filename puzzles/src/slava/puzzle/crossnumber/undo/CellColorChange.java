package slava.puzzle.crossnumber.undo;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.template.undo.UndoableChange;

public class CellColorChange extends UndoableChange {
	CrossNumberModel model;
	int p;

	public CellColorChange(CrossNumberModel model, int p) {
		this.model = model;
		this.p = p;
		redo();
	}
	
	public void undo() {
		model.flip(p);
	}
	
	public void redo() {
		model.flip(p);
	}

}
