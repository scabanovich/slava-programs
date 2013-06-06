package slava.puzzle.crossnumber.undo;

import javax.swing.JOptionPane;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.template.undo.UndoableChange;

public class SetSumChange extends UndoableChange {
	CrossNumberModel model;
	int p;
	boolean preferHorisontal;
	int oldSum;
	int newSum;
	
	public SetSumChange(CrossNumberModel model, int p, boolean preferHorisontal, int oldSum, int newSum) {
		this.model = model;
		this.p = p;
		this.preferHorisontal = preferHorisontal;
		this.oldSum = oldSum;
		this.newSum = newSum;
	}

	public void undo() {
		set(oldSum);
	}
	
	public void redo() {
		set(newSum);
	}
	
	void set(int value) {
		try {
			if(preferHorisontal) {
				model.getField().setHSum(p, "" + value);
			} else {
				model.getField().setVSum(p, "" + value);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
