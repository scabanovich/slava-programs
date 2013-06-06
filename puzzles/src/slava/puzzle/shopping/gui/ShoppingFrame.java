package slava.puzzle.shopping.gui;

import slava.puzzle.shopping.gui.action.ShoppingActionSolve;
import slava.puzzle.shopping.model.*;
import slava.puzzle.template.action.*;
import slava.puzzle.template.gui.*;

public class ShoppingFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Shopping Route");
		ShoppingModel model = new ShoppingModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new ShoppingActionSolve());
		//configureManager... 
		frame.setModel(model);
		frame.setComponent(new ShoppingComponent());
		frame.run();
	}

}
