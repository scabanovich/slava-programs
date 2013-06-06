package ic2006_3;

import com.slava.chess.ChessFigures;
import com.slava.common.CellSet;

public class XactCoverSolver {
	ChessFigures figures;
	int[] form;
	
	CellSet occupied;
	CellSet hit;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysP;
	
	int solutionCount;
	int maxFigures = 0;
	
	public XactCoverSolver() {}
	
	public void setFigures(ChessFigures fgs) {
		figures = fgs;
		occupied = new CellSet(fgs.getField().getSize());
		hit = new CellSet(fgs.getField().getSize());
	}
	
	public void setForm(int[] f) {
		form = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[30];
		way = new int[30];
		waysF = new int[30][200];
		waysP = new int[30][200];

		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		int p = getUnhitTarget();
		if(p >= 0) {
			for (int f = 0; f < 5; f++) {
				int[] ms = figures.getMoves(f, p);
				for (int i = 0; i < ms.length; i++) {
					if(form[ms[i]] == 1 || occupied.value(ms[i]) > 0 || hit.value(ms[i]) > 0) continue;
					if(canAdd(ms[i], f)){
						waysF[t][wayCount[t]] = f;
						waysP[t][wayCount[t]] = ms[i];
						wayCount[t]++;
					}					
				}
			}
		} else {
			// here we may take into account same position in many ways.
			for (p = 0; p < figures.getField().getSize(); p++) {
				if(form[p] == 1 || occupied.value(p) > 0 || hit.value(p) > 0) continue;
				for (int f = 0; f < 5; f++) {
					if(canAdd(p, f)){
						waysF[t][wayCount[t]] = f;
						waysP[t][wayCount[t]] = p;
						wayCount[t]++;
					}					
				}
			}
		}
	}
	
	int getUnhitTarget() {
		for (int p = 0; p < figures.getField().getSize(); p++) {
			if(form[p] == 1 && hit.value(p) == 0) return p;
		}
		return -1;
	}
	
	boolean canAdd(int p, int f) {
		int[] ms = figures.getMoves(f, p);
		for (int i = 0; i < ms.length; i++) {
			if(hit.value(ms[i]) > 0 && form[ms[i]] == 1) return false;
			if(occupied.value(ms[i]) > 0) return false;
		}
		return true;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		add(p, f);
		t++;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int f) {
		occupied.add(p, f + 1);
		int[] ms = figures.getMoves(f, p);
		for (int i = 0; i < ms.length; i++) {
			hit.add(ms[i]);
		}
	}
	
	void back() {
		--t;
		int f = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		remove(p, f);
	}
	
	void remove(int p, int f) {
		occupied.remove(p, f + 1);
		int[] ms = figures.getMoves(f, p);
		for (int i = 0; i < ms.length; i++) {
			hit.remove(ms[i]);
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) {
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(isSolution()) {
				if(t > maxFigures) {
					maxFigures = t;
					System.out.println("t=" + maxFigures);
					printSolution();
				}
			}
		}
	}
	
	boolean isSolution() {
		//if(true) return true;
		return getUnhitTarget() < 0;
	}
	
	char[] DESIGNATIONS = {'K', 'Q', 'R', 'B', 'N'};
	
	void printSolution() {
		for (int p = 0; p < figures.getField().getSize(); p++) {
			int f = occupied.value(p) - 1;
			int h = hit.value(p);
			String ch = h > 0 ? (f >= 0 ? "E" + DESIGNATIONS[f] : "" + h) 
					: f < 0 ? "+" : "" + DESIGNATIONS[f];
			System.out.print(" " + ch);
			if(figures.getField().isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		for (int p = 0; p < figures.getField().getSize(); p++) {
			int f = occupied.value(p) - 1;
			if(f < 0) continue;
			char xc = (char)(97 + figures.getField().getX(p));
			int yc = figures.getField().getHeight() - figures.getField().getY(p);
			System.out.print("" + DESIGNATIONS[f] + "" + xc + "" + yc + ",");
		}
		System.out.println("");
	}

}
