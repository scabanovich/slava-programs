package slava.puzzle.template.action;

import java.util.*;
import javax.swing.JComponent;
import slava.puzzle.template.model.PuzzleModel;

public class PuzzleActionManager {
	protected PuzzleModel model;
	protected JComponent component;
	protected Map actions = new HashMap();
	protected Thread thread = null;
	
	public PuzzleActionManager() {}
	
	public void initActions() {
		addAction("solve", new PuzzleActionSolve());
		addAction("stop", new PuzzleActionStop());
		addAction("open", new PuzzleLoadAction());
		addAction("save", new PuzzleSaveAction());
		addAction("saveAs", new PuzzleSaveAsAction());
		addAction("exit", new PuzzleActionExit());
		addAction("undo", new PuzzleUndoAction());
		addAction("redo", new PuzzleRedoAction());
	}
	
	public void initUndoActions() {
		
	}
	
	public void setModel(PuzzleModel model) {
		this.model = model;
	}
	
	public PuzzleModel getModel() {
		return model;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}
	
	public JComponent getComponent() {
		return component;
	}
	
	public void addAction(String name, PuzzleAction action) {
		actions.put(name, action);
		action.setManager(this);
	}
	
	public void removeAction(String name) {
		actions.remove(name);
	}
	
	public PuzzleAction getAction(String name) {
		return (PuzzleAction)actions.get(name);
	}
	
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Thread getThread() {
		return thread;
	}

}
