package ik6_1;

public class Cards_6_1_CellSet {
	static int VALUE_KIND = 0;
	static int SUIT_KIND = 1;
	int kind;
	int[] cells;
	int[] usage;
	
	public Cards_6_1_CellSet(int kind, int[] cells, int[] usage) {
		this.kind = kind;
		this.cells = cells;
		this.usage = usage;		
	}


}
