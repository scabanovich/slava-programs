package champ2005;

import java.util.*;

public class Year2005 {
	
	public void approaces(double x) {
		TreeMap map = new TreeMap();
		for (int i = 10; i < 700000; i++) {
			int j = (int)Math.round(x * i);
			double delta = Math.abs(x - (1d * j) / i);
			Double d = new Double(delta);
			if(!map.containsKey(d)) {
				map.put(d, new int[]{j,i});
			}
		}
		int[][] apps = (int[][])map.values().toArray(new int[0][]);
		int q = apps.length;
		if(q > 20) q = 20;
		for (int i = 0; i < q; i++) {
			int up = apps[i][0];
			int down = apps[i][1];
			int score = getScore(x, up, down);
			System.out.println("" + 2 + "+" + (up - 2 * down) + "/" + down + " : " + score);
		}
		
	}
	
	int getScore(double x, int up, int down) {
		double a = (1d * up) / down;
		return getScore(x, a);
	}
	int getScore(double x, double approach) {
		double delta = Math.abs(x - approach);
		int q = 0;
		while(delta < 1) {
			q++;
			delta *= 10;
		}
		int c = (int)Math.floor(delta);
		return q * 10 - c;		
	}

	public static void main(String[] args) {
		//System.out.println(":" + (10037/19) + ":" + (10037%19));
		double x = 2.005678987656789876d;
		double approach = 2d + 3d / (8d * 6d * (4d + 7d + 0d) + (5d / 19d));
		System.out.println("x=" + x);
		System.out.println("a=" + approach);
		Year2005 y = new Year2005();
		System.out.println("score=" + y.getScore(x, approach));
		y.approaces(x);
	}
}

/*
 * 2d + 3d / (8d * 6d * (4d + 7d + 0d) + (5d / 19d));
 * score = 102
 */
