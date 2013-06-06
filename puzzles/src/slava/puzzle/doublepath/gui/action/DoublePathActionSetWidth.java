package slava.puzzle.doublepath.gui.action;

import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.doublepath.gui.DoublePathComponent;
import slava.puzzle.doublepath.model.*;
import slava.puzzle.template.action.PuzzleAction;

public class DoublePathActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		DoublePathModel model = (DoublePathModel)manager.getModel();
		DoublePathField f = model.getField();
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
		DoublePathModel model = (DoublePathModel)manager.getModel();
		DoublePathField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		model.setSize(width, height);
		((DoublePathComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}
