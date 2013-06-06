package slava.puzzle.sudoku.ui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.sudoku.model.SudokuModel;
import slava.puzzle.sudoku.ui.SudokuPreferencesDialog;
import slava.puzzle.template.action.*;

public class SudokuPreferencesAction extends PuzzleAction {
	
	public SudokuPreferencesAction() {}

	public void actionPerformed(ActionEvent e) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuPreferencesDialog d = new SudokuPreferencesDialog();
		d.setInput(model);
		int i = d.execute(manager.getComponent());
		if(i == 0) manager.getComponent().repaint();		
	}

}
