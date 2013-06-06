package ik6_1;

public class Cards_6_1_Solver {
	Cards_6_1_Field field;
	
	int[] state;
	int[] cardUsage;
	int[][] cardRestriction; //[p][v]
	
	int[] temp;
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public Cards_6_1_Solver() {}
	
	public void setField(Cards_6_1_Field field) {
		this.field = field;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.size];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		cardUsage = new int[field.cardCount];
		cardRestriction = new int[field.size][field.cardCount];
		initRestrictions();
		
		place = new int[field.size + 1];
		wayCount = new int[field.size + 1];
		way = new int[field.size + 1];
		ways = new int[field.size + 1][field.cardCount];
		temp = new int[field.cardCount];
		
		t = 0;
		solutionCount = 0;
	}
	
	void initRestrictions() {
		for (int i = 0; i < field.sets.length; i++) {
			Cards_6_1_CellSet set = field.sets[i];
			for (int c = 0; c < field.cardCount; c++) {
				if(set.kind == Cards_6_1_CellSet.SUIT_KIND) {
					if(set.usage[field.cardSuit[c]] == 1) continue;
				} else {
					if(set.usage[field.cardValue[c]] == 1) continue;
				}
				for (int j = 0; j < set.cells.length; j++) {
//					System.out.println(set.cells[j] + ":" + c);
					cardRestriction[set.cells[j]][c]++;
				}
			}
		}		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.size) return;
		if(!checkSets()) return;
		place[t] = -1;
		int wcb = field.cardCount + 1;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			int wc = computeWayCount(p);
			if(wc < wcb) {
				if(wc == 0) return;
				wcb = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(place[t] >= 0) wayCount[t] = wcb;
	}
	
	boolean checkSets() {
		for (int i = 0; i < field.sets.length; i++) {
			if(!checkSet(field.sets[i])) return false;
		}
		return true;
	}
	
	boolean checkSet(Cards_6_1_CellSet set) {
		return set.kind == Cards_6_1_CellSet.SUIT_KIND 
			? checkSuitSet(set.cells, set.usage)
			: checkValueSet(set.cells, set.usage);
	}

	boolean checkValueSet(int[] cells, int[] usage) {
		int f = 0;
		int[] cus = new int[field.valueCount];
		for (int i = 0; i < cells.length; i++) {
			int p = cells[i];
			if(state[p] < 0) ++f; else cus[field.cardValue[state[p]]]++;
		}
		for (int i = 0; i < field.valueCount; i++) {
			if(usage[i] > 0) {
				if(cus[i] == 0) --f;
			} else {
				if(cus[i] > 0) return false;
			}
		}		
		return f >= 0;
	}

	boolean checkSuitSet(int[] cells, int[] usage) {
		int f = 0;
		int[] cus = new int[field.suitCount];
		for (int i = 0; i < cells.length; i++) {
			int p = cells[i];
			if(state[p] < 0) ++f; else cus[field.cardSuit[state[p]]]++;
		}
		for (int i = 0; i < field.suitCount; i++) {
			if(usage[i] > 0) {
				if(cus[i] == 0) --f;
			} else {
				if(cus[i] > 0) return false;
			}
		}		
		return f >= 0;
	}

	int computeWayCount(int p) {
		int wc = 0;
		for (int c = 0; c < field.cardCount; c++) {
			if(cardUsage[c] > 0 || cardRestriction[p][c] > 0) continue;
			temp[wc] = c;
			++wc;
		}
		return wc;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		cardUsage[v]++;
		state[p] = v;
		int[] ns = field.neighbours[p];
		for (int c = 0; c < field.cardCount; c++) if(field.areCloseCards(v, c)) {
			for (int i = 0; i < ns.length; i++) cardRestriction[ns[i]][c]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		cardUsage[v]--;
		state[p] = -1;
		int[] ns = field.neighbours[p];
		for (int c = 0; c < field.cardCount; c++) if(field.areCloseCards(v, c)) {
			for (int i = 0; i < ns.length; i++) cardRestriction[ns[i]][c]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(t);
			}
			if(t == field.size && checkSets()) {
				solutionCount++;
				printSolution();
			}			
		}		
		
	}
	
	int[] codePlaces = new int[]{33,27,21,15,9,3};
	
	void printSolution() {
		System.out.println("");
		for (int p = 0; p < field.size; p++) {
			System.out.print(" " + field.getCardName(state[p]));
			
		}
		System.out.println("");
		String code = "";
		for (int i = 0; i < codePlaces.length; i++) {
			if(i > 0) code += ",";
			code += field.getCardName(state[codePlaces[i]]);
		}
		System.out.println("Code=" + code);
	}

}
