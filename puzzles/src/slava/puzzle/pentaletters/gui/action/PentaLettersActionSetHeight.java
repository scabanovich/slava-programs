package slava.puzzle.pentaletters.gui.action;

import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.pentaletters.model.PentaLettersField;
import slava.puzzle.pentaletters.model.PentaLettersModel;

public class PentaLettersActionSetHeight extends PentaLettersActionSetWidth {
	
	public void actionPerformed(ActionEvent e) {
		PentaLettersModel model = (PentaLettersModel)manager.getModel();
		PentaLettersField f = model.getField();
		int l = f.getHeight();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Height:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 5 || l > 15) throw new Exception("Out of range");
			setSize(f.getWidth(), l);
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
