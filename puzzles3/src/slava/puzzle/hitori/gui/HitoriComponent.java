package slava.puzzle.hitori.gui;

import java.awt.*;
import java.awt.event.*;
import com.slava.common.RectangularField;
import slava.puzzle.hitori.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.ui.util.PaintUtil;

public class HitoriComponent extends PuzzleComponent implements HitoriModelListener {
	private static final long serialVersionUID = 1L;

	RectangularField field = new RectangularField();
	protected int cellSize = 32;
	protected Dimension componentSize = null;
	protected Point[] nodeCorners;
	
	int selectedNode = -1;

	public HitoriComponent() {
		ML ml = new ML();
		addMouseListener(ml);
		KL kl = new KL();
		addKeyListener(kl);
	}

	public HitoriModel getHitoriModel() {
		return (HitoriModel)model;
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		getHitoriModel().setListener(this);
		fieldSizeChanged();
	}

	public void fieldSizeChanged() {
		field = getHitoriModel().getField();
		int w = (field.getWidth() + 2) * cellSize; 
		int h = (field.getHeight() + 2) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		updateNodeCenters(); 
	}

	void updateNodeCenters() {
		nodeCorners = new Point[field.getSize()];
		for (int i = 0; i < field.getSize(); i++) {
			int ix = cellSize * (field.getX(i) + 1);
			int iy = cellSize * (field.getY(i) + 1);
			nodeCorners[i] = new Point(ix, iy);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintNodes(g);
		paintStatusLine(g);
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);
	static Color SOLUTIONCOLOR = new Color(60, 60, 60);
	
	void paintFieldBorder(Graphics g) {
		PaintUtil.paintFieldBorder(g, cellSize, field.getWidth(), field.getHeight(), LINECOLOR);
	}
	
	void paintNodes(Graphics g) {
		int dx = cellSize / 2 - 4;
		int dy = cellSize / 2 + 6;
		int[] ns = getHitoriModel().getProblemInfo().getNumbers();
		int[] ss = getHitoriModel().getProblemInfo().getSolution();
		for (int i = 0; i < field.getSize(); i++) {
			int ix = nodeCorners[i].x;
			int iy = nodeCorners[i].y;
			Color c = ss == null || ss[i] == 0 ? Color.WHITE : ss[i] == 1 ? SOLUTIONCOLOR : Color.RED;
			if(ss == null && i == selectedNode) {
				c = Color.YELLOW;
			}
			g.setColor(c);
			g.fillRect(ix + 1, iy + 1, cellSize - 2, cellSize - 2);
			g.setColor(Color.BLACK);
			if(ns[i] > 0) g.drawString("" + ns[i], ix + dx, iy + dy);
		}
	}
	
	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}
	
	class ML extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(i >= 0) {
				selectedNode = i;
	    		getHitoriModel().setSolutionInfo(null);
	    		getHitoriModel().getProblemInfo().setSolution(null);
				repaint();
			}
		}
	}
	
	class KL extends KeyAdapter {
	    public void keyPressed(KeyEvent e) {
	    	if(selectedNode < 0) return;
	    	int ch = e.getKeyCode();
	    	int n = getNumber(ch);
	    	if(n >= 0 && getHitoriModel().getProblemInfo().getSolution() == null) {
	    		getHitoriModel().getProblemInfo().getNumbers()[selectedNode] = n;
	    		selectedNode++;
	    		if(selectedNode >= field.getSize()) selectedNode++;
	    		repaint();
	    	}
	    }		
	}
	
	int getNumber(int ch) {
		if(ch == 32) return 0;
		if(ch >= 48 && ch < 58) return ch - 48;
		if(ch >= 97 && ch < 97 + field.getWidth()) return ch - 87;
		if(ch >= 65 && ch < 65 + field.getWidth()) return ch - 55;
		return -1;
	}

	protected boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + field.getWidth())&& p.y < cellSize * (1 + field.getHeight());
	}
	
	protected int getNodeIndex(Point p) {
		int ix = (p.x - cellSize) / cellSize;
		int iy = (p.y - cellSize) / cellSize;
		return iy * field.getWidth() + ix;
	}

}
