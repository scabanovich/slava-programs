package slava.crossword.undo;

import slava.crossword.ui.*;

public class NetChange extends UndoableChange {
	int place;
	int oldValue;
	int newValue;
	CrosswordComponentModel model;
	
	public NetChange(CrosswordComponentModel model, int place, int oldValue, int newValue) {
		this.model = model;
		this.place = place;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public void undo() {
		model.setNetAt(place, oldValue);
	}
	
	public void redo() {
		model.setNetAt(place, newValue);
	}

}
