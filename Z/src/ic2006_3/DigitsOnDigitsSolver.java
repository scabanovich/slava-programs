package ic2006_3;

import com.slava.common.RectangularField;
import com.slava.common.TwoDimField;

public class DigitsOnDigitsSolver {
	RectangularField field;
	TwoDimField octafield;
	DigitsOnDigitsPlacement[][] placements;
	int[] filter;
	int figuresToPut;
	int[] maxFigureCount = {2,2,2,2};
	int[][] sums;
	
	int[] state;
	int[] stateF;
	int[] usedFigures;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysI;
	
	int solutionCount;

	public DigitsOnDigitsSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
		octafield = new TwoDimField();
		octafield.setOrts(TwoDimField.DIAGONAL_ORTS);
		octafield.setSize(f.getWidth(), f.getHeight());
	}
	
	public void setPlacements(DigitsOnDigitsPlacement[][] placements) {
		this.placements = placements;
	}
	
	public void setFilter(int[] f) {
		filter = f;
	}
	
	public void setSums(int[][] s) {
		sums = s;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		figuresToPut = 0;
		for (int i = 0; i < maxFigureCount.length; i++) {
			figuresToPut += maxFigureCount[i];
		}
		state = new int[field.getSize()];
		stateF = new int[field.getSize()];
		for (int i = 0; i < stateF.length; i++) stateF[i] = -1;
		
		usedFigures = new int[12];
		
		wayCount = new int[100];
		way = new int[100];
		waysF = new int[100][600];
		waysI = new int[100][600];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == figuresToPut) return;
		if(!checkSums(false)) return;
		int f = t == 0 ? 0 : waysF[t - 1][way[t - 1]];
		int im = 0;
		if(usedFigures[f] >= maxFigureCount[f]) {
			f++;
		} else if(t > 0) {
			im = waysI[t - 1][way[t - 1]] + 1;
		}
		for (int i = im; i < placements[f].length; i++) {
			DigitsOnDigitsPlacement placement = placements[f][i];
			if(!canAdd(placement)) continue;
			waysF[t][wayCount[t]] = f;
			waysI[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	boolean checkSums(boolean isFinal) {
		for (int i = 0; i < sums.length; i++) {
			int s = sums[i][2];
			int d = sums[i][1];
			int p = sums[i][0];
			while(p >= 0) {
				s -= state[p];
				p = octafield.jump(p, d);
			}
			if(s < 0) return false;
			if(isFinal && s > 0) return false;
		}
		return true;
	}
	
	boolean canAdd(DigitsOnDigitsPlacement placement) {
		int index = placement.getIndex();
		if(usedFigures[index] >= maxFigureCount[index]) return false;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			if(filter[p] > 0) return false;
			if(state[p] > 0) return false;
		}
		return true;
	}
	
	void move() {
		int i = waysI[t][way[t]];
		int f = waysF[t][way[t]];
		DigitsOnDigitsPlacement placement = placements[f][i];
		add(placement);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(DigitsOnDigitsPlacement placement) {
		usedFigures[placement.getIndex()]++;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			state[p] = placement.getValues()[i];
			stateF[p] = placement.getIndex();
		}
	}
	
	void back() {
		--t;
		int i = waysI[t][way[t]];
		int f = waysF[t][way[t]];
		DigitsOnDigitsPlacement placement = placements[f][i];
		remove(placement);
	}
	
	void remove(DigitsOnDigitsPlacement placement) {
		usedFigures[placement.getIndex()]--;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			state[p] = 0;
			stateF[p] = -1;		}
	}

	void anal() {
		int tm = 0;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(tm);
			}
			if(t == figuresToPut && checkSums(true)) {
				solutionCount++;
				if(solutionCount % 10000 == 0) System.out.println(solutionCount);
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("");
		for (int p = 0; p < state.length; p++) {
			String ch = state[p] <= 0 ? "+" : "" + state[p];			
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		for (int p = 0; p < state.length; p++) {
			String ch = stateF[p] < 0 ? "+" : "" + stateF[p];			
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
	}

}
