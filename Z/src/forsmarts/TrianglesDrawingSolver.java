package forsmarts;

import java.util.Arrays;

import com.slava.common.RectangularField;

public class TrianglesDrawingSolver {
	RectangularField field;
	int[] points;
	TrianglesBuilder.Triangle[] ts;
	int limit;
	
	int[] usedSquares;
	int[] usedPoints;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] distribution;
	
	int solutionCount;	
	
	public TrianglesDrawingSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setPoints(int[] points) {
		this.points = points;
		setTriangles(TrianglesBuilder.createTriangles(field, points));
		int q = 0;
		for (int p = 0; p < points.length; p++) {
			if(points[p] == 1) q++;
		}
		limit = q / 3;
	}
	
	public void setTriangles(TrianglesBuilder.Triangle[] ts) {
		this.ts = ts;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		usedSquares = new int[limit + 1];
		usedPoints = new int[field.getSize()];
		wayCount = new int[limit + 1];
		way = new int[limit + 1];
		ways = new int[limit + 1][ts.length];
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == limit) return;
		if(!isOk()) return;
		makeDistribution();
		int p = getNarrowestPoint();
		if(p < 0 || distribution[p] == 0) return;
		getWaysForPlace(p);
	}
	
	void makeDistribution() {
		distribution = new int[field.getSize()];
		for (int i = 0; i < ts.length; i++) {
			if(canAdd(ts[i])) {
				for (int k = 0; k < 3; k++) {
					distribution[ts[i].points[k]]++;
				}
			}
		}
	}
	
	void getWaysForPlace(int p) {
		for (int i = 0; i < ts.length; i++) {
			if(!canAdd(ts[i]) || !contains(p, ts[i])) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	boolean contains(int p, TrianglesBuilder.Triangle t) {
		for (int i = 0; i < 3; i++) if(t.points[i] == p) return true;
		return false;
	}
	
	int getNarrowestPoint() {
		int pn = -1;
		int m = ts.length + 1;
		for (int p = 0; p < distribution.length; p++) {
			if(points[p] != 1 || usedPoints[p] == 1) continue;
			if(distribution[p] < m) {
				m = distribution[p];
				pn = p;
			}
		}
		return pn;
	}
	
	boolean canAdd(TrianglesBuilder.Triangle t) {
		if(t.doubledSquare >= usedSquares.length || usedSquares[t.doubledSquare] > 0) return false;
		for (int i = 0; i < 3; i++) if(usedPoints[t.points[i]] > 0) return false;
		return true;
	}
	
	void move() {
		int i = ways[t][way[t]];
		if(ts[i].doubledSquare < usedSquares.length) {
			usedSquares[ts[i].doubledSquare]++;
		}
		for (int k = 0; k < 3; k++) {
			usedPoints[ts[i].points[k]] = 1;
		}

		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		if(ts[i].doubledSquare < usedSquares.length) {
			usedSquares[ts[i].doubledSquare]--;
		}
		for (int k = 0; k < 3; k++) {
			usedPoints[ts[i].points[k]] = 0;
		}
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
			if(t == limit && isSolution()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	boolean isOk() {
		if(t < 2) return true;
		TrianglesBuilder.Triangle t2 = ts[ways[t - 1][way[t - 1]]];
		for (int i = 0; i < t - 1; i++) {
			TrianglesBuilder.Triangle t1 = ts[ways[i][way[i]]];
			if(TrianglesBuilder.intersect(field, t1, t2)) return false;
		}
		return true;		
	}
	
	boolean isSolution() {
		for (int i = 0; i < t; i++) {
			for (int j = i + 1; j < t; j++) {
				TrianglesBuilder.Triangle t1 = ts[ways[i][way[i]]];
				TrianglesBuilder.Triangle t2 = ts[ways[j][way[j]]];
				if(TrianglesBuilder.intersect(field, t1, t2)) return false;
			}
		}
		return true;
	}
	
	void printSolution() {
		System.out.println("Solution");
		StringBuffer key = new StringBuffer();
		for (int s = 1; s <= limit; s++) {
			for (int k = 0; k < t; k++) {
				int tri = ways[k][way[k]];
				TrianglesBuilder.Triangle tr = ts[tri];
				if(tr.doubledSquare != s) continue;
				for (int i = 0; i < 3; i++) {
					int p = tr.points[i];
					char xc = (char)(65 + field.getX(p));
					int y = field.getY(p) + 1;
					String ds = " " + xc + "" + y;
					System.out.print(ds);
					if(i == 0) key.append(ds);
				}
				System.out.println("");
			}
		}
		System.out.println("");
		System.out.println(key.toString());
	}

}
