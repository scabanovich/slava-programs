package slava.puzzle.template.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import slava.puzzle.template.model.PuzzleLoader;

public class PuzzleSaveAction extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		PuzzleLoader loader = manager.model.getLoader();
		try {
			loader.save();
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.component, exc.getMessage());
			exc.printStackTrace();
		}		
	}

}
