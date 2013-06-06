package slava.puzzles.hexa.fence.analysis;

import java.util.ArrayList;

import com.slava.common.RectangularField;

public class HexaFenceSolver {
	int directionsCount = 6;
	RectangularField field;
	int[] form;
	int[] data;
	HexaFenceContinuityCheck continuityCheck = new HexaFenceContinuityCheck(directionsCount);
	
	int solutionLimit = -1;
	
	int[] state;
	int vacantPlaces;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;

	int treeCount;
	int solutionCount;
	ArrayList solutions = new ArrayList();

	public HexaFenceSolver() {}
	
	public void setDirectionCount(int d) {
		if(d == directionsCount) return;
		directionsCount = d;
		continuityCheck = new HexaFenceContinuityCheck(directionsCount);		
	}

	public void setField(RectangularField field) {
		this.field = field;
		continuityCheck.setField(field);
	}
	
	public void setForm(int[] form) {
		this.form = form;
		continuityCheck.setForm(form);
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = -1;
		vacantPlaces = 0;
		for (int p = 0; p < state.length; p++) {
			if(form[p] > 0) {
				vacantPlaces++;
			}
		}
		wayCount = new int[vacantPlaces + 1];
		way = new int[vacantPlaces + 1];
		place = new int[vacantPlaces + 1];
		ways = new int[vacantPlaces + 1][2];
		temp = new int[2];
		t = 0;
		solutions = new ArrayList();
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(vacantPlaces == 0) return;
		if(!checkData()) return;
		if(!continuityCheck.check(state)) return;
		int mwc = 3;
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0 || state[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < mwc) {
				mwc = wc;
				place[t] = p;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				if(wc == 1) break;
			}
		}
		if(mwc < 3) {
			wayCount[t] = mwc;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int v = 0; v < 2; v++) {
			state[p] = v;
			boolean b = checkData();
			state[p] = -1;
			if(b) {
				temp[wc] = v;
				wc++;
			}
		}
		return wc;
	}
	
	boolean checkData() {
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0 || data[p] < 0) continue;
			if(getMaxLength(p) < data[p]) return false;
			if(getMinLength(p) > data[p]) return false;			
		}
		return true;
	}
	
	int getMinLength(int p) {
		int c = 0;
		for (int d = 0; d < directionsCount; d++) {
			int q = field.jump(p, d);
			if(isFence(p, q)) ++c;
		}
		return c;
	}
	
	int getMaxLength(int p) {
		int c = 0;
		for (int d = 0; d < directionsCount; d++) {
			int q = field.jump(p, d);
			if(mayBeFence(p, q)) ++c;
		}
		return c;
	}
	
	boolean isFence(int p, int q) {
		if(q < 0 || form[q] == 0) {
			return state[p] == 1;
		} else {
			return state[p] >= 0 && state[q] >= 0 && state[p] != state[q];
		}
	}
	
	boolean mayBeFence(int p, int q) {
		if(q < 0 || form[q] == 0) {
			return state[p] != 0;
		} else if(state[p] < 0 || state[q] < 0) {
			return true;
		} else {
			return state[p] != state[q];
		}
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		vacantPlaces--;
		state[p] = v;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		vacantPlaces++;
		state[p] = -1;
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
//				System.out.println("---->" + tm);
			}
			if(wayCount[t] == 0) 
				treeCount++;
			if(treeCount > 300 && solutionLimit > 0) {
				solutionCount = solutionLimit + 1;
				return;
			}
			if(vacantPlaces == 0 && continuityCheck.check(state)) {
				int[] solution = (int[])state.clone();
				solutionCount++;
				if(solutionCount <= solutionLimit) {
					solutions.add(solution);
				}
				if(solutionLimit > 0 && solutionCount > solutionLimit) {
					return;
				}
			}
		}		
	}

	public int getSolutionCount() {
		return solutionCount;
	}
	
	public ArrayList getSolutions() {
		return solutions;
	}
	
}
