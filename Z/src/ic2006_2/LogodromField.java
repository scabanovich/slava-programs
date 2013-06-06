package ic2006_2;

import java.util.*;

import com.slava.common.TwoDimField;

public class LogodromField extends TwoDimField {
	int[] filter;
	int[][] rows;
	
	int[] hvDirections = new int[]{0,2,4,6};
	
	public LogodromField() {
		setOrts(TwoDimField.DIAGONAL_ORTS);
	}
	
	public void setSize(int width) {
		super.setSize(width, width);
	}
	
	public void setFilter(int[] f) {
		filter = f;
		for (int p = 0; p < size; p++) {
			for (int d = 0; d < 8; d++) {
				if(filter[p] == 1) {
					jp[d][p] = -1;
				} else {
					int q = jp[d][p];
					if(q >= 0 && filter[q] == 1) jp[d][p] = -1;
				}
			}
		}
		makeRows();
	}
	
	void makeRows() {
		List list = new ArrayList();
		for (int i = 0; i < size; i++) {
			for (int d = 0; d < 4; d++) {
				if(filter[i] == 1) continue;
				int q = jp[d + 4][i];
				if(q >= 0) continue;
				list.add(createRow(i, d));
			}
		}
		rows = (int[][])list.toArray(new int[0][]);
//		for (int i = 0; i < rows.length; i++) {
//			for (int j = 0; j < rows[i].length; j++) {
//				System.out.print(" " + rows[i][j]);
//			}
//			System.out.println("");
//		}
	}
	
	private int[] createRow(int p, int d) {
		int c = 0;
		int q = p;
		while(q >= 0) {
			c++;
			q = jp[d][q];
		}		
		int[] r = new int[c];
		c = 0;
		q = p;
		while(q >= 0) {
			r[c] = q;
			c++;
			q = jp[d][q];
		}
		return r;
	}

}
