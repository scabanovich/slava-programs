package slava.puzzle.doublepath.gui;

import java.awt.*;
import java.awt.event.*;
import slava.puzzle.doublepath.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class DoublePathComponent extends PuzzleComponent {
	DoublePathModel doublePathModel;
	DoublePathField field;
	int cellSize = 30;
	Dimension componentSize = null;
	static Color LINECOLOR = new Color(216, 216, 216);
	
	public DoublePathComponent() {
		addMouseListener(new ML());
		addKeyListener(new KL());
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		doublePathModel = (DoublePathModel)model;
		field = doublePathModel.getField();
		int w = (doublePathModel.getField().getWidth() + 2) * cellSize; 
		int h = (doublePathModel.getField().getHeight() + 2) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize); 
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintStatusLine(g);
		paintNodes(g);
		paintSolution(g);
	}

	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
		g.setColor(Color.WHITE);
		int half = cellSize / 2;
		g.fillRect(x + 1, y + 1, w - 2, h - 2);

		g.setColor(LINECOLOR);
		for (int i = 0; i < field.getWidth(); i++) {
			int ix = cellSize * (i + 1) + half;
			g.drawLine(ix, y + 1, ix, y + h - 2);			
		}
		for (int i = 0; i < field.getHeight(); i++) {
			int iy = cellSize * (i + 1) + half;
			g.drawLine(x + 1, iy, x + w - 2, iy);			
		}
	}
	
	void paintStatusLine(Graphics g) {
		String s = doublePathModel.getStatusText();
		g.setColor(Color.BLACK);
		g.drawString(s, 5, componentSize.height - 10);
	}
	
	void paintNodes(Graphics g) {
		int half = cellSize / 2;
		for (int i = 0; i < field.getSize(); i++) {
			int s = field.getState(i);
			if(s == 0) continue;
			int ix = cellSize * (field.x(i) + 1) + half;
			int iy = cellSize * (field.y(i) + 1) + half;
			Color color = ((s % 2) == 1) ? Color.RED : Color.GREEN;
			g.setColor(color);
			g.fillOval(ix - 4, iy - 4, 8, 8);
		}
		int s = doublePathModel.getSelectedNode();
		if(s >= 0) {
			int ix = cellSize * (field.x(s) + 1) + half;
			int iy = cellSize * (field.y(s) + 1) + half;
			g.setColor(Color.BLUE);
			g.drawOval(ix - 4, iy - 4, 8, 8);
		}
	}

	void paintSolution(Graphics g) {
		int half = cellSize / 2;
		int[][] solution = doublePathModel.getCurrentSolution();
		if(solution == null) return;
		for (int i = 0; i < solution.length; i++) {
			int b = solution[i][0];
			int e = solution[i][1];
			int s = field.getState(b);
			Color color = ((s % 2) == 1) ? Color.RED : Color.GREEN;
			g.setColor(color);
			int ixb = cellSize * (field.x(b) + 1) + half;
			int iyb = cellSize * (field.y(b) + 1) + half;
			int ixe = cellSize * (field.x(e) + 1) + half;
			int iye = cellSize * (field.y(e) + 1) + half;
			g.drawLine(ixb, iyb, ixe, iye);
		}
	}

	class ML extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			int i = getNode(p);
			if(i == doublePathModel.getSelectedNode() || i < 0) return;
			doublePathModel.setSelectedNode(i);
			requestFocus();
			repaint();
		}
	}
	
	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + doublePathModel.getField().getWidth()) && p.y < cellSize * (1 + doublePathModel.getField().getHeight());
	}
	
	int getNode(Point p) {
		int ix = (p.x - cellSize) / cellSize; 
		int iy = (p.y - cellSize) / cellSize;
		int xc = (ix + 1) * cellSize + cellSize / 2;
		int yc = (iy + 1) * cellSize + cellSize / 2;
		if(Math.abs(p.x - xc) > 8) return -1;  
		if(Math.abs(p.y - yc) > 8) return -1;  
		return iy * field.getWidth() + ix;
	}

	class KL implements KeyListener {

		public void keyTyped(KeyEvent e) {}

		public void keyPressed(KeyEvent e) {}

		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_1) {
				setFieldState(1);
			} else if(e.getKeyCode() == KeyEvent.VK_2) {
				setFieldState(2);
			} else if(e.getKeyCode() == KeyEvent.VK_0) {
				setFieldState(0);
			} else if(e.getKeyChar() == ' ' && model.isShowingSolution()) {
				doublePathModel.nextSolution();
				repaint();
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				moveSelection(2);
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				moveSelection(0);
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				moveSelection(3);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				moveSelection(1);
			}
		}
		
	}
	
	void setFieldState(int v) {
		if(doublePathModel.getSelectedNode() < 0) return;
		int old = field.getState(doublePathModel.getSelectedNode());
		if(old == v || old > 2) return;
		field.setState(doublePathModel.getSelectedNode(), v);
		doublePathModel.clearSolutions();
		repaint();
	}
	
	void moveSelection(int d) {
		if(doublePathModel.getSelectedNode() < 0) return;
		int n = field.jump(doublePathModel.getSelectedNode(), d);
		if(n < 0) return;
		doublePathModel.setSelectedNode(n);
		repaint();		
	}

	
}
