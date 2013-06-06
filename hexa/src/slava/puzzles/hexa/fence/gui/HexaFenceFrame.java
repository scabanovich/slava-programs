package slava.puzzles.hexa.fence.gui;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.hexa.fence.gui.action.*;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFenceFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Hexagonal Fence");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new HexaFenceActionSolve());
		manager.addAction("generate", new HexaFenceActionGenerate());
//		manager.addAction("options", new SeaBattlePreferencesAction());
		manager.addAction("width", new HexaFenceActionSetWidth());
		manager.addAction("clean", new HexaFenceActionClean());

		HexaFenceModel model = new HexaFenceModel();
		frame.setModel(model);
		frame.setComponent(new HexaFenceComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Clean", "clean", null);
//		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
