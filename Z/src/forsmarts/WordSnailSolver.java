package forsmarts;

import java.util.*;

import com.slava.common.RectangularField;

public class WordSnailSolver {
	public interface SolutionRestriction {
		boolean accept(WordSnailSolver solver);
	}
	
	RectangularField field;
	int[] next;
	int[] prev;
	WordsCache words = new WordsCache();
	SolutionRestriction solutionRestriction;
	
	int[] state;
	int[] margin;
	int[][] hRowLetterUsage;
	int[][] vRowLetterUsage;
	int[] wordUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysW;
	int[][] waysP;
	
	int solutionCount;
	int treeCount;
	
	public WordSnailSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
		initNext();
	}
	
	void initNext() {
		next = new int[field.getSize()];
		prev = new int[field.getSize()];
		int[] used = new int[field.getSize()];
		for (int i = 0; i < next.length; i++) {
			next[i] = -1;
			prev[i] = -1;
		}
		int d = 0;
		int p = 0;
		while(true) {
			int d1 = d;
			int q = field.jump(p, d1);
			while(q < 0 || used[q] == 1) {
				d1++;
				if(d1 >= 4) d1 = 0;
				if(d1 == d) return;
				q = field.jump(p, d1);
			}
			used[p] = 1;
			next[p] = q;
			prev[q] = p;
			p = q;
			d = d1;
		}
	}
	
	public void setWords(String[] words) {
		this.words.setWords(words);
	}
	
	public void setSolutionRestriction(SolutionRestriction r) {
		solutionRestriction = r;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		margin = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		hRowLetterUsage = new int[field.getHeight()][words.code.length];
		vRowLetterUsage = new int[field.getWidth()][words.code.length];
		wordUsage = new int[words.intWords.length];

		int maxT = words.intWords.length + 1;
		wayCount = new int[maxT];
		way = new int[maxT];
		waysP = new int[maxT][100];
		waysW = new int[maxT][100];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.intWords.length) return;
		int wcm = 1000;
		for (int w = 0; w < wordUsage.length; w++) {
			if(wordUsage[w] > 0) continue;
			int wc = getMoveCountForWord(w);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				for (int i = 0; i < wc; i++) {
					waysW[t][i] = w;
					waysP[t][i] = temp[i];
				}
			}
		}
		if(wcm < 1000) {
			wayCount[t] = wcm;
		}
	}
	
	int[] temp = new int[100];
	
	int getMoveCountForWord(int w) {
		int wc = 0;
		for (int p = 0; p < state.length; p++) {
			if(!canAdd(w, p)) continue;
			boolean b = add(w, p);
			if(b) {
				temp[wc] = p;
				wc++;
			}
			remove(w, p);
		}
		
		return wc;
	}
	
	void move() {
		int w = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		add(w, p);
		++t;
		srch();
		way[t] = -1;
	}
	
	boolean canAdd(int w, int p) {
		int[] wi = words.intWords[w];
		for (int i = 0; i < wi.length; i++) {
			if(p < 0 || state[p] >= 0 || margin[p] > 0) return false;
			if(hRowLetterUsage[field.getY(p)][wi[i]] > 0) return false;
			if(vRowLetterUsage[field.getX(p)][wi[i]] > 0) return false;
			p = next[p];
		}		
		return true;
	}
	
	boolean add(int w, int p) {
		wordUsage[w]++;
		int[] wi = words.intWords[w];
		int q = prev[p];
		if(q >= 0) margin[q]++;
		boolean ok = true;
		for (int i = 0; i < wi.length; i++) {
			state[p] = wi[i];
			hRowLetterUsage[field.getY(p)][wi[i]]++;
			if(hRowLetterUsage[field.getY(p)][wi[i]] > 1) {
				ok = false;
			}
			vRowLetterUsage[field.getX(p)][wi[i]]++;
			if(vRowLetterUsage[field.getX(p)][wi[i]] > 1) {
				ok = false;
			}
			p = next[p];
		}
		if(p >= 0) margin[p]++;
		return ok && (solutionRestriction == null || solutionRestriction.accept(this));
	}
	
	void back() {
		--t;
		int w = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		remove(w, p);
	}
	
	void remove(int w, int p) {
		wordUsage[w]--;
		int[] wi = words.intWords[w];
		int q = prev[p];
		if(q >= 0) margin[q]--;
		for (int i = 0; i < wi.length; i++) {
			state[p] = -1;
			hRowLetterUsage[field.getY(p)][wi[i]]--;
			vRowLetterUsage[field.getX(p)][wi[i]]--;
			p = next[p];
		}
		if(p >= 0) margin[p]--;
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
			if(wayCount[t] == 0) treeCount++;
			if(isSolution()) {
				solutionCount++;
				printSolution();
			}
		}		
	}
	
	boolean isSolution() {
		return t == wordUsage.length && (solutionRestriction == null || solutionRestriction.accept(this));
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int i = 0; i < field.getSize(); i++) {
			int c = state[i];
			char ch = (c < 0) ? '+' : words.code[c];
			System.out.print(" " + ch);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		System.out.println("Key: " + getKey());
	}
	
	String getKey() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getWidth(); i++) {
			int c = state[field.getIndex(i, field.getWidth() - i - 1)];
			char ch = (c < 0) ? '-' : words.code[c];
			sb.append(ch);
		}
		return sb.toString();
	}

}

class WordsCache {
	String[] words;
	int[][] intWords;
	char[] code;
	Map indices;
	
	public void setWords(String[] ws) {
		words = ws;
		intWords = new int[ws.length][];
		indices = new HashMap();
		for (int i = 0; i < ws.length; i++) {
			intWords[i] = new int[ws[i].length()];
			for (int k = 0; k < intWords[i].length; k++) {
				char ch = ws[i].charAt(k);
				Character cho = new Character(ch);
				Integer io = (Integer)indices.get(cho);
				if(io == null) {
					io = new Integer(indices.size());
					indices.put(cho, io);
				}
				intWords[i][k] = io.intValue();
			}
		}
		code = new char[indices.size()];
		Iterator it = indices.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			Object value = indices.get(key);
			char ch = ((Character)key).charValue();
			int i = ((Integer)value).intValue();
			code[i] = ch;
		}
		
		for (int i = 0; i < code.length; i++) {
			System.out.print(" " + code[i]);
		}
		System.out.println("");
	}
	
	public int getIndex(char ch) {
		Character cho = new Character(ch);
		Integer io = (Integer)indices.get(cho);
		return io == null ? -1 : io.intValue();
	}
	
}
