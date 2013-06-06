package ik6_1;

import java.util.ArrayList;

public class SecondHexField {
	static int[] REVERSE = {2,3,0,1,5,4};
	int width;
	int size;
	int[] x;
	int[] y;
	int[][] xy;
	int[][] jp;
	
	int[] form;
	
	int[][] rows;
	int[][] rowsByPlace;
	
	int[][] row_conditions; //[index]{row, value, index}
	
	public SecondHexField() {}
	
	public void setSize(int width) {
		this.width = width;
		size = width * width;
		x = new int[size];
		y = new int[size];
		xy = new int[width][width];
		for (int i = 0; i < size; i++) {
			x[i] = i % width;
			y[i] = i / width;
			xy[x[i]][y[i]] = i;
		}
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == width - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
			jp[4][i] = (x[i] == width - 1 || y[i] == width - 1) ? -1 : i + 1 + width;
			jp[5][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
		}
	}
	
	public void setForm(int[] form) {
		this.form = form;
		for (int i = 0; i < size; i++) {
			for (int d = 0; d < 6; d++) {
				if(form[i] == 0) {
					jp[d][i] = -1;
				} else {
					int q = jp[d][i];
					if(q >= 0 && form[q] == 0) jp[d][i] = -1;
				}
			}
		}
		buildRows();
	}
	
	void buildRows() {
		ArrayList list = new ArrayList();
		for (int p = 0; p < size; p++) {
			if(form[p] == 0) continue;
			int[] r = buildRow(p, 0);
			if(r != null && r.length > 0) list.add(r);
			r = buildRow(p, 1);
			if(r != null && r.length > 0) list.add(r);
			r = buildRow(p, 4);
			if(r != null && r.length > 0) list.add(r);
		}
		rows = (int[][])list.toArray(new int[0][]);
		rowsByPlace = new int[size][];
		for (int p = 0; p < size; p++) {
			if(form[p] == 0) continue;
			int c = 0;
			for (int i = 0; i < rows.length; i++) {
				if(contains(rows[i], p)) ++c;
			}
			rowsByPlace[p] = new int[c];
			c = 0;
			for (int i = 0; i < rows.length; i++) {
				if(contains(rows[i], p)) {
					rowsByPlace[p][c] = i;
					++c;
				}
			}
		}
//		for (int p = 0; p < size; p++) {
//			if(form[p] == 0) continue;
//			System.out.print("" + p + ": ");
//			for (int i = 0; i < rowsByPlace[p].length; i++) System.out.print("" + rowsByPlace[p][i] + ",");
//			System.out.println("");
//		}
	}
	
	int[] buildRow(int p, int d) {
		if(jp[REVERSE[d]][p] >= 0) return null;
		int c = 0;
		int q = p;
		while(q >= 0) {
			++c;
			q = jp[d][q];
		}
		int[] rs = new int[c];
		c = 0;
		q = p;
		while(q >= 0) {
			rs[c] = q;
			++c;
			q = jp[d][q];
		}
		return rs;
	}
	
	boolean contains(int[] row, int p) {
		for (int i = 0; i < row.length; i++) {
			if(row[i] == p) return true;
		}
		return false;
	}
	
	public void setConditions(int[][] xyd_conditions) {
		row_conditions = new int[xyd_conditions.length][3];
		for (int i = 0; i < row_conditions.length; i++) {
			row_conditions[i][0] = findRow(xyd_conditions[i]);
			row_conditions[i][1] = xyd_conditions[i][3];
			row_conditions[i][2] = xyd_conditions[i][4];
			System.out.println(row_conditions[i][0]);
		}
	}
	
	int findRow(int[] n_condition) {
		int b = xy[n_condition[0]][n_condition[1]];
		int n = jp[n_condition[2]][b];
		for (int row = 0; row < rows.length; row++) {
			if(rows[row][0] == b && rows[row][1] == n) return row;
		}
		return -1;
	}
	

}
