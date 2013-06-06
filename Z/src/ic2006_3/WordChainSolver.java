package ic2006_3;

import java.util.*;

import com.slava.common.TwoDimField;

public class WordChainSolver {
	TwoDimField field;
	int[] form;
	char[] initial;
	String[] words;
	
	int[][] digitalWords;
	int[] digitalInitial;
	Map letterIndices;
	char[] letters;
	
	int[] state;
	int[][] hLetterUsage;
	int[][] vLetterUsage;
	int[][] dLetterUsage;
	int[] wordUsage;
	
	int t = 0;
	int[] wayCount;
	int[] way;
	int[][] waysW;
	int[][] waysI;
	int[][] waysP;
	
	int treeCount;
	int solutionCount;

	public WordChainSolver() {}
	
	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setForm(int[] f) {
		form = f;
	}
	
	public void setInitialData(char[] is) {
		initial = is;
	}
	
	public void setWords(String[] ws) {
		words = ws;
		createDigitalWords();
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		hLetterUsage = new int[field.getHeight()][letters.length];
		vLetterUsage = new int[field.getWidth()][letters.length];
		dLetterUsage = new int[field.getHeight()][letters.length];
		wordUsage = new int[digitalWords.length];
		
		wayCount = new int[field.getSize()];
		way = new int[field.getSize()];
		waysW = new int[field.getSize()][600];
		waysI = new int[field.getSize()][600];
		waysP = new int[field.getSize()][600];
		
		t = 0;
		treeCount = 0;
		solutionCount = 0;
	}
	
	int getDiagonal(int p) {
		return field.getY(p) - field.getX(p) + 4;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t > 5 && !isConnected()) return;
		if(t == 0) {
			for (int w = 0; w < digitalWords.length; w++) {
				int i = 0;
				int pm = 63; //0;
				for (int p = pm; p < state.length; p++) {
					addWay(w, i, p);
				}
			}
			return;
		}
		int w = waysW[t - 1][way[t - 1]];
		int i = waysI[t - 1][way[t - 1]] + 1;
		int p = waysP[t - 1][way[t - 1]];
		if(i == digitalWords[w].length) {
			i = 0;
			for (w = 0; w < digitalWords.length; w++) {
				if(wordUsage[w] > 0) continue;
				if(t + digitalWords[w].length > 47) continue;
				for (int d = 0; d < 6; d++) {
					int q = field.jump(p, d);
					addWay(w, i, q);
				}
			}			
		} else {
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				addWay(w, i, q);
			}
		}		
	}
	
	void addWay(int w, int i, int p) {
		if(p < 0 || form[p] == 0 || state[p] >= 0) return;
		int c = digitalWords[w][i];
		if(digitalInitial[p] >= 0 && digitalInitial[p] != c) return;
		if(hLetterUsage[field.getY(p)][c] > 0) return;
		if(vLetterUsage[field.getX(p)][c] > 0) return;
		int d = getDiagonal(p);
		if(d >= 0 && d < dLetterUsage.length) {
			if(dLetterUsage[d][c] > 0) return;
		}
		for (int q = 0; q < digitalInitial.length; q++) {
			if(q != p && digitalInitial[q] == c) {
				if(field.getX(p) == field.getX(q)) return;
				if(field.getY(p) == field.getY(q)) return;
				if(getDiagonal(q) >= 0 && getDiagonal(p) == getDiagonal(q)) return;
			}
		}
		waysW[t][wayCount[t]] = w;
		waysI[t][wayCount[t]] = i;
		waysP[t][wayCount[t]] = p;
		wayCount[t]++;
	}
	
	void move() {
		int w = waysW[t][way[t]];
		int i = waysI[t][way[t]];
		int p = waysP[t][way[t]];
		int c = digitalWords[w][i];
		state[p] = c;
		hLetterUsage[field.getY(p)][c]++;
		vLetterUsage[field.getX(p)][c]++;
		int d = getDiagonal(p);
		if(d >= 0 && d < dLetterUsage.length) dLetterUsage[d][c]++;
		if(i == 0) wordUsage[w]++;
		++t;
		srch();
		way[t] = -1;		
	}
	
	void back() {
		--t;
		int w = waysW[t][way[t]];
		int i = waysI[t][way[t]];
		int p = waysP[t][way[t]];
		int c = digitalWords[w][i];
		state[p] = -1;
		hLetterUsage[field.getY(p)][c]--;
		vLetterUsage[field.getX(p)][c]--;
		int d = getDiagonal(p);
		if(d >= 0 && d < dLetterUsage.length) dLetterUsage[d][c]--;		
		if(i == 0) wordUsage[w]--;
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
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCount % 10000000 == 0) {
					System.out.println(treeCount + " : " + getCompleteness());
				}
			}
			if(t > tm) {
				tm = t;
				System.out.println(tm);
			}
			if(t == 47 && isSolution()) {
				solutionCount++;
				printSolution();
			}
		}
	}
	
	boolean isSolution() {
		return true;
	}
	
	double getCompleteness() {
		double p = 1;
		double s = 0;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}		
		return s;
	}

	void printSolution() {
		for (int i = 0; i < initial.length; i++) {
			int s = state[i];
			char c = ' ';
			if(s < 0 || form[i] == 0) {
				c = '-';
			} else {
				c = letters[s];
			}
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
	
	int[] visited;
	int[] stack;
	
	boolean isConnected() {
		visited = new int[state.length];
		stack = new int[state.length];
		int volume = 1;
		int c = 0;
		stack[0] = waysP[t - 1][way[t - 1]];
		visited[stack[0]] = 1;
		while(c < volume) {
			int p = stack[c];
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q < 0 || form[q] == 0 || state[q] >= 0) continue;
				if(visited[q] > 0) continue;
				visited[q] = 1;
				stack[volume] = q;
				volume++;
			}
			c++;
		}
		if(t + volume - 1 < 47) return false;
		for (int p = 0; p < digitalInitial.length; p++) {
			if(digitalInitial[p] >= 0 && visited[p] == 0 && state[p] < 0) return false;
		}
		
		return true;
	}
	
	void createDigitalWords() {
		letterIndices = new HashMap();
		digitalWords = new int[words.length][];
		for (int i = 0; i < words.length; i++) {
			digitalWords[i] = new int[words[i].length()];
			for (int j = 0; j < digitalWords[i].length; j++) {
				String c = words[i].substring(j, j + 1);
				Integer n = (Integer)letterIndices.get(c);
				if(n == null) {
					n = new Integer(letterIndices.size());
					letterIndices.put(c, n);
				}
				digitalWords[i][j] = n.intValue();
			}
		}
		letters = new char[letterIndices.size()];
		Iterator it = letterIndices.keySet().iterator();
		while(it.hasNext()) {
			String s = it.next().toString();
			Integer n = (Integer)letterIndices.get(s);
			letters[n.intValue()] = s.charAt(0);
		}
		for (int i = 0; i < letters.length; i++) {
			System.out.print(" " + letters[i]);
		}
		System.out.println("");
		digitalInitial = new int[initial.length];
		for (int i = 0; i < initial.length; i++) {
			char c = initial[i];
			if(c == '\0' || c == ' ') {
				digitalInitial[i] = -1;
			} else {
				Integer n = (Integer)letterIndices.get("" + c);
				digitalInitial[i] = (n == null) ? -1 : n.intValue();
			}
		}
		for (int i = 0; i < initial.length; i++) {
			String s = "" + digitalInitial[i];
			if(s.length() < 2) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
	
	
}
