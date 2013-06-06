package slava.crossword.undo;

import java.util.*;
import slava.crossword.ui.CrosswordComponentModel;

public class CleanLettersChange extends UndoableChange {
	CrosswordComponentModel model;
	UndoableChange[] changes;
	
	public CleanLettersChange(CrosswordComponentModel model) {
		this.model = model;
		List list = new ArrayList();
		for (int i = 0; i < model.getSize(); i++) {
			char ch = model.getCharContent()[i];
			if(ch == ' ') continue;
			UndoableChange change = new ValueChange(model, i, ch, ' ');
			list.add(change);
		}
		changes = (UndoableChange[])list.toArray(new UndoableChange[0]);
	}

	public void undo() {
		for (int i = 0; i < changes.length; i++) changes[i].undo();
	}
	
	public void redo() {
		for (int i = 0; i < changes.length; i++) changes[i].redo();
	}

}
