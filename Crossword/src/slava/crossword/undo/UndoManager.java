package slava.crossword.undo;

public class UndoManager {
	static UndoManager instance = new UndoManager();
	
	public static UndoManager getInstance() {
		return instance;
	}
	
	UndoableChange head = new UndoableChange();
	UndoableChange current = head;
	
	public void undo() {
		if(current == head) return;
		try {
			current.undo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		current = current.previous;
	}
	
	public void redo() {
		if(current.next == null) return;
		current = current.next;
		try {
			current.redo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChange(UndoableChange change) {
		current.next = change;
		change.previous = current;
		current = change;
	}
	
	public void clean() {
		current = head;
		head.next = null;
	}

}
