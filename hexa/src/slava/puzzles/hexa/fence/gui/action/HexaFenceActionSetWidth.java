package slava.puzzles.hexa.fence.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.slava.common.RectangularField;
import slava.puzzle.template.action.PuzzleAction;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzles.hexa.common.HexaField;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFenceActionSetWidth extends PuzzleAction {
	
	public void actionPerformed(ActionEvent e) {
		HexaFenceModel model = (HexaFenceModel)manager.getModel();
		HexaField f = model.getField();
		int l = f.getSideLength();
		String s = JOptionPane.showInputDialog(manager.getComponent(), "Width:", "" + l);
		if(s == null) return;
		try {
			l = Integer.parseInt(s);
			if(l < 3 || l > 8) throw new Exception("Out of range");
			setSize(l, f.getHeight());
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Incorrect value", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void setSize(int width, int height) {
		HexaFenceModel model = (HexaFenceModel)manager.getModel();
		RectangularField f = model.getField();
		if(width == f.getWidth()) return;
		model.changeFieldSize(width);
		model.setSolutionInfo(null);
		model.setSolutions(new ArrayList());
		((PuzzleComponent)manager.getComponent()).setModel(model);
		manager.getComponent().repaint();
	}

}