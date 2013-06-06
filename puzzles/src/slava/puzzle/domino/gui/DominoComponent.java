package slava.puzzle.domino.gui;

import java.awt.*;
import java.awt.event.*;

import slava.puzzle.domino.model.DominoModel;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class DominoComponent extends PuzzleComponent {
	DominoModel dominoModel;
	int cellSize = 20;
	Dimension componentSize = null;

	public DominoComponent() {
		addMouseListener(new ML());
	}
	
	public DominoModel getDominoModel() {
		return (DominoModel)model;
	}
	
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		dominoModel = getDominoModel();
		int w = (dominoModel.getWidth() + 2) * cellSize; 
		int h = (dominoModel.getHeight() + 2) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize); 
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintDominoCells(g);
		paintStatusLine(g);
		paintDominoBorders(g);
		paintSolution(g);
		paintRestrictions(g);
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = dominoModel.getWidth() * cellSize;
		int h = dominoModel.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}
	
	void paintDominoCells(Graphics g) {
		for (int i = 0; i < dominoModel.getSize(); i++) {
			Color c = (dominoModel.getFieldGroupIndex(i) == 0) ? Color.WHITE : Color.CYAN;
			g.setColor(c);
			int x = cellSize * (1 + dominoModel.x(i));
			int y = cellSize * (1 + dominoModel.y(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
		}
	}
	
	void paintDominoBorders(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < dominoModel.getSize(); i++) {
			int n = dominoModel.jump(i, 0);
			if(n != -1) {
				if(dominoModel.getFieldGroupIndex(i) != dominoModel.getFieldGroupIndex(n)) {
					int x = cellSize * (2 + dominoModel.x(i));
					int y = cellSize * (1 + dominoModel.y(i));
					int h = cellSize;
					g.drawLine(x, y, x, y + h);
				}
			}
			n = dominoModel.jump(i, 1);
			if(n != -1) {
				if(dominoModel.getFieldGroupIndex(i) != dominoModel.getFieldGroupIndex(n)) {
					int x = cellSize * (1 + dominoModel.x(i));
					int y = cellSize * (2 + dominoModel.y(i));
					int h = cellSize;
					g.drawLine(x, y, x + h, y);
				}
			}
		}
	}
	
	void paintStatusLine(Graphics g) {
		String s = (dominoModel.isRunning()) ? "Wait..." :
					"" + dominoModel.getPiecesCount() + " pieces of total " + dominoModel.getRequiredPiecesNumber() + " are set";
		g.setColor(Color.BLACK);
		g.drawString(s, 5, componentSize.height - 10);
	}
	
	class ML extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			int i = getCell(p);
			int j = getNextCell(p, i);
			if(i < 0 || j < 0) return;
			dominoModel.flip(i, j);
			repaint();
		}
	}
	
	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + dominoModel.getWidth()) && p.y < cellSize * (1 + dominoModel.getHeight());
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - cellSize) / cellSize;
		int y = (p.y - cellSize) / cellSize;
		return x + y * dominoModel.getWidth();
	}

	int getNextCell(Point p, int i) {
		int x = (dominoModel.x(i) + 1) * cellSize;
		int y = (dominoModel.y(i) + 1) * cellSize;
		int d = 2;
		int dc = p.x - x;
		if(cellSize + x - p.x < dc) {
			dc = cellSize + x - p.x;
			d = 0;
		}
		if(p.y - y < dc) {
			dc = p.y - y;
			d = 3;
		}
		if(cellSize + y - p.y < dc) {
			dc = cellSize + y - p.y;
			d = 1;
		}
		return (dc < 3) ? dominoModel.jump(i, d) : -1;
	}
	
	private void paintSolution(Graphics g) {
		if(!model.isShowingSolution()) return;
		g.setColor(Color.BLACK);
		for (int i = 0; i < dominoModel.getSize(); i++) {
			if(dominoModel.getFieldGroupIndex(i) < 1) continue;
			int x = cellSize * (1 + dominoModel.x(i));
			int y = cellSize * (2 + dominoModel.y(i));
			g.drawString("" + dominoModel.getValue(i), x + 5, y - 5);
		}
	}
	
	private void paintRestrictions(Graphics g) {
		if(!dominoModel.isShowingSolution()) return;
		g.setColor(Color.RED);
		for (int i = 0; i < dominoModel.getHeight(); i++) {
			int x = cellSize;
			int y = cellSize * (2 + i);
			if(dominoModel.getHRestrictions(i).length > 0)
				g.drawString("+", x - 10, y - 5);
		}
		for (int i = 0; i < dominoModel.getWidth(); i++) {
			int x = cellSize * (1 + i);
			int y = cellSize;
			if(dominoModel.getVRestrictions(i).length > 0)
				g.drawString("+", x + 5, y - 5);
		}
	}
	
}
