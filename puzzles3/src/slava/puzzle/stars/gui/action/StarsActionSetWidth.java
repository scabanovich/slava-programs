package slava.puzzle.stars.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import slava.puzzle.stars.gui.StarsComponent;
import slava.puzzle.stars.model.*;
import slava.puzzle.template.action.PuzzleAction;

public class StarsActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		StarsModel model = (StarsModel)manager.getModel();
		StarsSetsField f = model.getField();
		int l = f.getWidth();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 8 || l > 12) throw new Exception("Out of range");
			setSize(l, f.getHeight());
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void setSize(int width, int height) {
		StarsModel model = (StarsModel)manager.getModel();
		StarsSetsField f = model.getField();
		if(width == f.getWidth()) return;
		model.getField().setSize(width);
		model.setSolutionInfo(null);
		model.setSolutions(new ArrayList());
		((StarsComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}