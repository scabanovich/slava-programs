package slava.puzzles.connectfour.ui;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.connectfour.model.ConnectFourModel;
import slava.puzzles.connectfour.ui.action.ConnectFourActionSolve;

public class ConnectFourFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Connect Four");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new ConnectFourActionSolve());

		ConnectFourModel model = new ConnectFourModel();
		frame.setModel(model);
		frame.setComponent(new ConnectFourComponent());

		frame.run();
	}
}
