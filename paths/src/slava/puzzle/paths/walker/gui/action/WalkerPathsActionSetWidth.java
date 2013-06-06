package slava.puzzle.paths.walker.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.slava.common.RectangularField;

import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.template.action.PuzzleAction;
import slava.puzzle.template.gui.PuzzleComponent;

public class WalkerPathsActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		WalkerPathsModel model = (WalkerPathsModel)manager.getModel();
		RectangularField f = model.getField();
		int l = f.getWidth();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 4 || l > 11) throw new Exception("Out of range");
			setSize(l, f.getHeight());
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void setSize(int width, int height) {
		WalkerPathsModel model = (WalkerPathsModel)manager.getModel();
		RectangularField f = model.getField();
		if(width == f.getWidth() && height == f.getHeight()) return;
		model.changeFieldSize(width, height);
		model.setSolutionInfo(null);
		model.setSolutions(new ArrayList());
		((PuzzleComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}