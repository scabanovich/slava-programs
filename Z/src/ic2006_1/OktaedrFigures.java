package ic2006_1;

import java.util.ArrayList;
import java.util.Arrays;

public class OktaedrFigures {
	/**
	 * Each figure is described as sequence of instructions
	 * {%Index of preceding part%, %Direction to the current part%}
	 */
	int[][][] figures = {
		{{0,1},{1,0},{2,2},{3,0},{4,2},{5,0},{2,1}}, //A
		{{0,1},{1,0},{2,2},{3,0},{4,1},{4,2},{6,0}}, //B
		{{0,1},{1,0},{2,2},{3,0},{4,2},{5,0},{6,1}}, //C
		{{0,0},{1,2},{2,0},{3,2},{4,0},{1,1},{2,1}}, //D
		{{0,0},{1,2},{2,0},{3,2},{4,0},{1,1},{4,1}}, //E
		{{0,0},{1,2},{2,0},{3,2},{4,0},{2,1},{3,1}}, //F
		{{0,2},{1,0},{2,2},{3,0},{4,1},{1,1},{6,0}}, //G
		{{0,2},{1,0},{2,2},{3,0},{2,1},{3,1},{6,2}}, //H
		{{0,2},{1,0},{2,2},{3,0},{0,1},{1,1},{6,0}}, //I
		{{0,0},{1,2},{2,0},{3,1},{4,0},{5,2},{6,0}}, //J
		{{0,1},{1,0},{2,2},{3,0},{4,1},{5,0},{6,2}}, //K
	};
	
	TriangularField field;
	int[][][] placements;
	
	public void setField(TriangularField f) {
		field = f;
	}
	
	public void makePlacements() {
		placements = new int[figures.length][][];
		for (int i = 0; i < figures.length; i++) {
			ArrayList list = new ArrayList();
			getInitialPlacements(figures[i], list);
			int c = 0;
			while(c < list.size()) {
				int[] pcc = (int[])list.get(c);
				add(list, field.rotate(pcc));
				add(list, field.reflect(pcc));
				add(list, field.shiftAlongX(pcc));
				add(list, field.shiftAlongY(pcc));
				++c;
			}
			placements[i] = (int[][])list.toArray(new int[0][]);
			System.out.println("-->" + placements[i].length);
//			printPlacement(pc);
		}
	}
	
	void getInitialPlacements(int[][] figure, ArrayList list) {
		for (int p = 0; p < field.getSize(); p++) {
			int[] pc = new int[figure.length + 1];
			boolean b = fillPlacement(figure, pc, p);
			if(!b) continue;
			Arrays.sort(pc);
			if(!contains(list, pc)) list.add(pc);
		}
	}
	
	private boolean fillPlacement(int[][] figure, int[] pc, int p) {
		pc[0] = p;
		for (int i = 0; i < figure.length; i++) {
			p = pc[figure[i][0]];
			int d = figure[i][1];
			int q = field.jp[p][d];
			if(q < 0) return false;
			pc[i + 1] = q;
		}
		return true;
	}
	
	void printPlacement(int[] pc) {
		for (int i = 0; i < pc.length; i++) {
			System.out.print(" " + pc[i]);
		}
		System.out.println("");
	}
	
	void add(ArrayList list, int[] pc) {
		if(pc != null && !contains(list, pc)) list.add(pc);
	}
	
	boolean contains(ArrayList list, int[] pc) {
		for (int i= 0; i < list.size(); i++) {
			int[] other = (int[])list.get(i);
			if(equal(other, pc)) return true;
		}
		return false;
	}
	
	boolean equal(int[] p1, int[] p2) {
		for (int i = 0; i < p1.length; i++) {
			if(p1[i] != p2[i]) return false;
		}
		return true;
	}
}
