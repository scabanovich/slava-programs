package slava.puzzle.tictactoe.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.slava.common.RectangularField;

import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzle.tictactoe.model.TicTacToeModel;

public class TicTacToeComponent extends PuzzleComponent {
	int margin = 5;
	int cellSize = 20;
	int halfCellSize = (cellSize / 2);
	Dimension componentSize = null;

	public TicTacToeComponent() {
		addMouseListener(new ML());
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		int w = getModel().getField().getWidth() * cellSize + margin * 2; 
		int h = getModel().getField().getHeight() * cellSize + margin * 2;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize); 
	}

	public TicTacToeModel getModel() {
		return (TicTacToeModel)model;
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintWin(g);
	}

	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = margin, y = margin;
		int w = getModel().getField().getWidth() * cellSize;
		int h = getModel().getField().getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}

	int m = 3;
	
	void paintCells(Graphics g) {
		RectangularField field = getModel().getField();
		for (int i = 0; i < field.getSize(); i++) {
			g.setColor(Color.WHITE);
			int x = margin + cellSize * field.getX(i);
			int y = margin + cellSize * field.getY(i);
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			g.setColor(Color.BLACK);
			String s = "";
			int v = getModel().getValue(i);
			g.setColor(getColor(v));
			int x1 = x + m, x2 = x + cellSize - m, y1 = y + m, y2 = y + cellSize - m;
			if(v == TicTacToeModel.CROSS) {
				g.drawLine(x1, y1, x2, y2);
				g.drawLine(x1, y2, x2, y1);
			} else if(v == TicTacToeModel.ZERO) {
				g.drawOval(x1, y1, x2 - x1, y2 - y1);
			}
		}
	}

	Color getColor(int turn) {
		return turn == TicTacToeModel.CROSS ? Color.RED : Color.BLUE;
	}

	void paintWin(Graphics g) {
		if(getModel().isCompleted()) {
			RectangularField f = getModel().getField();
			g.setColor(getColor(1 - getModel().getTurn()));
			int b = getModel().getFiveStart(), e = getModel().getFiveEnd();
			int xb = margin + halfCellSize + f.getX(b) * cellSize;
			int yb = margin + halfCellSize + f.getY(b) * cellSize;
			int xe = margin + halfCellSize + f.getX(e) * cellSize;
			int ye = margin + halfCellSize + f.getY(e) * cellSize;
			g.drawLine(xb, yb, xe, ye);
		}
	}
	
	class ML extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			if(isInField(p)) {
				int i = getCell(p);
				if(i < 0) return;
				getModel().move(i);
				repaint();

			}
		}
	}

	boolean isInField(Point p) {
		return p.x > margin && p.y > margin && p.x < margin + cellSize * getModel().getField().getWidth() && p.y < margin + cellSize * getModel().getField().getHeight();
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - margin) / cellSize;
		int y = (p.y - margin) / cellSize;
		return getModel().getField().getIndex(x, y);
	}
	
}
