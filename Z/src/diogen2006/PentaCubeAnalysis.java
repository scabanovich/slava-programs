package diogen2006;

import forsmarts.fitting2d.PackingAnalysis;

public class PentaCubeAnalysis extends PackingAnalysis {
	PentaCubeForm cubeForm;
	int[][] z12Filter;
	
	public PentaCubeAnalysis() {}
	
	public void setCubeForm(PentaCubeForm cubeForm) {
		this.cubeForm = cubeForm;
		initZFilter();
	}
	
	void initZFilter() {
		int w = cubeForm.cubeWidth + cubeForm.cubeDepht + 1;
		z12Filter = new int[w][w];
		for (int ix1 = 0; ix1 < cubeForm.cubeWidth; ix1++) {
			for (int ix2 = cubeForm.cubeWidth + 1; ix2 < w; ix2++) {
				int p = (ix2 - cubeForm.cubeWidth - 1) * cubeForm.cubeWidth + ix1;
				z12Filter[ix1][ix2] = cubeForm.zFilter[p];
			}
		}
	}

	protected void filterWays() {
		if(t >= 3) return;
		int limit = wayCount[t]; //60;
		if(wayCount[t] < limit) return;
		for (int i = 0; i < limit; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			int c = ways[t][i]; ways[t][i] = ways[t][j]; ways[t][j] = c;
		}
		wayCount[t] = limit;
	}

	protected boolean isIllegalState() {
		if(t > 7) return false;
		for (int iy = 0; iy < field.getHeight(); iy++) {
			boolean rc = isRowComplete(iy);
			if(rc && !checkRow(iy)) return true;
			if(!rc && !checkRowPartial(iy)) return true;
		}
		return false;
	}
	
	boolean isRowComplete(int iy) {
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(state[p] >= 0 || shape[p] > 0 || fieldRestriction[p] > 0) continue;
			return false;
		}
		return true;
	}

	protected boolean checkSolution() {
		int rows = 0;
		for (int iy = 0; iy < field.getHeight(); iy++) {
			if(checkRow(iy)) ++rows;
		}

		if(rows < field.getHeight() - 1) return false;

		if(solutionCount > 100 && rows < field.getHeight()) return false;
		System.out.println("rows=" + rows);
		return true;
	}
	
	boolean checkRow(int iy) {
		for (int ix1 = 0; ix1 < cubeForm.cubeWidth; ix1++) {
			int p = field.getIndex(ix1, iy);
			if(state[p] < 0) continue;
			if(!checkARay(ix1, iy)) return false;
		}
		for (int ix2 = cubeForm.cubeWidth + 1; ix2 < field.getWidth(); ix2++) {
			int p = field.getIndex(ix2, iy);
			if(state[p] < 0) continue;
			if(!checkBRay(ix2, iy)) return false;
		}
		return true;
	}
	
	boolean checkARay(int ix1, int iy) {
		for (int ix2 = cubeForm.cubeWidth + 1; ix2 < field.getWidth(); ix2++) {
			int p = field.getIndex(ix2, iy);
			if(state[p] < 0) continue;
			if(z12Filter[ix1][ix2] > 0) return true;
		}
		return false;
	}
	
	boolean checkBRay(int ix2, int iy) {
		for (int ix1 = 0; ix1 < cubeForm.cubeWidth; ix1++) {
			int p = field.getIndex(ix1, iy);
			if(state[p] < 0) continue;
			if(z12Filter[ix1][ix2] > 0) return true;
		}
		return false;
	}
	
	boolean checkRowPartial(int iy) {
		for (int ix1 = 0; ix1 < cubeForm.cubeWidth; ix1++) {
			int p = field.getIndex(ix1, iy);
			if(state[p] < 0) continue;
			if(!checkARayPartial(ix1, iy)) return false;
		}
		for (int ix2 = cubeForm.cubeWidth + 1; ix2 < field.getWidth(); ix2++) {
			int p = field.getIndex(ix2, iy);
			if(state[p] < 0) continue;
			if(!checkBRayPartial(ix2, iy)) return false;
		}
		return true;
	}
	
	boolean checkARayPartial(int ix1, int iy) {
		for (int ix2 = cubeForm.cubeWidth + 1; ix2 < field.getWidth(); ix2++) {
			int p = field.getIndex(ix2, iy);
			if(fieldRestriction[p] > 0) continue;
			if(shape[p] > 0 && state[p] < 0) continue;
			if(z12Filter[ix1][ix2] > 0) return true;
		}
		return false;
	}
	
	boolean checkBRayPartial(int ix2, int iy) {
		for (int ix1 = 0; ix1 < cubeForm.cubeWidth; ix1++) {
			int p = field.getIndex(ix1, iy);
			if(fieldRestriction[p] > 0) continue;
			if(shape[p] > 0 && state[p] < 0) continue;
			if(z12Filter[ix1][ix2] > 0) return true;
		}
		return false;
	}
}
