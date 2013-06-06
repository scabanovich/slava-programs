package slava.puzzles.seabattle.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.slava.common.RectangularField;

import slava.puzzle.template.gui.PuzzleComponent;

public abstract class AbstractSeaBattleComponent extends PuzzleComponent {
	protected RectangularField field;
	protected int cellSize = 30;
	protected int halfCellSize = (cellSize / 2);

	protected int margin = 20;
	protected int statusLineHeight = 20;
	protected int valuesWidth = 24;
	protected int fleetWidth = 120;
	protected Dimension componentSize;
	
	public AbstractSeaBattleComponent() {}

	protected void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

}
