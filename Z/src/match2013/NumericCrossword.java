package match2013;

import java.util.Map;
import java.util.Set;

import com.slava.common.RectangularField;

public class NumericCrossword {
	RectangularField field;
	int[] form = {
//		 1,1,1,1,1,0,0,0,0,0,1,1,
//		 0,0,0,0,0,0,1,1,0,0,0,1,
//		 0,0,1,0,0,1,1,0,0,1,0,1,
//		 0,1,1,1,0,0,1,0,1,1,0,1,
//		 0,0,1,0,0,0,0,0,1,1,0,0,
//		 0,0,0,0,1,0,0,0,0,0,0,1,
//		 1,0,0,1,1,0,0,0,0,0,1,1,
//		 1,0,0,0,1,0,1,1,0,1,1,0,
//		 1,1,1,0,1,0,0,1,0,0,0,0,
//		 0,0,0,0,0,0,0,1,1,0,1,0,
//		 1,1,1,0,1,1,0,0,0,0,1,0,
//		 1,0,1,0,0,1,1,1,0,1,1,1,
//			1,1,0,0,1,1,0,0,1,0,0,0,
//			1,0,0,1,1,0,0,0,1,0,1,1,
//			1,0,0,1,0,0,1,1,1,0,1,0,
//			1,0,0,0,0,0,0,0,0,0,1,1,
//			0,0,1,0,0,0,0,1,1,0,0,0,
//			0,1,1,0,0,1,0,0,1,1,0,0,
//			0,0,1,0,1,1,1,0,1,0,0,1,
//			1,0,1,0,0,1,0,0,0,0,1,1,
//			1,0,0,0,0,0,0,0,0,0,1,1,
//			1,0,1,1,1,0,1,1,0,0,0,0,
//			1,0,0,1,0,0,1,0,0,1,1,1,
//			1,0,0,1,0,1,1,0,1,1,0,0,
			0,0,1,0,0,1,1,1,0,1,1,0,
			1,1,1,0,1,1,0,0,0,0,1,1,
			1,0,0,0,0,0,0,1,1,0,0,1,
			0,0,0,0,1,0,0,1,0,0,0,0,
			0,1,0,1,1,0,0,1,1,0,0,0,
			1,1,0,0,1,1,0,0,0,0,1,0,
			0,1,0,0,0,0,0,0,0,1,1,1,
			0,1,0,1,1,1,0,0,0,0,1,0,
			0,0,0,0,1,0,0,1,1,0,0,0,
			1,1,1,0,1,0,0,0,1,0,1,1,
			1,0,0,0,0,0,0,0,1,0,1,1,
			1,0,1,1,1,1,1,0,1,0,0,1,
	};
	int regionCount = 0;
	int[] regions;
	
	int[] ignoreEquations;

	int[][] wordShapes;
	String[] wordCodes;
	int[][][] words; //[eq][index][letter]

	int[] state;
	int[] equationUsage;
	int[][] digitUsage;
	int[][] digitUsageInRegion;
	
	int t;
	int[] equation;
	int[] wayCount;
	int[] way;
	int[][] ways;

	public NumericCrossword() {
	}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		buildRegions();
		buildWordShapes();
		buildWords();
		equationUsage = new int[24];
		digitUsage = new int[field.getSize()][10];
		digitUsageInRegion = new int[regionCount][10];

		ignoreEquations =new int[24];
		ignoreEquations[1] = 1;
//		ignoreEquations[3] = 1;
		ignoreEquations[23] = 1;
//		ignoreEquations[8] = 1;
	
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;

		wayCount = new int[field.getWidth() + field.getHeight() + 1];
		way = new int[wayCount.length];
		equation = new int[wayCount.length];
		ways = new int[wayCount.length][1000];
		
		t = 0;
	}

	void buildWordShapes() {
		wordShapes = new int[24][];
		wordCodes = new String[24];
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int eq = ix;
			String code = "";
			int d = 0, s = 0;
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int p = field.getIndex(ix, iy);
				if(form[p] == 0) {
					if(d > 0) {
						s += d;
						code += d;
						d = 0;
					}
				} else {
					d++;
				}
			}
			if(d > 0) {
				s += d;
				code += d;
			}
			wordCodes[eq] = code;
			wordShapes[eq] = new int[s];
			s = 0;
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int p = field.getIndex(ix, iy);
				if(form[p] == 1) {
					wordShapes[eq][s] = p;
					s++;
				}
			}
		}

		for (int iy = 0; iy < field.getHeight(); iy++) {
			int eq = iy + field.getWidth();
			String code = "";
			int d = 0, s = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(form[p] == 0) {
					if(d > 0) {
						s += d;
						code += d;
						d = 0;
					}
				} else {
					d++;
				}
			}
			if(d > 0) {
				s += d;
				code += d;
			}
			wordCodes[eq] = code;
			wordShapes[eq] = new int[s];
			s = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(form[p] == 1) {
					wordShapes[eq][s] = p;
					s++;
				}
			}
		}
//		for (int eq = 0; eq < wordCodes.length; eq++) {
//			System.out.print(eq + " " + wordCodes[eq] + "   ");
//			for (int i = 0; i < wordShapes[eq].length; i++) System.out.print(" " + wordShapes[eq][i]);
//		}
	}

	void buildWords() {
		System.out.println("words:");
		words = new int[24][][];
		Equations b = new Equations();
		Map<String, Set<int[]>> map = b.map;
		for (int eq = 0; eq < wordCodes.length; eq++) {
			int[] n = new int[wordCodes[eq].length()];
			for (int i = 0; i < n.length; i++) n[i] = (int)(wordCodes[eq].charAt(i) - '0');
			long r = b.runToMap(n);
			if(!map.containsKey(wordCodes[eq])) throw new RuntimeException();
			words[eq] = (int[][])map.get(wordCodes[eq]).toArray(new int[0][]);
			System.out.println(eq + " '" + wordCodes[eq] + "' " + words[eq].length + " 1/" + r);
		}
	}

	void buildRegions() {
		regions = new int[form.length];
		for (int i = 0; i < regions.length; i++) regions[i] = -1;
		int r = -1;
		int[] stack = new int[10];
		for (int i = 0; i < regions.length; i++) {
			if(regions[i] >= 0 || form[i] == 0) continue;
			r++;
			int volume = 1;
			stack[0] = i;
			int c = 0;
			while(c < volume) {
				int p = stack[c];
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || form[q] == 0 || regions[q] >= 0) continue;
					stack[volume] = q;
					volume++;
					regions[q] = r;
				}
				c++;
			}
		}
		regionCount = r + 1;
		for (int i = 0; i < regions.length; i++) {
			String s = " ";
			if(regions[i] < 0) s += "+"; else s += (char)('a' + regions[i]);
			System.out.print(s);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

	void srch() {
		wayCount[t] = 0;
		if(t == 24) return;
		
		int eqb = -1;
		int wcb = 100000;
		for (int eq = 0; eq < wordShapes.length; eq++) {
			if(equationUsage[eq] > 0 || ignoreEquations[eq] > 0) continue;
			int wc = countWays(eq);
			if(wc < wcb) {
				wcb = wc;
				eqb = eq;
			}
//			if(wc == 0) System.out.println("!" + t + " "  + eq);
		}
		if(eqb < 0) return;
		equation[t] = eqb;
		wayCount[t] = collectWays(eqb);		
	}

	int countWays(int eq) {
		int ws = 0;
		for (int i = 0; i < words[eq].length; i++) {
			if(canPlace(eq, i)) {
				ws++;
				if(ws >= 200) return ws;
			}
		}			
		return ws;
	}

	int collectWays(int eq) {
		int ws = 0;
		for (int i = 0; i < words[eq].length; i++) {
			if(canPlace(eq, i)) {
				ways[t][ws] = i;
				ws++;
				if(ws >= 200) return ws;
			}
		}
		return ws;
	}

	boolean canPlace(int eq, int index) {
		int[] values = words[eq][index];
		for (int i = 0; i < values.length; i++) {
			int p = wordShapes[eq][i];
			int v = values[i];
			if(state[p] == -1) {
				if(digitUsage[p][v] > 0) {
					return false;
				}
				if(digitUsageInRegion[regions[p]][v] > 0) {
					return false;
				}
			} else if(state[p] != v) {
				return false;
			}
		
		}
		return true;
	}

	void move() {
		int eq = equation[t];
		int index = ways[t][way[t]];
		fillEquation(eq, index);		
		++t;
		srch();
		way[t] = -1;
	}

	void fillEquation(int eq, int index) {
		equationUsage[eq]++;
		int[] values = words[eq][index];
		for (int i = 0; i < values.length; i++) {
			int p = wordShapes[eq][i];
			int v = values[i];
			int eq1 = getCrossEquation(p, eq);
			if(state[p] == -1) {
				state[p] = v;
				if(equationUsage[eq1] == 0) {
					for (int j = 0; j < wordShapes[eq1].length; j++) {
						int q = wordShapes[eq1][j];
						if(q != p) digitUsage[q][v]++;
					}
				}
				digitUsageInRegion[regions[p]][v]++;
			} else if(state[p] != values[i]) {
				throw new RuntimeException();
			}
		}
	}

	int getCrossEquation(int p, int eq) {
		int ix = field.getX(p), iy = field.getY(p);
		return (ix != eq) ? ix : iy + field.getWidth();
	}

	void back() {
		t--;
		int eq = equation[t];
		int index = ways[t][way[t]];
		releaseEquation(eq, index);
	}
	void releaseEquation(int eq, int index) {
		equationUsage[eq] = 0;
		int[] values = words[eq][index];
		for (int i = 0; i < values.length; i++) {
			int p = wordShapes[eq][i];
			int v = values[i];
			int eq1 = getCrossEquation(p, eq);
			if(equationUsage[eq1] == 0) {
				for (int j = 0; j < wordShapes[eq1].length; j++) {
					int q = wordShapes[eq1][j];
					if(q != p) digitUsage[q][v]--;
				}
				state[p] = -1;
				digitUsageInRegion[regions[p]][v]--;
			}
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
			if(t >= tm) {
				tm = t;
				System.out.println("t=" + tm);
				printState();
			}
			int f = 0;
			if(t == 24) {
				
			}
		}
	}

	void printState() {
		for (int p = 0; p < state.length; p++) {
			String s = " ";
			if(form[p] == 0) s += "+"; else if(state[p] < 0) s += "x"; else s += state[p];
			System.out.print(s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		NumericCrossword p = new NumericCrossword();
		RectangularField f = new RectangularField();
		f.setSize(12, 12);
		p.setField(f);
		p.solve();
		
	}
}

/**
22
 1 2 + + 8 4 + + 7 + + +
 4 + + 1 0 + + + 2 + 3 6
 6 + + 7 + + 8 4 3 + 2 +
 7 + + + + + + + + + 4 9
 + + 2 + + + + 1 6 + + +
 + 4 6 + + 2 + + 5 8 + 3
 + + 7 + 1 5 3 + 9 + + 2
 5 + 8 + + 9 + + + + 6 4
 3 + + + + + + + + + 9 +
 8 + 1 2 3 + 5 9 + + + +
 9 + + 8 + + 7 + + 6 5 +
 2 + + 9 + 3 6 + 8 4 1 + 
 
*/