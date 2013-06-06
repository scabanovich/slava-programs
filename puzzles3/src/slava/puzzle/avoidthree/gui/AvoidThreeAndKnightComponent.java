package slava.puzzle.avoidthree.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.slava.common.TwoDimField;

import slava.puzzle.avoidthree.model.AvoidThreeAndKnightModel;
import slava.puzzle.avoidthree.model.AvoidThreeAndKnightPuzzle;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class AvoidThreeAndKnightComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	
	TwoDimField field;
	AvoidThreeAndKnightPuzzle puzzle;

	int cellSize = 30;
	int halfCellSize = (cellSize / 2);

	int margin = 20;
	int statisticsMarginHeight = 20;
	int statusLineHeight = 20;
	Dimension componentSize;

	AvoidThreeAndKnightPuzzleInput puzzleInput = new AvoidThreeAndKnightPuzzleInput();

	public AvoidThreeAndKnightComponent() {
		ML listener = new ML();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		puzzleInput.setComponent(this);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getAvoidThreeModel().getField();
		puzzle = getAvoidThreeModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * cellSize + margin + margin + statisticsMarginHeight + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getAvoidThreeModel());
	}

	public AvoidThreeAndKnightModel getAvoidThreeModel() {
		return (AvoidThreeAndKnightModel)model;
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintStatistics(g);
		paintStatusLine(g);
		if(!hasFocus() && getAvoidThreeModel().isSettingPuzzleMode()) {
			requestFocus();
		}
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = margin, y = margin;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}

	void paintCells(Graphics g) {
		for (int i = 0; i < field.getSize(); i++) {
			int fv = puzzle.getFilter()[i];
			if(fv == 1) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.BLACK);
			}
			int x = margin + cellSize * (field.getX(i));
			int y = margin + cellSize * (field.getY(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);

			if(puzzleInput.isEnabled() && puzzleInput.getPosition() == i) {
				g.setColor(Color.ORANGE);
				g.drawArc(x + 5, y + 5, 20, 20, 0, 360);
			}
			if(fv == 1 && puzzle.getValue(i) != AvoidThreeAndKnightPuzzle.EMPTY_VALUE) {
				int v = puzzle.getValue(i);
				g.setColor((v == AvoidThreeAndKnightPuzzle.ERROR_VALUE) ? Color.RED
						: puzzle.isCondition(i) ? Color.BLUE
						: Color.GRAY);
				String s = "";
				if(v == AvoidThreeAndKnightPuzzle.ERROR_VALUE) s = "E";
				else {
					s = "" + (char)(v - 2 + 97);
				}
				if(s.length() >= 0) g.drawString(s, x + halfCellSize - 4 * s.length(), y + halfCellSize + 4);
			}
		}
	}

	void paintStatistics(Graphics g) {
		//TODO
	}

	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	class ML extends MouseAdapter implements MouseMotionListener {
		int filterState = -1;

		public void mousePressed(MouseEvent e) {
			if(!isInField(e.getPoint())) return;
			int p = getCell(e.getPoint());
			if(p < 0) return;
			if(!getAvoidThreeModel().isDrawingFieldMode()) {
				puzzleInput.setPosition(p);
				return;
			}
			filterState = 1 - puzzle.getFilter()[p];
			puzzle.getFilter()[p] = filterState;
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			filterState = -1;
		}

		public void mouseDragged(MouseEvent e) {
			if(filterState < 0 || !isInField(e.getPoint())) return;
			int p = getCell(e.getPoint());
			if(p < 0 || p >= field.getSize() || filterState == puzzle.getFilter()[p]) return;
			puzzle.getFilter()[p] = filterState;
			repaint();
		}

		public void mouseMoved(MouseEvent e) {}
	}

	boolean isInField(Point p) {
		return p.x > margin && 
			   p.y > margin && 
			   p.x < componentSize.width - margin && 
			   p.y < componentSize.height - margin - statisticsMarginHeight - statusLineHeight;
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - margin) / cellSize;
		int y = (p.y - margin) / cellSize;
		return x + y * field.getWidth();
	}
	
}
