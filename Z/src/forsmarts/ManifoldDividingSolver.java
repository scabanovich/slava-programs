package forsmarts;

import java.util.Arrays;

import com.slava.common.RectangularField;

public class ManifoldDividingSolver {
	int piecesCount;
	RectangularField field;
	ManifoldDividingPlacements.Placement[] placements;
	
	int[] state;
	int[] pieceUsage;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] distribution;
	
	int solutionCount;
	int treeCount;

	public ManifoldDividingSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setPlacements(ManifoldDividingPlacements ps) {
		placements = ps.placements;
		piecesCount = ps.piecesCount;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		distribution = new int[field.getSize()];
		pieceUsage = new int[piecesCount];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		place = new int[piecesCount + 1];
		wayCount = new int[piecesCount + 1];
		way = new int[piecesCount + 1];
		ways = new int[piecesCount + 1][placements.length];
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == piecesCount) return;
		getDistribution();
		int place = getNarrowestPlace();
		for (int i = 0; i < placements.length; i++) {
			if(pieceUsage[placements[i].index] > 0) continue;
			int[] r = placements[i].places;
			if(canAdd(r) && contains(r, place)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
//		System.out.println("t=" + t + " place=" + place + " ways=" + wayCount[t]);
//		int o = 0;
	}
	
	void getDistribution() {
		for (int i = 0; i < distribution.length; i++) distribution[i] = 0;
		for (int i = 0; i < placements.length; i++) {
			if(pieceUsage[placements[i].index] > 0) continue;
			int[] r = placements[i].places;
			if(canAdd(r)) {
				for (int k = 0; k < r.length; k++) {
					distribution[r[k]]++;
				}				
			}
		}
	}
	
	int getNarrowestPlace() {
		int wcm = 10000;
		int place = -1;
		for (int i = 0; i < distribution.length; i++) {
			if(state[i] >= 0) continue;
			if(distribution[i] < wcm) {
				wcm = distribution[i];
				if(wcm == 0) return i; else place = i;
			}
		}
		return place;
	}
	
	boolean contains(int[] r, int p) {
		for (int i = 0; i < r.length; i++) {
			if(r[i] == p) return true;
		}
		return false;
	}
	
	boolean canAdd(int[] r) {
		for (int i = 0; i < r.length; i++) {
			if(state[r[i]] >= 0) return false;
		}
		return true;
	}
	
	
	void move() {
		int pi = ways[t][way[t]];
		int index = placements[pi].index;
		pieceUsage[index]++;
		int[] ps = placements[pi].places;
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = t;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int pi = ways[t][way[t]];
		int index = placements[pi].index;
		pieceUsage[index]--;
		int[] ps = placements[pi].places;
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = -1;
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
			if(wayCount[t] == 0) treeCount++;
			if(t == piecesCount) {
				++solutionCount;
				if(solutionCount < 10) printSolution();
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] < 0) ? '.' : (char)(97 + state[i]);
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
		System.out.println(getSolutionKey());
	}
	
	String getSolutionKey() {
		StringBuffer sb = new StringBuffer();
		int[] p3 = new int[piecesCount];
		int p3c = 0;
		for (int i = 0; i < t; i++) {
			int pi = ways[i][way[i]];
			if(placements[pi].places.length != 3) continue;
			p3[p3c] = placements[pi].places[1];
			p3c++;
		}
		int[] p3f = new int[p3c];
		System.arraycopy(p3, 0, p3f, 0, p3c);
		p3 = p3f;
		Arrays.sort(p3);
		for (int i = 0; i < p3c; i++) {
			int ix = field.getX(p3[i]) + 1;
			int iy = field.getY(p3[i]) + 1;
			if(i > 0) sb.append(", ");
			sb.append((char)(64 + ix)).append(iy);
		}
		return sb.toString();
	}
	
}
