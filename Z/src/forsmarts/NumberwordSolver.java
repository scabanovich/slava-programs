package forsmarts;

import com.slava.common.RectangularField;

public class NumberwordSolver {
	RectangularField field;
	String[] words;
	int[][] nwords;
	int[] letterIndices;
	int[] scoring;

	int letterCount;
	
	int[] state;
	int[][] hUsage; //[row][letter]
	int[][] vUsage; //[column][letter]
	int[] wordUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] cursor;
	
	int currentScoring;
	int maxScoring;
	
	public NumberwordSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setWords(String[] words, int[] scoring) {
		this.words = words;
		this.scoring = scoring;
		createNWords();
	}
	
	void createNWords() {
		nwords = new int[words.length][];
		letterIndices = new int[256];
		for (int i = 0; i < letterIndices.length; i++) letterIndices[i] = -1;
		int index = 0;
		for (int i = 0; i < words.length; i++) {
			nwords[i] = new int[words[i].length()];
			for (int j = 0; j < nwords[i].length; j++) {
				int q = (int)words[i].charAt(j);
				if(letterIndices[q] < 0) {
					letterIndices[q] = index;
					index++;
				}
				nwords[i][j] = letterIndices[q];
//				System.out.print(" " + nwords[i][j]);
			}
//			System.out.println("");
		}
		letterCount = index;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		hUsage = new int[field.getHeight()][letterCount];
		vUsage = new int[field.getHeight()][letterCount];
		wordUsage = new int[nwords.length];
		if(words[0].length() == 0) wordUsage[0] = 1;
		
		wayCount = new int[nwords.length];
		way = new int[nwords.length];
		ways = new int[nwords.length][nwords.length];
		cursor = new int[nwords.length];
		cursor[0] = 0;
		t = 0;
		currentScoring = 0;
		maxScoring = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		for (int n = 0; n < nwords.length; n++) {
			if(wordUsage[n] > 0) continue;
			boolean ok = add(n);
			remove(n);
			if(ok) {
				ways[t][wayCount[t]] = n;
				wayCount[t]++;
			}
		}
	}
	
	void move() {
		int n = ways[t][way[t]];
		add(n);
		++t;
		srch();
		way[t] = -1;
	}
	
	boolean add(int n) {
		int[] nword = nwords[n];
		int c = cursor[t];
		wordUsage[n]++;
		currentScoring += scoring[n];
		boolean result = true;
		for (int i = 0; i < nword.length; i++) {
			state[c] = nword[i];
			hUsage[field.getY(c)][nword[i]]++;
			if(hUsage[field.getY(c)][nword[i]] > 1) result = false;
			vUsage[field.getX(c)][nword[i]]++;
			if(vUsage[field.getX(c)][nword[i]] > 1) result = false;
			c++;
		}
		cursor[t + 1] = c;
		return result;
	}
	
	void back() {
		--t;
		int n = ways[t][way[t]];
		remove(n);
	}
	
	void remove(int n) {
		int[] nword = nwords[n];
		int c = cursor[t];
		wordUsage[n]--;
		currentScoring -= scoring[n];
		for (int i = 0; i < nword.length; i++) {
			state[c] = -1;
			hUsage[field.getY(c)][nword[i]]--;
			vUsage[field.getX(c)][nword[i]]--;
			c++;
		}
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
			if(wayCount[t] == 0 && currentScoring > maxScoring) {
				maxScoring = currentScoring;
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Width=" +  field.getWidth() + " Scoring " + maxScoring + " FilledCells=" + cursor[t]);
		for (int i = 0; i < t; i++) {
			int n = ways[i][way[i]];
			System.out.print(" " + n);
		}
		System.out.println("");
	}

}
