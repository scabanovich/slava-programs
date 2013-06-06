package slava.puzzle.cardnet.analysis;

import slava.puzzle.cardnet.model.CardSet;
import slava.puzzle.cardnet.model.CardnetPuzzleInfo;

public class CardnetAnalyzer {
	CardnetPuzzleInfo puzzle;
	int[] restrictingNumbers;
	int[] restrictingSuits;
	
	int[] values;
	int[] usedCards;
	
	int cellsToFill;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;	
	
	boolean isConsideringLineInfo = true;
	int solutionLimit;
	boolean isRandomizing;
	int wayCountLimit = -1;
	int[] solution;
	int solutionCount;
	
	public void setPuzzleInfo(CardnetPuzzleInfo puzzle) {
		this.puzzle = puzzle;
	}
	
	public void setRestrictions(int[] numbers, int[] suits) {
		restrictingNumbers = numbers;
		restrictingSuits = suits;		
	}
	
	public void setSolutionLimit(int solutionLimit) {
		this.solutionLimit = solutionLimit;
	}
	
	public void setRandomizing(boolean b) {
		isRandomizing = b;
	}
	
	public void setWayCountLimit(int n) {
		wayCountLimit = n;
	}
	
	public void setConsideringLineInfo(boolean b) {
		isConsideringLineInfo = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		values = new int[puzzle.getField().getSize()];
		for (int p = 0; p < values.length; p++) values[p] = -1;
		usedCards = new int[puzzle.getCardSet().getSize()];
		cellsToFill = 0;
		for (int p = 0; p < values.length; p++) {
			if(puzzle.getFilterValue(p) == 1) ++cellsToFill;
		}
		place = new int[cellsToFill + 1];
		wayCount = new int[cellsToFill + 1];
		way = new int[cellsToFill + 1];
		ways = new int[cellsToFill + 1][puzzle.getCardSet().getSize()];
		tempWays = new int[puzzle.getCardSet().getSize()];
		solutionCount = 0;
		solution = null;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(cellsToFill == 0) return;
		int wcm = 1000;
		place[t] = -1;
		for (int p = 0; p < values.length; p++) {
			if(puzzle.getFilterValue(p) != 1) continue;
			if(values[p] != -1) continue;
			int wc = getWayCount(p);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				place[t] = p;
				for (int i = 0; i < wc; i++) ways[t][i] = tempWays[i];
			}
		}
		if(place[t] >= 0) wayCount[t] = wcm;
		if(wayCount[t] > 1 && isRandomizing) {
			randomize();
		}
	}
	
	void randomize() {
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			int q = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = q;
		}
		if(wayCountLimit > 0 && wayCount[t] > wayCountLimit) wayCount[t] = wayCountLimit;
	}
	
	int[] tempWays;
	
	int getWayCount(int p) {
		CardSet cards = puzzle.getCardSet();
		int h = puzzle.getField().y(p);
		int v = puzzle.getField().x(p);
		int wc = 0;
		for (int c = 0; c < usedCards.length; c++) {
			if(usedCards[c] == 1) continue;
			int n = cards.getNumber(c);
			int s = cards.getSuit(c);
			if(restrictingNumbers != null && restrictingNumbers[p] >= 0 && restrictingNumbers[p] != n) continue;
			if(restrictingSuits != null && restrictingSuits[p] >= 0 && restrictingSuits[p] != s) continue;
			boolean ok = true;
			for (int d = 0; d < 8 && ok; d++) {
				int q = puzzle.getField().jp(d, p);
				if(q < 0 || values[q] < 0) continue;
				if(n == cards.getNumber(values[q]) || 
				  s == cards.getSuit(values[q])) ok = false;
			}
			if(!ok) continue;
			if(isConsideringLineInfo) {
				if(puzzle.isHNumberSet(h) && puzzle.getHNumbers(h)[n] != 1) continue;
				if(puzzle.isVNumberSet(v) && puzzle.getVNumbers(v)[n] != 1) continue;
				if(puzzle.isHSuitSet(h) && puzzle.getHSuits(h)[s] != 1) continue;
				if(puzzle.isVSuitSet(v) && puzzle.getVSuits(v)[s] != 1) continue;
				values[p] = c;
				ok = checkHNumbers(h) && checkHSuits(h) && checkVNumbers(v) && checkVSuits(v);
				values[p] = -1;
			}
			if(ok) {
				tempWays[wc] = c;
				++wc;
			}
		}
		return wc;
	}
	
	boolean checkHNumbers(int h) {
		if(!puzzle.isHNumberSet(h)) return true;
		int nc = puzzle.getCardSet().getNumberCount();
		int[] usage = new int[nc];
		int empty = 0;
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) != 1) continue;
			if(values[p] < 0) {
				empty++;
			} else {
				int n = puzzle.getCardSet().getNumber(values[p]);
				usage[n] = 1;
			}
		}
		int unused = 0;
		for (int i = 0; i < nc; i++) {
			if(usage[i] == 0 && puzzle.getHNumbers(h)[i] == 1) unused++;
		}
		return unused <= empty;
	}
	boolean checkVNumbers(int v) {
		if(!puzzle.isVNumberSet(v)) return true;
		int nc = puzzle.getCardSet().getNumberCount();
		int[] usage = new int[nc];
		int empty = 0;
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) != 1) continue;
			if(values[p] < 0) {
				empty++;
			} else {
				int n = puzzle.getCardSet().getNumber(values[p]);
				usage[n] = 1;
			}
		}
		int unused = 0;
		for (int i = 0; i < nc; i++) {
			if(usage[i] == 0 && puzzle.getVNumbers(v)[i] == 1) unused++;
		}
		return unused <= empty;
	}
	boolean checkHSuits(int h) {
		if(!puzzle.isHSuitSet(h)) return true;
		int nc = puzzle.getCardSet().getSuitCount();
		int[] usage = new int[nc];
		int empty = 0;
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) != 1) continue;
			if(values[p] < 0) {
				empty++;
			} else {
				int s = puzzle.getCardSet().getSuit(values[p]);
				usage[s] = 1;
			}
		}
		int unused = 0;
		for (int i = 0; i < nc; i++) {
			if(usage[i] == 0 && puzzle.getHSuits(h)[i] == 1) unused++;
		}
		return unused <= empty;
	}
	boolean checkVSuits(int v) {
		if(!puzzle.isVSuitSet(v)) return true;
		int nc = puzzle.getCardSet().getSuitCount();
		int[] usage = new int[nc];
		int empty = 0;
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) != 1) continue;
			if(values[p] < 0) {
				empty++;
			} else {
				int s = puzzle.getCardSet().getSuit(values[p]);
				usage[s] = 1;
			}
		}
		int unused = 0;
		for (int i = 0; i < nc; i++) {
			if(usage[i] == 0 && puzzle.getVSuits(v)[i] == 1) unused++;
		}
		return unused <= empty;
	}
	
	void move() {
		int p = place[t];
		int c = ways[t][way[t]];
		values[p] = c;
		usedCards[c] = 1;
		cellsToFill--;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int c = ways[t][way[t]];
		values[p] = -1;
		usedCards[c] = 0;
		cellsToFill++;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(cellsToFill == 0) {
				if(solution == null) solution = (int[])values.clone();
				++solutionCount;
				if(solutionCount >= solutionLimit) return;
				
			}
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}

}
