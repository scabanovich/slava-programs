package forsmarts;

import com.slava.common.TwoDimField;

public class RoundAndRoundField extends TwoDimField {
	int length;
	int[] filter;
	
	int[] triangleP, triangleD;
	int[][] triangleIndex;
	int triangleCount;
	
	int[][] triangleJump;
	int[][] triangleJumpBorder;
	
	int[] reverse = {3,4,5,0,1,2};
	
	public RoundAndRoundField() {
		setOrts(TRIANGULAR_ORTS);
	}
	
	public void setSize(int length) {
		this.length = length;
		int w = length * 2 + 1;
		super.setSize(w, w);
		initFilter();
		initTriangles();
		initTriangleJumps();
	}
	
	void initFilter() {
		filter = new int[size];
		for (int p = 0; p < size; p++) {
			if(Math.abs(x[p] - y[p]) > length) {
				filter[p] = 0;
			} else {
				filter[p] = 1;
			}
		}
		for (int p = 0; p < size; p++) {
			if(filter[p] == 0) {
				for (int d = 0; d < 6; d++) jp[d][p] = -1;
			} else {
				for (int d = 0; d < 6; d++) {
					int q = jp[d][p];
					if(q >= 0 && filter[q] == 0) {
						jp[d][p] = -1;
					}
				}
			}
		}
	}
	
	void initTriangles() {
		int ti = 0;
		for (int p = 0; p < size; p++) {
			for (int d = 0; d < 2; d++) {
				if(jp[d][p] < 0 || jp[d + 1][p] < 0) continue;
				ti++;
			}
		}
		triangleP = new int[ti];
		triangleD = new int[ti];
		triangleIndex = new int[size][2];
		for (int p = 0; p < size; p++) {
			for (int d = 0; d < 2; d++) triangleIndex[p][d] = -1;
		}
		ti = 0;
		for (int p = 0; p < size; p++) {
			for (int d = 0; d < 2; d++) {
				if(jp[d][p] < 0 || jp[d + 1][p] < 0) continue;
				triangleP[ti] = p;
				triangleD[ti] = d;
				triangleIndex[p][d] = ti;
				ti++;
			}
		}
		triangleCount = ti;
		System.out.println("TriangleCount=" + triangleCount);
	}
	
	void initTriangleJumps() {
		triangleJump = new int[3][triangleCount];
		triangleJumpBorder = new int[3][triangleCount];
		for (int t = 0; t < triangleCount; t++) {
			int tp = triangleP[t], td = triangleD[t];
			for (int d = 0; d < 3; d++) {
				triangleJump[d][t] = -1;
				triangleJumpBorder[d][t] = -1;
				if(td == 0 && d == 0) {
					int q = jp[5][tp];
					if(q < 0) continue;
					triangleJump[d][t] = triangleIndex[q][1];
					triangleJumpBorder[d][t] = tp;
				} else if(td == 0 && d == 1) {
					triangleJump[d][t] = triangleIndex[tp][1];
					if(triangleJump[d][t] < 0) continue;
					triangleJumpBorder[d][t] = tp;
				} else if(td == 0 && d == 2) {
					int q = jp[0][tp];
					triangleJump[d][t] = triangleIndex[q][1];
					if(triangleJump[d][t] < 0) continue;
					triangleJumpBorder[d][t] = q;
				} else if(td == 1 && d == 0) {
					int q = jp[2][tp];
					if(q < 0) continue;
					triangleJump[d][t] = triangleIndex[q][0];
					triangleJumpBorder[d][t] = q;
				} else if(td == 1 && d == 1) {
					triangleJump[d][t] = triangleIndex[tp][0];
					if(triangleJump[d][t] < 0) continue;
					triangleJumpBorder[d][t] = tp;
				} else if(td == 1 && d == 2) {
					int q = jp[3][tp];
					if(q < 0) continue;
					triangleJump[d][t] = triangleIndex[q][0];
					if(triangleJump[d][t] < 0) continue;
					triangleJumpBorder[d][t] = tp;
				}
			}
		}
	}
	
	public int getTriangleCount() {
		return triangleCount;
	}
	
	public int getTriangleIndex(int p, int d) {
		return triangleIndex[p][d];
	}
	
	public int jumpTriangle(int t, int d) {
		return triangleJump[d][t];
	}
	
	public int getTriangleJumpBorderStart(int p, int d) {
		return triangleJumpBorder[d][p];
	}

}
