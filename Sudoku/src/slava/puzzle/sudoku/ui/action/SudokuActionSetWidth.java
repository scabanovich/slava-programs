package slava.puzzle.sudoku.ui.action;

import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.sudoku.model.*;
import slava.puzzle.sudoku.ui.SudokuComponent;
import slava.puzzle.template.action.PuzzleAction;

public class SudokuActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuDesignField f = model.getField();
		int l = f.getWidth();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 5 || l > 15) throw new Exception("Out of range");
			setSize(l, f.getHeight());
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void setSize(int width, int height) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuDesignField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		model.setSize(width);
		((SudokuComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}
