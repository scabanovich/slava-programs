package slava.crossword.undo;

import java.util.*;

import slava.crossword.runtime.WordBase;
import slava.crossword.ui.CrosswordComponentModel;

public class ValueSetChange extends UndoableChange {
	int place;
	char oldValue;
	char newValue;
	CrosswordComponentModel model;
	UndoableChange[] changes;
	
	public ValueSetChange(CrosswordComponentModel model, byte[] bs) {
		this.model = model;
		List list = new ArrayList();
		char[] content = model.getCharContent();
		for (int i = 0; i < content.length; i++) {
			if(model.getNetAt(i) != 1) continue;
			char oldValue = content[i];
			char newValue = WordBase.instance.getLetterCoder().getWindowsChar(bs[i]);
			if(oldValue == newValue) continue;
			UndoableChange change = new ValueChange(model, i, oldValue, newValue);
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
