package slava.puzzle.football.gui.action;

import java.awt.event.*;

import javax.swing.JOptionPane;

import slava.puzzle.football.gui.FootballComponent;
import slava.puzzle.football.model.FootballField;
import slava.puzzle.football.model.FootballModel;
import slava.puzzle.template.action.PuzzleAction;

public class FootballActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		FootballModel model = (FootballModel)manager.getModel();
		FootballField f = model.getField();
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
		FootballModel model = (FootballModel)manager.getModel();
		FootballField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		f.setSize(width, height);
		model.setState(null);
		((FootballComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}
