package slava.puzzles.seabattle.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import slava.puzzle.template.model.PuzzleModel;
import slava.puzzles.seabattle.model.*;

public class SeaBattleComponent extends AbstractSeaBattleComponent implements SeaBattleConstants {
	private static final long serialVersionUID = 1L;
	SeaBattlePuzzle puzzle;
	
	SeaBattlePuzzleInput puzzleInput = new SeaBattlePuzzleInput();
	
	
	public SeaBattleComponent() {
		ML mouse = new ML();
		addMouseListener(mouse);
		puzzleInput.setComponent(this);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getSeaBattleModel().getField();
		puzzle = getSeaBattleModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + margin + margin + valuesWidth + fleetWidth; 
		int h = field.getHeight() * cellSize + margin + margin + statusLineHeight + valuesWidth;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		puzzleInput.setModel(getSeaBattleModel());
	}
	
	public SeaBattleModel getSeaBattleModel() {
		return (SeaBattleModel)model;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintHValues(g);
		paintVValues(g);
		paintFleet(g);
		paintStatusLine(g);
		if(!hasFocus()) {
			requestFocus();
		}
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = margin, y = margin + valuesWidth;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}

	void paintCells(Graphics g) {
		int[] data = puzzle.getData();
		if(model.isShowingSolution()) {
			data = getSeaBattleModel().getSelectedSolution();
			if(data == null) data = puzzle.getData();
		}
		for (int i = 0; i < field.getSize(); i++) {
			int x = margin + cellSize * (field.getX(i));
			int y = margin + valuesWidth + cellSize * (field.getY(i));
			if(data[i] == EMPTY) {
				g.setColor(Color.WHITE);
				g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			} else if(data[i] == MIDDLE) {
				g.setColor(Color.BLACK);
				g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			} else if(data[i] == WATER) {
				g.setColor(Color.BLUE);
				g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
			} else if(data[i] == SINGLE) {
				g.setColor(Color.BLUE);
				g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
				g.setColor(Color.BLACK);
				g.fillOval(x + 1, y + 1, cellSize - 1, cellSize - 1);
			} else {
				g.setColor(Color.BLUE);
				g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
				g.setColor(Color.BLACK);
				g.fillOval(x + 1, y + 1, cellSize - 1, cellSize - 1);
				if(data[i] == EAST_END) {
					g.fillRect(x + 1, y + 1, halfCellSize, cellSize - 1);
				} else if(data[i] == WEST_END) {
					g.fillRect(x + halfCellSize, y + 1, halfCellSize, cellSize - 1);
				} else if(data[i] == SOUTH_END) {
					g.fillRect(x + 1, y + 1, cellSize - 1, halfCellSize);
				} else if(data[i] == NORTH_END) {
					g.fillRect(x + 1, y + halfCellSize, cellSize - 1, halfCellSize);
				}
			}
			
			if(!getSeaBattleModel().isShowingSolution() && i == puzzleInput.getPosition() && puzzleInput.getState() == SeaBattlePuzzleInput.STATE_B) {
				g.setColor(Color.YELLOW);
				g.drawOval(x + 3, y + 3, cellSize - 5, cellSize - 5);
			}
			
		}
	}
	
	void paintVValues(Graphics g) {
		for (int i = 0; i < field.getWidth(); i++) {
			int v = puzzle.getVValues()[i];
			int x = margin + cellSize * i;
			int y = margin;
			g.setColor(Color.WHITE);
			if(!getSeaBattleModel().isShowingSolution() && i == puzzleInput.getVPosition() && puzzleInput.getState() == SeaBattlePuzzleInput.STATE_V) {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(x + 3, y - 3, cellSize - 5, cellSize - 5);
			if(v >= 0) {
				g.setColor(Color.BLACK);
				g.drawString("" + v, x - 2 + halfCellSize, y - 1 + halfCellSize);
			}
		}
	}

	void paintHValues(Graphics g) {
		for (int i = 0; i < field.getHeight(); i++) {
			int v = puzzle.getHValues()[i];
			int x = componentSize.width - margin - valuesWidth - fleetWidth;
			int y = margin + valuesWidth + cellSize * i;
			g.setColor(Color.WHITE);
			if(!getSeaBattleModel().isShowingSolution() && i == puzzleInput.getHPosition() && puzzleInput.getState() == SeaBattlePuzzleInput.STATE_H) {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(x + 3, y + 3, cellSize - 5, cellSize - 5);
			if(v >= 0) {
				g.setColor(Color.BLACK);
				g.drawString("" + v, x - 2 + halfCellSize, y + 5 + halfCellSize);
			}
		}
	}
	
	void paintFleet(Graphics g) {
		int x1 = componentSize.width - fleetWidth - margin + 30;
		int x2 = x1 + 70;
		for (int i = 1; i < 6; i++) {
			int sc = getSeaBattleModel().getPuzzleInfo().getShipCount(i);
			int y = margin + valuesWidth + i * cellSize;
			g.setColor(Color.BLACK);
			g.fillOval(x1, y + halfCellSize - 7, 14 * i, 14);
			
			g.setColor(Color.WHITE);
			if(!getSeaBattleModel().isShowingSolution() && i == puzzleInput.getSPosition() && puzzleInput.getState() == SeaBattlePuzzleInput.STATE_S) {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(x2 + 3, y + 3, cellSize - 5, cellSize - 5);
			g.setColor(Color.BLACK);
			g.drawString("" + sc, x2 + halfCellSize - 3, y + halfCellSize + 5);
		}
	}

	class ML extends MouseAdapter {
		
		public void mouseReleased(MouseEvent event) {
			if(model.isShowingSolution()) return;
			Point p = event.getPoint();
			if(isInField(p)) {
				int pos = getCell(p);
				puzzleInput.setPosition(pos);
			} else if(isInHValues(p)) {
				int pos = getHCell(p);
				puzzleInput.setHPosition(pos);
			} else if(isInVValues(p)) {
				int pos = getVCell(p);
				puzzleInput.setVPosition(pos);
			} else if(isInSValues(p)) {
				int pos = getSCell(p);
				puzzleInput.setSPosition(pos);
			}
		}

	}

	boolean isInField(Point p) {
		return p.x > margin && 
			   p.y > margin + valuesWidth && 
			   p.x < componentSize.width - margin - valuesWidth - fleetWidth && 
			   p.y < componentSize.height - margin - statusLineHeight;
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - margin) / cellSize;
		int y = (p.y - margin - valuesWidth) / cellSize;
		return x + y * field.getWidth();
	}
	
	boolean isInHValues(Point p) {
		return p.x > componentSize.width - margin - valuesWidth - fleetWidth &&
			   p.x < componentSize.width - margin - fleetWidth &&
			   p.y > margin + valuesWidth &&
			   p.y < componentSize.height - margin - statusLineHeight;		
	}
	
	int getHCell(Point p) {
		if(!isInHValues(p)) return -1;
		return (p.y - margin - valuesWidth) / cellSize;
	}
	
	boolean isInVValues(Point p) {
		return p.y > margin &&
			   p.y < margin + valuesWidth &&
			   p.x > margin &&
			   p.x < componentSize.width - margin - valuesWidth - fleetWidth;
	}
	
	int getVCell(Point p) {
		if(!isInVValues(p)) return -1;
		return (p.x - margin) / cellSize;
	}
	
	boolean isInSValues(Point p) {
		return p.y > margin + valuesWidth + cellSize &&
			   p.y < margin + valuesWidth + cellSize * 6 &&
			   p.x > componentSize.width - margin - fleetWidth + 100 &&
			   p.x < componentSize.width - margin - fleetWidth + 100 + cellSize;
	}
	
	int getSCell(Point p) {
		if(!isInSValues(p)) return -1;
		return (p.y - margin - valuesWidth) / cellSize;
	}
	
}
