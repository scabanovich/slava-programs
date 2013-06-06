package com.slava.math;

/**
 * Given are two kind of coins with values x and y,
 * they are mutually simple, their sum is odd.
 * What maximum value cannot be represented by 
 * these coins combination?
 */

public class MaxUnreachable {
	
	public static int getMaxUnreachable(int x, int y) {
		int h = x * y * 2;
		int[] state = new int[h + 1];
		state[0] = 1;
		for (int i = 0; i <= h; i++) {
			if(state[i] == 0) continue;
			int j = i + x;
			if(j <= h) state[j] = 1;
			j = i + y;
			if(j <= h) state[j] = 1;
		}
		for (int i = h; i >= 0; i--) {
			if(state[i] == 0) return i;
		}
		return -1;
	}

	public static void main(String[] args) {
		for (int x = 2; x < 1000; x++) {
			System.out.println("x=" + x);
		for (int y = x + 1; y < 2000; y++) {
			if(EuclidEquation.getMaxCommonDivisor(x, y) > 1) continue;
			int z = getMaxUnreachable(x, y);
			int zt = (x * y - x - y);
//			System.out.println(z);
//			System.out.println("" + zt);
			if(z != zt) System.out.println("Error x=" + x + " y=" + y);
		}
		}
	}

}
