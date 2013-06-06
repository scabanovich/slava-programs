package forsmarts;

import java.util.ArrayList;

public class SumsInRightAnglesSolver {
	SumsInRightAnglesField field;
	int[] values;
	int[][] triangles;
	
	int[][] triangleDistribution;
	
	int[] state;
	int[] rightAngles;
	int trianglesToPut;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public SumsInRightAnglesSolver() {}

	public void setField(SumsInRightAnglesField f) {
		field = f;
	}
	
	public void setValues(int[] values) {
		this.values = values;
		setTriangles(field.makeTriangles(values));
	}
	
	public void setTriangles(int[][] triangles) {
		this.triangles = triangles;
//		System.out.println("Triangles Count=" + triangles.length);
//		for (int i = 0; i < triangles.length; i++) {
//			System.out.println("" + triangles[i][0] + " " + triangles[i][1] + " " + triangles[i][2]);
//		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		rightAngles = new int[state.length];
		createTriangleDistribution();
		trianglesToPut = 0;
		for (int i = 0; i < values.length; i++) {
			if(values[i] > 0) trianglesToPut++;
		}
		trianglesToPut = trianglesToPut / 3;
		place = new int[trianglesToPut + 1];
		wayCount = new int[trianglesToPut + 1];
		way = new int[trianglesToPut + 1];
		ways = new int[trianglesToPut + 1][100];
		t = 0;
	}
	
	void createTriangleDistribution() {
		triangleDistribution = new int[field.getSize()][];
		for (int i = 0; i < field.getSize(); i++) {
			ArrayList list = new ArrayList();
			for (int j = 0; j < triangles.length; j++) {
				if(triangles[j][0] == i || triangles[j][1] == i || triangles[j][2] == i) {
					list.add(new Integer(j));
				}
			}
			Integer[] is = (Integer[])list.toArray(new Integer[0]);
			triangleDistribution[i] = new int[is.length];
			for (int k = 0; k < is.length; k++) triangleDistribution[i][k] = is[k].intValue();
//			if(is.length != 0) System.out.println("i=" + i + " tr=" + is.length);
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(trianglesToPut == 0) return;
		int wcm = 100;
		for (int p = 0; p < state.length; p++) {
			if(state[p] != 0 || values[p] == 0) continue;
			int wc = computeWays(p);
			if(wc == 0) return;
			if(wc < wcm) {
				wcm = wc;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
			}
		}		
		if(wcm < 100) {
			wayCount[t] = wcm;
		}
	}
	
	int[] temp = new int[100];
	
	int computeWays(int p) {
		int wc = 0;
		for (int i = 0; i < triangleDistribution[p].length; i++) {
			int[] triangle = triangles[triangleDistribution[p][i]];
			if(canAdd(triangle)) {
				temp[wc] = triangleDistribution[p][i];
				++wc;
			}
		}			
		return wc;
	}
	
	boolean canAdd(int[] triangle) {
		for (int i = 0; i < triangle.length; i++) {
			if(state[triangle[i]] != 0) return false;
		}
		return true;		
	}
	
	void move() {
		int[] triangle = triangles[ways[t][way[t]]];
		add(triangle);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int[] triangle) {
		for (int i = 0; i < triangle.length; i++) {
			state[triangle[i]] = t + 1;
		}
		if(isRightTriangle(triangle)) {
			rightAngles[triangle[0]] = getSquare(triangle);
		}
		trianglesToPut--;
	}
	boolean isRightTriangle(int[] triangle) {
		return field.getX(triangle[0]) == field.getX(triangle[1]) ||
		field.getX(triangle[0]) == field.getX(triangle[2]) ||
		field.getX(triangle[1]) == field.getX(triangle[2]) ||
		field.getY(triangle[0]) == field.getY(triangle[1]) ||
		field.getY(triangle[0]) == field.getY(triangle[2]) ||
		field.getY(triangle[1]) == field.getY(triangle[2]);		
	}
	int getSquare(int[] triangle) {
		int dx1 = field.getX(triangle[1]) - field.getX(triangle[0]);
		int dy1 = field.getY(triangle[1]) - field.getY(triangle[0]);
		int dx2 = field.getX(triangle[2]) - field.getX(triangle[0]);
		int dy2 = field.getY(triangle[2]) - field.getY(triangle[0]);
		return Math.abs(dx1 * dy2 - dx2 * dy1);
	}
	
	void back() {
		--t;
		int[] triangle = triangles[ways[t][way[t]]];
		remove(triangle);
	}
	
	void remove(int[] triangle) {
		for (int i = 0; i < triangle.length; i++) {
			state[triangle[i]] = 0;
		}
		if(isRightTriangle(triangle)) {
			rightAngles[triangle[0]] = 0;
		}
		trianglesToPut++;
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
			if(trianglesToPut == 0) {
				solutionCount++;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] == 0) ? '.' : ((char)(96 + state[i]));
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < state.length; i++) {
			String c = (rightAngles[i] == 0) ? "." : "" + rightAngles[i];
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		StringBuffer sb = new StringBuffer();
		for (int s = 1; s < 100; s++) {
			int v = getValueForSquare(s);
			if(v > 0) {
				sb.append(',').append(v);
			}
		}
		System.out.println("Key=" + sb.toString());		
	}
	
	int getValueForSquare(int s) {
		for (int p = 0; p < state.length; p++) {
			if(rightAngles[p] == s) return values[p];
		}
		return -1;
	}

}
