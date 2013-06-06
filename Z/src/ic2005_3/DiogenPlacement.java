package ic2005_3;

import com.slava.common.RectangularField;

public class DiogenPlacement {
	int[] hAreas = new int[]{9,8,5,2,4,7,10,6};
	int[] vAreas = new int[]{8,9,3,9,2,8,8,4};
	RectangularField field;
	DiogenFigure[] figures;
	int figureCount;
	
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysP;
	
	public DiogenPlacement() {
		field = new RectangularField();
		field.setSize(9, 9);
		figures = new DiogenFigures().getFigures();
		figureCount = 6;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		wayCount = new int[figureCount + 1];
		way = new int[figureCount + 1];
		waysF = new int[figureCount + 1][200];
		waysP = new int[figureCount + 1][200];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == figureCount) return;
		for (int f = 0; f < figures.length; f++) {
			if(t != figures[f].index) continue;
			for (int p = 0; p < field.getSize(); p++) {
				if(canPut(f, p)) {
					waysF[t][wayCount[t]] = f;
					waysP[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean canPut(int f, int p) {
		int[][] points = figures[f].points;
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			if(q < 0 || state[q] > 0) return false;
		}
		int ix = field.getX(p);
		int[] fvAreas = figures[f].vAreas;
		for (int i = 0; i < fvAreas.length; i++) {
			if(fvAreas[i] > vAreas[ix + i]) return false;
		}
		int iy = field.getY(p);
		int[] fhAreas = figures[f].hAreas;
		for (int i = 0; i < fhAreas.length; i++) {
			if(fhAreas[i] > hAreas[iy + i]) return false;
		}
		return true;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		put(f, p);
		++t;
		srch();
		way[t] = -1;
	}
	
	void put(int f, int p) {
		int[][] points = figures[f].points;
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			state[q] = t + 1;
		}
		int ix = field.getX(p);
		int[] fvAreas = figures[f].vAreas;
		for (int i = 0; i < fvAreas.length; i++) {
			vAreas[ix + i] -= fvAreas[i];
		}
		int iy = field.getY(p);
		int[] fhAreas = figures[f].hAreas;
		for (int i = 0; i < fhAreas.length; i++) {
			hAreas[iy + i] -= fhAreas[i];
		}
	}
	
	void back() {
		--t;
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		remove(f, p);
	}
	
	void remove(int f, int p) {
		int[][] points = figures[f].points;
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			state[q] = 0;
		}
		int ix = field.getX(p);
		int[] fvAreas = figures[f].vAreas;
		for (int i = 0; i < fvAreas.length; i++) {
			vAreas[ix + i] += fvAreas[i];
		}
		int iy = field.getY(p);
		int[] fhAreas = figures[f].hAreas;
		for (int i = 0; i < fhAreas.length; i++) {
			hAreas[iy + i] += fhAreas[i];
		}
	}
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(t);
			}
			if(t == figureCount) {
				System.out.println("solution");
				printSolution();
			}
		}
	}
	
	char[] cs = new char[]{'+', 'd', 'i', 'o', 'g', 'e', 'n'};
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			int u = state[i];
			System.out.print(" " + cs[u]);
			if(field.isRightBorder(i)) System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		DiogenPlacement p = new DiogenPlacement();
		p.solve();
	}

}

/*
NNXIXEEX 
solution
 d d d d + e e e +
 d d d d + e e e +
 + d d i i e e e +
 + + + i i e e e +
 + + + i i + + o +
 n n n i i + o o o
 n n n + g g o o o
 n n n g g g g o +
 n n n g g g + + +
*/
