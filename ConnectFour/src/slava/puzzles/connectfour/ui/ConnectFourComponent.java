package slava.puzzles.connectfour.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.slava.common.RectangularField;

import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzles.connectfour.model.ConnectFourModel;
import slava.puzzles.connectfour.solve.ConnectFourSmartState;
import slava.puzzles.connectfour.solve.ConnectFourSolver;
import slava.puzzles.connectfour.solve.ConnectFourState;

public class ConnectFourComponent extends PuzzleComponent {
	private static final long serialVersionUID = 1L;

	static Color FIRST_COLOR = Color.YELLOW.darker();
	static Color SECOND_COLOR = Color.RED.brighter();
	RectangularField field;

	protected int cellSize = 40;
	protected int vCellSize = cellSize;
	protected int halfCellSize = (cellSize / 2);

	protected int margin = 30;
	protected int statusLineHeight = 30;
	protected Dimension componentSize;

	protected Point[] places;
	ConnectFourInput input = new ConnectFourInput();

	public ConnectFourComponent() {
		ML mouse = new ML();
		addMouseListener(mouse);
		input.setComponent(this);
	}

	public ConnectFourModel getConnectFourModel() {
		return (ConnectFourModel)model;
	}

	protected void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getConnectFourModel().getField();
		int w = field.getWidth() * cellSize + margin + margin; 
		int h = field.getHeight() * vCellSize + margin + margin + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		input.setModel(getConnectFourModel());
		buildPlaces();
	}

	void buildPlaces() {
		places = new Point[field.getSize()];
		for (int p = 0; p < places.length; p++) {
			int x = margin + halfCellSize + field.getX(p) * cellSize;
			int y = margin + halfCellSize + vCellSize * (field.getHeight() - 1 - field.getY(p));
			places[p] = new Point(x, y);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintStatusLine(g);
		paintCompletedLines(g);
		paintSmartState(g);
		paintTurn(g);
		paintAnalysisResults(g);
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
		
		g.setColor(Color.GRAY.darker().brighter());
		for (int ix = 1; ix < field.getWidth(); ix++) {
			int x1 = x + ix * cellSize;
			g.drawLine(x1, y + 1, x1, y + h - 1);
		}
		for (int iy = 1; iy < field.getHeight(); iy++) {
			int y1 = y + iy * vCellSize;
			g.drawLine(x + 1, y1, x + w - 1, y1);
		}
	}

	void paintCells(Graphics g) {
		ConnectFourState state = getConnectFourModel().getState();
		for (int p = 0; p < field.getSize(); p++) {
			if(places[p] == null) continue;
			int x = places[p].x;
			int y = places[p].y;
			if(state.getValue(p) == ConnectFourState.EMPTY) {
				g.setColor(Color.WHITE);
			} else if(state.getValue(p) == ConnectFourState.FIRST) {
				g.setColor(FIRST_COLOR);
				g.fillOval(x - 12, y - 12, 24, 24);
			} else if(state.getValue(p) == ConnectFourState.SECOND) {
				g.setColor(SECOND_COLOR);
				g.fillOval(x - 12, y - 12, 24, 24);
			}
		}
	}

	void paintSmartState(Graphics g) {
		if(getConnectFourModel().isCompleted()) {
			return;
		}
		ConnectFourSmartState smartState = getConnectFourModel().getState().getSmartState();
		for (int p = 0; p < field.getSize(); p++) {
			int value = smartState.getSmartValue(p);
			if(places[p] == null) continue;
			int x = places[p].x;
			int y = places[p].y;
			if(value == ConnectFourSmartState.LAST_FOR_BOTH) {
				g.setColor(Color.GREEN);
				g.drawOval(x - 12, y - 12, 12, 12);
			} else if(value == ConnectFourSmartState.LAST_FOR_FIRST) {
				g.setColor(Color.YELLOW.darker());
				g.drawOval(x - 12, y - 12, 12, 12);
			} else if(value == ConnectFourSmartState.LAST_FOR_SECOND) {
				g.setColor(Color.RED.darker());
				g.drawOval(x - 12, y - 12, 12, 12);
			} else if(value == ConnectFourSmartState.EMPTY_UNREACHEABLE) {
				g.setColor(Color.BLACK);
				g.drawOval(x - 12, y - 12, 12, 12);
			} else if(value == ConnectFourSmartState.FILLED_IRRELEVANT) {
				g.setColor(Color.BLACK);
				g.drawOval(x - 12, y - 12, 12, 12);
			}
		}
	}

	void paintTurn(Graphics g) {
		int x = componentSize.width - margin;
		int y = componentSize.height - statusLineHeight;
		if(!getConnectFourModel().isCompleted()) {
			if(getConnectFourModel().getState().getFilled() % 2 == 0) {
				g.setColor(FIRST_COLOR);
			} else {
				g.setColor(SECOND_COLOR);
			}
			g.fillOval(x - 12, y - 12, 24, 24);
		}
	}


	void paintAnalysisResults(Graphics g) {
		byte[] results = getConnectFourModel().getAnalysisResults();
		if(results == null) {
			results = getConnectFourModel().getState().getSmartState().getResults();
		}
		paintAnalysisResults(g, results);
	}
	void paintAnalysisResults(Graphics g, byte[] results) {
		if(results != null) {
			RectangularField f = getConnectFourModel().getField();
			ConnectFourState state = getConnectFourModel().getState();
			if(state.getFilled() % 2 == 0) {
				g.setColor(FIRST_COLOR);
			} else {
				g.setColor(SECOND_COLOR);
			}
			for (int ix = 0; ix < results.length; ix++) {
				if(results[ix] > ConnectFourSolver.UNKNOWN) {
					int p = f.getIndex(ix, 0);
					if(p >= 0) {
						int x = places[p].x - 4;
						int y = places[p].y + cellSize;
						int turn = state.getTurn();
						int next = ConnectFourState.NEXT_TURN[turn];
						if(results[ix] == turn) {
							g.drawString("W", x, y);
						} else if(results[ix] == next) {
							g.drawString("L", x, y);
						} else if(results[ix] == ConnectFourSolver.DRAW) {
							g.drawString("D", x, y);
						} else if(results[ix] == ConnectFourSolver.FORCED) {
							g.drawString("F", x, y);
						} else if(results[ix] == ConnectFourSolver.DRAW_OR_FIRST + turn) {
							g.drawString("D+", x, y);
						} else if(results[ix] == ConnectFourSolver.DRAW_OR_FIRST + next) {
							g.drawString("D-", x, y);
						}
					}
				}
			}
		}
	}

	void paintCompletedLines(Graphics g) {
		if(getConnectFourModel().isCompleted()) {
			ConnectFourState state = getConnectFourModel().getState();
			for (int p = 0; p < state.getField().getSize(); p++) {
				int c = state.getValue(p);
				if(c == ConnectFourState.EMPTY) continue;
				for (int d = 0; d < 4; d++) {
					int q = state.getField().jump(p, d + 4);
					if(q >= 0 && state.getValue(q) == c) continue;
					int m = 1;
					q = p;
					while(true) {
						int q1 = state.getField().jump(q, d);
						if(q1 < 0 || state.getValue(q1) != c) {
							break;
						}
						q = q1;
						m++;
					}
					if(m >= 4) {
						if(c == ConnectFourState.FIRST) {
							g.setColor(FIRST_COLOR);
						} else {
							g.setColor(SECOND_COLOR);
						}
						g.drawLine(places[p].x, places[p].y, places[q].x, places[q].y);
					}
				}
			}
		}
	}

	class ML extends MouseAdapter {
		
		public void mouseReleased(MouseEvent event) {
			if(model.isShowingSolution() || model.isRunning()) return;
			Point p = event.getPoint();
			if(isInField(p)) {
				int pos = getCell(p);
				if(pos >= 0) {
					int x = field.getX(pos);
					if(getConnectFourModel().canMove(x)) {
						getConnectFourModel().move(x);
						repaint();
					}
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
			if(Math.abs(p.x - q.x) < cellSize / 2 && Math.abs(p.y - q.y) < vCellSize / 2) {
				return i;
			}
		}
		return -1;
	}
	
}
