package forsmarts;

import com.slava.common.TwoDimField;

public class WorldWarSolver {
	TwoDimField field;
	int[] form;
	int[] data;
	int[] datavicinity;

	WorldWarPlacements.Placement[] placements;
	int piecesCount;
	
	int[] state;
	int[] pieceUsage;
	int[] occupancy;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	int treeCount;

	public WorldWarSolver() {}
	
	public void setField(TwoDimField field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setPlacements(WorldWarPlacements ps) {
		this.placements = ps.placements;
		piecesCount = ps.piecesCount;
	}
	
	public void setData(int[] data) {
		this.data = data;
		datavicinity = new int[data.length];
		for (int p = 0; p < data.length; p++) {
			if(data[p] < 0) continue;
			datavicinity[p] = 1;
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && form[q] > 0) datavicinity[q] = 1;
			}
		}
	}
	
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		pieceUsage = new int[piecesCount + 1];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		occupancy = new int[field.getSize()];
		int mt = field.getSize() / 4;
		wayCount = new int[mt];
		way = new int[mt];
		ways = new int[mt][placements.length];
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		int[] ds = distributePlacements();
		if(!isOk(ds)) return;
		if(isFinished()) return;
		int b = t == 0 ? 0 : ways[t - 1][way[t - 1]] + 1;
		for (int i = b; i < placements.length; i++) {
			if(canAdd(placements[i].places)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	int[] distributePlacements() {
		int[] ds = new int[field.getSize()];
		int b = t == 0 ? 0 : ways[t - 1][way[t - 1]] + 1;
		for (int i = b; i < placements.length; i++) {
			int[] ps = placements[i].places;
			if(!canAdd(ps)) continue;
			for (int j = 0; j < ps.length; j++) {
				int p = ps[j];
				ds[p] = 1;
				for (int d = 0; d < 6; d++) {
					int q = field.jump(p, d);
					if(q >= 0 && form[q] > 0) ds[q] = 1;
				}
			}
		}
		return ds;
	}
	
	boolean canAdd(int[] r) {
		boolean ok = false;
		for (int i = 0; i < r.length; i++) {
			if(state[r[i]] >= 0) return false;
			if(datavicinity[r[i]] > 0) ok = true;
		}
		return ok;
	}
	
	boolean isOk(int[] ds) {
		for (int i = 0; i < data.length; i++) {
			if(data[i] < 0) continue;
			if(data[i] < occupancy[i]) return false;
			if(data[i] > occupancy[i] && ds[i] == 0) return false;
		}
		return true;
	}
	
	void move() {
		int i = ways[t][way[t]];
		add(placements[i].places);
		pieceUsage[placements[i].index]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int[] ps) {
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = t;
			occupancy[p]++;
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && form[q] > 0) occupancy[q]++;
			}
		}
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		remove(placements[i].places);
		pieceUsage[placements[i].index]--;
	}
	
	void remove(int[] ps) {
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = -1;
			occupancy[p]--;
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && form[q] > 0) occupancy[q]--;
			}
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
			if(isFinished()) {
				++solutionCount;
				//if(solutionCount < 10) 
					printSolution();
			}
		}
	}
	
	boolean isFinished() {
		for (int i = 0; i < data.length; i++) {
			if(data[i] >= 0 && data[i] != occupancy[i]) return false;
		}
		return true;
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] < 0) ? '.' : (char)(97 + state[i]);
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
		for (int i = 0; i < pieceUsage.length; i++) System.out.print(" " + pieceUsage[i]);
		System.out.println("");
		System.out.println("");
		
//		System.out.println(getSolutionKey());
	}

}
