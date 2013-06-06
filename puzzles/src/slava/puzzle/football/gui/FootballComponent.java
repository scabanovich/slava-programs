package slava.puzzle.football.gui;

import java.awt.*;
import slava.puzzle.football.model.FootballField;
import slava.puzzle.football.model.FootballModel;
import slava.puzzle.template.gui.PuzzleComponent;
import slava.puzzle.template.model.PuzzleModel;
import slava.ui.util.PaintUtil;

public class FootballComponent extends PuzzleComponent {
	protected FootballField field;
	protected int selectedNode = -1;
	protected int cellSize = 32;
	protected Dimension componentSize = null;
	protected Point[] nodeCorners;
	
	public FootballComponent() {
		
	}

	public FootballModel getFootballModel() {
		return (FootballModel)model;
	}
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getFootballModel().getField();
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
		paintStatusLine(g);
	}
	
	static Color LINECOLOR = new Color(216, 216, 216);

	void paintFieldBorder(Graphics g) {
		PaintUtil.paintFieldBorder(g, cellSize, field.getWidth(), field.getHeight(), LINECOLOR);
	}
	
	static Color[] NODE_STATE_COLORS = new Color[]{
		Color.BLACK,              // status is not available
		new Color(255, 255, 128), // connected to start only
		new Color(128, 255, 255), // connected to finish only
		new Color(0, 255, 0),     // connected to both
		Color.RED                 // connected to both with mistake
	};
	
	static Color[] NODE_STATE_BG_COLORS = new Color[]{
		Color.CYAN,               // status is not available
		Color.MAGENTA,            // connected to start only
		Color.ORANGE,             // connected to finish only
		Color.GREEN,              // connected to both
		Color.RED                 // connected to both with mistake
	};
	
	void paintNodes(Graphics g) {
		int dx = cellSize / 2 - 4;
		int dy = cellSize / 2 + 6;
		for (int i = 0; i < field.getSize(); i++) {
			int state = getFootballModel().getStateAt(i);
			int ix = nodeCorners[i].x;
			int iy = nodeCorners[i].y;
			Color c = (i == selectedNode) ? Color.BLUE : (state == 0) ? Color.CYAN : NODE_STATE_BG_COLORS[state];
			g.setColor(c);
			g.fillRect(ix + 1, iy + 1, cellSize - 2, cellSize - 2);
			g.setColor(Color.BLACK);
			String s = "" + field.getValueAt(i);
///			g.setColor(NODE_STATE_COLORS[state]);
			g.drawString(s, ix + dx, iy + dy);
		}
	}
	
	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

}
