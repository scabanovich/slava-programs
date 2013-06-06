package slava.puzzle.cardnet.analysis;

import slava.puzzle.cardnet.model.CardnetPuzzleInfo;

public class CardnetLogicalAnalyzer {
	CardnetPuzzleInfo puzzle;
	int[] numbers;
	int[] suits;
	int[] values;
	
	int[][] allowedNumbers;
	int[][] allowedSuits;

	int[] usedCards;
	int unusedCardsCount;
	int[] unusedNumberCount;
	int[] unusedSuitCount;
	
	int wayCount;
	int[] ways;
	
	boolean isUnsolvable = false;	
	
	public void setPuzzleInfo(CardnetPuzzleInfo puzzle) {
		this.puzzle = puzzle;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int size = puzzle.getField().getSize();
		numbers = new int[size];
		suits = new int[size];
		values = new int[size];
		for (int p = 0; p < size; p++) {
			numbers[p] = -1;
			suits[p] = -1;
			values[p] = -1;
		}
		unusedNumberCount = new int[puzzle.getCardSet().getNumberCount()];
		for (int n = 0; n < unusedNumberCount.length; n++) unusedNumberCount[n] = puzzle.getCardSet().getSuitCount(); 
		unusedSuitCount = new int[puzzle.getCardSet().getSuitCount()];
		for (int s = 0; s < unusedSuitCount.length; s++) unusedSuitCount[s] = puzzle.getCardSet().getNumberCount(); 
		usedCards = new int[puzzle.getCardSet().getSize()];
		unusedCardsCount = 0;
		for (int q = 0; q < size; q++) unusedCardsCount += puzzle.getFilterValue(q);
		allowedNumbers = new int[size][puzzle.getCardSet().getNumberCount()];
		for (int p = 0; p < size; p++) {
			int h = puzzle.getField().y(p);
			int v = puzzle.getField().x(p);
			for (int n = 0; n < allowedNumbers[0].length; n++) {
				allowedNumbers[p][n] = 0;
				if(puzzle.getFilterValue(p) == 0) continue;
				if(puzzle.isHNumberSet(h) && puzzle.getHNumbers(h)[n] != 1) continue;
				if(puzzle.isVNumberSet(v) && puzzle.getVNumbers(v)[n] != 1) continue;
				allowedNumbers[p][n] = 1;
			}
		}
		allowedSuits = new int[size][puzzle.getCardSet().getSuitCount()];
		for (int p = 0; p < size; p++) {
			int h = puzzle.getField().y(p);
			int v = puzzle.getField().x(p);
			for (int s = 0; s < allowedSuits[0].length; s++) {
				allowedSuits[p][s] = 0;
				if(puzzle.getFilterValue(p) == 0) continue;
				if(puzzle.isHSuitSet(h) && puzzle.getHSuits(h)[s] != 1) continue;
				if(puzzle.isVSuitSet(v) && puzzle.getVSuits(v)[s] != 1) continue;
				allowedSuits[p][s] = 1;
			}
		}
		ways = new int[1000];
	}
	
	int t;
	void anal() {
		t = 0;
		while (true) {
			int u = unusedCardsCount;
			++t;
			checkPlaces();
			if(isFinished()) break;
			++t;
			checkHLines();
			if(isFinished()) break;
			++t;
			checkVLines();
			if(isFinished()) break;
			if(u == unusedCardsCount) break;
		}
		System.out.println("Complexity=" + t);
	}
	
	void addNumber(int p, int n) {
		numbers[p] = n;
		unusedNumberCount[n]--;
		if(unusedNumberCount[n] == 0) {
			for (int q = 0; q < puzzle.getField().getSize(); q++) {
				allowedNumbers[q][n] = 0;
			}
		}
		if(suits[p] >= 0) {
			int c = puzzle.getCardSet().getCard(n, suits[p]);
			cardAdded(p, c);
		} else {
			for (int q = 0; q < puzzle.getField().getSize(); q++) {
				if(numbers[q] == numbers[p] && suits[q] >= 0) allowedSuits[p][suits[q]] = 0;
			}
		}
		for (int d = 0; d < 8; d++) {
			int q = puzzle.getField().jp(d, p);
			if(q < 0) continue;
			allowedNumbers[q][n] = 0;
		}
	}
	
	void addSuit(int p, int s) {
		suits[p] = s;
		unusedSuitCount[s]--;
		if(unusedSuitCount[s] == 0) {
			for (int q = 0; q < puzzle.getField().getSize(); q++) {
				allowedSuits[q][s] = 0;
			}
		}
		if(numbers[p] >= 0) {
			int c = puzzle.getCardSet().getCard(numbers[p], s);
			cardAdded(p, c);
		} else {
			for (int q = 0; q < puzzle.getField().getSize(); q++) {
				if(suits[q] == suits[p] && numbers[q] >= 0) allowedNumbers[p][numbers[q]] = 0;
			}
		}
		for (int d = 0; d < 8; d++) {
			int q = puzzle.getField().jp(d, p);
			if(q < 0) continue;
			allowedSuits[q][s] = 0;
		}
	}
	
	void cardAdded(int p, int c) {
		usedCards[c] = 1;
		unusedCardsCount--;
		values[p] = c;
		for (int q = 0; q < puzzle.getField().getSize(); q++) {
			if(suits[q] == suits[p] && q != p) allowedNumbers[q][numbers[p]] = 0;
			if(numbers[q] == numbers[p] && q != p) allowedSuits[q][suits[p]] = 0;
		}
	}
	
	void checkPlaces() {
		if(t == 49) {
			//int q = 0;
		}
		for (int p = 0; p < puzzle.getField().getSize() && !isFinished(); p++) {
			if(numbers[p] >= 0 || puzzle.getFilterValue(p) == 0) continue;
			int wc = getNumberWayCount(p);
			if(wc == 0) {
				isUnsolvable = true;
				break;
			} else if(wc == 1) {
				addNumber(p, ways[0]);
			}
		}
		for (int p = 0; p < puzzle.getField().getSize() && !isFinished(); p++) {
			if(suits[p] >= 0 || puzzle.getFilterValue(p) == 0) continue;
			int wc = getSuitWayCount(p);
			if(wc == 0) {
				isUnsolvable = true;
				break;
			} else if(wc == 1) {
				addSuit(p, ways[0]);
			}
		}
	}
	
	int getNumberWayCount(int p) {
		int wc = 0;
		for (int n = 0; n < unusedNumberCount.length; n++) {
			if(allowedNumbers[p][n] == 0) continue;
			ways[wc] = n;
			++wc;
		}
		return wc;
	}
	
	int getSuitWayCount(int p) {
		int wc = 0;
		for (int s = 0; s < unusedSuitCount.length; s++) {
			if(allowedSuits[p][s] == 0) continue;
			ways[wc] = s;
			++wc;
		}
		return wc;
	}
	
	void checkHLines() {
		if(t == 49) {
			//int q = 0;
		}
		for (int h = 0; h < puzzle.getField().getHeight() && !isFinished(); h++) {
			if(!puzzle.isHNumberSet(h)) continue;
			for (int n = 0; n < unusedNumberCount.length; n++) {
				if(puzzle.getHNumbers(h)[n] == 0 || isHNumberUsed(h, n) ) continue;
				int wc = getHNumberWayCount(h, n);
				if(wc == 0) {
					isUnsolvable = true;
					break;
				} else if(wc == 1) {
					addNumber(ways[0], n);
				}
			}
		}
		for (int h = 0; h < puzzle.getField().getHeight() && !isFinished(); h++) {
			if(!puzzle.isHSuitSet(h)) continue;
			for (int s = 0; s < unusedSuitCount.length; s++) {
				if(puzzle.getHSuits(h)[s] == 0 || isHSuitUsed(h, s)) continue;
				int wc = getHSuitWayCount(h, s);
				if(wc == 0) {
					isUnsolvable = true;
					break;
				} else if(wc == 1) {
					addSuit(ways[0], s);
				}
			}
		}
	}
	boolean isHNumberUsed(int h, int n) {
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(numbers[p] == n) return true;
		}
		return false;
	}
	int getHNumberWayCount(int h, int n) {
		int wc = 0;
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) == 0 || numbers[p] >= 0 || allowedNumbers[p][n] == 0) continue;
			ways[wc] = p;
			++wc;
		}
		return wc;
	}
	boolean isHSuitUsed(int h, int s) {
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(suits[p] == s) return true;
		}
		return false;
	}
	int getHSuitWayCount(int h, int s) {
		int wc = 0;
		for (int v = 0; v < puzzle.getField().getWidth(); v++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) == 0 || suits[p] >= 0 || allowedSuits[p][s] == 0) continue;
			ways[wc] = p;
			++wc;
		}
		return wc;
	}
	

	void checkVLines() {
		if(t == 49) {
			//int q = 0;
		}
		for (int v = 0; v < puzzle.getField().getWidth() && !isFinished(); v++) {
			if(!puzzle.isVNumberSet(v)) continue;
			for (int n = 0; n < unusedNumberCount.length; n++) {
				if(puzzle.getVNumbers(v)[n] == 0 || isVNumberUsed(v, n) ) continue;
				int wc = getVNumberWayCount(v, n);
				if(wc == 0) {
					isUnsolvable = true;
					break;
				} else if(wc == 1) {
					addNumber(ways[0], n);
				}
			}
		}
		for (int v = 0; v < puzzle.getField().getWidth() && !isFinished(); v++) {
			if(!puzzle.isVSuitSet(v)) continue;
			for (int s = 0; s < unusedSuitCount.length; s++) {
				if(puzzle.getVSuits(v)[s] == 0 || isVSuitUsed(v, s)) continue;
				int wc = getVSuitWayCount(v, s);
				if(wc == 0) {
					isUnsolvable = true;
					break;
				} else if(wc == 1) {
					addSuit(ways[0], s);
				}
			}
		}
	}
	boolean isVNumberUsed(int v, int n) {
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(numbers[p] == n) return true;
		}
		return false;
	}
	int getVNumberWayCount(int v, int n) {
		int wc = 0;
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) == 0 || numbers[p] >= 0 || allowedNumbers[p][n] == 0) continue;
			ways[wc] = p;
			++wc;
		}
		return wc;
	}
	boolean isVSuitUsed(int v, int s) {
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(suits[p] == s) return true;
		}
		return false;
	}
	int getVSuitWayCount(int v, int s) {
		int wc = 0;
		for (int h = 0; h < puzzle.getField().getHeight(); h++) {
			int p = puzzle.getField().getIndex(h, v);
			if(puzzle.getFilterValue(p) == 0 || suits[p] >= 0 || allowedSuits[p][s] == 0) continue;
			ways[wc] = p;
			++wc;
		}
		return wc;
	}

	boolean isFinished() {
		return isUnsolvable || isSolutionFound();
	}
	
	public boolean isUnsolvable() {
		return isUnsolvable;
	}
	
	public boolean isSolutionFound() {
		return unusedCardsCount == 0;
	}
	
	public int[] getSolution() {
		return values;
	}
	
	public int getComplexity() {
		return t;
	}
	
}
