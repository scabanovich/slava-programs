package slava.puzzle.ellen.gui;

import java.awt.*;
import java.awt.event.*;
import slava.puzzle.ellen.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class EllenComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	EllenField field;
	int selectedNode = -1;
	int cellSize = 32;
	Dimension componentSize = null;
	Point[] nodeCorners;
	
	public EllenComponent() {
		addMouseListener(new ML());
		addKeyListener(new KL());
	}

	public EllenModel getPentaLettersModel() {
		return (EllenModel)model;
	}
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getPentaLettersModel().getField();
		int w = (field.getWidth() + 2) * cellSize; 
		int h = (field.getHeight() + 2) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		updateNodeCenters(); 
	}
	
	void updateNodeCenters() {
		nodeCorners = new Point[field.getSize()];
		for (int i = 0; i < field.getSize(); i++) {
			int ix = cellSize * (field.x(i) + 1);
			int iy = cellSize * (field.y(i) + 1);
			nodeCorners[i] = new Point(ix, iy);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintNodes(g);
		paintPentaBorders(g);
		paintStatusLine(g);
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);
	static Color SOLUTIONCOLOR = new Color(255, 116, 116);
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
		g.setColor(Color.WHITE);
		g.fillRect(x + 1, y + 1, w - 2, h - 2);

		g.setColor(LINECOLOR);
		for (int i = 1; i < field.getWidth(); i++) {
			int ix = cellSize * (i + 1);
			g.drawLine(ix, y + 1, ix, y + h - 2);			
		}
		for (int i = 1; i < field.getHeight(); i++) {
			int iy = cellSize * (i + 1);
			g.drawLine(x + 1, iy, x + w - 2, iy);			
		}
	}
	
	void paintNodes(Graphics g) {
		int dx = cellSize / 2 - 4;
		int dy = cellSize / 2 + 6;
		for (int i = 0; i < field.getSize(); i++) {
			int ix = nodeCorners[i].x;
			int iy = nodeCorners[i].y;
			Color c = (i == selectedNode) ? Color.BLUE : Color.CYAN;
			g.setColor(c);
			g.fillRect(ix + 1, iy + 1, cellSize - 2, cellSize - 2);
			g.setColor(Color.BLACK);
			int letter = field.getLetterAt(i);
			if(letter == 1) {
				String s = "o";
				g.drawString(s, ix + dx, iy + dy);
			}
		}
	}
	
	void paintPentaBorders(Graphics g) {
		if(!model.isShowingSolution()) return;
		g.setColor(Color.BLACK);
		for (int i = 0; i < field.getSize(); i++) {
			int i2 = field.jump(i, 0);
			if(i2 >= 0 && field.getGroupAt(i) != field.getGroupAt(i2)) {
				int x1 = nodeCorners[i2].x;
				int y1 = nodeCorners[i2].y;
				g.drawLine(x1, y1, x1, y1 + cellSize);
			}
			i2 = field.jump(i, 1);
			if(i2 >= 0 && field.getGroupAt(i) != field.getGroupAt(i2)) {
				int x1 = nodeCorners[i2].x;
				int y1 = nodeCorners[i2].y;
				g.drawLine(x1, y1, x1 + cellSize, y1);
			}
		}		
	}

	
	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	class ML extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(i < 0) {
			} else {
				selectedNode = i;
				repaint();
			}
		}
	}
	
	class KL extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if(e.getKeyChar() == ' ' && model.isShowingSolution()) {
				getPentaLettersModel().nextSolution();
				repaint();
			}
			if(selectedNode >= 0) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					jumpToNeighbourNode(2);
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					jumpToNeighbourNode(0);
				} else if(e.getKeyCode() == KeyEvent.VK_UP) {
					jumpToNeighbourNode(3);
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					jumpToNeighbourNode(1);
				} else if(e.getKeyChar() == '-') {
					setLetter(0);
				} else if(e.getKeyChar() == 'o') {
					setLetter(1);
				}
			}
		}
		
	}
	

	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + field.getWidth())&& p.y < cellSize * (1 + field.getHeight());
	}
	
	int getNodeIndex(Point p) {
		int ix = (p.x - cellSize) / cellSize;
		int iy = (p.y - cellSize) / cellSize;
		//int dx = Math.abs(p.x - (ix * cellSize + cellSize));
		//int dy = Math.abs(p.y - (iy * cellSize + cellSize));
		return iy * field.getWidth() + ix;
	}
	
	void setLetter(int letter) {
		field.setLetterAt(selectedNode, letter);
		selectedNode++;
		if(selectedNode >= field.getSize()) selectedNode = 0;
		repaint();
	}
	
	void jumpToNeighbourNode(int d) {
		int n = field.jump(selectedNode, d);
		if(n < 0) return;
		selectedNode = n;
		repaint();
	}

}
