package slava.puzzle.crossnumber.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import slava.puzzle.crossnumber.CrossNumberField;
import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.undo.CellColorChange;
import slava.puzzle.crossnumber.undo.SetSumChange;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzle.template.undo.UndoManager;

public class CrossNumberComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	CrossNumberField field;
	int cellSize = 40;
	Dimension componentSize = null;

	public CrossNumberComponent() {
		addMouseListener(new ML());
		addMouseMotionListener(new ML());
	}
	
	public CrossNumberModel getCrossNumberModel() {
		return (CrossNumberModel)model;
	}
	
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		updateComponentSize();
	}

	int q = 0;
	void updateComponentSize() {
		field = getCrossNumberModel().getField();
		updateCellSize();
		int w = (field.getWidth() + 2) * cellSize; 
		int h = (field.getHeight() + 2) * cellSize + 20;
		if(q == w * 100 + h) return;
		q = w * 100 + h;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize); 
	}
	void updateCellSize() {
		int w = field.getWidth();
		int h = field.getHeight();
		if(w > 16 || h > 16) {
			cellSize = 32;
		} else if(w > 12 || h > 12) {
			cellSize = 36;
		} else {
			cellSize = 40;
		}
	}
	
	public void paint(Graphics g) {
		updateComponentSize();
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
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
			int status = field.getStatus(i);
			Color c = (status == 4) ? Color.WHITE : (status == 0) ? Color.BLUE : Color.BLACK;
			g.setColor(c);
			int x = cellSize * (1 + field.x(i));
			int y = cellSize * (1 + field.y(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			if(status == 3) {
				c = Color.WHITE;
				g.setColor(c);
				g.drawLine(x + 1, y + 1, x + cellSize - 1, y + cellSize - 1);
			}
			g.setColor(Color.WHITE);
			if((status & 1) != 0) {
				int s = field.getHSum(i);
				if(s >= 0) g.drawString("" + s, x + cellSize - 16, y + 14);
			}
			if((status & 2) != 0) {
				int s = field.getVSum(i);
				if(s >= 0) g.drawString("" + s, x + 4, y + cellSize - 5);
			}
			g.setColor(Color.BLACK);
			if(model.isShowingSolution() && status == 4) {
				int v = field.getValue(i);
				if(v > 0) g.drawString("" + v, x + 10, y + (cellSize / 2));
				if(v < 0) g.drawString("x", x + 10, y + (cellSize / 2));
				else g.drawString("", x + 10, y + (cellSize / 2));
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
			if(!isInField(p)) return;
			int i = getCell(p);
			if(i < 0) return;
			model.setSolutionInfo(null);
			if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
				setSum(i, isUpperRight(p, i));
			} else if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0) {
				UndoManager.getInstance().addChange(new CellColorChange(getCrossNumberModel(), i));
//				getCrossNumberModel().flip(i);
				repaint();
			}
		}
		public void mouseDragged(MouseEvent e) {}
		
		TipCellDirectionInfo cd = new TipCellDirectionInfo();
		public void mouseMoved(MouseEvent e) {
			if(!cd.setPoint(e.getPoint())) return;
			String tipText = (cd.place < 0) ? null : getCrossNumberModel().getSolutionDistributionInfo(cd.place, cd.isHorisontal); 
			setToolTipText(tipText);
		}
	}

	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + getCrossNumberModel().getField().getWidth()) && p.y < cellSize * (1 + field.getHeight());
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - cellSize) / cellSize;
		int y = (p.y - cellSize) / cellSize;
		return x + y * field.getWidth();
	}
	
	boolean isUpperRight(Point p, int i) {
		int delta = cellSize * (field.x(i) - field.y(i));
		int d = p.x - p.y;
		return delta < d;
	}
	
	void setSum(int i, boolean preferHorisontal) {
		int status = field.getStatus(i);
		if(status == 4 || status == 0) return;
		preferHorisontal = (status == 1) ? true : (status == 2) ? false : preferHorisontal;
		String s = JOptionPane.showInputDialog(this, "Input ");
		if(s == null) return;
		int oldSum = (preferHorisontal) ? field.getHSum(i) : field.getVSum(i);
		try {
			if(preferHorisontal) {
				field.setHSum(i, s);
			} else {
				field.setVSum(i, s);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		int newSum = (preferHorisontal) ? field.getHSum(i) : field.getVSum(i);
		if(newSum != oldSum) {
			UndoManager.getInstance().addChange(new SetSumChange(getCrossNumberModel(), i, preferHorisontal, oldSum, newSum));
		}
		repaint();
	}
	
	class TipCellDirectionInfo {
		int place = -1;
		boolean isHorisontal = true;
		boolean setPoint(Point p) {
			int i = -1;
			boolean h = true;
			if(!model.isRunning() && model.isShowingSolution()) { 
				i = getCell(p);
				int status = (i < 0) ? 0 : field.getStatus(i);
				if(i < 0 || status == 0 || status == 4) {
					i = -1;
				} else {
					h = isUpperRight(p, i);
					h = (status == 1) ? true : (status == 2) ? false : h;
				}
			}
			boolean changed = (place != i) || (h != isHorisontal);
			if(changed) {
				place = i;
				isHorisontal = h;
			}
			return changed;
		}
	}

}
