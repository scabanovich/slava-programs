package slava.puzzle.crossnumber.undo;

import javax.swing.JOptionPane;

import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.template.undo.UndoableChange;

public class GenerationChange extends UndoableChange {
	CrossNumberModel model;
	int[] oldHSum;
	int[] newHSum;
	int[] oldVSum;
	int[] newVSum;
	
	public GenerationChange(CrossNumberModel model, int[] hsum, int[] vsum) {
		this.model = model;
		oldHSum = (int[])model.getField().getHSum().clone();
		oldVSum = (int[])model.getField().getVSum().clone();
		newHSum = hsum;
		newVSum = vsum;
	}

	public void undo() {
		set(oldHSum, oldVSum);
	}
	
	public void redo() {
		set(newHSum, newVSum);
	}
	
	void set(int[] hsum, int[] vsum) {
		try {
			int[] hs = model.getField().getHSum();
			for (int i = 0; i < hs.length; i++) {
				if(newHSum[i] != -2) hs[i] = hsum[i];
			}
			int[] vs = model.getField().getVSum();
			for (int i = 0; i < vs.length; i++) {
				if(newVSum[i] != -2) vs[i] = vsum[i];
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
