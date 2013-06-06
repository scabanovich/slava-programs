package slava.puzzle.pentaletters.gui;

import java.awt.*;
import java.awt.event.*;
import slava.puzzle.pentaletters.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.ui.util.PaintUtil;

public class PentaLettersComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	protected PentaLettersField field;
	protected int selectedNode = -1;
	protected int cellSize = 32;
	protected Dimension componentSize = null;
	protected Point[] nodeCorners;
	
	public PentaLettersComponent() {
		addMouseListener(new ML());
		addKeyListener(new KL());
	}

	public PentaLettersModel getPentaLettersModel() {
		return (PentaLettersModel)model;
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
		updateNodeCenters();
		paintFieldBorder(g);
		paintNodes(g);
		paintPentaBorders(g);
		paintStatusLine(g);
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);
	static Color SOLUTIONCOLOR = new Color(255, 116, 116);
	
	void paintFieldBorder(Graphics g) {
		PaintUtil.paintFieldBorder(g, cellSize, field.getWidth(), field.getHeight(), LINECOLOR);
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
			String s = "" + toChar(field.getLetterAt(i));
			g.drawString(s, ix + dx, iy + dy);
		}
	}
	
	protected char toChar(int letter) {
		return (char)(65 + letter);
	}
	
	protected void paintPentaBorders(Graphics g) {
		if(!model.isShowingSolution()) return;
		doPaintPentaBorders(g);
	}
	
	protected void doPaintPentaBorders(Graphics g) {
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
			doKeyReleasedOnSelectedNode(e);
		}
	}
	
	protected void doKeyReleasedOnSelectedNode(KeyEvent e) {
		if(selectedNode < 0) return;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			jumpToNeighbourNode(2);
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			jumpToNeighbourNode(0);
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			jumpToNeighbourNode(3);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			jumpToNeighbourNode(1);
		} else if(e.getKeyChar() == 'a') {
			setLetter(0);
		} else if(e.getKeyChar() == 'b') {
			setLetter(1);
		} else if(e.getKeyChar() == 'c') {
			setLetter(2);
		} else if(e.getKeyChar() == 'd') {
			setLetter(3);
		} else if(e.getKeyChar() == 'e') {
			setLetter(4);
		}
	}	

	protected boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + field.getWidth())&& p.y < cellSize * (1 + field.getHeight());
	}
	
	protected int getNodeIndex(Point p) {
		int ix = (p.x - cellSize) / cellSize;
		int iy = (p.y - cellSize) / cellSize;
		//int dx = Math.abs(p.x - (ix * cellSize + cellSize));
		//int dy = Math.abs(p.y - (iy * cellSize + cellSize));
		return iy * field.getWidth() + ix;
	}
	
	protected void setLetter(int letter) {
		field.setLetterAt(selectedNode, letter);
		selectedNode++;
		if(selectedNode >= field.getSize()) selectedNode = 0;
		repaint();
	}
	
	protected void jumpToNeighbourNode(int d) {
		int n = field.jump(selectedNode, d);
		if(n < 0) return;
		selectedNode = n;
		repaint();
	}

}
