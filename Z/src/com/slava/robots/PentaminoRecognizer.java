package com.slava.robots;

import com.slava.common.RectangularField;

public class PentaminoRecognizer {

	static int[][] distanceIndex = {
		{0,1,3,6,8},
		{1,2,4,7,0},
		{3,4,5,0,0},
		{6,7,0,0,0},
		{8,0,0,0,0}
	};

	public static String getFigureCode(RectangularField field, int[] nodes, int size) {
		int[][] q = new int[size][9];
		int[] ns = new int[size];

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				int p1 = nodes[i];
				int p2 = nodes[j];
				int dx = Math.abs(field.getX(p1) - field.getX(p2));
				int dy = Math.abs(field.getY(p1) - field.getY(p2));
				int index = distanceIndex[dx][dy];
				q[i][index]++;
				q[j][index]++;
				if(index == 1) {
					ns[i]++;
					ns[j]++;
				}
			}
		}
		
		int[] s = new int[9];
		for (int i = 0; i < q.length; i++) {
			if(q[i][1] == 4) return "X";
			if(q[i][8] == 1) return "I";
			for (int v = 0; v < 9; v++) {
				s[v] += q[i][v];
			}
		}

		for (int v = 0; v < 9; v++) {
			s[v] = s[v] / 2;
		}
		if(s[1] == 5) return "P";
		if(s[7] == 1) {
			return s[6] == 1 ? "L" : "N";
		}
		if(s[2] == 1) return "V";
		if(s[6] == 1) return "Y";
		if(s[2] == 3) {
			return s[3] == 0 ? "W" : "F";
		}
		if(s[3] == 1 && s[5] == 1) return "Z";
		int[] nsd = new int[5];
		for (int i = 0; i < ns.length; i++) {
			nsd[ns[i]]++;
		}
		
		if(nsd[1] == 3) return "T";
		return "U";
	}

}
