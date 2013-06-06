package slava.puzzle.cardnet.model;

public class CardnetPuzzleInfo {
	CardnetField field;
	CardSet cards;
	int[] fieldFilter;
	int[] values;
	int[] hNumberIsSet;
	int[][] hNumbers;
	int[] vNumberIsSet;
	int[][] vNumbers;
	int[] hSuitIsSet;
	int[][] hSuits;
	int[] vSuitIsSet;
	int[][] vSuits;
	boolean hasProblem = false;
	boolean hasSolution = false;
	
	public CardnetPuzzleInfo(CardnetField field, CardSet cards) {
		this.field = field;
		this.cards = cards;
		revalidate();
	}
	
	public CardnetField getField() {
		return field;
	}
	
	public CardSet getCardSet() {
		return cards;
	}
	
	void revalidate() {
		fieldFilter = new int[field.getSize()];
		values = new int[field.getSize()];
		hNumberIsSet = new int[field.getHeight()];
		hNumbers = new int[field.getHeight()][cards.getNumberCount()];
		vNumberIsSet = new int[field.getWidth()];
		vNumbers = new int[field.getWidth()][cards.getNumberCount()];
		hSuitIsSet = new int[field.getHeight()];
		hSuits = new int[field.getHeight()][cards.getSuitCount()];
		vSuitIsSet = new int[field.getWidth()];
		vSuits = new int[field.getWidth()][cards.getSuitCount()];
		clearProblem();
	}
	
	public int getFilterValue(int p) {
		return fieldFilter[p];
	}
	
	public void setFilterValue(int p, int v) {
		fieldFilter[p] = v;
	}
	
	public boolean hasProblem() {
		return hasProblem;
	}
	
	public boolean hasSolution() {
		return hasSolution;
	}
	
	public void setProblem() {
		hasProblem = true;
	}
	
	public void clearProblem() {
		if(!hasProblem) return;
		for (int i = 0; i < values.length; i++) values[i] = -1;
		for (int h = 0; h < hNumberIsSet.length; h++) hNumberIsSet[h] = 0;
		for (int h = 0; h < hNumbers.length; h++) {
			for (int n = 0; n < cards.getNumberCount(); n++) hNumbers[h][n] = 0;
		}
		for (int v = 0; v < vNumberIsSet.length; v++) vNumberIsSet[v] = 0;
		for (int v = 0; v < vNumbers.length; v++) {
			for (int n = 0; n < cards.getNumberCount(); n++) vNumbers[v][n] = 0;
		}
		for (int h = 0; h < hSuitIsSet.length; h++) hSuitIsSet[h] = 0;
		for (int h = 0; h < hSuits.length; h++) {
			for (int n = 0; n < cards.getSuitCount(); n++) hSuits[h][n] = 0;
		}
		for (int v = 0; v < vSuitIsSet.length; v++) vSuitIsSet[v] = 0;
		for (int v = 0; v < vSuits.length; v++) {
			for (int n = 0; n < cards.getSuitCount(); n++) vSuits[v][n] = 0;
		}
		hasProblem = false;
		hasSolution = false;
	}

	public boolean isHNumberSet(int h) {
		return hNumberIsSet[h] == 1;
	}
	
	public boolean isVNumberSet(int v) {
		return vNumberIsSet[v] == 1;
	}
	
	public boolean isHSuitSet(int h) {
		return hSuitIsSet[h] == 1;
	}
	
	public boolean isVSuitSet(int v) {
		return vSuitIsSet[v] == 1;
	}
	
	public int[] getHNumbers(int h) {
		return hNumbers[h];
	}
	
	public int[] getVNumbers(int v) {
		return vNumbers[v];
	}
	
	public int[] getHSuits(int h) {
		return hSuits[h];
	}
	
	public int[] getVSuits(int v) {
		return vSuits[v];
	}
	
	public int getValue(int p) {
		return values[p];
	}
	
	public void makeProblemForValues(int[] values) {
		for (int i = 0; i < this.values.length; i++) this.values[i] = values[i];
		hasProblem = true;
		hasSolution = true;
		for (int p = 0; p < field.getSize(); p++) {
			int h = field.y(p);
			int v = field.x(p);
			int c = values[p];
			if(c < 0) continue;
			int n = cards.getNumber(c);
			int s = cards.getSuit(c);
			hNumbers[h][n] = 1;
			vNumbers[v][n] = 1;
			hSuits[h][s] = 1;
			vSuits[v][s] = 1;
			hNumberIsSet[h] = 1;
			hSuitIsSet[h] = 1;
			vNumberIsSet[v] = 1;
			vSuitIsSet[v] = 1;
		}
	}
	
	public void clearSolution() {
		if(!hasSolution) return;
		hasSolution = false;
	}
	
	public void setSolution(int[] values) {
		hasSolution = values != null;
		if(hasSolution) for (int i = 0; i < this.values.length; i++) this.values[i] = values[i];
	}
	
	public void setHNumberSet(int h, boolean b) {
		hNumberIsSet[h] = (b) ? 1 : 0;
	}
	
	public void setVNumberSet(int v, boolean b) {
		vNumberIsSet[v] = (b) ? 1 : 0;
	}
	
	public void setHSuitSet(int h, boolean b) {
		hSuitIsSet[h] = (b) ? 1 : 0;
	}
	
	public void setVSuitSet(int v, boolean b) {
		vSuitIsSet[v] = (b) ? 1 : 0;
	}
}
