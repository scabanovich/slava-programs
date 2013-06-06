package slava.puzzle.cardnet.model;

public class CardnetHTML {
	public class Cell {
		public String text;
		public int state;
	}
	
	int leftMargin;
	int realWidth;
	int rightMargin;
	int topMargin;
	int realHeight;
	int bottomMargin;
	int tableWidth;
	int tableHeight;
	int[] xMapping;
	int[] yMapping;
	Cell[][] cells;
	CardnetPuzzleInfo puzzle;
	
	public Cell[][] getTable(CardnetPuzzleInfo puzzle) {
		this.puzzle = puzzle;
		createTableSize();
		cells = new Cell[tableHeight][tableWidth];
		for (int h = 0; h < tableHeight; h++) {
			for (int v = 0; v < tableWidth; v++) {
				cells[h][v] = new Cell();
				cells[h][v].state = -2;
				cells[h][v].text = "";
			}
		}
		fillLeftMargin();
		fillTopMargin();
		fillRightMargin();
		fillBottomMargin();
		fillField();
		return cells;
	}
	
	void createTableSize() {
		xMapping = new int[puzzle.getField().getWidth()];
		for (int v = 0; v < xMapping.length; v++) xMapping[v] = -1;
		yMapping = new int[puzzle.getField().getHeight()];
		for (int h = 0; h < yMapping.length; h++) yMapping[h] = -1;
		for (int p = 0; p < puzzle.getField().getSize(); p++) {
			if(puzzle.getFilterValue(p) == 1) {
				xMapping[puzzle.getField().x(p)] = 1;
				yMapping[puzzle.getField().y(p)] = 1;
			}
		}
		leftMargin = 0;
		for (int h = 0; h < yMapping.length; h++) {
			if(yMapping[h] < 0 || !puzzle.isHNumberSet(h)) continue;
			int c = 0;
			int nc = puzzle.getCardSet().getNumberCount();
			for (int i = 0; i < nc; i++) c += puzzle.getHNumbers(h)[i];
			if(c > leftMargin) leftMargin = c;
		}
		rightMargin = 0;
		for (int h = 0; h < yMapping.length; h++) {
			if(yMapping[h] < 0 || !puzzle.isHSuitSet(h)) continue;
			int c = 0;
			int nc = puzzle.getCardSet().getSuitCount();
			for (int i = 0; i < nc; i++) c += puzzle.getHSuits(h)[i];
			if(c > rightMargin) rightMargin = c;
		}
		topMargin = 0;
		for (int v = 0; v < xMapping.length; v++) {
			if(xMapping[v] < 0 || !puzzle.isVNumberSet(v)) continue;
			int c = 0;
			int nc = puzzle.getCardSet().getNumberCount();
			for (int i = 0; i < nc; i++) c += puzzle.getVNumbers(v)[i];
			if(c > topMargin) topMargin = c;
		}
		bottomMargin = 0;
		for (int v = 0; v < xMapping.length; v++) {
			if(xMapping[v] < 0 || !puzzle.isVSuitSet(v)) continue;
			int c = 0;
			int nc = puzzle.getCardSet().getSuitCount();
			for (int i = 0; i < nc; i++) c += puzzle.getVSuits(v)[i];
			if(c > bottomMargin) bottomMargin = c;
		}

		realWidth = 0;
		int index = leftMargin;
		for (int v = 0; v < xMapping.length; v++) {
			if(xMapping[v] < 0) continue;
			xMapping[v] = index;
			index++;
			realWidth++;
		}
		tableWidth = leftMargin + realWidth + rightMargin;

		realHeight = 0;
		index = topMargin;
		for (int h = 0; h < yMapping.length; h++) {
			if(yMapping[h] < 0) continue;
			yMapping[h] = index;
			index++;
			realHeight++;
		}
		tableHeight = topMargin + realHeight + bottomMargin;
	}
	
	void fillLeftMargin() {
		for (int h = 0; h < yMapping.length; h++) {
			int ht = yMapping[h];
			if(ht < 0 || !puzzle.isHNumberSet(h)) continue;
			int v = leftMargin - 1;
			for (int c = puzzle.getCardSet().getNumberCount() - 1; c >= 0; c--) {
				if(puzzle.getHNumbers(h)[c] == 1) {
					cells[ht][v].state = -1;
					cells[ht][v].text = puzzle.getCardSet().getNumberAsString(c);
					v--;
				}
			}
		}
	}
	
	void fillTopMargin() {
		for (int v = 0; v < xMapping.length; v++) {
			int vt = xMapping[v];
			if(vt < 0 || !puzzle.isVNumberSet(v)) continue;
			int h = topMargin - 1;
			for (int c = puzzle.getCardSet().getNumberCount() - 1; c >= 0; c--) {
				if(puzzle.getVNumbers(v)[c] == 1) {
					cells[h][vt].state = -1;
					cells[h][vt].text = puzzle.getCardSet().getNumberAsString(c);
					h--;
				}
			}
		}
	}
	
	void fillRightMargin() {
		for (int h = 0; h < yMapping.length; h++) {
			int ht = yMapping[h];
			if(ht < 0 || !puzzle.isHSuitSet(h)) continue;
			int v = tableWidth - rightMargin;
			for (int c = 0; c < puzzle.getCardSet().getSuitCount(); c++) {
				if(puzzle.getHSuits(h)[c] == 1) {
					cells[ht][v].state = -1;
					cells[ht][v].text = puzzle.getCardSet().getSuitAsString(c);
					v++;
				}
			}
		}
	}
	
	void fillBottomMargin() {
		for (int v = 0; v < xMapping.length; v++) {
			int vt = xMapping[v];
			if(vt < 0 || !puzzle.isVNumberSet(v)) continue;
			int h = tableHeight - bottomMargin;
			for (int c = 0; c < puzzle.getCardSet().getSuitCount(); c++) {
				if(puzzle.getVSuits(v)[c] == 1) {
					cells[h][vt].state = -1;
					cells[h][vt].text = puzzle.getCardSet().getSuitAsString(c);
					h++;
				}
			}
		}
	}
	
	void fillField() {
		for (int p = 0; p < puzzle.getField().getSize(); p++) {
			int h = puzzle.getField().y(p);
			int v = puzzle.getField().x(p);
			int ht = yMapping[h];
			int vt = xMapping[v];
			if(ht < 0 || vt < 0) continue;
			if(puzzle.getFilterValue(p) == 0) {
				cells[ht][vt].state = 0;
			} else {
				cells[ht][vt].state = 1;
				if(puzzle.hasSolution()) {
					cells[ht][vt].text = puzzle.getCardSet().toString(puzzle.getValue(p));
				}
			}
		}
	}

}
