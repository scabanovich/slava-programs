package slava.puzzle.crossnumber.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import slava.puzzle.crossnumber.CrossNumberField;
import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.template.undo.UndoManager;

public class CrossNumberActionSetHeight extends CrossNumberActionSetWidth {

	public void actionPerformed(ActionEvent e) {
		CrossNumberModel model = (CrossNumberModel)manager.getModel();
		CrossNumberField f = model.getField();
		int l = f.getHeight();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Height:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 5 || l > 20) throw new Exception("Out of range");
			setSize(f.getWidth(), l);
			UndoManager.getInstance().clean();
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
