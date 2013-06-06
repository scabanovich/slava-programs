package slava.puzzle.paths.nook.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.slava.common.RectangularField;

import slava.puzzle.paths.nook.model.NookPathsModel;
import slava.puzzle.paths.nook.model.NookPathsPuzzle;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;

public class NookPathsComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;

	protected RectangularField field;

	protected int cellSize = 30;
	protected int halfCellSize = (cellSize / 2);

	protected int margin = 20;
	protected int statusLineHeight = 20;
	protected Dimension componentSize;
	
	NookPathsPuzzle puzzle;
	NookPathsPuzzleInput puzzleInput = new NookPathsPuzzleInput();

	public NookPathsComponent() {
		ML mouse = new ML();
		addMouseListener(mouse);
		puzzleInput.setComponent(this);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getNookPathsModel().getField();
		puzzle = getNookPathsModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * cellSize + margin + margin + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getNookPathsModel());
	}
	
	public NookPathsModel getNookPathsModel() {
		return (NookPathsModel)model;
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
		int[] data = puzzle.getData();
		if(model.isShowingSolution()) {
			data = getNookPathsModel().getSelectedSolution();
			if(data == null) data = puzzle.getData();
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
			if(data[i] > 0) {
				g.setColor(Color.BLACK);
				int dx = data[i] < 10 ? 5 : 10;
				g.drawString("" + data[i], x + halfCellSize - dx, y + halfCellSize + 7);
			}
			if(!getNookPathsModel().isShowingSolution() && i == puzzleInput.getPosition() && puzzleInput.getState() == NookPathsPuzzleInput.STATE_A) {
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
