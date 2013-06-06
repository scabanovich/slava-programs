package slava.puzzle.paths.walker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.paths.walker.model.WalkerPathsPuzzle;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import com.slava.common.RectangularField;

public class WalkerPathsComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;

	protected RectangularField field;

	protected int cellSize = 30;
	protected int halfCellSize = (cellSize / 2);

	protected int margin = 20;
	protected int statusLineHeight = 20;
	protected Dimension componentSize;
		
	protected WalkerPathsPuzzle puzzle;
	protected WalkerPathsPuzzleInput puzzleInput = new WalkerPathsPuzzleInput();

	public WalkerPathsComponent() {
		ML mouse = new ML();
		addMouseListener(mouse);
		puzzleInput.setComponent(this);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getWalkerPathsModel().getField();
		puzzle = getWalkerPathsModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * cellSize + margin + margin + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getWalkerPathsModel());
	}
	
	public WalkerPathsModel getWalkerPathsModel() {
		return (WalkerPathsModel)model;
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
	
	protected void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = margin, y = margin;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}

	void paintCells(Graphics g) {
		int[][] parts = puzzle.getParts();
		int[] turns = puzzle.getTurns();
		if(model.isShowingSolution()) {
			parts = getWalkerPathsModel().getSelectedSolution();
			if(parts == null) parts = puzzle.getParts();
		}

		for (int i = 0; i < field.getSize(); i++) {
			int x = margin + cellSize * (field.getX(i));
			int y = margin + cellSize * (field.getY(i));
			if(puzzle.getFilter()[i] == 1) {
				g.setColor(Color.BLACK);
			} else if((field.getX(i) + field.getY(i)) % 2 == 0) {
				g.setColor(Color.GRAY.brighter());
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			//TODO
			for (int d = 0; d < 4; d++) {
				if(parts[i][d] < 0) continue;
				g.setColor(Color.BLACK);
				if(parts[i][d] == 0) {
					if(d == 0) {
						g.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
					} else if(d == 1) {
						g.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
					}
				} else {
					int cx = x + halfCellSize;
					int cy = y + halfCellSize;
					int dx = (d == 0) ? halfCellSize : (d == 2) ? -halfCellSize : 0;
					int dy = (d == 1) ? halfCellSize : (d == 3) ? -halfCellSize : 0;
					g.drawLine(cx, cy, cx + dx, cy + dy);					
				}
			}
			
			if(turns[i] != 0 && puzzle.getFilter()[i] == 0) {
				g.setColor(Color.BLACK);
				g.drawOval(x + 9, y + 9, cellSize - 17, cellSize - 17);
				g.setColor(turns[i] == 1 ? Color.WHITE : Color.BLACK);
				g.fillOval(x + 10, y + 10, cellSize - 18, cellSize - 18);
			}
			
			if(!getWalkerPathsModel().isShowingSolution() && i == puzzleInput.getPosition() && puzzleInput.getState() == WalkerPathsPuzzleInput.STATE_A) {
				g.setColor(Color.YELLOW);
				g.drawOval(x + 3, y + 3, cellSize - 5, cellSize - 5);
			}
		}

	}

	class ML extends MouseAdapter {
		
		public void mouseReleased(MouseEvent event) {
			if(model.isShowingSolution()) return;
			Point p = event.getPoint();
			if(isInField(p)) {
				int pos = getCell(p);
				puzzleInput.setPosition(pos);
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
		int x = (p.x - margin) / cellSize;
		int y = (p.y - margin) / cellSize;
		return x + y * field.getWidth();
	}
	
}
