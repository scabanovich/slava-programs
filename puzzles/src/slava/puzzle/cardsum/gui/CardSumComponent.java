package slava.puzzle.cardsum.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.cardsum.model.*;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class CardSumComponent extends PuzzleComponent {
	CardSumField field;
	int cellSize = 40;
	int halfCellSize = (cellSize / 2);
	Dimension componentSize = null;
	
	public CardSumComponent() {
		addMouseListener(new ML());
		addMouseMotionListener(new ML());
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = ((CardSumModel)model).getField();
		int w = (field.getWidth() + 2) * cellSize; 
		int h = (field.getHeight() + 3) * cellSize + 20;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize); 
	}
	
	public CardSumModel getCardSumModel() {
		return (CardSumModel)model;
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintSums(g);
		paintStatusLine(g);
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}
	
	void paintCells(Graphics g) {
		for (int i = 0; i < field.size(); i++) {
			g.setColor(Color.WHITE);
			int x = cellSize * (1 + field.x(i));
			int y = cellSize * (1 + field.y(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			g.setColor(Color.BLACK);
			String s = "";
			if(model.isShowingSolution()) {
				s = field.getVisual(getCardSumModel().getSolutionCardAt(i));
				if(field.getCard(i) < 0) g.setColor(Color.CYAN.darker());
			} else if(field.getCard(i) >= 0) {
				s = field.getVisual(field.getCard(i));
			}
			if(s.length() >= 0) g.drawString(s, x + halfCellSize - 4 * s.length(), y + halfCellSize + 4);
		}
	}
	
	void paintSums(Graphics g) {
		int y = cellSize * (1 + field.getHeight()) + halfCellSize;
		int y1 = y + halfCellSize;
		for (int i = 0; i < field.getWidth(); i++) {
			int x = cellSize * (1 + field.x(i)) + 1;
			g.setColor(Color.WHITE);
			g.fillOval(x, y, cellSize - 3, cellSize - 3);
			g.setColor(Color.BLACK);
			g.drawOval(x, y, cellSize - 3, cellSize - 3);
			int v = field.getSum(i);
			if(v >= 0) {
				String s = "" + v;
				g.drawString(s, x + halfCellSize - 5 * s.length(), y1);			
			}
		}
	}	
	
	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}
	class ML extends MouseAdapter implements MouseMotionListener {
		public void mouseReleased(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(isInField(p)) {
				int i = getCell(p);
				if(i < 0) return;
				if(model.isShowingSolution()) {
					model.setSolutionInfo(null);
					repaint();
				}
				if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
					setCard(i);
				}
			} else if(isInSums(p)) {
				int column = getSumColumn(p);
				if(column < 0) return;
				model.setSolutionInfo(null);
				if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
					setSum(column);
				}
			}
		}
		public void mouseDragged(MouseEvent e) {}
		
		TipInfo cd = new TipInfo();
		public void mouseMoved(MouseEvent e) {
			if(!cd.setPoint(e.getPoint())) return;
			String tipText = (cd.place >= 0) ? getCardSumModel().getCardDistributionTip(cd.place)
			  : (cd.column >= 0) ? getCardSumModel().getSumDistributionTip(cd.column)
			  : null ; 
			setToolTipText(tipText);
		}
	}

	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + field.getWidth()) && p.y < cellSize * (1 + field.getHeight());
	}
	
	boolean isInSums(Point p) {
		return p.x > cellSize && p.x < cellSize * (1 + field.getWidth()) && 
		       p.y > cellSize * (1 + field.getHeight()) + halfCellSize &&
		       p.y < cellSize * (2 + field.getHeight()) + halfCellSize;
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - cellSize) / cellSize;
		int y = (p.y - cellSize) / cellSize;
		return x + y * field.getWidth();
	}
	
	int getSumColumn(Point p) {
		if(!isInSums(p)) return -1;
		return (p.x - cellSize) / cellSize;
	}
	
	void setCard(int i) {
		String v = field.getVisual(field.getCard(i));
		String s = JOptionPane.showInputDialog(this, "Input ", v);
		if(s == null || s.equals(v)) return;
		try {
			getCardSumModel().setCardAt(i, s);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		repaint();
	}
	
	void setSum(int column) {
		int sum = field.getSum(column);
		String v = (sum < 0) ? "" : "" + sum;
		String s = JOptionPane.showInputDialog(this, "Input sum at column=" + column, v);
		if(s == null || s.equals(v)) return;
		try {
			getCardSumModel().setSumAt(column, s);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		repaint();
	}
	
	class TipInfo {
		int place = -1;
		int column = -1;
		boolean setPoint(Point p) {
			int i = -1;
			int c = -1;
			if(!model.isRunning() && model.isShowingSolution()) { 
				i = getCell(p);
				c = getSumColumn(p);
			}
			boolean changed = (place != i) || (column != c);
			place = i;
			column = c;
			return changed;
		}
	}

}
