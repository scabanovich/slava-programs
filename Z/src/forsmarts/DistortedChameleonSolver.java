package forsmarts;

import java.util.*;
import com.slava.common.TwoDimField;

public class DistortedChameleonSolver {
	TwoDimField field;
	int[] filter;
	DistortedChameleonFigure[] figures;
	int[] valueSet;
	
	int[][] sumRestrictions;
	
	int[] state;
	int[][] sums; // {h,v,d}{s1,s2,...}
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysF;
	
	public DistortedChameleonSolver() {}
	
	public void setField(TwoDimField field) {
		this.field = field;
	}
	
	public void setFilter(int[] filter) {
		this.filter = filter;
	}
	
	public void setFigures(DistortedChameleonFigure[] fs) {
		figures = fs;
		initValueSet();
	}
	
	void initValueSet() {
		HashSet set = new HashSet();
		for (int i = 0; i < figures.length; i++) {
			set.add(new Integer(figures[i].value));
		}
		Integer[] is = (Integer[])set.toArray(new Integer[0]);
		int[] vs = new int[is.length];
		for (int i = 0; i < is.length; i++) vs[i] = is[i].intValue();
		Arrays.sort(vs);
		valueSet = new int[vs.length];
		for (int i = 0; i < vs.length; i++) valueSet[i] = vs[vs.length - 1 - i];
	}
	
	public void setSumRestrictions(int[][] sumRestrictions) {
		this.sumRestrictions = sumRestrictions;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		
		sums = new int[3][field.getWidth() + 1];
		
		wayCount = new int[valueSet.length + 1];
		way = new int[valueSet.length + 1];
		waysP = new int[valueSet.length + 1][600];
		waysF = new int[valueSet.length + 1][600];
		
		t = 0;
	}
	
	
	void srch() {
		wayCount[t] = 0;
		if(t == valueSet.length) return;
		if(!checkState()) return;
		for (int f = 0; f < figures.length; f++) {
			if(figures[f].value != valueSet[t]) continue;
			for (int p = 0; p < state.length; p++) {
				if(canAdd(p, f)) {
					waysF[t][wayCount[t]] = f;
					waysP[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean checkState() {
		if(sumRestrictions == null) return true;
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < sumRestrictions[d].length; i++) {
				if(sumRestrictions[d][i] >= 0 && sumRestrictions[d][i] < sums[d][i]) return false;
			}
		}
		return true;
	}
	
	boolean canAdd(int p, int f) {
		DistortedChameleonFigure figure = figures[f];
		int[][] points = figure.points;
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			if(q < 0 || filter[q] == 0 || state[q] != -1) return false;
		}
		return true;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		add(p, f);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int f) {
		DistortedChameleonFigure figure = figures[f];
		int[][] points = figure.points;
		int value = valueSet[t];
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			state[q] = value;
		}
		int[][] cs = figure.crossings;
		for (int i = 0; i < cs[0].length; i++) {
			sums[0][field.getX(p) + cs[0][i]] += value;
		}				     
		for (int i = 0; i < cs[1].length; i++) {
			sums[1][field.getY(p) + cs[1][i]] += value;
		}				     
		for (int i = 0; i < cs[2].length; i++) {
			sums[2][getZ(p) + cs[2][i]] += value;
		}				     
	}
	
	int getZ(int p) {
		int x = field.getX(p); 
		int y = field.getY(p);
		int w2 = (field.getWidth() - 1) / 2;
		return y - x + w2;
	}
	
	void back() {
		--t;
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		remove(p, f);
	}
	
	void remove(int p, int f) {
		DistortedChameleonFigure figure = figures[f];
		int[][] points = figure.points;
		int value = valueSet[t];
		for (int i = 0; i < points.length; i++) {
			int q = field.jumpXY(p, points[i][0], points[i][1]);
			state[q] = -1;
		}
		int[][] cs = figure.crossings;
		for (int i = 0; i < cs[0].length; i++) {
			sums[0][field.getX(p) + cs[0][i]] -= value;
		}				     
		for (int i = 0; i < cs[1].length; i++) {
			sums[1][field.getY(p) + cs[1][i]] -= value;
		}				     
		for (int i = 0; i < cs[2].length; i++) {
			sums[2][getZ(p) + cs[2][i]] -= value;
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
			if(t == valueSet.length && checkSolution()) {
				printSolution();
			}
		}
	}
	
	boolean checkSolution() {
		if(sumRestrictions == null) return true;
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < sumRestrictions[d].length; i++) {
				if(sumRestrictions[d][i] >= 0 && sumRestrictions[d][i] != sums[d][i]) return false;
			}
		}
		return true;
	}
	
	void printSolution() {
		System.out.println("Solution");
		for (int p = 0; p < state.length; p++) {
			String c = "+";
			if(filter[p] == 0) {
				c = ".";
			} else if(state[p] >= 0) {
				c = "" + state[p];
			}
			System.out.print(" " + c);			
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		printKey();
	}
	
	void printKey() {
		int[] used = new int[20];
		for (int p = 0; p < state.length; p++) {
			if(state[p] < 0 || used[state[p]] > 0) continue;
			used[state[p]] = 1;
			System.out.print(" " + state[p]);
		}
		
		System.out.println("");
	}

}
