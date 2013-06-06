package mozaika;

import java.util.ArrayList;
import java.util.List;

import com.slava.common.RectangularField;

public class MozaikaSolver {
	RectangularField field;
	int pieceCount = 9;
	RectangularField pieceField;
	
	class Piece {
		//[rotation][index]
		int[][] figure;
	}

	int[] settingState;
	IMozaikaValidator validator;
	
	Piece[] pieces;

	int[] state;
	int[] pieceUsage;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysI;
	int[][] waysR;
	
	int solutionCount;
	int treeCount;

	public MozaikaSolver() {
	}

	public void setField(RectangularField field) {
		this.field = field;
		pieceField = new RectangularField();
		int s = field.getSize() == 81 
			? 3 : 4;
		pieceField.setSize(s, s);
	}

	public void setValidator(IMozaikaValidator v) {
		validator = v;
	}
	
	public void setSettingState(int[] s) {
		settingState = s;
		pieces = new Piece[pieceCount];
		for (int i = 0; i < pieceCount; i++) {
			List figures = new ArrayList();
			int ix = i % 3;
			int iy = i / 3;
			pieces[i] = new Piece();
			pieces[i].figure = new int[4][pieceField.getSize()];
			for (int k = 0; k < pieceField.getSize(); k++) {
				int kx = pieceField.getX(k);
				int ky = pieceField.getY(k);
				int fx = ix * pieceField.getWidth() + kx;
				int fy = iy * pieceField.getHeight() + ky;
				int p = field.getIndex(fx, fy);
				pieces[i].figure[0][k] = s[p];
			}
			figures.add(pieces[i].figure[0]);
			for (int d = 1; d < 4; d++) {
				for (int k = 0; k < pieceField.getSize(); k++) {
					pieces[i].figure[d][k] = 
						pieces[i].figure[d - 1][pieceField.getRotation()[k]];
				}				
				if(!contains(figures, pieces[i].figure[d])) {
					figures.add(pieces[i].figure[d]);
				}
			}
			pieces[i].figure = (int[][])figures.toArray(new int[0][]);
			System.out.println("Fig " + i + " - " + pieces[i].figure.length);
		}
	}
	private boolean contains(List figures, int[] figure) {
		for (int i = 0; i < figures.size(); i++) {
			int[] f = (int[])figures.get(i);
			if(equal(f, figure)) return true;
		}
		return false;
	}
	private boolean equal(int[] f1, int[] f2) {
		for (int i = 0; i < f1.length; i++) {
			if(f1[i] != f2[i]) return false;
		}
		return true;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		pieceUsage = new int[pieceCount];
		
		wayCount = new int[pieceCount + 1];
		way = new int[pieceCount + 1];
		waysI = new int[pieceCount + 1][pieceCount * 4];
		waysR = new int[pieceCount + 1][pieceCount * 4];

		t = 0;		
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!isValid()) {
			return;
		}
		for (int i = 0; i < pieceCount; i++) {
			if(pieceUsage[i] > 0) continue;
			int rc = (t == 4) ? 1 : 4;
			for (int d = 0; d < rc && d < pieces[i].figure.length; d++) {
				waysI[t][wayCount[t]] = i;
				waysR[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
	}
	
	boolean isValid() {
		return validator.isValid(state);
	}
	
	void move() {
		int piece = waysI[t][way[t]];
		int r = waysR[t][way[t]];
		pieceUsage[piece]++;
		int ix = t % 3;
		int iy = t / 3;
		int[] figure = pieces[piece].figure[r];
		for (int k = 0; k < pieceField.getSize(); k++) {
			int kx = pieceField.getX(k);
			int ky = pieceField.getY(k);
			int fx = ix * pieceField.getWidth() + kx;
			int fy = iy * pieceField.getHeight() + ky;
			int p = field.getIndex(fx, fy);
			state[p] = figure[k];
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int piece = waysI[t][way[t]];
		pieceUsage[piece]--;
		int ix = t % 3;
		int iy = t / 3;
		for (int k = 0; k < pieceField.getSize(); k++) {
			int kx = pieceField.getX(k);
			int ky = pieceField.getY(k);
			int fx = ix * pieceField.getWidth() + kx;
			int fy = iy * pieceField.getHeight() + ky;
			int p = field.getIndex(fx, fy);
			state[p] = -1;
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
			if(t > tm) {
				tm = t;
				System.out.println(tm);
			}
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(t == pieceCount && isValid()
					) {
				solutionCount++;
				if(solutionCount < 6) {
					printSolution();
				}
			}
		}
	}

	void printSolution() {
		for (int p = 0; p < state.length; p++) {
			String s = state[p] < 0 ? "-" : "" + state[p];
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}

}
