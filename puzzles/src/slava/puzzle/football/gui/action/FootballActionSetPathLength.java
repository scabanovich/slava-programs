package slava.puzzle.football.gui.action;

import java.awt.event.*;

import javax.swing.JOptionPane;

import slava.puzzle.football.model.FootballModel;
import slava.puzzle.template.action.PuzzleAction;

public class FootballActionSetPathLength extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		FootballModel model = (FootballModel)manager.getModel();
		int l = model.getGeneratedPathLength();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Generated Path Length:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 2 || l > 20) throw new Exception("Out of range");
			model.setGeneratedPathLength(l);
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
