package olia;

import com.slava.common.RectangularField;

public class ChangeableCroswordSolver {
	RectangularField field;
	String[] words;
	
	int[] wordUsage;
	int[] hWord;
	int[] vWord;
	int[][][] range; //[cell][{h,v}][{begin,end}]
	String[] parts;
	
	int t;
	int[] wayCount;
	int[] way;
	int[] place;
	int[] direction;
	int[][] waysW; //word
	int[][] waysB; //beginning of range
	int[][] waysE; //ending of range
	
	int[] tempW;
	int[] tempB;
	int[] tempE;
	
	int solutionCount;
	
	String[] solution;
	
	public ChangeableCroswordSolver() {}
	
	public void setProblem(String[] words, int width, int height) {
		if(width + height != words.length) {
			throw new RuntimeException("Inconsistent data.");
		}
		RectangularField f = new RectangularField();
		f.setSize(width, height);
		setField(f);
		setWords(words);
	}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setWords(String[] words) {
		this.words = words;
	}
	
	public void solve() {
		check();
		init();
		anal();
	}
	
	void check() {
		if(field.getWidth() + field.getHeight() != words.length) {
			throw new RuntimeException("Inconsistent data.");
		}
	}
	
	void init() {
		wordUsage = new int[words.length];
		hWord = new int[field.getHeight()];
		for (int i = 0; i < hWord.length; i++) hWord[i] = -1;
		vWord = new int[field.getWidth()];
		for (int i = 0; i < vWord.length; i++) vWord[i] = -1;
		range = new int[field.getSize()][2][2];
		for (int p = 0; p < range.length; p++) {
			for (int d = 0; d < 2; d++) {
				for (int i = 0; i < 2; i++) range[p][d][i] = -1;
			}
		}
		parts = new String[field.getSize()];
		
		wayCount = new int[field.getSize() * 2 + 1];
		way = new int[field.getSize() * 2 + 1];
		place = new int[field.getSize() * 2 + 1];
		direction = new int[field.getSize() * 2 + 1];
		waysW = new int[field.getSize() * 2 + 1][1000];
		waysB = new int[field.getSize() * 2 + 1][1000];
		waysE = new int[field.getSize() * 2 + 1][1000];
		
		tempW = new int[1000];
		tempB = new int[1000];
		tempE = new int[1000];
		
		t = 0;
		solutionCount = 0;
		solution = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize() * 2) return;
		int bwc = 1000;
		for (int p = 0; p < field.getSize(); p++) {
			for (int d = 0; d < 2; d++) {
				if(range[p][d][0] >= 0) continue;
				int wc = getWaysForCell(p, d);
				if(wc == 0) return;
				if(wc < bwc) {
					bwc = wc;
					for (int i = 0; i < wc; i++) {
						waysW[t][i] = tempW[i];
						waysB[t][i] = tempB[i];
						waysE[t][i] = tempE[i];
					}
					place[t] = p;
					direction[t] = d;
				}
			}
		}
		if (bwc < 1000) {
			wayCount[t] = bwc;
		}
		
	}
	
	int getWaysForCell(int p, int d) {
		int c = 0;
		int cw = (d == 0) ? hWord[field.getY(p)] : vWord[field.getX(p)];
		String part = parts[p];
		int wb = (cw < 0) ? 0 : cw;
		int we = (cw < 0) ? words.length : cw + 1;
		for (int w = wb; w < we; w++) {
			if(w != cw && wordUsage[w] > 0) continue;
			int[] rangeBegin = getRangeBeginning(p, d);
			int[] rangeEnd = getRangeEnd(p, d);
			if(rangeEnd[0] > Integer.MAX_VALUE - 100) {
				rangeEnd[0] = words[w].length() - (Integer.MAX_VALUE - rangeEnd[0]);
			}
			int b2 = rangeBegin[1] == 1 ? rangeEnd[0] : rangeBegin[0];
			for (int b = rangeBegin[0]; b <= b2; b++) {
				int e2 = rangeEnd[1] == 1 ? b : rangeEnd[0];
				if(e2 < b) e2 = b;
				for (int e = e2; e <= rangeEnd[0]; e++) {
					if(part != null && !part.equals(words[w].substring(b, e))) {
						continue;
					}
					tempW[c] = w;
					tempB[c] = b;
					tempE[c] = e;
					c++;
				}
			}			
		}		
		return c;
	}
	
	int[] getRangeBeginning(int p, int d) {
		int d2 = d + 2;
		p = field.jump(p, d2);
		int add = 0;
		int free = 0;
		while(p >= 0) {
			if(range[p][d][1] >= 0) {
				return new int[]{range[p][d][1] + add, free};
			} else if(parts[p] != null) {
				add += parts[p].length();
			} else {
				free = 1;
			}
			p = field.jump(p, d2);
		}
		return new int[]{add, free};
	}
	
	int[] getRangeEnd(int p, int d) {
		p = field.jump(p, d);
		int add = 0;
		int free = 0;
		while(p >= 0) {
			if(range[p][d][0] >= 0) {
				return new int[]{range[p][d][0] - add, free};
			} else if(parts[p] != null) {
				add += parts[p].length();
			} else {
				free = 1;
			}
			p = field.jump(p, d);
		}
		return new int[]{Integer.MAX_VALUE - add, free};
	}
	
	void move() {
		int p = place[t];
		int d = direction[t];
		int w = waysW[t][way[t]];
		int b = waysB[t][way[t]];
		int e = waysE[t][way[t]];
		wordUsage[w]++;
		if(wordUsage[w] == 1) {
			if(d == 0) {
				hWord[field.getY(p)] = w;
			} else {
				vWord[field.getX(p)] = w;
			}
		}
		range[p][d][0] = b;
		range[p][d][1] = e;
		parts[p] = words[w].substring(b, e);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int d = direction[t];
		int w = waysW[t][way[t]];
		wordUsage[w]--;
		if(wordUsage[w] == 0) {
			if(d == 0) {
				hWord[field.getY(p)] = -1;
			} else {
				vWord[field.getX(p)] = -1;
			}
		}
		range[p][d][0] = -1;
		range[p][d][1] = -1;
		if(range[p][0][0] < 0 && range[p][1][0] < 0) {
			parts[p] = null;
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
			if(t == field.getSize() * 2) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (String[])parts.clone();
				}
				onSolutionFound();
			}
			
		}
	}
	
	void onSolutionFound() {
		
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public String[] getSolution() {
		return solution;
	}
	
	public void printSolution(String[] solution) {
		for (int p = 0; p < field.getSize(); p++) {
			String s = solution[p];
			while(s.length() < 3) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
}
