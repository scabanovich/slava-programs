package olia;

import java.util.*;

import com.slava.common.RectangularField;

public class TwoWordsSolver {
	RectangularField field;
	
	String[] words;
	Map charValues = new HashMap();
	char[] letters;
	char[] initialFilling;
	
	int printSolutionLimit = 3;
	
	int[][] digitWords;
	
	int[] state;
	int[] usage;
	int[][] wState;

	int stepCount;
	int[] wordForStep;
	int[] letterForStep;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int[] solution;
	
	public TwoWordsSolver() {
		field = new RectangularField();
		field.setSize(4, 4);
	}
	
	public void setWords(String[] words) {
		this.words = words;
		digitWords = new int[words.length][];
		List ls = new ArrayList();
		for (int i = 0; i < words.length; i++) {
			digitWords[i] = new int[words[i].length()];
			for (int j = 0; j < words[i].length(); j++) {
				char c = words[i].charAt(j);
				Character ch = new Character(c);
				if(!charValues.containsKey(ch)) {
					int k = ls.size();
					charValues.put(ch, new Integer(k));
					ls.add(ch);
				}
				Integer q = (Integer)charValues.get(ch);
				digitWords[i][j] = q.intValue();
			}
		}
		Character[] cs = (Character[])ls.toArray(new Character[0]);
		letters = new char[cs.length];
		for (int i = 0; i < cs.length; i++) letters[i] = cs[i].charValue();
		
		stepCount = 0;
		for (int i = 0; i < digitWords.length; i++) {
			stepCount += digitWords[i].length;
		}
		
		wordForStep = new int[stepCount];
		letterForStep = new int[stepCount];
		int step = 0;
		for (int i = 0; i < digitWords.length; i++) {
			for (int j = 0; j < digitWords[i].length; j++) {
				wordForStep[step] = i;
				letterForStep[step] = digitWords[i][j];
				step++;
			}
		}		
	}
	
	int getLetterIndex(char c) {
		Integer q = (Integer)charValues.get(new Character(c));
		return q == null ? -1 : q.intValue();
	}
	
	char getLetter(int index) {
		return letters[index];
	}

	public void setInitialFilling(char[] cs) {
		initialFilling = cs;
	}
	

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = -1;
		usage = new int[field.getSize()];
		wState = new int[words.length][field.getSize()];
		for (int i = 0; i < wState.length; i++) {
			for (int p = 0; p < state.length; p++) {
				wState[i][p] = -1;
			}
		}
		
		applyInitialFilling();
		
		place = new int[stepCount + 1];
		wayCount = new int[stepCount + 1];
		way = new int[stepCount + 1];
		ways = new int[stepCount + 1][field.getSize()];
		
		solutionCount = 0;
		solution = null;
		t = 0;
	}
	
	void applyInitialFilling() {
		for (int p = 0; p < initialFilling.length; p++) {
			char c = initialFilling[p];
			if(c == EMP) continue;
			int v = getLetterIndex(c);
			if(v < 0) continue;
			usage[p]++;
			state[p] = v;
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == stepCount) return;
		int w = wordForStep[t];
		int letter = letterForStep[t];
		if(t == 0 || wordForStep[t] != wordForStep[t - 1]) {
			for (int p = 0; p < state.length; p++) {
				if(state[p] < 0 || state[p] == letter) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		} else {
			int p = place[t - 1];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0) continue;
				if(wState[w][q] >= 0) continue;
				if(state[q] >= 0 && state[q] != letter) continue;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		place[t] = p;
		usage[p]++;
		if(usage[p] == 1) {
			state[p] = letterForStep[t];
		}
		wState[wordForStep[t]][p] = letterForStep[t];		
		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		usage[p]--;
		if(usage[p] == 0) {
			state[p] = -1;
		}
		wState[wordForStep[t]][p] = -1;		
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
			if(t == stepCount) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.clone();
				}
				if(solutionCount <= printSolutionLimit || printSolutionLimit < 1) {
					printSolution();
				}
			}
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	void printSolution() {
		System.out.println("");
		for (int p = 0; p < state.length; p++) {
			int v = state[p];
			char c = (v < 0) ? '-' : getLetter(v);
			System.out.print(" " + c);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

	static String[] WORDS = {
		"DECLARATION",
		"LIBRARY"
	};
	
	static char EMP = '\0';
	
	static char[] INITIAL = {
		EMP, EMP, EMP, EMP,
		'O', EMP, EMP, EMP,
		EMP, EMP, EMP, EMP,
		EMP, EMP, EMP, EMP
	};

	public static void main(String[] args) {
		TwoWordsSolver p = new TwoWordsSolver();
		p.setWords(WORDS);
		p.setInitialFilling(INITIAL);
		p.solve();
		System.out.println("Solutions: " + p.getSolutionCount());
	}

}

/**
 	plenipotentiary   rest
	internationalism  rate
	misunderstanding  sister   u
*/
