package com.slava.binary;

/*
 * An oficier put all his soliers to a line
 * and is picking up squads for cleaning n rest rooms. 
 * Each squad is composed as follows:
 * He picks up the first soldier and in the line, and 
 * after that each k-th, where k is fixed for the 
 * selection, but can be taken by officier from 
 * a set of his preferences, for example {3,4}, 
 * so that selection process for i-th rest room may 
 * get its own k_i of that set.
 * You have to find a place in the line of soldiers,
 * that is garanteed from assignment to a rest room 
 * cleaning, no matter what is the officier's strategy.
 */
public class Soldiers {
	int limit = 10000000;
	
	void sieveRowByStep(int[] row, int step) {
		int h = 0;
		for (int i = 0; i < row.length; i++) {
			if(row[i] == 1) continue;
			if(h == 0) {
				row[i] = 1;
				h = step - 1;
			} else {
				h--;
			}
		}
	}
	
	int[] sieveRowBySteps(int[] steps) {
		int[] row = new int[limit];
		for (int k = 0; k < steps.length; k++) sieveRowByStep(row, steps[k]);
		return row;
	}
	
	public int sieve(int length, int[] options) {
		int v = 1;
		for (int q = 0; q < length; q++) v = v * options.length;
		int[] row = new int[limit];
		for (int k = 0; k < v; k++) {
			int[] r = sieveRowBySteps(getSteps(k, length, options));
			for (int i = 0; i < r.length; i++) if(r[i] > 0) row[i] = 1;
		}
		for (int i = 0; i < row.length; i++) {
			if(row[i] == 0) return i;
		}
		return -1;
	}
	
	int[] getSteps(int k, int length, int[] options) {
		int[] steps = new int[length];
		for (int i = 0; i < length; i++) {
			steps[i] = options[(k % options.length)];
			k = k / 2;
		}
		return steps;
	}

	public static void main(String[] args) {
		Soldiers s = new Soldiers();
		int k = s.sieve(8, new int[]{4,3});
		System.out.println("Result=" + k);
	}

}
