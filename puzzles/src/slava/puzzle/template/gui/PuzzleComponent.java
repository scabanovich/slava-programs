package slava.puzzle.template.gui;

import javax.swing.JComponent;

import slava.puzzle.template.model.PuzzleModel;

public class PuzzleComponent extends JComponent {
	protected PuzzleModel model;
	
	public void setModel(PuzzleModel model) {
		this.model = model;
	}

}
