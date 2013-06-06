package slava.puzzle.doublepath.analysis;

import java.util.ArrayList;
import java.util.List;

import slava.puzzle.doublepath.model.DoublePathField;

public class DoublePathAnalysis {
	DoublePathField field;
	DoublePathBinds binds = new DoublePathBinds();
	
	int[] pointBinds;
	int[] transitionUsage;
	int[] transitionRestriction;
		
	int tLimit;
	int whiteBinds;
	int blackBinds;
	int t;
	int[] startPoint;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int solutionCount;
	int[][] solution;
	List solutionList;
	
	int printSolutionLimit;
	
	public void setField(DoublePathField field) {
		this.field = field;
		binds.setField(field);
	}
	
	public void setPrintSolutionLimit(int v) {
		printSolutionLimit = v;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		whiteBinds = binds.whiteCount - 1;
		blackBinds = binds.blackCount - 1;
		tLimit = whiteBinds + blackBinds;
		wayCount = new int[tLimit + 1];
		way = new int[tLimit + 1];
		ways = new int[tLimit + 1][tLimit];
		startPoint = new int[tLimit + 1];
		t = 0;
		pointBinds = new int[binds.points.length];
		for (int i = 0; i < pointBinds.length; i++) {
			pointBinds[i] = (binds.isEndPoint(i)) ? 1 : 2;
		}
		transitionUsage = new int[binds.transitionCount];
		transitionRestriction = new int[binds.transitionCount];
		solutionCount = 0;
		solutionList = new ArrayList();
	}

	int[] temp = new int[100];	
	void srch() {
		wayCount[t] = 0;
		if(t == tLimit) return;
		int place = -1;
		int ws = 1000;
		for (int i = 0; i < pointBinds.length; i++) {
			if(pointBinds[i] == 0) continue;
			if(pointBinds[i] == 2) {
				int q = 0;
				for (int j = 0; j < binds.transitions[i].length; j++) {
					int tr = binds.transitionIndex[i][j];
					if(transitionRestriction[tr] > 0 || transitionUsage[tr] > 0) continue;
					int pe = binds.transitions[i][j];
					if(pointBinds[pe] == 0) continue;
					++q;
				}
				if(q < 2) return;
			} else {
				int color = field.getState(binds.points[i]);
				boolean isWhite = color == 2 || color == 4;
				int wc = 0;
				for (int j = 0; j < binds.transitions[i].length; j++) {
					int tr = binds.transitionIndex[i][j];
					if(transitionRestriction[tr] > 0 || transitionUsage[tr] > 0) continue;
					int pe = binds.transitions[i][j];
					if(pointBinds[pe] == 0) continue;
					if(pointBinds[pe] == 1 && 
					   ((isWhite && whiteBinds > 1) || (!isWhite && blackBinds > 1))) continue;
					temp[wc] = j;
					++wc;
				}
				if(wc < ws) {
					if(wc == 0) return;
					ws = wc;
					for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
					place = i;
				}				
			}
		}
		if(place < 0) return;
		wayCount[t] = ws;
		startPoint[t] = place;
	}
	
	void move() {
		int ps = startPoint[t];
		int d = ways[t][way[t]];
		int pe = binds.transitions[ps][d];
		int tr = binds.transitionIndex[ps][d];
		int s = field.getState(binds.points[ps]);
		if(s == 1 || s == 3) blackBinds--; else whiteBinds--;		
		pointBinds[ps]--;
		pointBinds[pe]--;
		transitionUsage[tr]++;
		for (int i = 0; i < binds.intersections[tr].length; i++) {
			if(binds.intersections[tr][i] == 1) transitionRestriction[i]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int ps = startPoint[t];
		int d = ways[t][way[t]];
		int pe = binds.transitions[ps][d];
		int tr = binds.transitionIndex[ps][d];
		int s = field.getState(binds.points[ps]);
		if(s == 1 || s == 3) blackBinds++; else whiteBinds++;		
		pointBinds[ps]++;
		pointBinds[pe]++;
		transitionUsage[tr]--;
		for (int i = 0; i < binds.intersections[tr].length; i++) {
			if(binds.intersections[tr][i] == 1) transitionRestriction[i]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		//int maxt = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == tLimit) {
				solutionCount++;
				buildSolution();
			}
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public List getSolutions() {
		return solutionList;
	}
	
	private void buildSolution() {
		if(solutionCount <= printSolutionLimit) {
			///makeVisualSolution();
			makeSolution();
		}
	}
	
	private void printSolution() {
		for (int i = 0; i < t; i++) {
			int ps = startPoint[i];
			int d = ways[i][way[i]];
			int pe = binds.transitions[ps][d];
			int p_i = binds.points[ps];
			int x = field.x(p_i), y = field.y(p_i);
			System.out.print("  " + x + ":" + y);
			p_i = binds.points[pe];
			x = field.x(p_i); y = field.y(p_i);
			System.out.print("-" + x + ":" + y);
			
		}
		System.out.println("");
	}
	
	void makeSolution() {
		int[][] solution = new int[tLimit][2];
		for (int tt = 0; tt < tLimit; tt++) {
			int pb = startPoint[tt];
			int pbi = binds.points[pb];
			int d = ways[tt][way[tt]];
			int pe = binds.transitions[pb][d];
			int pei = binds.points[pe];
			solution[tt][0] = pbi;
			solution[tt][1] = pei;
		}
		solutionList.add(solution);
	}
	
	void makeVisualSolution() {
		int[] sn = new int[field.getSize()];
		boolean wh = false;
		boolean bl = false;
		for (int i = sn.length - 1; i >= 0; i--) {
			int c = field.getState(i);
			if(c == 3) {
				if(!bl) {
					sn[i] = 1;
					bl = true;
				} else {
					sn[i] = binds.blackCount;
				}				
			} else if(c == 4) {
				if(!wh) {
					sn[i] = 1;
					wh = true;
				} else {
					sn[i] = binds.whiteCount;
				} 
				
			}
		}
		int wmin = 1, bmin = 1, wmax = binds.whiteCount, bmax = binds.blackCount;
		for (int tt = 0; tt < tLimit; tt++) {
			int pb = startPoint[tt];
			int pbi = binds.points[pb];
			int d = ways[tt][way[tt]];
			int pe = binds.transitions[pb][d];
			int pei = binds.points[pe];
			boolean w = field.isWhite(pbi);
			if(w && sn[pbi] == wmin) {
				wmin++;
				sn[pei] = wmin;
			} else if(w && sn[pbi] == wmax) {
				wmax--;
				sn[pei] = wmax;
			} else if(w && sn[pei] == wmax) {
				wmax--;
				sn[pbi] = wmax;
			} else if(w && sn[pei] == wmin) {
				wmin++;
				sn[pbi] = wmin;
			} else if(!w && sn[pbi] == bmin) {
				bmin++;
				sn[pei] = bmin;
			} else if(!w && sn[pbi] == bmax) {
				bmax--;
				sn[pei] = bmax;
			} else if(!w && sn[pei] == bmin) {
				bmin++;
				sn[pbi] = bmin;
			} else if(!w && sn[pei] == bmax) {
				bmax--;
				sn[pbi] = bmax;
			}
		}
		for (int i = 0; i < sn.length; i++) {
			char c = ' ';
			if(sn[i] == 0) {
				c = '+';
			} else if(field.isWhite(i)) {
				c = (char)(96 + sn[i]);
			} else {
				c = (char)(64 + sn[i]);
			}
			System.out.print(" " + c);
			if(field.x(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		DoublePathField field = new DoublePathField();
		field.setSize(WIDTH, FIELD.length / WIDTH);
		for (int i = 0; i < field.getSize(); i++) {
			field.setState(i, FIELD[i]);
		}
		DoublePathAnalysis a = new DoublePathAnalysis();
		a.setPrintSolutionLimit(5);
		a.setField(field);
		a.solve();
		System.out.println("Solution count=" + a.solutionCount);
	}
	
	static int WIDTH = 10;
	static int[] FIELD = new int[] {
		0,2,2,0,2,0,0,0,4,3,
		0,1,0,1,0,0,1,0,2,0,
		0,0,0,0,0,2,0,0,0,0,
		2,0,0,0,0,0,0,0,0,1,
		0,0,0,2,1,1,1,0,2,0,
		0,0,1,0,0,0,0,2,1,0,
		0,0,1,0,0,0,2,0,0,0,
		0,2,0,2,0,1,0,0,2,0,
		4,3,0,0,0,0,0,0,0,1,
	};

}
