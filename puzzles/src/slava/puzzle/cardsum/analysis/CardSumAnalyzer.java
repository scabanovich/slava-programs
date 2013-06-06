package slava.puzzle.cardsum.analysis;

import java.util.*;
import slava.puzzle.cardsum.model.CardSumField;

public class CardSumAnalyzer {
	CardSumField field;
	int size;
	
	int[] cards;
	int[] vsum;
	
	int[][] neighbours;
	int[][] vneighbours;
	
	int t;
	int[] place;	
	int[] wayCount;
	int[][] ways;
	int[] way;
	
	int tLimit;
	int solutionCount;
	int[] solution;
	int solutionLimit;
	boolean randomizing;

	int[][] cardDistribution; 
	int[][] sumDistribution; 
	
	public void setField(CardSumField field) {
		this.field = field;
	}
	
	public void setSolutionLimit(int k) {
		solutionLimit = k;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		size = field.size();
		tLimit = 0;
		cards = new int[size];
		for (int i = 0; i < size; i++) {
			cards[i] = field.getCard(i);
			if(cards[i] < 0) ++tLimit;
		}
		initQuickReferences();
		wayCount = new int[tLimit + 1];
		ways = new int[tLimit + 1][9];
		way = new int[tLimit + 1];
		place = new int[tLimit + 1];
		t = 0;
		solutionCount = 0;
		solution = null;
		cardDistribution = new int[size][field.getWidth()];
		for (int i = 0; i < size; i++) for (int v = 0; v < field.getWidth(); v++) {
			cardDistribution[i][v] = 0;
		}
		sumDistribution = new int[size][50];
		for (int i = 0; i < size; i++) for (int s = 0; s < 50; s++) {
			sumDistribution[i][s] = 0;
		}
	}
	
	void initQuickReferences() {
		vsum = new int[field.getWidth()];
		for (int i = 0; i < size; i++) {
			int x = field.x(i);
			int v = field.getValueAt(i);
			if(v > 0) vsum[x] += v;
		}
		neighbours = new int[size][];
		for (int i = 0; i < size; i++) {
			ArrayList l = new ArrayList();
			for (int d = 0; d < 4; d++) appendNeighbours(l, i, d);
			for (int d = 4; d < 8; d++) {
				int q = field.jump(i, d);
				if(q >= 0) l.add(new Integer(q));
			}
			neighbours[i] = new int[l.size()];
			for (int k = 0; k < l.size(); k++) neighbours[i][k] = ((Integer)l.get(k)).intValue();
		}
		vneighbours = new int[size][];
		for (int i = 0; i < size; i++) {
			ArrayList l = new ArrayList();
			appendNeighbours(l, i, 1);
			appendNeighbours(l, i, 3);
			vneighbours[i] = new int[l.size()];
			for (int k = 0; k < l.size(); k++) vneighbours[i][k] = ((Integer)l.get(k)).intValue();
		}
	}
	
	void appendNeighbours(ArrayList list, int place, int d) {
		int q = field.jump(place, d);
		while(q >= 0) {
			list.add(new Integer(q));
			q = field.jump(q, d);
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tLimit) return;
		place[t] = -1;
		int c = 10;
		for (int p = 0; p < size; p++) {
			if(cards[p] >= 0) continue;
			int cp =  getWayCount(p);
			if(cp < c) {
				c = cp;
				place[t] = p;
				for (int i = 0; i < c; i++) ways[t][i] = temp[i];
			}
		}
		if(c == 10) return;
		wayCount[t] = c;
		randomize();		
	}
	
	int[] temp = new int[9];
	
	private int getWayCount(int p) {
		int c = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			if(checkNeighbours(p, i) && checkSum(p, i)) {
				temp[c] = i;
				c++;
			}
		}
		return c;
	}
	
	int[] mins = new int[]{0, 2,5,9,15};
	int[] maxs = new int[]{0, 11,21,30,38};
	
	boolean checkNeighbours(int p, int v) {
		for(int i = 0; i < neighbours[p].length; i++) {
			if(v == cards[neighbours[p][i]]) return false;
		}
		return true;
	}
	
	boolean checkSum(int p, int card) {
		int x = field.x(p);
		int xsum = field.getSum(x);
		if(xsum < 0) return true;
		int empty = 0;
		int sum = field.getValue(card);
		for(int i = 0; i < vneighbours[p].length; i++) {
			int ncard = cards[vneighbours[p][i]];
			if(ncard < 0) {
				empty++;
			} else {
				sum += field.getValue(ncard);
			}
		}
		return (xsum >= sum + mins[empty] && xsum <= sum + maxs[empty]);
	}
	
	void randomize() {
		if(wayCount[t] < 2 || !randomizing) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(i == j) continue;
			int k = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = k;
		}
	}
	
	void move() {
		int p = place[t];
		int k = ways[t][way[t]];
		cards[p] = k;
		int x = field.x(p);
		vsum[x] += field.getValue(cards[p]);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;		
		int p = place[t];
		int x = field.x(p);
		vsum[x] -= field.getValue(cards[p]);
		cards[p] = -1;
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
			if(t == tLimit) {
				++solutionCount;
				if(solutionCount == 1) {
					solution = (int[])cards.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		for (int i = 0; i < size; i++) cardDistribution[i][cards[i]]++;
		for (int x = 0; x < field.getWidth(); x++) sumDistribution[x][vsum[x]]++;
	}

	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int[][] getCardDistribution() {
		return cardDistribution;
	}
	
	public int[][] getSumDistribution() {
		return sumDistribution;
	}
	
	void printSolution() {
		for (int i = 0; i < size; i++) {
			if(cards[i] > 0) System.out.print(" " + cards[i]);
		}
		System.out.println("");
	}

}
