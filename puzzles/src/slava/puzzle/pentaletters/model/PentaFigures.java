package slava.puzzle.pentaletters.model;

import java.util.*;

public class PentaFigures {
	protected int figureSize;

	protected int[][][] forms;

	int[][][] figures;
	int[] figureIndex;

	protected int width;
	protected int height;
	protected int size;
	protected int[] field;

	protected void initTestField(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		field = new int[size];
	}

	void printForms() {
		for (int fi = 0; fi < forms.length; fi++) {
			printFigure(forms[fi], 0, 0);
		}
	}

	protected void printFigure(int[][] v, int dx, int dy) {
		for (int i = 0; i < size; i++) field[i] = 0;
		for (int pi = 0; pi < v.length; pi++) {
			int q = (v[pi][1] + dy) * width + (v[pi][0] + dx);
			field[q] = (pi < figureSize) ? 1 : 2;
		}
		System.out.println("");
		for (int i = 0; i < size; i++) {
			if(i % width == 0) System.out.println("");
			System.out.print(" " + field[i]);
		}
	}

	public PentaFigures() {
	  initForms();
	}
	  
	protected void initForms() {
		figureSize = 5;
		forms = new int[][][]{
			  {{1,1},{2,1},{3,1},{4,1},{5,1}}, //I
			  {{1,1},{2,1},{3,1},{4,1},{1,2}},
			  {{1,1},{2,1},{3,1},{1,2},{2,2}},
			  {{1,1},{2,1},{3,1},{4,1},{3,2}},
			  {{1,1},{2,1},{3,1},{1,2},{3,2}}, //U
			  {{1,1},{2,1},{3,1},{2,2},{2,3}},
			  {{2,1},{1,2},{2,2},{3,2},{2,3}},
			  {{1,1},{2,1},{3,1},{3,2},{4,2}},
			  {{1,1},{2,1},{3,1},{1,2},{1,3}}, //V
			  {{2,1},{1,2},{2,2},{3,2},{3,3}},
			  {{3,1},{1,2},{2,2},{3,2},{1,3}},
			  {{1,1},{2,1},{2,2},{3,2},{3,3}}
		};
	}

	void normalize(int[][] v) {
		int minY = 100;
		for (int i = 0; i < figureSize; i++) if(v[i][1] < minY) minY = v[i][1];
		for (int i = 0; i < v.length; i++) v[i][1] -= minY;
		int minX = 100;
		for (int i = 0; i < figureSize; i++) if(v[i][1] == 0 && v[i][0] < minX) minX = v[i][0];
		for (int i = 0; i < v.length; i++) v[i][0] -= minX;
	}

	public void makeFigures() {
		List figureList = new ArrayList();
		List numberList = new ArrayList();
		for (int fi = 0; fi < forms.length; fi++) {
			int[][][] figures = new int[8][][];
			for (int d = 0; d < 8; d++) {
				int[][] v = transform(forms[fi], d);
				normalize(v);
				figures[d] = v;
				if(isDistinct(figures, d)) {
					figureList.add(v);
					numberList.add(new Integer(fi));
				}
			}
		}
		figures = (int[][][])figureList.toArray(new int[0][][]);
		Integer[] is = (Integer[])numberList.toArray(new Integer[0]);
		figureIndex = new int[is.length];
		for (int i = 0; i < is.length; i++) figureIndex[i] = is[i].intValue();
	}

	int[][] transform(int[][] v, int d) {
		int[][] t = new int[v.length][2];
		for (int i = 0; i < v.length; i++) {
			int x = v[i][0], y = v[i][1], x1 = x, y1 = y;
			if(d > 3) {
				x1 = y;
				y1 = x;
			}
			if((d % 2) == 1) x1 = -x1;
			if(((d / 2) % 2) == 1) y1 = -y1;
			t[i][0] = x1;
			t[i][1] = y1;
		}
		return t;
	}

	private boolean isDistinct(int[][][] figures, int n) {
		for (int i = 0; i < n; i++) {
			if(areEqualFigures(figures[i], figures[n])) return false;
		}
		return true;
	}

	// wrong: points are not sorted
	private boolean areEqualFigures(int[][] f1, int[][] f2) {
		int[][] f = new int[10][10];
		for (int ix = 0; ix < 10; ix++) for (int iy = 0; iy < 10; iy++) f[ix][iy] = 0;
		for (int i = 0; i < f1.length; i++) {
			f[f1[i][0] + 5][f1[i][1]]++;
		}
		for (int i = 0; i < f2.length; i++) {
			f[f2[i][0] + 5][f2[i][1]]--;
		}
		for (int ix = 0; ix < 8; ix++) for (int iy = 0; iy < 8; iy++) {
			if(f[ix][iy] != 0) return false;
		} 
		return true;
	}
	
	public int[][][] getFigures() {
		return figures;
	}
	
	public int[] getFigureIndices() {
		return figureIndex;
	}
	
	public void printFigures() {
		initTestField(10, 7);
		for (int i = 0; i < figures.length; i++) {
			printFigure(figures[i], 4, 1);
		}
	}

	public static void main(String[] args) {
		PentaFigures f = new PentaFigures();
		f.makeFigures();
		f.printFigures();
	}

}
