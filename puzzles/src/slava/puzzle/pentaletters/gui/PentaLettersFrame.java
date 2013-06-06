package slava.puzzle.pentaletters.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.pentaletters.gui.action.*;
import slava.puzzle.pentaletters.model.*;
import slava.puzzle.template.action.*;
import slava.puzzle.template.gui.*;

public class PentaLettersFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Letters in Pentamino");
		PentaLettersModel model = new PentaLettersModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new PentaLettersActionSolve());
		manager.addAction("generate", new PentaLettersActionGenerate());
		manager.addAction("generateLogical", new PentaLettersActionGenerateLogical());
		manager.addAction("solveLogically", new PentaLettersLogicalSolve());
		manager.addAction("width", new PentaLettersActionSetWidth());
		manager.addAction("height", new PentaLettersActionSetHeight());
		frame.setModel(model);
		frame.getMenuBar().createMenuItem("Run", "Generate Logical", "generateLogical", null);
		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solveLogically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.setComponent(new PentaLettersComponent());
		frame.run();
	}

}
