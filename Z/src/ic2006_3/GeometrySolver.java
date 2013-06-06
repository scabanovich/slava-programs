package ic2006_3;

import com.slava.common.RectangularField;

public class GeometrySolver {
	RectangularField field;

	int[] points;
	GeometryFigure[][] figuresForPoint;
	
	int[] usedAreas;
	int usedAreasCount;
	int[] usedRectAreas;
	int usedRectAreasCount;
	int[] usedTriangularAreas;
	int usedTriangularAreasCount;
	int freePointCount;
	
	int[] state;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;
	
	public GeometrySolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setPoints(int[] ps) {
		points = ps;
	}
	
	public void setFigures(GeometryFigure[][] figuresForPoint) {
		this.figuresForPoint = figuresForPoint;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		usedAreas = new int[field.getSize()];
		usedAreasCount = 0;
		usedRectAreas = new int[field.getSize()];
		usedRectAreasCount = 0;
		usedTriangularAreas = new int[field.getSize()];
		usedTriangularAreasCount = 0;
		
		state = new int[field.getSize()];
		freePointCount = 0;
		for (int i = 0; i < points.length; i++) {
			if(points[i] > 0) freePointCount++;
		}
		
		place = new int[100];
		wayCount = new int[100];
		way = new int[100];
		ways = new int[100][300];
		temp = new int[300];

		t = 0;
		solutionCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(freePointCount == 0) return;
		int wcm = 1000;
		for (int p = 0; p < state.length; p++) {
			if(points[p] == 0 || state[p] > 0) continue;
			int wc = getWayCount(p);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(wcm < 1000) {
			wayCount[t] = wcm;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		GeometryFigure[] fs = figuresForPoint[p];
		for (int i = 0; i < fs.length; i++) {
			if(canAdd(fs[i])) {
				temp[wc] = i;
				wc++;
			}
		}
		return wc;
	}
	
	boolean canAdd(GeometryFigure f) {
		if(usedAreas[f.area] == 0 && usedAreasCount == 4) return false;
		if(f.points.length == 3) {
			if(usedTriangularAreas[f.area] > 0) return false;
			if(usedTriangularAreas[f.area] == 0 && usedTriangularAreasCount == 4) return false;
		} else {
			if(usedRectAreas[f.area] > 1) return false;
			if(usedRectAreas[f.area] == 0 && usedRectAreasCount == 4) return false;
		}
		for (int i = 0; i < f.points.length; i++) {
			if(state[f.points[i]] > 0) return false;
		}
		return true;
	}
	
	void move() {
		int p = place[t];
		int i = ways[t][way[t]];
		GeometryFigure f = figuresForPoint[p][i];
		add(f);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(GeometryFigure f) {
		usedAreas[f.area]++;
		if(usedAreas[f.area] == 1) usedAreasCount++;
		if(f.points.length == 3) {
			usedTriangularAreas[f.area]++;
			if(usedTriangularAreas[f.area] == 1) usedTriangularAreasCount++;
		} else {
			usedRectAreas[f.area]++;
			if(usedRectAreas[f.area] == 1) usedRectAreasCount++;
		}
		for (int i = 0; i < f.points.length; i++) {
			state[f.points[i]] = t + 1;
		}
		freePointCount -= f.points.length;
	}
	
	void back() {
		--t;
		int p = place[t];
		int i = ways[t][way[t]];
		GeometryFigure f = figuresForPoint[p][i];
		remove(f);
	}
	
	void remove(GeometryFigure f) {
		usedAreas[f.area]--;
		if(usedAreas[f.area] == 0) usedAreasCount--;
		if(f.points.length == 3) {
			usedTriangularAreas[f.area]--;
			if(usedTriangularAreas[f.area] == 0) usedTriangularAreasCount--;
		} else {
			usedRectAreas[f.area]--;
			if(usedRectAreas[f.area] == 0) usedRectAreasCount--;
		}
		for (int i = 0; i < f.points.length; i++) {
			state[f.points[i]] = 0;
		}
		freePointCount += f.points.length;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(freePointCount == 0) {
				solutionCount++;
				System.out.println("Solution");
				printSolution();
			}
		}
	}

	void printSolution() {
		for (int p = 0; p < field.getSize(); p++) {
			char c = '+';
			if(state[p] > 0) c = (char)(96 + state[p]);
			System.out.print(" " +c);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		System.out.println("key=" + getKey());
	}

	String getKey() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < t; i++) {
			int fi = ways[i][way[i]];
			int p = place[i];
			GeometryFigure f = figuresForPoint[p][fi];
			if(f.points.length != 3) continue;
			int q = f.points[0];
			char qx = (char)(97 + field.getX(q));
			int qy = field.getHeight() - field.getY(q);
			sb.append(qx).append(qy).append('-').append(f.area).append(',');
		}
		return sb.toString();
	}
}
