package champ2013;

import java.util.Random;

import forsmarts.fitting2d.AbstractPackingAnalysis;
import forsmarts.fitting2d.Packing2dRunner;
import forsmarts.fitting2d.PackingAnalysis;
import forsmarts.fitting2d.PentaFigures;
import forsmarts.fitting2d.Problem2D;

public class Penta2013 {

	public static void main(String[] args) {
		while(true) {
			Packing2dRunner p = new Packing2dRunner();
			int sc = p.run(new Problem2013(), new Packing2013());
			System.out.println("Solutions = " + sc);
			if(sc > 0) break;
		}
	}

}

class Problem2013 extends Problem2D {
	
	public Problem2013() {
		xSize = 30;
		ySize = 20;
		figures = PentaFigures.FIGURES;
		isReflectionAllowed = true;
		usageLimit = new int[figures.length];
		for (int i = 0; i < figures.length; i++) usageLimit[i] = 1;
		designations = PentaFigures.DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;
		
		showSolutionLimit = 5;
		solutionLimit = 5;
	}

}

class Packing2013 extends AbstractPackingAnalysis {
	int perimeterLength = 84;

	protected int[] stateT;

	protected int[][] waysF;	
	protected int[][] waysP;

	int center1, center2;
	
	public Packing2013() {}
	
	protected void init() {
		stateT = new int[size];
		for (int i = 0; i < size; i++) stateT[i] = -1;
		wayCount = new int[13];
		way = new int[13];
		waysF = new int[13][1000];
		waysP = new int[13][1000];
		super.init();
		center1 = field.getIndex(5, 8);
		center2 = field.getIndex(5, 9);
	}
	
	protected void srch() {
		wayCount[t] = 0;
		if(t >= tMax) return;
		if(isIllegalState()) return;
		for (int f = 0; f < usage.length; f++) {
			if(usage[f] == usageLimits[f]) continue;
			for (int i = 0; i < placements[f].length; i++) {
				if(canPlace(f, i)) {
					waysF[t][wayCount[t]] = f;
					waysP[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			}
		}
		randomize();
		if(t < 3 && wayCount[t] > 5) {
			wayCount[t] = 5;
		}
	}

	Random seed = new Random();

	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t] - 1; i++) {
			int j = i + seed.nextInt(wayCount[t] - i);
			int c = waysF[t][i]; waysF[t][i] = waysF[t][j]; waysF[t][j] = c;
			c = waysP[t][i]; waysP[t][i] = waysP[t][j]; waysP[t][j] = c;
		}
	}
	int count = 0;
	/**
	 * Override if need to check matching additional conditions.
	 * @return
	 */	
	protected boolean isIllegalState() {
		if(t < 3) return false;
		count++;
		if(count % 150000 == 0) {
			for (int i = 0; i < t; i++) {
				System.out.print(" " + way[i]);
			}
			System.out.println("");
		}
		int[][] borders = getBorders();
		if(borders == null) {
			return true;
		}
		int tm = t == 12 ? t : t - 1;
		int[][] pairs = new int[12][12];
		for (int i = 1; i < tm; i++) {
			for (int s = 0; s < 2; s++) {
				int a = borders[i][s], b = borders[i - 1][s];
				if(pairs[a][b] > 0) return true;
				pairs[a][b] = 1;
				pairs[b][a] = 1;
			}
		}
		
		int[] stat = new int[12];
		for (int i = 0; i < t - 1; i++) {
			stat[borders[i][0]]++;
			if(i > 0) stat[borders[i][1]]++;
		}
		if(t < 9) {
			if(stat[5] > 2) return true;
			if(stat[6] > 2) return true;
			if(stat[7] > 2) return true;
			if(stat[8] > 1) return true;
			if(stat[9] > 1) return true;
			if(stat[10] > 0) return true;
			if(stat[8] == 1 && (stat[6] > 1 || stat[7] > 1)) return true;
			
			//variant 2: 1111 2222 3333 4444 55 66 77
			if(pairs[5][5] > 0 || pairs[5][6] > 0 || pairs[5][7] > 0) return true;
			
		}
		
		return false;
	}
	
	protected boolean canPlace(int f, int pi) {
					if(t < 10 && (f == 11 || f == 0)) return false; //for variant 2
		int index = placements[f][pi].getIndex();
		if(usage[index] == usageLimits[index]) return false;
		int[] ps = placements[f][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			if(fieldRestriction != null && fieldRestriction[ps[i]] > 0) return false;
			if(state[ps[i]] >= 0 || (shapeMode != NO_SHAPE && shape[ps[i]] > 0)) return false;
		}
		if(t == 0) {
			int v = 0;
			for (int i = 0; i < ps.length; i++) {
				if(ps[i] == center1 || ps[i] == center2) v++; 
			}
			return v == 2;
		} else {
			int f1 = waysF[t - 1][way[t - 1]];
			int n = 0;
			for (int i = 0; i < ps.length; i++) {
				for (int d = 0; d < 4; d++) {
					int q = field.jump(ps[i], d);
					if(q < 0 || state[q] < 0) continue;
					if(state[q] != f1) return false;
					n++;
				}
				for (int d = 0; d < 8; d++) {
					int q = octafield.jump(ps[i], d);
					if(q < 0) {
						return false; // Do not approach border. We have a filed long enough. 
					}
					if(q < 0 || state[q] < 0) continue;
					if(state[q] != f1) return false;
				}
			}
			if(t == 1 && n < 5) return false;
			if(t < 2 && n < 4) return false;
//			if((t < 7) && n < 3) return false;
			if(n < 2) return false;
		}		
		return true;
	}
	
	protected void move() {
		int pi = waysP[t][way[t]];
		int f = waysF[t][way[t]];
		int[] ps = placements[f][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = f;
			stateT[p] = t;
			shape[p]++;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]++;
			}
			if(shapeMode == HVD_SHAPE) {
				for (int d = 1; d < 8; d += 2) {
					int q = octafield.jump(p, d);
					if(q >= 0) shape[q]++;
				}
			}
		}
		usage[f]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	protected void back() {
		--t;
		int pi = waysP[t][way[t]];
		int f = waysF[t][way[t]];
		int[] ps = placements[f][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = -1;
			stateT[p] = -1;
			shape[p]--;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]--;
			}
			if(shapeMode == HVD_SHAPE) {
				for (int d = 1; d < 8; d += 2) {
					int q = octafield.jump(p, d);
					if(q >= 0) shape[q]--;
				}
			}
		}
		usage[f]--;
	}


	protected boolean checkSolution() {
		if(isIllegalState()) return false;
		int p = getPerimeter();
		if(p >= perimeterLength) {
			return false;
		}
		perimeterLength = p;
		return true;
	}

	int getPerimeter() {
		int result = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) {
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q >= 0 && state[q] < 0) {
						result++;
					}
				}
			}
		}
		return result;
	}

	int[][] getBorders() {
		int[] m = new int[state.length];
		for (int i = 0; i < m.length; i++) m[i] = -1;
		boolean h0 = false, ht = false;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) {
				for (int d = 0; d < 8; d++) {
					int q = octafield.jump(p, d);
					if(q >= 0 && state[q] < 0 && m[q] < 0) {
						m[q] = 0;
						if(!h0 && boundsOnly(q, 0)) {
							m[q] = 8;
							h0 = true;
						} else if(!ht && boundsOnly(q, t - 1)) {
							m[q] = 8;
							ht = true;
						}
					}
				}
			}
		}		
		
		int v = 0;
		for (int p = 0; p < state.length; p++) {
			if(m[p] == 0) {
				v++;
				int[] stack = new int[100];
				stack[0] = p;
				m[p] = v;
				int c = 0;
				int n = 1;
				while(c < n) {
					int q = stack[c];
					for (int d = 0; d < 4; d++) {
						int r = field.jump(q, d);
						if(r >= 0 && m[r] == 0) {
							stack[n] = r;
							m[r] = v;
							n++;
						}
					}
					c++;
				}
			}
		}

		int[][] borders = new int[t][2];
		for (int p = 0; p < state.length; p++) {
			if(stateT[p] >= 0) {
				boolean end = stateT[p] == 0 || stateT[p] == t - 1;
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || stateT[q] >= 0) continue;
					if(m[q] < 0) throw new RuntimeException("");
					int s = m[q];
					if(s > 2 && s != 8) {
						return null;
					}
					if(s > 1 || end) s = 0;
					borders[stateT[p]][s]++;
					if(end) borders[stateT[p]][1]++;
				}
			}
		}
		
//		
//		for (int i = 0; i < borders.length; i++) {
//			System.out.print(" " + borders[i][0]);
//		}
//		System.out.println("");
//		for (int i = 0; i < borders.length; i++) {
//			System.out.print(" " + borders[i][1]);
//		}
//		System.out.println("");
//		System.out.println("");
		return borders;
		
	}
	boolean bounds(int p, int k) {
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && stateT[q] == k) return true;
		}
		return false;
	}

	boolean boundsOnly(int p, int k) {
		int result = 0;
		for (int d = 0; d < 8; d++) {
			int q = octafield.jump(p, d);
			if(q < 0 || stateT[q] < 0) continue;
			if(stateT[q] != k) return false;
		}
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || stateT[q] < 0) continue;
			if(stateT[q] == k) result++;
		}
		return result == 1;
	}

	protected void printSolution() {
		super.printSolution();
		System.out.println();
//		for (int p = 0; p < state.length; p++) {
//			String s = "" + ((stateT[p] < 0) ? "-" : "" + stateT[p]);
//			System.out.print(s);
//			if(field.isRightBorder(p)) System.out.println("");
//		}
//		System.out.println("");
		int[][] borders = getBorders();
		int perim = - borders[0][0] - borders[t - 1][0];
		for (int i = 0; i < borders.length; i++) {
			perim += borders[i][0];
			System.out.print(" " + borders[i][0]);
		}
		System.out.println("");
		for (int i = 0; i < borders.length; i++) {
			perim += borders[i][1];
			System.out.print(" " + borders[i][1]);
		}
		System.out.println("");
		System.out.println("length=" + perimeterLength + " " + perim);
		System.out.println("");
	}

}

/**
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
----------------X-------------,
-----NN--------XXX------------,
---NNNL--------UXU------------,
---LLLL--------UUU------------,
---PP-------F-VVV-------------,
--TPPP----ZFFFV---------------,
--TTT-----ZZZFV---------------,
--TYYYY----WZ-----------------,
----YIIIIIWW------------------,
---------WW-------------------,
------------------------------,

 7 4 4 1 3 3 2 4 5 2 2 7
 7 1 1 5 3 4 6 2 1 6 3 7
length=76

------------------------------,
-----------TPPI---------------,
-----------TPPIZ--------------,
----------TTTPIZZZV-----------,
----------WW--I--ZV-----------,
---------WW---I-VVV-----------,
---------WFF-----UUU----------,
-------Y-FF------UXU----------,
------YYYYF------XXX----------,
----LLLL----------X-----------,
----LNNN----------------------,
----NN------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,

 7 1 2 6 3 1 1 5 3 3 2 7
 7 4 6 1 4 5 2 2 4 4 3 7
length=76

------------------------------,
-----IIIIINY------------------,
---LLLL--NNYW-----------------,
---LF----NYYWW----------------,
---FF----N-Y-WW---------------,
----FFT------UUU--------------,
----TTT------UXU--------------,
---PPPT------XXX--------------,
----PP--------X---------------,
----VVVZ----------------------,
----VZZZ----------------------,
----VZ------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,

 7 1 1 5 3 3 2 5 4 2 2 7
 7 4 4 1 3 4 6 1 2 6 3 7
length=76


------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
-----UU-----------------------,
-----UWWTTTF------------------,
-----UUWWTFF------------------,
--------WT-FFNN---------------,
-----------NNN--------L-------,
-----------YYYY----V--LP------,
-------------YIIIIIV--LPPZZ---,
-----------------VVVXLLPPZ----,
-------------------XXX--ZZ----,
--------------------X---------,
------------------------------,

 8 1 3 4 5 2 4 4 1 6 3 9
 8 3 2 2 1 5 3 3 7 1 1 9
length=78

------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
----ZZPPN---------------------,
-----ZPPN-L-------------------,
-----ZZPNNL---------I---------,
---------NLFTTTX--V-IY--------,
---------LLFFTXXX-V-IYY-------,
----------FF-T-XVVVWIYU-U-----,
------------------WWIYUUU-----,
-----------------WW-----------,
------------------------------,
------------------------------,

 8 2 4 4 1 3 4 6 1 5 4 9
 8 1 2 2 5 3 3 2 7 1 1 9
length=78

------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
---------N--------------------,
---------NY--TTT----UU-FZZ----,
----LLLLNNYVVVT----IUFFFZ-----,
----PPPLNYYV--TX--WIUUFZZ-----,
----PP----YV--XXXWWI----------,
---------------XWW-I----------,
-------------------I----------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,

 6 5 5 2 2 7 3 3 2 4 3 8
 6 1 1 3 5 1 4 4 6 2 1 8
length=78 78

------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------I-----------,
-----LUU----TY----IV----------,
-----LUNNNTTTYY--PIV----WW----,
-----LUU-NN-TYZZPPIVVVXFFWW---,
-----LL------Y-ZPPI--XXXFFW---,
---------------ZZ-----X-F-----,
------------------------------,
------------------------------,
------------------------------,
------------------------------,
------------------------------,

 8 3 2 4 4 1 3 4 5 1 1 8
 8 2 5 3 3 6 1 2 2 6 4 8
length=78 78
*/