package slava.puzzle.football.gui.action;

import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.football.model.*;

public class FootballActionSetHeight extends FootballActionSetWidth {
	
	public void actionPerformed(ActionEvent e) {
		FootballModel model = (FootballModel)manager.getModel();
		FootballField f = model.getField();
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
