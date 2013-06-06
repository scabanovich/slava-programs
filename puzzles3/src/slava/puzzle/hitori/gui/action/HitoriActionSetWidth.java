package slava.puzzle.hitori.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import com.slava.common.RectangularField;
import slava.puzzle.hitori.model.HitoriModel;
import slava.puzzle.template.action.PuzzleAction;

public class HitoriActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		HitoriModel model = (HitoriModel)manager.getModel();
		RectangularField f = model.getField();
		int l = f.getWidth();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 4 || l > 20) throw new Exception("Out of range");
			setSize(l, f.getHeight());
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void setSize(int width, int height) {
		HitoriModel model = (HitoriModel)manager.getModel();
		RectangularField f = model.getField();
		if(width == f.getWidth()) return;
		model.setSolutionInfo(null);
		model.getProblemInfo().setSolution(null);
		model.changeFieldSize(width);
		manager.getComponent().repaint();
	}

}
