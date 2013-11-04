package slava.puzzle.tictactoe.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

import com.slava.common.RectangularField;

import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzle.tictactoe.model.TicTacToeModel;
import slava.puzzle.tictactoe.model.TicTacToeState;
import slava.puzzle.tictactoe.model.solver.Result;

public class TicTacToeComponent extends PuzzleComponent {
	int margin = 5;
	int cellSize = 30;
	int halfCellSize = (cellSize / 2);
	int statusHeight = cellSize;
	Dimension componentSize = null;

	public TicTacToeComponent() {
		addMouseListener(new ML());
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		int w = getModel().getField().getWidth() * cellSize + margin * 2; 
		int h = getModel().getField().getHeight() * cellSize + margin * 3 + statusHeight;
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
		paintStatus(g);
		paintForceWin(g);
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
			drawToken(g, v, x, y);
//			int lc = getModel().getState().getLongest(i, TicTacToeState.CROSS);
//			if(lc != 0) g.drawString("" + lc, x + 3, y + 10);
//			int lz = getModel().getState().getLongest(i, TicTacToeState.ZERO);
//			if(lz != 0) g.drawString("" + lz, x + 15, y + 10);
		}
	}

	void drawToken(Graphics g, int v, int x, int y) {
		g.setColor(getColor(v));
		int x1 = x + m, x2 = x + cellSize - m, y1 = y + m, y2 = y + cellSize - m;
		if(v == TicTacToeState.CROSS) {
			g.drawLine(x1, y1, x2, y2);
			g.drawLine(x1, y2, x2, y1);
		} else if(v == TicTacToeState.ZERO) {
			g.drawOval(x1, y1, x2 - x1, y2 - y1);
		}
	}

	Color getColor(int turn) {
		return turn == TicTacToeState.CROSS ? Color.RED : Color.BLUE;
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

	void paintForceWin(Graphics g) {
		Result result = getModel().getResult();
		if(result == null) return;
		RectangularField f = getModel().getField();
		int[] forceWin = result.getForceWin();
		if(forceWin != null) {
			int turn = getModel().getState().getTurn();
			for (int t = 0; t < forceWin.length; t++) {
				int p = forceWin[t];
				g.setColor(getColor(turn));
				int x = margin + f.getX(p) * cellSize + halfCellSize;
				int y = margin + f.getY(p) * cellSize + halfCellSize + 6;
				g.drawString("" + (t + 1), x, y);
				turn = 1 - turn;
			}
		} else {
			boolean isForceDefence = result.isForceDefence();
			Map<Integer,Integer> ds = result.getResults();
			int losses = 0, uncertain = 0;
			for (Integer p: ds.keySet()) {
				int k = ds.get(p);
				if(k < 0) uncertain++; else losses++;
			}
			for (Integer p: ds.keySet()) {
				g.setColor(getColor(getModel().getTurn()));
				int x = margin + f.getX(p) * cellSize + halfCellSize;
				int y = margin + f.getY(p) * cellSize + halfCellSize + 6;
				int k = ds.get(p);
				if(k < 0 && !isForceDefence) continue;
				String s = k > 0 ? "L" + k : "?";
				g.drawString(s, x, y);
			}
		}
	}

	void paintStatus(Graphics g) {
		if(!getModel().isCompleted()) {
			int turn = getModel().getTurn();
			int x = margin;
			int y = componentSize.height - statusHeight - margin + 2;
			drawToken(g, turn, x, y);
			x += cellSize;
			y += 20;
			if(!getModel().getState().getPlacesForFive(turn).isEmpty()) {
				g.drawString("Wins in 1 move.", x, y);
			} else if(getModel().getState().getPlacesForFive(1 - turn).size() > 1) {
				g.drawString("Loses in 1 move.", x, y);
			} else if(getModel().getState().getPlacesForFive(1 - turn).size() == 1) {
				int p = getModel().getState().getPlacesForFive(1 - turn).iterator().next();
				g.drawString("Must close 4.", x, y);
				int xc = margin + cellSize * getModel().getField().getX(p) + halfCellSize;
				int yc = margin + cellSize * getModel().getField().getY(p) + halfCellSize + 6;
				g.drawString("!", xc, yc);
			}
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
