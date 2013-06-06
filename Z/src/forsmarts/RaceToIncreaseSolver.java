package forsmarts;

import com.slava.common.TwoDimField;

public class RaceToIncreaseSolver {
	TwoDimField field;
	int[] data;
	
	int[] state;
	
	int t;
	int[] values;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysD, waysL;
	
	int wayLength;
	int longestWay;

	
	public RaceToIncreaseSolver() {}
	
	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[data.length];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		values = new int[data.length + 1];
		place = new int[data.length + 1];
		wayCount = new int[data.length + 1];
		way = new int[data.length + 1];
		waysD = new int[data.length + 1][field.getSize()];
		waysL = new int[data.length + 1][field.getSize()];
		values[0] = -1;
		t = 0;
		wayLength = 0;
		longestWay = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == data.length) return;
		if(t == 0) {
			for (int p = 0; p < field.getSize(); p++) {
				if(data[p] >= 0) {
					waysD[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		} else {
			for (int d = 0; d < 6; d++) {
				if(t > 1 && d == waysD[t - 1][way[t - 1]]) continue;
				int p = place[t - 1];
				int s = data[p];
				int l = 0;
				while(true) {
					p = field.jump(p, d);
					l++;
					if(p < 0 || data[p] < 0 || state[p] >= 0) break;
					s += data[p];
					if(s > values[t - 1]) {
						waysD[t][wayCount[t]] = d;
						waysL[t][wayCount[t]] = l;
						wayCount[t]++;
					}
				}
			}
		}
	}
	
	
	void move() {
		int d = waysD[t][way[t]];
		int l = waysL[t][way[t]];
		if(t == 0) {
			int p = d;
			values[t] = -1;
			place[t] = p;
			state[p] = t;
		} else {
			wayLength += l;
			int p = place[t - 1];
			int s = data[p];
			for (int i = 0; i < l; i++) {
				p = field.jump(p, d);
				s += data[p];
				state[p] = t;
			}
			values[t] = s;
			place[t] = p;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = waysD[t][way[t]];
		int l = waysL[t][way[t]];
		if(t == 0) {
			int p = d;
			state[p] = -1;
		} else {
			wayLength -= l;
			int p = place[t - 1];
			for (int i = 0; i < l; i++) {
				p = field.jump(p, d);
				state[p] = -1;
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
			if(wayLength > longestWay) {
				longestWay = wayLength;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Way length=" + wayLength);
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] < 0) ? '.' : (char)(97 + state[i]);
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		System.out.println(getSolutionKey());
	}
	
	String getSolutionKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(1 + wayLength).append(';');
		int ix = field.getX(place[0]);
		int iy = field.getY(place[0]);
		char xc = (char)(65 + ix);
		int yc = iy + 1;
		sb.append(xc).append(yc).append(';');
		for (int i = 1; i < t; i++) {
			if(i > 1) sb.append(',');
			sb.append(values[i]);
		}
		return sb.toString();
	}

}
