package forsmarts;

import java.util.*;

public class SumsInRightAnglesField extends XPlusYField {
	
	public void setSize(int width, int height) {
		super.setSize(width, height);		
	}
	
	public int[][] makeTriangles(int[] values) {
		List list = new ArrayList();
		for (int i = 0; i < size; i++) {
			if(values[i] == 0) continue;
			for (int j1 = 0; j1 < size; j1++) {
				if(j1 == i || values[j1] == 0 || values[j1] >= values[i]) continue;
				int j2 = getRotatedVertexA(i, j1);
				if(j2 >= 0 && j2 > j1 && values[j2] > 0 && values[j2] + values[j1] == values[i]) {
					list.add(new int[]{i, j1, j2});
				}
				j2 = getRotatedVertexB(i, j1);
				if(j2 >= 0 && j2 > j1 && values[j2] > 0 && values[j2] + values[j1] == values[i]) {
					list.add(new int[]{i, j1, j2});
				}
			}
		}
		return (int[][])list.toArray(new int[0][]);
	}
	
	public int getRotatedVertexA(int p, int q) {
		int dx = x[q] - x[p];
		int dy = y[q] - y[p];
		int ix = x[p] - dy;
		int iy = y[p] + dx;
		return getIndex(ix, iy);
	}

	public int getRotatedVertexB(int p, int q) {
		int dx = x[q] - x[p];
		int dy = y[q] - y[p];
		int ix = x[p] + dy;
		int iy = y[p] - dx;
		return getIndex(ix, iy);
	}

}
