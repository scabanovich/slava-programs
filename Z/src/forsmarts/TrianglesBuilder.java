package forsmarts;

import java.util.ArrayList;

import com.slava.common.RectangularField;

public class TrianglesBuilder {
	public static class Triangle {
		public int[] points;
		public int doubledSquare;
	}
	
	public static Triangle[] createTriangles(RectangularField f, int[] points) {
		ArrayList list = new ArrayList();
		for (int p1 = 0; p1 < points.length; p1++) {
			if(points[p1] != 1) continue;
			for (int p2 = p1 + 1; p2 < points.length; p2++) {
				if(points[p2] != 1) continue;
				for (int p3 = p2 + 1; p3 < points.length; p3++) {
					if(points[p3] != 1) continue;
					int[] t = new int[]{p1, p2, p3};
					int s = computeDoubledSquare(f, t);
					if(s == 0) continue;
					if(!check(f, points, t)) continue;
					Triangle tr = new Triangle();
					tr.points = t;
					tr.doubledSquare = s;
					list.add(tr);
				}
			}
		}
		
		return (Triangle[])list.toArray(new Triangle[0]);
	}
	
	static int computeDoubledSquare(RectangularField f, int[] points) {
		int dx1 = f.getX(points[1]) - f.getX(points[0]);
		int dy1 = f.getY(points[1]) - f.getY(points[0]);
		int dx2 = f.getX(points[2]) - f.getX(points[0]);
		int dy2 = f.getY(points[2]) - f.getY(points[0]);
		return Math.abs(dx1 * dy2 - dx2 * dy1);
	}
	
	static boolean isInside(RectangularField f, int[] points, int point) {
		int x1 = f.getX(points[1]) - f.getX(points[0]);
		int y1 = f.getY(points[1]) - f.getY(points[0]);
		int x2 = f.getX(points[2]) - f.getX(points[0]);
		int y2 = f.getY(points[2]) - f.getY(points[0]);

		int x = f.getX(point) - f.getX(points[0]);
		int y = f.getY(point) - f.getY(points[0]);
		
		int a = x * y2 - y * x2;
		int b = y * x1 - x * y1;
		int c = x1 * y2 - y1 * x2;
		
		if(c == 0) return false;
		if(c < 0) {
			c = -c;
			a = -a;
			b = -b;
		}
		if(a < 0 || a > c) return false;
		if(b < 0 || b > c) return false;
		if (a + b > c) return false;
		return true;
	}
	
	static private boolean check(RectangularField f, int[] points, int[] t) {
		for (int p = 0; p < f.getSize(); p++) {
			if(points[p] != 1) continue;
			if(p == t[0] || p == t[1] || p == t[2]) continue;
			if(isInside(f, t, p)) return false;
		}
		return true;
	}
	
	static boolean intersect(RectangularField f, int p1, int p2, int p3, int p4) {
		int x21 = f.getX(p2) - f.getX(p1);
		int x31 = f.getX(p3) - f.getX(p1);
		int x43 = f.getX(p4) - f.getX(p3);
		int y21 = f.getY(p2) - f.getY(p1);
		int y31 = f.getY(p3) - f.getY(p1);
		int y43 = f.getY(p4) - f.getY(p3);
		int C = x21 * y43 - y21 * x43;
		int A = x31 * y43 - y31 * x43;
		int B = x31 * y21 - y31 * x21;
		if(C < 0) {
			C = -C;
			A = -A;
			B = -B;
		}
		if(C != 0) {
			return B >= 0 && B <= C && A >= 0 && A <= C;
		} else {
			if(A != 0 || B != 0) return false;
			if(x21 != 0) {
				
			} else if(y21 != 0) {
				
			}
		}
		return false;
	}
	
	public static boolean intersect(RectangularField f, Triangle t1, Triangle t2) {
		for (int i1 = 0; i1 < 2; i1++) {
			for (int i2 = i1 + 1; i2 < 3; i2++) {
				for (int i3 = 0; i3 < 2; i3++) {
					for (int i4 = i3 + 1; i4 < 3; i4++) {
						int p1 = t1.points[i1];
						int p2 = t1.points[i2];
						int p3 = t2.points[i3];
						int p4 = t2.points[i4];
						if(intersect(f, p1, p2, p3, p4)) return true;
					}
				}
			}
		}
		return false;
	}
	

}
