package slava.puzzle.shopping.gui;

import java.awt.*;
import java.awt.event.*;

import slava.puzzle.shopping.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class ShoppingComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	ShoppingField f;
	Node selectedNode = null;
	Transition selectedTransition = null;
	int cellSize = 64;
	Dimension componentSize = null;
	Point[] nodeCenters;
	
	public ShoppingComponent() {
		addMouseListener(new ML());
		addKeyListener(new KL());
	}
	
	public ShoppingModel getShoppingModel() {
		return (ShoppingModel)model;
	}
	
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		f = getShoppingModel().getField();
		int w = (f.getWidth() + 1) * cellSize; 
		int h = (f.getHeight() + 1) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		updateNodeCenters(); 
	}
	
	void updateNodeCenters() {
		nodeCenters = new Point[f.getSize()];
		for (int i = 0; i < f.getSize(); i++) {
			int ix = cellSize * (f.x(i) + 1);
			int iy = cellSize * (f.y(i) + 1);
			nodeCenters[i] = new Point(ix, iy);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintTransitions(g);
		paintNodes(g);
		paintStatusLine(g);
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);
	static Color SOLUTIONCOLOR = new Color(255, 116, 116);
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize / 2, y = cellSize / 2;
		int w = f.getWidth() * cellSize;
		int h = f.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
		g.setColor(Color.WHITE);
		g.fillRect(x + 1, y + 1, w - 2, h - 2);

		g.setColor(LINECOLOR);
		for (int i = 0; i < f.getWidth(); i++) {
			int ix = cellSize * (i + 1);
			g.drawLine(ix, y + 1, ix, y + h - 2);			
		}
		for (int i = 0; i < f.getHeight(); i++) {
			int iy = cellSize * (i + 1);
			g.drawLine(x + 1, iy, x + w - 2, iy);			
		}
	}
	
	void paintNodes(Graphics g) {
		for (int i = 0; i < f.getSize(); i++) {
			Node n = f.getNode(i);
			if(!n.isEnabled() && n != selectedNode) continue; 
			int ix = nodeCenters[i].x;
			int iy = nodeCenters[i].y;
			Color c = (!n.isEnabled()) ? Color.WHITE : Color.LIGHT_GRAY;
			g.setColor(c);
			g.fillOval(ix - 12, iy - 12, 24, 24);
			c = (n == selectedNode) ? Color.BLUE : (n.isEnabled()) ? Color.BLACK : LINECOLOR;
			g.setColor(c);
			g.drawOval(ix - 12, iy - 12, 24, 24);
			int k = n.getKind();
			if(k > 0) {
				String s = (k == 1) ? "P" : "S";
				g.setColor(Color.BLACK);
				g.drawString(s, ix - 3, iy + 4);
			}
		}
	}
	
	void paintTransitions(Graphics g) {
		//Graphics2D g2 = (Graphics2D)g;
		for (int i = 0; i < f.getSize(); i++) {
			Node n = f.getNode(i);
			Transition[] ts = n.getTransitions();
			for (int j = 0; j < ts.length; j++) {
				Node n1 = ts[j].getSource();
				Node n2 = ts[j].getTarget();
				if(n1.getId() > n2.getId()) continue;
				Transition backward = ts[j].getBackwardTransition();
				boolean isSelected = ts[j] == selectedTransition || backward == selectedTransition;
				boolean isPassed = model.isShowingSolution() && (ts[j].getSolutions() > 0 || ts[j].getBackwardTransition().getSolutions() > 0);
				Color c = (isPassed) ? SOLUTIONCOLOR : (isSelected) ? Color.BLUE : Color.BLACK;
				g.setColor(c);
				int ix1 = nodeCenters[n1.getId()].x;
				int iy1 = nodeCenters[n1.getId()].y;
				int ix2 = nodeCenters[n2.getId()].x;
				int iy2 = nodeCenters[n2.getId()].y;
				g.drawLine(ix1, iy1, ix2, iy2);
				
				if(!ts[j].isEnabled()) {
					int ex = (ix1 * 3 + ix2) / 4;
					int ey = (iy1 * 3 + iy2) / 4;
					g.setColor(Color.RED);
					g.fillOval(ex - 4, ey - 4, 8, 8);
				}
				if(!backward.isEnabled()) {
					int ex = (ix1 + ix2 * 3) / 4;
					int ey = (iy1 + iy2 * 3) / 4;
					g.setColor(Color.RED);
					g.fillOval(ex - 4, ey - 4, 8, 8);
				}
				int cx = (ix1 + ix2) / 2 - 3;
				int cy = (iy1 + iy2) / 2 - 3;
				g.setColor(Color.BLACK);
				String s = ts[j].getKind() == 0 ? "" : ts[j].getKind() == 1 ? "P" : "S";
				if(model.isShowingSolution()) {
					int q = ts[j].getSolutions() + backward.getSolutions();
					if(q > 1) s += "-" + ts[j].getSolutions() + "-" + backward.getSolutions();
				}
				if(s.length() > 0) g.drawString(s, cx, cy);
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
			model.setSolutionInfo(null);			
			int i = getNodeIndex(p);
			if(i < 0) {
				selectedNode = null;
				pressedNode = null;
				Transition t = getTransition(p);
				if(t != null && t != selectedTransition) {
					selectedTransition = t;
					repaint();
				}
			} else {
				Node releasedNode = f.getNode(i);
				if(pressedNode != null && pressedNode != releasedNode) {
					drawTransition(pressedNode, releasedNode);
				} else if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
					if(selectedNode == null) {
						onNodeClicked(i);
					} else {
						drawTransition(selectedNode, releasedNode);
					}
				} else {
					onNodeClicked(i);
				}
			}
			pressedNode = null;
			repaint();
		}
		
		Node pressedNode = null;
		public void mousePressed(MouseEvent e) {
			if(model.isRunning()) return;
			int i = getNodeIndex(e.getPoint());
			if(i < 0) {
				pressedNode = null;
			} else {
				pressedNode = f.getNode(i);
			}
		}
		
	}
	
	void onNodeClicked(int i) {
		selectedNode = f.getNode(i);
		selectedTransition = null;
	}

	void drawTransition(Node source, Node target) {
		if(source == null) return;
		setNodeEnabled(source, true);
		if(target == source) return;
		setNodeEnabled(target, true);
		Transition t = source.findTransition(target.getId());
		if(t == null) {
			new Transition(source, target);
		} else {
			t.setEnabled(true);
		}
		t = target.findTransition(source.getId());
		if(t == null) {
			t = new Transition(target, source);
			t.setEnabled(false);				
		}
		selectedNode = null;
		selectedTransition = t;
	}

	boolean isInField(Point p) {
		return p.x > cellSize / 2 && p.y > cellSize / 2 && p.x < cellSize * (1 + f.getWidth()) - cellSize / 2 && p.y < cellSize * (1 + f.getHeight()) - cellSize / 2;
	}
	
	int getNodeIndex(Point p) {
		int ix = (p.x - (cellSize / 2)) / cellSize;
		int iy = (p.y - (cellSize / 2)) / cellSize;
		int dx = Math.abs(p.x - (ix * cellSize + cellSize));
		int dy = Math.abs(p.y - (iy * cellSize + cellSize));
		if(dx * dx + dy * dy > 144) return -1;
		return iy * f.getWidth() + ix;
	}

	Transition getTransition(Point p) {
		for (int i = 0; i < f.getSize(); i++) {
			Transition[] ts = f.getNode(i).getTransitions();
			for (int j = 0; j < ts.length; j++) {
				Node n1 = ts[j].getSource();
				Node n2 = ts[j].getTarget();
				if(n1.getId() > n2.getId()) continue;
				if(isBetween(nodeCenters[n1.getId()], p, nodeCenters[n2.getId()])) return ts[j];
			}
		}
		return null;
	}
	
	boolean isBetween(Point p1, Point pc, Point p2) {
		if(p1.x < p2.x && (pc.x < p1.x || pc.x > p2.x)) return false;
		if(p1.y < p2.y && (pc.y < p1.y || pc.y > p2.y)) return false;
		if(p1.x == p2.x && Math.abs(pc.x - p1.x) > 3) return false;
		if(p1.y == p2.y && Math.abs(pc.y - p1.y) > 3) return false;
		double delta = (pc.x * (p2.y - p1.y) + pc.y * (p1.x - p2.x) - p1.x * p2.y + p2.x * p1.y) / Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
		return delta < 3;
	}

	class KL extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if(selectedNode != null) {
				if(e.getKeyChar() == 'p') {
					setNodeKind(1);
				} else if(e.getKeyChar() == 's') {
					setNodeKind(2);
				} else if(e.getKeyChar() == ' ') {
					setNodeKind(0);
				} else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					setNodeEnabled(selectedNode, false);
				} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					jumpToNeighbourNode(2);
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					jumpToNeighbourNode(0);
				} else if(e.getKeyCode() == KeyEvent.VK_UP) {
					jumpToNeighbourNode(3);
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					jumpToNeighbourNode(1);
				}
			} else if(selectedTransition != null) {
				if(e.getKeyChar() == 'p') {
					setTransitionKind(1);
				} else if(e.getKeyChar() == 's') {
					setTransitionKind(2);
				} else if(e.getKeyChar() == ' ') {
					setTransitionKind(0);
				} else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					selectedTransition.dispose();
					selectedTransition = null;
					repaint();
				} else if(e.getKeyChar() == '1') {
					setTransitionDirection(1);
				} else if(e.getKeyChar() == '2') {
					setTransitionDirection(2);
				} else if(e.getKeyChar() == '3') {
					setTransitionDirection(3);
				}
			}
		}
		
	}
	
	void setNodeEnabled(Node n, boolean b) {
		if(n.isEnabled() == b) return;
		n.setEnabled(b);
		if(!b) n.setKind(0);
		repaint();		
	}
	
	void setNodeKind(int k) {
		if(selectedNode.getKind() == k && selectedNode.isEnabled()) return;
		selectedNode.setEnabled(true);
		selectedNode.setKind(k);
		repaint();
	}
	
	void setTransitionKind(int k) {
		if(selectedTransition.getKind() == k) return;
		selectedTransition.setKind(k);
		selectedTransition.getBackwardTransition().setKind(k);
		repaint();
	}
	
	void jumpToNeighbourNode(int d) {
		Node n = f.getNeighbour(selectedNode, d);
		if(n == null) return;
		selectedNode = n;
		repaint();
	}
	
	void setTransitionDirection(int k) {
		Transition backward = selectedTransition.getBackwardTransition();
		boolean sE = (k % 2) == 1;
		boolean bE = ((k / 2) % 2) == 1;
		selectedTransition.setEnabled(sE);
		backward.setEnabled(bE);
		repaint(); 
	}
	
}
