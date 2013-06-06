package slava.puzzle.sudoku.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import slava.puzzle.sudoku.model.*;
import slava.puzzle.template.gui.*;
import slava.puzzle.template.model.PuzzleModel;

public class SudokuComponent extends PuzzleComponent implements SudokuModelListener {
	private static final long serialVersionUID = 1L;
	
	SudokuDesignField field;
	SudokuProblemInfo problem;

	int selectedNode = -1;
	int cellSize = 32;
	Dimension componentSize = null;
	Point[] nodeCorners;

	ML commonMouseListener = new ML();
	PartitioningSumMouseListener partitioningSumMouseListener = new PartitioningSumMouseListener(this);

	public SudokuComponent() {
		addMouseListener(commonMouseListener);
		addMouseMotionListener(commonMouseListener);
		addKeyListener(new KL());
	}
	
	public SudokuModel getSudokuModel() {
		return (SudokuModel)model;
	}

	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getSudokuModel().getField();
		problem = getSudokuModel().getProblemInfo();
		fieldResized();
		getSudokuModel().addListener(this);
	}

	public void fieldResized() {
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
		paintBorders(g);
		paintDifferenceOneData(g);
		paintLessOrGreaterData(g);
		paintXVData(g);
		paintPartitioningSumData(g);
		paintStatusLine(g);		
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);
	static Color SOLUTIONCOLOR = new Color(255, 116, 116);
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
		g.setColor(Color.WHITE);
		g.fillRect(x + 1, y + 1, w - 2, h - 2);

		g.setColor(LINECOLOR);
		for (int i = 1; i < field.getWidth(); i++) {
			int ix = cellSize * (i + 1);
			g.drawLine(ix, y + 1, ix, y + h - 2);			
		}
		for (int i = 1; i < field.getHeight(); i++) {
			int iy = cellSize * (i + 1);
			g.drawLine(x + 1, iy, x + w - 2, iy);			
		}
	}
	
	void paintNodes(Graphics g) {
		int dx = cellSize / 2 - 4;
		int dy = cellSize / 2 + 6;
		int[] solution = problem.getSolution();
		int[] numbers = problem.getNumbers();
		for (int i = 0; i < field.getSize(); i++) {
			int ix = nodeCorners[i].x;
			int iy = nodeCorners[i].y;
			Color c = ((i == selectedNode) ? Color.CYAN.darker() : Color.CYAN);
			if(problem.isUsingPartitioningSumRestriction()) {
				if(problem.getPartitioning()[i] == partitioningSumMouseListener.partitionIndex) {
					c = ((i == selectedNode) ? Color.YELLOW.darker() : Color.YELLOW);
				}
			} else if (problem.getRegions()[i] == regionIndex) {
				c = ((i == selectedNode) ? Color.YELLOW.darker() : Color.YELLOW);
			}
			g.setColor(c);
			g.fillRect(ix + 1, iy + 1, cellSize - 2, cellSize - 2);
			g.setColor(Color.BLACK);
			int q = solution != null ? solution[i] : numbers[i];
			if(q > 0) {
				String s = "" + q;
				g.drawString(s, ix + dx, iy + dy);
			}
		}
	}
	
	void paintBorders(Graphics g) {
		g.setColor(Color.BLACK);
		int[] regions = problem.getRegions();
		for (int i = 0; i < field.getSize(); i++) {
			int i2 = field.jp(i, 0);
			if(i2 >= 0 && regions[i] != regions[i2]) {
				int x1 = nodeCorners[i2].x;
				int y1 = nodeCorners[i2].y;
				g.drawLine(x1, y1, x1, y1 + cellSize);
			}
			i2 = field.jp(i, 1);
			if(i2 >= 0 && regions[i] != regions[i2]) {
				int x1 = nodeCorners[i2].x;
				int y1 = nodeCorners[i2].y;
				g.drawLine(x1, y1, x1 + cellSize, y1);
			}
		}		
	}
	
	void paintDifferenceOneData(Graphics g) {
		if(!problem.isUsingDifferenceOneRestriction()) return;
		int[][] data = problem.getDifferenceOneData();
		for (int p = 0; p < field.getSize(); p++) {
			int q = field.jp(p, 0);
			if(data[p][0] == 1 && q >= 0) {
				int x1 = nodeCorners[q].x - 1;
				int y1 = nodeCorners[q].y + cellSize / 2 - 1;
				g.setColor(Color.BLACK);
				g.drawOval(x1, y1, 4, 4);
			}
			q = field.jp(p, 1);
			if(data[p][1] == 1 && q >= 0) {
				int x1 = nodeCorners[q].x + cellSize / 2 - 1;
				int y1 = nodeCorners[q].y - 1;
				g.setColor(Color.BLACK);
				g.drawOval(x1, y1, 4, 4);
			}
		}
	}
	
	void paintLessOrGreaterData(Graphics g) {
		if(!problem.isUsingLessOrGreaterRestriction()) return;
		g.setColor(Color.BLACK);
		int[][] data = problem.getLessOrGreaterData();
		for (int p = 0; p < field.getSize(); p++) {
			int q = field.jp(p, 0);
			if(data[p][0] != 0 && q >= 0) {
				int x1 = nodeCorners[q].x - 1;
				int y1 = nodeCorners[q].y + cellSize / 2 - 1;
				if(data[p][0] > 0) {
					g.drawPolyline(new int[]{x1 - 4, x1 + 4, x1 - 4}, new int[]{y1 - 4, y1, y1 + 4}, 3);
				} else {
					g.drawPolyline(new int[]{x1 + 4, x1 - 4, x1 + 4}, new int[]{y1 - 4, y1, y1 + 4}, 3);
				}
			}
			q = field.jp(p, 1);
			if(data[p][1] != 0 && q >= 0) {
				int x1 = nodeCorners[q].x + cellSize / 2 - 1;
				int y1 = nodeCorners[q].y - 1;
				if(data[p][1] > 0) {
					g.drawPolyline(new int[]{x1 - 4, x1, x1 + 4}, new int[]{y1 - 4, y1 + 4, y1 - 4}, 3);
				} else {
					g.drawPolyline(new int[]{x1 - 4, x1, x1 + 4}, new int[]{y1 + 4, y1 - 4, y1 + 4}, 3);
				}
			}
		}
	}

	void paintXVData(Graphics g) {
		if(!problem.isUsingXVRestriction()) return;
		g.setColor(Color.BLACK);
		int[][] data = problem.getXVData();
		for (int p = 0; p < field.getSize(); p++) {
			int q = field.jp(p, 0);
			if(data[p][0] > 0 && q >= 0) {
				int x1 = nodeCorners[q].x - 3;
				int y1 = nodeCorners[q].y + cellSize / 2 + 3;
				if(data[p][0] == 1) { //x
					g.drawString("V", x1, y1);
				} else {
					g.drawString("X", x1, y1);
				}
			}
			q = field.jp(p, 1);
			if(data[p][1] > 0 && q >= 0) {
				int x1 = nodeCorners[q].x + cellSize / 2 - 3;
				int y1 = nodeCorners[q].y + 3;
				if(data[p][1] == 1) {
					g.drawString("V", x1, y1);
				} else {
					g.drawString("X", x1, y1);
				}
			}
		}
	}

	void paintPartitioningSumData(Graphics g) {
		if(!problem.isUsingPartitioningSumRestriction()) return;
		int[] partitioning = problem.getPartitioning();
		int[] sums = problem.getSums();
		int[] sumUsage = new int[sums.length];
		for (int p = 0; p < field.getSize(); p++) {
			int r = partitioning[p];
			if(r < 0) continue;
			for (int d = 0; d < 4; d++) {
				int q = field.jp(p, d);
				if(q < 0 || q >= partitioning.length || partitioning[q] != r) {
					g.setColor(Color.BLUE);
					if(d == 0) {
						int x1 = nodeCorners[p].x + cellSize - 2;
						int y1 = nodeCorners[p].y;
						g.drawLine(x1, y1 + 2, x1, y1 + cellSize - 2);
					} else if(d == 1) {
						int x1 = nodeCorners[p].x;
						int y1 = nodeCorners[p].y + cellSize - 2;
						g.drawLine(x1 + 2, y1, x1 + cellSize - 2, y1);
					} else if(d == 2) {
						int x1 = nodeCorners[p].x + 2;
						int y1 = nodeCorners[p].y;
						g.drawLine(x1, y1 + 2, x1, y1 + cellSize - 2);
					} else if(d == 3) {
						int x1 = nodeCorners[p].x;
						int y1 = nodeCorners[p].y + 2;
						g.drawLine(x1 + 2, y1, x1 + cellSize - 2, y1);
					}
				}
				if(r < sums.length && sumUsage[r] == 0) {
					g.setColor(Color.BLACK);
					sumUsage[r] = 1;
					int x1 = nodeCorners[p].x + 4;
					int y1 = nodeCorners[p].y + 10;
					Font f = g.getFont();
					Font f1 = new Font(f.getFontName(), f.getStyle(), 9);
					g.setFont(f1);
					g.drawString("" + sums[r], x1, y1);
					g.setFont(f);
				}
			}
		}
	}

	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}
	
	static int RESTRICTION_OFF = 0;
	static int RESTRICTION_DIFFERENCE_ONE = 1;
	static int RESTRICTION_LESS_OR_GREATER = 2;
	static int RESTRICTION_XV = 3;
	static int RESTRICTION_SUMS = 4;
	
	boolean drawMode = false;
	int restrictionDrawMode = RESTRICTION_OFF;
	int regionIndex = -1;
	
	class ML extends MouseAdapter implements MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			removeSolutionMode();
			int i = getNodeIndex(p);
			if(i != selectedNode) {
				int value = -1;
				if(!drawMode && e.isAltDown() && selectedNode >= 0) {
					value = problem.getNumbers()[selectedNode];
				}
				selectedNode = i;
				if(!drawMode && restrictionDrawMode == RESTRICTION_OFF) {
					regionIndex = problem.getRegions()[selectedNode];
				}
				if(value >= 0) {
					problem.getNumbers()[selectedNode] = value;
				}
				repaint();
			}
			if(drawMode) {
				if(regionIndex < 0) {
					regionIndex = getNextIndex();
				}
				setRegion(i, regionIndex);
			}
		}

		public void mouseClicked(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(drawMode) {
				regionIndex = getNextIndex();
				setRegion(i, regionIndex);
			}
			if(restrictionDrawMode == RESTRICTION_DIFFERENCE_ONE) {
				int d = getDirectionIndex(p);
				if(d >= 0) flipDifferenceOne(i, d);
			}
			if(restrictionDrawMode == RESTRICTION_LESS_OR_GREATER) {
				int d = getDirectionIndex(p);
				if(d >= 0) flipLessOrGreater(i, d);
			}
			if(restrictionDrawMode == RESTRICTION_XV) {
				int d = getDirectionIndex(p);
				if(d >= 0) flipXV(i, d);
			}
		}

		public void mouseDragged(MouseEvent e) {
			if(model.isRunning()) return;
			Point p = e.getPoint();
			if(!isInField(p)) return;
			requestFocus();
			int i = getNodeIndex(p);
			if(drawMode) {
				if(regionIndex < 0) {
					regionIndex = getNextIndex();
				}
				setRegion(i, regionIndex);
			}
		}

		public void mouseMoved(MouseEvent e) {
		}
	}

	class KL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if(model.isRunning()) return;
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				if(!drawMode 
						&& !problem.isUsingDifferenceOneRestriction() 
						&& !problem.isUsingLessOrGreaterRestriction() 
						&& !problem.isUsingXVRestriction()
						&& !problem.isUsingPartitioningSumRestriction()
				) {
					drawMode = true;
				} else if(restrictionDrawMode == RESTRICTION_OFF) {
					if(problem.isUsingDifferenceOneRestriction()) {
						restrictionDrawMode = RESTRICTION_DIFFERENCE_ONE;
					} else if(problem.isUsingLessOrGreaterRestriction()) {
						restrictionDrawMode = RESTRICTION_LESS_OR_GREATER;
					} else if(problem.isUsingXVRestriction()) {
						restrictionDrawMode = RESTRICTION_XV;
					} else if(problem.isUsingPartitioningSumRestriction()) {
						restrictionDrawMode = RESTRICTION_SUMS;
						removeMouseListener(commonMouseListener);
						removeMouseMotionListener(commonMouseListener);
						addMouseListener(partitioningSumMouseListener);
						addMouseMotionListener(partitioningSumMouseListener);
					}
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			if(model.isRunning()) return;
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				drawMode = false;
				restrictionDrawMode = RESTRICTION_OFF;
				if(problem.isUsingPartitioningSumRestriction()) {
					removeMouseListener(partitioningSumMouseListener);
					removeMouseMotionListener(partitioningSumMouseListener);
					addMouseListener(commonMouseListener);
					addMouseMotionListener(commonMouseListener);
				}
				return;
			}
			if(e.getKeyChar() == ' ' && model.isShowingSolution()) {
//				getSudokuModel().nextSolution();
				repaint();
			}
			if(selectedNode >= 0) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					jumpToNeighbourNode(2);
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					jumpToNeighbourNode(0);
				} else if(e.getKeyCode() == KeyEvent.VK_UP) {
					jumpToNeighbourNode(3);
				} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					jumpToNeighbourNode(1);
				} else if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
					removeSolutionMode();
					int n = (int)e.getKeyChar() - (int)'0';
					if(n >= 0 && n <= field.getWidth()) {
						setNumber(n);
					}
				} else if(e.getKeyChar() >= 'a' && e.getKeyChar() <= 'f') {
					removeSolutionMode();
					int n = (int)e.getKeyChar() - ((int)'a') + 10;
					if(n >= 0 && n <= field.getWidth()) {
						setNumber(n);
					}
				}
			}
		}
	}
	
	boolean isInField(Point p) {
		return p.x > cellSize && p.y > cellSize && p.x < cellSize * (1 + field.getWidth())&& p.y < cellSize * (1 + field.getHeight());
	}
	
	int getNodeIndex(Point p) {
		int ix = (p.x - cellSize) / cellSize;
		int iy = (p.y - cellSize) / cellSize;
		return iy * field.getWidth() + ix;
	}
	
	int getDirectionIndex(Point p) {
		int dx = p.x - ((p.x - cellSize) / cellSize) * cellSize - 3 * cellSize / 2;
		int dy = p.y - ((p.y - cellSize) / cellSize) * cellSize - 3 * cellSize / 2;
		if(Math.abs(dy) > Math.abs(dx) + 2) {
			return (dy > 0) ? 1 : 3;
		} else if(Math.abs(dx) > Math.abs(dy) + 2) {
			return (dx > 0) ? 0 : 2;
		}
		return -1;
	}
	
	void jumpToNeighbourNode(int d) {
		int n = field.jp(selectedNode, d);
		if(n < 0) return;
		selectedNode = n;
		repaint();
	}
	
	void setNumber(int c) {
		problem.getNumbers()[selectedNode] = c;
		repaint();
	}
	
	int getNextIndex() {
		return problem.getNextFreeIndex();
	}
	
	void setRegion(int p, int c) {
		if(problem.getRegions()[p] == c) return;
		removeSolutionMode();
		problem.getRegions()[p] = c;
		repaint();
	}
	
	void flipDifferenceOne(int p, int d) {
		int q = field.jp(p, d);
		if(q < 0) return;
		int[][] data = problem.getDifferenceOneData();
		int v = 1 - data[p][d];
		int d1 = (d > 1) ? d - 2 : d + 2;
		data[p][d] = v;
		data[q][d1] = v;
		repaint();
	}
	
	void flipLessOrGreater(int p, int d) {
		int q = field.jp(p, d);
		if(q < 0) return;
		int[][] data = problem.getLessOrGreaterData();
		int v = 1 + data[p][d];
		if(v > 1) v = -1;
		int d1 = (d > 1) ? d - 2 : d + 2;
		data[p][d] = v;
		data[q][d1] = -v;
		repaint();
	}

	void flipXV(int p, int d) {
		int q = field.jp(p, d);
		if(q < 0) return;
		int[][] data = problem.getXVData();
		int v = 1 + data[p][d];
		if(v > 1) v = -1;
		int d1 = (d > 1) ? d - 2 : d + 2;
		data[p][d] = v;
		data[q][d1] = -v;
		repaint();
	}

	void removeSolutionMode() {
		if(model.isShowingSolution()) {
			model.setSolutionInfo(null);
			getSudokuModel().getProblemInfo().setSolution(null);
			repaint();
		}
	}

}
