package com.slava.common;

public class CellSet {
	int[] cells;
	int count;
	
	public CellSet(int size) {
		cells = new int[size];
		count = 0;
	}
	
	public void add(int p) {
		if(cells[p] == 0) count++;
		cells[p]++;
	}

	public void remove(int p) {
		cells[p]--;
		if(cells[p] == 0) count--;
	}

	public void add(int p, int delta) {
		if(cells[p] == 0) count++;
		cells[p] += delta;
	}

	public void remove(int p, int delta) {
		cells[p] -= delta;
		if(cells[p] == 0) count--;
	}
	
	public int value(int p) {
		return cells[p];
	}
	
	public int getCount() {
		return count;
	}

}
