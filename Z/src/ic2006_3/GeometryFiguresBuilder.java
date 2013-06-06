package ic2006_3;

import java.util.*;
import com.slava.common.RectangularField;

public class GeometryFiguresBuilder {
	RectangularField field;
	int[] points;
	
	GeometryFigure[] figures;
	GeometryFigure[][] figuresForPoint;

	public GeometryFiguresBuilder() {}

	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setPoints(int[] ps) {
		points = ps;
	}
	
	public void build() {
		ArrayList list = new ArrayList();
		//triangles
		for (int p = 0; p < field.getSize(); p++) {
			if(points[p] == 0) continue;
			for (int a = 0; a < field.getSize(); a++) {
				if(a == p || points[a] == 0) continue;
				for (int b = a + 1; b < field.getSize(); b++) {
					if(b == p || points[b] == 0) continue;
					if(!isRightAngle(p, a, b)) continue;
					int area = computeArea(p, a, b);
					if(area % 2 == 1) continue; // no noninteger areas
					area = area / 2;
					if(area == 0) continue;
					list.add(new GeometryFigure(new int[]{p, a, b}, area));
				}
			}
		}
		//rectangles
		for (int p = 0; p < field.getSize(); p++) {
			if(points[p] == 0) continue;
			for (int a = p + 1; a < field.getSize(); a++) {
				if(points[a] == 0) continue;
				for (int b = a + 1; b < field.getSize(); b++) {
					if(points[b] == 0) continue;
					if(!isRightAngle(p, a, b)) continue;
					int q = getForthNode(p, a, b);
					if(q < 0 || points[q] == 0) continue;
					int area = computeArea(p, a, b);
					if(area == 0) continue;
					list.add(new GeometryFigure(new int[]{p, a, b, q}, area));
				}
			}
		}
		figures = (GeometryFigure[])list.toArray(new GeometryFigure[0]);
		
//		for (int i = 0; i < figures.length; i++) {
//			GeometryFigure f = figures[i];
//			System.out.println(f.toString());
//		}
		buildPointDistribution();
	}
	
	boolean isRightAngle(int p, int a, int b) {
		int ax = field.getX(a) - field.getX(p);
		int bx = field.getX(b) - field.getX(p);
		int ay = field.getY(a) - field.getY(p);
		int by = field.getY(b) - field.getY(p);
		int ab = ax * bx + ay * by;
		return ab == 0;
	}
	
	int computeArea(int p, int a, int b) {
		int ax = field.getX(a) - field.getX(p);
		int bx = field.getX(b) - field.getX(p);
		int ay = field.getY(a) - field.getY(p);
		int by = field.getY(b) - field.getY(p);
		int ab = ax * by - ay * bx;
		return Math.abs(ab);
	}
	
	int getForthNode(int p, int a, int b) {
		int ax = field.getX(a) - field.getX(p);
		int bx = field.getX(b) - field.getX(p);
		int ay = field.getY(a) - field.getY(p);
		int by = field.getY(b) - field.getY(p);
		int qx = field.getX(p) + ax + bx;
		int qy = field.getY(p) + ay + by;
		return field.getIndex(qx, qy);
	}
	
	void buildPointDistribution() {
		figuresForPoint = new GeometryFigure[field.getSize()][];
		for (int p = 0; p < field.getSize(); p++) {
			if(points[p] == 0) continue;
			ArrayList list = new ArrayList();
			for (int i = 0; i < figures.length; i++) {
				if(figures[i].contains(p)) list.add(figures[i]);
			}
			figuresForPoint[p] = (GeometryFigure[])list.toArray(new GeometryFigure[0]);
//			System.out.println("p=" + p + " figures=" + figuresForPoint[p].length);
		}		
	}

}
