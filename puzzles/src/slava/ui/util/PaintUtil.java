package slava.ui.util;

import java.awt.*;

public class PaintUtil {

	public static void paintFieldBorder(Graphics g, int cellSize, int gridWidth, int gridHeight, Color linecolor) {
		g.setColor(Color.BLACK);
		int x = cellSize, y = cellSize;
		int w = gridWidth * cellSize;
		int h = gridHeight * cellSize;
		g.drawRect(x, y, w, h);
		g.setColor(Color.WHITE);
		g.fillRect(x + 1, y + 1, w - 2, h - 2);

		g.setColor(linecolor);
		for (int i = 1; i < gridWidth; i++) {
			int ix = cellSize * (i + 1);
			g.drawLine(ix, y + 1, ix, y + h - 2);			
		}
		for (int i = 1; i < gridHeight; i++) {
			int iy = cellSize * (i + 1);
			g.drawLine(x + 1, iy, x + w - 2, iy);			
		}
	}
	

}
