package slava.puzzle.pentaletters.gui.action;

import java.awt.event.*;
import javax.swing.JOptionPane;

import slava.puzzle.pentaletters.gui.PentaLettersComponent;
import slava.puzzle.pentaletters.model.PentaLettersField;
import slava.puzzle.pentaletters.model.PentaLettersModel;
import slava.puzzle.template.action.PuzzleAction;

public class PentaLettersActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		PentaLettersModel model = (PentaLettersModel)manager.getModel();
		PentaLettersField f = model.getField();
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
		if((width * height) % 5 != 0 ) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Width or height must be devided by 5.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		PentaLettersModel model = (PentaLettersModel)manager.getModel();
		PentaLettersField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		model.setSize(width, height);
		((PentaLettersComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}
