package slava.puzzles.hexa.rook.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzles.hexa.common.HexaField;
import slava.puzzles.hexa.rook.model.HexaRookConstants;
import slava.puzzles.hexa.rook.model.HexaRookModel;

public class HexaRookComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;
	protected HexaField field;
	
	protected int cellSize = 40;
	protected int vCellSize = (int)(cellSize * Math.sqrt(3) / 2);
	protected int halfCellSize = (cellSize / 2);

	protected int margin = 20;
	protected int statusLineHeight = 20;
	protected Dimension componentSize;
	
	protected Point[] places;
	
	HexaRookPuzzleInput puzzleInput = new HexaRookPuzzleInput();

	public HexaRookComponent() {
		ML mouse = new ML();
		addMouseListener(mouse);
		puzzleInput.setComponent(this);
	}

	protected void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getHexaRookModel().getField();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * vCellSize + margin + margin + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getHexaRookModel());
		buildPlaces();
	}
	
	void buildPlaces() {
		places = new Point[field.getSize()];
		for (int p = 0; p < places.length; p++) {
			if(!field.isInField(p)) continue;
			int x = margin + halfCellSize + (2 * field.getX(p) + field.getSideLength() - 1 - field.getY(p)) * cellSize / 2;
			int y = margin + halfCellSize + vCellSize * field.getY(p);
			places[p] = new Point(x, y);
		}
	}
	
	public HexaRookModel getHexaRookModel() {
		return (HexaRookModel)model;
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintStatusLine(g);
		if(!hasFocus()) {
			requestFocus();
		}
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = margin, y = margin;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * vCellSize;
		g.drawRect(x, y, w, h);
	}

	void paintCells(Graphics g) {
		int[] data = getHexaRookModel().getPuzzleInfo().getPositions();
		if(model.isShowingSolution()) {
			data = getHexaRookModel().getSelectedSolution();
			if(data == null) data = getHexaRookModel().getPuzzleInfo().getPositions();
		}
		for (int p = 0; p < field.getSize(); p++) {
			if(places[p] == null) continue;
			boolean isSelection = !model.isShowingSolution() && p == puzzleInput.getFieldCursor();
			int x = places[p].x;
			int y = places[p].y;
			if(data[p] == HexaRookConstants.NOT_POSITION) {
				if(!isSelection) g.setColor(Color.BLACK); else g.setColor(Color.YELLOW.darker());
				g.fillOval(x - 6, y - 6, 12, 12);
			} else {
				if(!isSelection) g.setColor(Color.WHITE); else g.setColor(Color.YELLOW);
				g.fillOval(x - 12, y - 12, 24, 24);
			}
			if(data[p] > HexaRookConstants.SOME_POSITION) {
				g.setColor(Color.BLACK);
				int xd = data[p] >= 10 ? 8 : 3;
				g.drawString("" + data[p], x - xd, y + 5);
			}
		}
	}

	class ML extends MouseAdapter {
		
		public void mouseReleased(MouseEvent event) {
			if(model.isShowingSolution()) return;
			Point p = event.getPoint();
			if(isInField(p)) {
				int pos = getCell(p);
				if(pos >= 0) {
					puzzleInput.setFieldCursor(pos);
				}
			}			
		}
	}

	boolean isInField(Point p) {
		return p.x > margin && 
			   p.y > margin && 
			   p.x < componentSize.width - margin && 
			   p.y < componentSize.height - margin - statusLineHeight;
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		for (int i = 0; i < places.length; i++) {
			if(places[i] == null) continue;
			Point q = places[i];
			if(Math.abs(p.x - q.x) < 8 && Math.abs(p.y - q.y) < 8) {
				return i;
			}
		}
		return -1;
	}
	
}
