package slava.puzzle.stars.gui;

import java.awt.*;
import java.awt.event.*;

import slava.puzzle.stars.model.*;
import slava.puzzle.template.gui.*;
import slava.puzzle.template.model.PuzzleModel;
import slava.ui.util.PaintUtil;

public class StarsComponent extends PuzzleComponent implements StarsModelListener {
	StarsSetsField field;
	protected int cellSize = 32;
	protected Dimension componentSize = null;
	protected Point[] nodeCorners;

	int selectedNode = -1;
	
	public StarsComponent() {
		ML ml = new ML();
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}
	
	public StarsModel getStarsModel() {
		return (StarsModel)model;
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		getStarsModel().setListener(this);
		fieldSizeChanged();
	}

	public void fieldSizeChanged() {
		field = getStarsModel().getField();
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
		paintSetBorders(g);
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
			Color c = field.getSetAt(selectedNode) == field.getSetAt(i) ? Color.BLUE : Color.CYAN;
			g.setColor(c);
			g.fillRect(ix + 1, iy + 1, cellSize - 2, cellSize - 2);
			g.setColor(Color.BLACK);
			if(model.isShowingSolution()) {
				int[] solution = getStarsModel().getSelectedSolution();
				if(solution != null && solution[i] > 0) {
					String s = "x";
					g.drawString(s, ix + dx, iy + dy);
				}
			}
		}
	}
	
	protected char toChar(int letter) {
		return (char)(65 + letter);
	}
	
	protected void paintSetBorders(Graphics g) {
		doPaintSetBorders(g);
	}
	
	protected void doPaintSetBorders(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < field.getSize(); i++) {
			int i2 = field.jump(i, 0);
			if(i2 >= 0 && field.getSetAt(i) != field.getSetAt(i2)) {
				int x1 = nodeCorners[i2].x;
				int y1 = nodeCorners[i2].y;
				g.drawLine(x1, y1, x1, y1 + cellSize);
			}
			i2 = field.jump(i, 1);
			if(i2 >= 0 && field.getSetAt(i) != field.getSetAt(i2)) {
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
	
	Timer timer = new Timer();
	
	class Timer implements Runnable {
		boolean on = false;

		public void run() {
			try {Thread.sleep(500); } catch (Exception e) {}
			if(!on || selectedNode < 0) return;
			on = false;
			int c = field.getNewSetIndex();
			if(c < 0) return;
			getStarsModel().setSolutionInfo(null);
			field.setSetAt(selectedNode, c);
			repaint();
		}
		
	}

	class ML extends MouseAdapter implements MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(i >= 0) {
				selectedNode = i;
				repaint();
			}
			if(!timer.on) {
				timer.on = true;
				new Thread(timer).start();
			}
		}
		public void mouseReleased(MouseEvent e) {
			timer.on = false;
			selectedNode = -1;
			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			timer.on = false;
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			if(selectedNode < 0) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(i >= 0) {
				int c = field.getSetAt(selectedNode);
				if(field.getSetAt(i) != c && c >= 0) {
					getStarsModel().setSolutionInfo(null);
					field.setSetAt(i, c);
					repaint();
				}
			}
		}

		public void mouseMoved(MouseEvent e) {}
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

}
