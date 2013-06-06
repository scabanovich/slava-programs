package slava.puzzle.crossnumber.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import slava.puzzle.crossnumber.*;
import slava.puzzle.crossnumber.gui.CrossNumberComponent;
import slava.puzzle.template.action.PuzzleAction;
import slava.puzzle.template.undo.UndoManager;

public class CrossNumberActionSetWidth extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		CrossNumberField f = model.getField();
		int l = f.getWidth();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 5 || l > 20) throw new Exception("Out of range");
			setSize(l, f.getHeight());
			UndoManager.getInstance().clean();
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void setSize(int width, int height) {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		CrossNumberField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		f.setSize(width, height);
		model.setSolutionInfo(null);
		((CrossNumberComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}
