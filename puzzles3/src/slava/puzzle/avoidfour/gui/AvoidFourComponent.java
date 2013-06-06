package slava.puzzle.avoidfour.gui;

import java.awt.*;
import java.awt.event.*;
import slava.puzzle.avoidfour.model.*;
import slava.puzzle.template.gui.*;
import slava.puzzle.template.model.PuzzleModel;

public class AvoidFourComponent extends PuzzleComponent {
	AvoidFourField field;
	AvoidFourPuzzle puzzle;
	int cellSize = 30;
	int halfCellSize = (cellSize / 2);

	int margin = 20;
	int statisticsMarginHeight = 20;
	int statusLineHeight = 20;
	Dimension componentSize;
	
	AvoidFourPuzzleInput puzzleInput = new AvoidFourPuzzleInput();
	
	public AvoidFourComponent() {
		ML listener = new ML();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		puzzleInput.setComponent(this);
	}
	
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getAvoidFourModel().getField();
		puzzle = getAvoidFourModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * cellSize + margin + margin + statisticsMarginHeight + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getAvoidFourModel());
	}
	
	public AvoidFourModel getAvoidFourModel() {
		return (AvoidFourModel)model;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintStatistics(g);
		paintStatusLine(g);
		if(!hasFocus() && getAvoidFourModel().isSettingPuzzleMode()) {
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
			int x = margin + cellSize * (field.x(i));
			int y = margin + cellSize * (field.y(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);

			if(puzzleInput.isEnabled() && puzzleInput.getPosition() == i) {
				g.setColor(Color.ORANGE);
				g.drawArc(x + 5, y + 5, 20, 20, 0, 360);
			}
			if(fv == 1 && puzzle.getValue(i) > AvoidFourPuzzle.EMPTY_VALUE) {
				int v = puzzle.getValue(i);
				g.setColor((v == AvoidFourPuzzle.ERROR_VALUE) ? Color.RED
					: puzzle.isCondition(i) ? Color.BLUE
					: Color.GRAY);
				String s = (v == AvoidFourPuzzle.ERROR_VALUE) ? "E" :
				           (v == AvoidFourPuzzle.CROSS_VALUE) ? "X" :
				           (v == AvoidFourPuzzle.OSIGN_VALUE) ? "O" :
				           "";
				if(s.length() >= 0) g.drawString(s, x + halfCellSize - 4 * s.length(), y + halfCellSize + 4);
			}
		}
	}
	
	void paintStatistics(Graphics g) {
		int y = margin + cellSize * field.getHeight() + margin;
		String s = "" + puzzle.getValueCount(AvoidFourPuzzle.CROSS_VALUE) + ":" + puzzle.getValueCount(AvoidFourPuzzle.OSIGN_VALUE);
		g.drawString(s, margin, y); 
	}
	
	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	class ML extends MouseAdapter implements MouseMotionListener {
		int filterState = -1;

		public void mousePressed(MouseEvent e) {
			if(!isInField(e.getPoint())) return;
			if(!getAvoidFourModel().isDrawingFieldMode()) return;
			int p = getCell(e.getPoint());
			if(p < 0) return;
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
