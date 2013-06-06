package slava.puzzle.template.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import slava.puzzle.template.model.PuzzleLoader;

public class PuzzleSaveAsAction extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		PuzzleLoader loader = manager.getModel().getLoader();
		String name = loader.getName();
		name = JOptionPane.showInputDialog(manager.component, "File Name", name);
		if(name == null || name.length() == 0) return;
		loader.setName(name);
		try {
			loader.save();
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(manager.component, exc.getMessage());
		}		
	}

}
