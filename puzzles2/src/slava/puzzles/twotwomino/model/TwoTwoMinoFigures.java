package slava.puzzles.twotwomino.model;

import java.util.ArrayList;
import slava.puzzle.pentaletters.model.PentaFigures;

public class TwoTwoMinoFigures extends PentaFigures {
	int[] states = new int[]{3,5,10,12};
	int[][][] extendedFigures; //(x,y,state)
	ExtendedFigureTestField extendedFigureTestField = new ExtendedFigureTestField();
	
	public TwoTwoMinoFigures() {
		extendedFigureTestField.setSize(12, 12);
	}
	
	public int[][][] getExtendedFigures() {
		return extendedFigures;
	}

	protected void initForms() {
		figureSize = 4;
		forms = new int[][][]{
			  {{1,1},{2,1},{3,1},{4,1}}, //I
			  {{1,1},{2,1},{3,1},{1,2}},
			  {{1,1},{2,1},{3,1},{2,2}},
			  {{1,1},{2,1},{2,2},{3,2}},
			  {{1,1},{2,1},{1,2},{2,2}}
		};
	}
	
	public void makeExtendedFigures() {
		ArrayList list = new ArrayList();
		int[][][] figures = getFigures();
		for (int i = 0; i < figures.length; i++) {
			int[][] f = figures[i];
			for (int p1 = 0; p1 < f.length; p1++) {
				for (int s1i = 0; s1i < states.length; s1i++) {
					int s1 = states[s1i];
					for (int p2 = p1 + 1; p2 < f.length; p2++) {
						for (int s2i = 0; s2i < states.length; s2i++) {
							int s2 = states[s2i];
							int[][] extF = makeExtendedFigure(f, p1, s1, p2, s2);
							if(checkExtendedFigure(extF)) list.add(extF);
						}
					}
				}
			}
		}
		extendedFigures = (int[][][])list.toArray(new int[0][][]);
	}
	
	private int[][] makeExtendedFigure(int[][] f, int p1, int s1, int p2, int s2) {
		int[][] extF = new int[f.length][3];
		for (int i = 0; i < f.length; i++) {
			extF[i][0] = f[i][0];
			extF[i][1] = f[i][1];
			extF[i][2] = (i == p1) ? s1 : (i == p2) ? s2 : 15;
		}
		return extF;
	}
	
	private boolean checkExtendedFigure(int[][] f) {
		return extendedFigureTestField.checkExtendedFigure(f);
	}

	public void printFigures() {
		initTestField(7, 4);
		for (int i = 0; i < extendedFigures.length; i++) {
			printFigure(extendedFigures[i], 3, 0);
		}
	}

	protected void printFigure(int[][] v, int dx, int dy) {
		char[] cf = new char[size];
		for (int i = 0; i < size; i++) cf[i] = '.';
		for (int pi = 0; pi < v.length; pi++) {
			int q = (v[pi][1] + dy) * width + (v[pi][0] + dx);
			char ch = (v[pi][2] == 3) ? 'u' :
			          (v[pi][2] == 5) ? 'l' : 
			          (v[pi][2] == 10) ? 'r' :
			          (v[pi][2] == 12) ? 'd' : 'x';
			cf[q] = ch;
		}
		System.out.println("");
		for (int i = 0; i < size; i++) {
			if(i % width == 0) System.out.println("");
			System.out.print(" " + cf[i]);
		}
	}

	public static void main(String[] args) {
		TwoTwoMinoFigures f = new TwoTwoMinoFigures();
		f.makeFigures();
		f.makeExtendedFigures();
		///f.printFigures();
		System.out.println(f.getExtendedFigures().length);
	}
}
