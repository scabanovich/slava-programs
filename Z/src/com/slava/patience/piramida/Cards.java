package com.slava.patience.piramida;

public class Cards {
	static char[] CARD_SUIT = new char[]{'s','c','d','h'};
	static char[] CARD_VALUE = new char[]{'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
	int suitCount;
	int valueCount;
	int packSize;
	
	int[] suits;
	int[] values;
	
	int[][] valuesAreNeighboring;
	
	public Cards() {}
	
	public void setSize(int suitCount, int valueCount) {
		this.suitCount = suitCount;
		this.valueCount = valueCount;
		packSize = suitCount * valueCount;
		suits = new int[packSize];
		values = new int[packSize];
		for (int i = 0; i < packSize; i++) {
			values[i] = i % valueCount;
			suits[i] = i / valueCount;
		}
		valuesAreNeighboring = new int[packSize][packSize];
		for (int i = 0; i < packSize; i++) {
			int v1 = values[i];
			for (int j = 0; j < packSize; j++) {
				int v2 = values[j];
				if(v1 == v2 + 1 
				   || v2 == v1 + 1
				   || (v1 == 0 && v2 + 1 == valueCount)
				   || (v2 == 0 && v1 + 1 == valueCount)) {
					valuesAreNeighboring[i][j] = 1;
				}
				
			}
		}
	}
	
	public boolean areValuesNaighbouring(int c1, int c2) {
		return valuesAreNeighboring[c1][c2] == 1;
	}
	
	public String getCardDesignation(int c) {
		return "" + CARD_VALUE[values[c]] + CARD_SUIT[suits[c]];
	}
	
	public int[] parse(String s) {
		int[] cs = new int[s.length() / 2];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = -1;
			String q = s.substring(i * 2, i * 2 + 2);
			for (int c = 0; c < packSize; c++) {
				if(q.equals(getCardDesignation(c))) {
					cs[i] = c;
					break;
				}
			}
			if(cs[i] < 0) throw new RuntimeException("Parse error");
		}
		return cs;
	}

}
