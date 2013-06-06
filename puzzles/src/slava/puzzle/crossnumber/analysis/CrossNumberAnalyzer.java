package slava.puzzle.crossnumber.analysis;

import java.util.*;
import slava.puzzle.crossnumber.CrossNumberField;

public class CrossNumberAnalyzer {
	CrossNumberField field;
	int size;
	
	int[] values;
	int[] hsum; // assigned to each cell for quick reference
	int[] vsum; // assigned to each cell for quick reference
	int[][] hneighbours;
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

	int[][] hdistribution; 
	int[][] vdistribution; 
	
	public void setField(CrossNumberField field) {
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
		values = new int[size];
		for (int i = 0; i < size; i++) {
			values[i] = (field.getStatus(i) == 4) ? 0 : -1;
			if(field.getStatus(i) == 4) ++tLimit;
		}
		initQuickReferences();
		wayCount = new int[tLimit + 1];
		ways = new int[tLimit + 1][9];
		way = new int[tLimit + 1];
		place = new int[tLimit + 1];
		t = 0;
		solutionCount = 0;
		solution = null;
		hdistribution = new int[size][46];
		vdistribution = new int[size][46];
		for (int i = 0; i < size; i++) for (int s = 0; s < 46; s++) {
			hdistribution[i][s] = 0;
			vdistribution[i][s] = 0;
		}
	}
	
	void initQuickReferences() {
		hsum = new int[size];
		vsum = new int[size];
		for (int i = 0; i < size; i++) {
			int status = field.getStatus(i);
			if((status & 1) != 0) {
				int q = field.jump(i, 0);
				while(q >= 0 && values[q] >= 0) {
					hsum[q] = field.getHSum(i);
					q = field.jump(q, 0);
				}
			}
			if((status & 2) != 0) {
				int q = field.jump(i, 1);
				while(q >= 0 && values[q] >= 0) {
					vsum[q] = field.getVSum(i);
					q = field.jump(q, 1);
				}
			}
		}
		hneighbours = new int[size][];
		for (int i = 0; i < size; i++) {
			//int status = field.getStatus(i);
			if(field.getStatus(i) != 4) {
				hneighbours[i] = new int[0];
			} else {
				ArrayList l = new ArrayList();
				appendNeighbours(l, i, 0);
				appendNeighbours(l, i, 2);
				hneighbours[i] = new int[l.size()];
				for (int k = 0; k < l.size(); k++) hneighbours[i][k] = ((Integer)l.get(k)).intValue();
			} 
		}
		vneighbours = new int[size][];
		for (int i = 0; i < size; i++) {
			//int status = field.getStatus(i);
			if(field.getStatus(i) != 4) {
				vneighbours[i] = new int[0];
			} else {
				ArrayList l = new ArrayList();
				appendNeighbours(l, i, 1);
				appendNeighbours(l, i, 3);
				vneighbours[i] = new int[l.size()];
				for (int k = 0; k < l.size(); k++) vneighbours[i][k] = ((Integer)l.get(k)).intValue();
			} 
		}
	}
	
	void appendNeighbours(ArrayList list, int place, int d) {
		int q = field.jump(place, d);
		while(q >= 0 && values[q] >= 0) {
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
			if(values[p] != 0) continue;
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
		for (int i = 1; i < 10; i++) {
			if(checkH(p, i) && checkV(p, i)) {
				temp[c] = i;
				c++;
			}
		}
		return c;
	}
	
	int[] mins = new int[]{0,1,3,6,10,15,21,28,36,45};
	int[] maxs = new int[]{0,9,17,24,30,35,39,42,44,45};
	
	boolean checkH(int p, int v) {
		int empty = 0;
		int sum = v;
		for(int i = 0; i < hneighbours[p].length; i++) {
			int nv = values[hneighbours[p][i]];
			if(v == nv) return false;
			if(nv == 0) empty++; else sum += nv;
		}
		if(hsum[p] <= 0) return true;
		return (hsum[p] >= sum + mins[empty] && hsum[p] <= sum + maxs[empty]);
	}
	
	boolean checkV(int p, int v) {
		int empty = 0;
		int sum = v;
		for(int i = 0; i < vneighbours[p].length; i++) {
			int nv = values[vneighbours[p][i]];
			if(v == nv) return false; 
			if(nv == 0) empty++; else sum += nv;
		}
		if(vsum[p] <= 0) return true;
		return (vsum[p] >= sum + mins[empty] && vsum[p] <= sum + maxs[empty]);
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
		values[p] = k;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;		
		int p = place[t];
		values[p] = 0;
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
					solution = (int[])values.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		for (int i = 0; i < size; i++) {
			int status = field.getStatus(i);
			if((status & 1) != 0) {
				if(field.getHSum(i) <= 0) hdistribution[i][getSum(i, 0)]++;
			}
			if((status & 2) != 0) {
				if(field.getVSum(i) <= 0) vdistribution[i][getSum(i, 1)]++;
			}
		}
	}
	private int getSum(int place, int d) {
		int q = field.jump(place, d);
		int sum = 0;
		while(q >= 0 && field.getStatus(q) == 4) {
			sum += values[q];
			q = field.jump(q, d);
		}
		return sum;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int[][] getHDistribution() {
		return hdistribution;
	}
	
	public int[][] getVDistribution() {
		return vdistribution;
	}
	
	void printSolution() {
		for (int i = 0; i < size; i++) {
			if(values[i] > 0) System.out.print(" " + values[i]);
		}
		System.out.println("");
	}

}
