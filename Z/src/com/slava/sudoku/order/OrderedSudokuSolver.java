package com.slava.sudoku.order;

import com.slava.sudoku.SudokuSolver;

public class OrderedSudokuSolver extends SudokuSolver {
	int[][] numbers;
	
	public void setNumbers(int[][] ns) {
		numbers = ns;
		computeRangeValues();
	}
	
	protected boolean isWrong() {
		int current = 0;
		for (int i = 0; i < numbers.length; i++) {
			int[] number = numbers[i];
			current = findMinimumValue(number, 0, 0, current);
			if(current < 0) return true;
		}
		return false;
	}
	
	int findMinimumValue(int[] number, int index, int c, int current) {
		if(index == number.length) {
			if(c > current) return c; else return -1;
		}
		int p = number[index];
		if(state.getValue(p) >= 0) {
			c = c * 10 + (state.getValue(p) + 1);
			return findMinimumValue(number, index + 1, c, current);
		}
		int v = -1;
		while(true) {
			v = getNextValue(p, v);
			if(v < 0) return -1;
			int c1 = c * 10 + (v + 1);
			int m = findMinimumValue(number, index + 1, c1, current);
			if(m > current) return m;
		}
	}
	
	int getNextValue(int p, int vc) {
		if(state.getValue(p) >= 0) return -1;
		for (int v = vc + 1; v < field.getColorCount(); v++) {
			if(!state.isUsed(p, v)) return v;
		}
		return -1;
	}
	
	void computeRangeValues() {
		int[] mn = new int[field.getSize()];
		for (int i = 0; i < mn.length; i++) mn[i] = 1;
		for (int i = 0; i < numbers.length; i++) {
			if(i == 0 || numbers[i].length > numbers[i - 1].length) continue;
			int p = numbers[i][0];
			int[] ri = field.getPlaceToRegions()[p];
			int m = 0;
			for (int j = 0; j < i; j++) {
				if(numbers[i].length > numbers[j].length) continue;
				if(mn[numbers[j][0]] > m && contains(ri, numbers[j][0])) {
					m = mn[numbers[j][0]];
				}
			}
			mn[p] = m + 1;
		}

		int[] mx = new int[field.getSize()];
		for (int i = 0; i < mx.length; i++) mx[i] = field.getColorCount();
		for (int i = numbers.length - 1; i >= 0; i--) {
			if(i == numbers.length - 1 || numbers[i].length < numbers[i + 1].length) continue;
			int p = numbers[i][0];
			int[] ri = field.getPlaceToRegions()[p];
			int m = field.getColorCount() + 1;
			for (int j = numbers.length - 1; j > i; j--) {
				if(numbers[i].length < numbers[j].length) continue;
				if(mx[numbers[j][0]] < m && contains(ri, numbers[j][0])) {
					m = mx[numbers[j][0]];
				}
			}
			mx[p] = m - 1;
		}
		int[][] rs = new int[field.getSize()][2];
		for (int i = 0; i < rs.length; i++) {
			rs[i][0] = mn[i];
			rs[i][1] = mx[i];
		}
		setInitialRangeRestrictions(rs);
	}
	
	boolean contains(int[] ri, int q) {
		for (int i = 0; i < ri.length; i++) {
			int[] region = regions[ri[i]];
			for (int k = 0; k < region.length; k++) {
				if(region[k] == q) return true;
			}
		}
		return false;
	}
	
}
