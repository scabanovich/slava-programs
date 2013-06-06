package champukr;

import java.awt.*;
import javax.swing.*;

public class PathComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	int X_SIZE = 250;
	int Y_SIZE = 250;
	champukr.Point[] points;
	
	public PathComponent() {
		setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
		setBackground(Color.WHITE);
	}
	
	public void setPoints(champukr.Point[] points) {
		this.points = points;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(points == null) return;
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.length; i++) {
			int ix = (int)(X_SIZE * points[i].x);
			int iy = (int)(Y_SIZE * points[i].y);
			g.drawOval(ix, iy, 2, 2);
		}
		for (int i = 0; i < points.length; i++) {
			int ix1 = (int)(X_SIZE * points[i].x);
			int iy1 = (int)(Y_SIZE * points[i].y);
			int j = i + 1;
			if(j == points.length) j = 0;
			int ix2 = (int)(X_SIZE * points[j].x);
			int iy2 = (int)(Y_SIZE * points[j].y);
			g.drawLine(ix1, iy1, ix2, iy2);
		}
	}
	
	static void show(champukr.Point[] points) {
		JFrame f = new JFrame();
		PathComponent c = new PathComponent();
		c.setPoints(points);
		f.setContentPane(c);
		f.pack();
		f.setVisible(true);
	}

}
