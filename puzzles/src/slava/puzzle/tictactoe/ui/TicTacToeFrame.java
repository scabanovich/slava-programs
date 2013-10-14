package slava.puzzle.tictactoe.ui;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzle.tictactoe.model.TicTacToeModel;
import slava.puzzle.tictactoe.ui.action.TicTacToeBackAction;
import slava.puzzle.tictactoe.ui.action.TicTacToeNewGameAction;

public class TicTacToeFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Tic Tac Toe");
		TicTacToeModel model = new TicTacToeModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("back", new TicTacToeBackAction());
		manager.addAction("newgame", new TicTacToeNewGameAction());
		frame.setModel(model);
		frame.setComponent(new TicTacToeComponent());
		frame.getMenuBar().createMenuItem("Play", "New Game", "newgame", null);
		frame.getMenuBar().createMenuItem("Play", "Back", "back", KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
		frame.run();
	}
}
