package slava.puzzle.football.gui;

import slava.puzzle.football.gui.action.*;
import slava.puzzle.football.model.FootballModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class FootballFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Football");
		FootballModel model = new FootballModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
//		manager.addAction("solve", new PentaLettersActionSolve());
		manager.addAction("generate", new FootballActionGenerate());
		manager.addAction("width", new FootballActionSetWidth());
		manager.addAction("height", new FootballActionSetHeight());
		manager.addAction("pathLength", new FootballActionSetPathLength());
		frame.setModel(model);
		frame.setComponent(new FootballComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.getMenuBar().createMenuItem("Edit", "Set path length", "pathLength", null);
		frame.run();
	}

}
