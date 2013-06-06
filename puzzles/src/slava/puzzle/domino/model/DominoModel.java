package slava.puzzle.domino.model;

import slava.puzzle.template.model.PuzzleModel;

public class DominoModel extends PuzzleModel {
	int width;
	int height;
	int size;
	
	int max = 6;
	
	int[] field;
	int[] x;
	int[] y;
	int[][] jp;

	int[] values;
	int[][] hRestrictions;
	int[][] vRestrictions;
	
	public DominoModel() {
		setSize(10, 11);
		setLoader(new DominoLoader());
	}

	public void setProblem(int[] values, int[][] hRestrictions, int[][] vRestrictions) {
		this.values = values;
		this.hRestrictions = hRestrictions;
		this.vRestrictions = vRestrictions;
		solutionInfo = "Solution";
	}

	public void setProblem(int[] values, int[] hOutline, int[] vOutline) {
		this.values = values;
		this.hRestrictions = new int[hOutline.length][];
		for (int i = 0; i < hOutline.length; i++) {
			hRestrictions[i] = new int[hOutline[i]];
			//fill values
		}
		this.vRestrictions = new int[vOutline.length][];
		for (int i = 0; i < vOutline.length; i++) {
			vRestrictions[i] = new int[vOutline[i]];
			//fill values
		}
		solutionInfo = "Solution";
	}	
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		field = new int[size];
		x = new int[size];
		y = new int[size];
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSize() {
		return size;
	}
	
	public int x(int i) {
		return x[i];
	}

	public int y(int i) {
		return y[i];
	}
	
	public int jump(int i, int d) {
		return jp[d][i];
	}

	public void flip(int i, int j) {
		solutionInfo = null;
		if(field[i] == field[j]) {
			if(field[i] != 0) {
				field[i] = 0;
				field[j] = 0;				
			} else {
				int v = findNewValue(i, j);
				field[i] = v;
				field[j] = v;				
			}
		} else {
			int v = (field[i] != 0) ? field[i] : field[j];
			disconnect(i, j);
			field[i] = v;
			field[j] = v;				
		}
	}
	
	private int findNewValue(int i, int j) {
		int[] vs = new int[8];
		for (int v = 0; v < vs.length; v++) vs[v] = 0;
		for (int d = 0; d < 4; d++) {
			int k = jp[d][i];
			if(k >= 0 && k != j && field[k] < 8) vs[field[k]] = 1;
			k = jp[d][j];
			if(k >= 0 && k != i && field[k] < 8) vs[field[k]] = 1;
		}
		for (int v = 1; v < vs.length; v++) if(vs[v] == 0) return v;
		return 0;
	}
	
	private void disconnect(int i, int j) {
		if(field[i] != 0) {
			for (int d = 0; d < 4; d++) {
				int k = jp[d][i];
				if(k >= 0 && k != j && field[k] == field[i]) field[k] = 0;
			}			
		}
		if(field[j] != 0) {
			for (int d = 0; d < 4; d++) {
				int k = jp[d][j];
				if(k >= 0 && k != i && field[k] == field[j]) field[k] = 0;
			}			
		}		
	}
	
	public int getRequiredPiecesNumber() {
		return (max * (max + 1)) / 2; 
	}
	
	public int getPiecesCount() {
		int s = 0; 
		for (int i = 0; i < size; i++) if(field[i] != 0) ++s;
		return s / 2;
	}
	
	public int getFieldGroupIndex(int i) {
		return field[i];
	}
	
	public int getValue(int i) {
		return values[i];
	}
	
	public int[] getHRestrictions(int i) {
		return hRestrictions[i];
	}
	
	public int[] getVRestrictions(int i) {
		return vRestrictions[i];
	}
	
	public int[] getFieldCopy() {
		return (int[])field.clone();
	}
	
	public int[] getHRestrictionOutline() {
		int[] is = new int[hRestrictions.length];
		for (int i = 0; i < is.length; i++) is[i] = hRestrictions[i].length;
		return is; 
	}

	public int[] getVRestrictionOutline() {
		int[] is = new int[vRestrictions.length];
		for (int i = 0; i < is.length; i++) is[i] = vRestrictions[i].length;
		return is; 
	}

}
