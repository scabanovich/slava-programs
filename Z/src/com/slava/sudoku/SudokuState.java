package com.slava.sudoku;

public class SudokuState {
	protected ISudokuField field;
	protected int[][] regions;

	protected int[][] used; // [p][v]
	protected int[] freeCount; // [p]

	protected int[][] usedRegionColors;
	protected int[] freeRegionColorCount;
	
	protected int[] values;
	protected int vacancies;
	
	public void setField(ISudokuField field) {
		this.field = field;
		regions = field.getRegions();
		vacancies = field.getSize();
		values = new int[vacancies];
		used = new int[field.getSize()][field.getColorCount()];
		freeCount = new int[vacancies];
		usedRegionColors = new int[regions.length][field.getColorCount()];
		freeRegionColorCount = new int[regions.length];
	}
	
	public void init() {
		vacancies = field.getSize();
		for (int i = 0; i < values.length; i++) values[i] = -1;
		
		for (int i = 0; i < field.getSize(); i++) for (int j = 0; j < field.getColorCount(); j++) used[i][j] = 0;
		for (int i = 0; i < freeCount.length; i++) freeCount[i] = field.getColorCount();
		
		for (int i = 0; i < regions.length; i++) for (int j = 0; j < field.getColorCount(); j++) usedRegionColors[i][j] = 0;
		for (int i = 0; i < freeRegionColorCount.length; i++) freeRegionColorCount[i] = field.getColorCount();
	}

	public boolean canAdd(int p, int v) {
		return (values[p] == -1) && (used[p][v] == 0);
	}
	
	public void add(int p, int v) {
		vacancies--;
		values[p] = v;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				used[region[k]][v]++;
				if(used[region[k]][v] == 1) {
					freeCount[region[k]]--;
				}
			}
			usedRegionColors[ri[r]][v] = 1;
			freeRegionColorCount[ri[r]]--;
		}
	}
	
	public void addRangeRestriction(int p, int[] rg) {
		for (int v = 0; v < rg[0] - 1; v++) {
			used[p][v]++;
			if(used[p][v] == 1) freeCount[p]--;
		}
		for (int v = rg[1]; v < field.getColorCount(); v++) {
			used[p][v]++;
			if(used[p][v] == 1) freeCount[p]--;
		}
	}

	public void remove(int p, int v) {
		vacancies++;
		values[p] = -1;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				used[region[k]][v]--;
				if(used[region[k]][v] == 0) {
					freeCount[region[k]]++;
				}
			}
			usedRegionColors[ri[r]][v] = 0;
			freeRegionColorCount[ri[r]]++;
		}
	}
	
	public int[] getValues() {
		return values;
	}
	
	public int getValue(int p) {
		return values[p];
	}
	
	public boolean isUsed(int p, int v) {
		return used[p][v] != 0;
	}
	
	public int getFreeColorCount(int p) {
		return freeCount[p];
	}

}
