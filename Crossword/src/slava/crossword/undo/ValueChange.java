package slava.crossword.undo;

import slava.crossword.ui.*;

public class ValueChange extends UndoableChange {
	int place;
	char oldValue;
	char newValue;
	CrosswordComponentModel model;
	
	public ValueChange(CrosswordComponentModel model, int place, char oldValue, char newValue) {
		this.model = model;
		this.place = place;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public void undo() {
		model.processKey(place, oldValue);
	}
	
	public void redo() {
		model.processKey(place, newValue);
	}

}
