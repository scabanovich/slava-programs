package slava.puzzle.cardsum.gui;

import slava.puzzle.cardsum.gui.action.CardSumActionSolve;
import slava.puzzle.cardsum.model.CardSumModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class CardSumFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Cards");
		CardSumModel model = new CardSumModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new CardSumActionSolve());
		frame.setModel(model);
		frame.setComponent(new CardSumComponent());
		frame.run();
	}


}
