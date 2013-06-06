package forsmarts;

import java.util.*;

public class RoundAndRoundLoopsEnumerator {
	RoundAndRoundField field;
	int maxCornerCount;
	
	RoundAndRoundLoopFilter loopFilter;
	
	int[] state;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int cornerCount;
	
	int loopCount;
	List loops;
	
	public RoundAndRoundLoopsEnumerator() {}
	
	public void setField(RoundAndRoundField f) {
		field = f;
	}
	
	public void setMaxCornerCount(int c) {
		maxCornerCount = c;
	}
	
	public void setLoopFilter(RoundAndRoundLoopFilter f) {
		loopFilter = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		wayCount = new int[field.getSize()];
		place = new int[field.getSize()];
		way = new int[field.getSize()];
		ways = new int[field.getSize()][field.getSize()];		
		cornerCount = 0;
		t = 0;
		loopCount = 0;
		loops = new ArrayList();
	}
	
	void srch() {
		wayCount[t] = 0;
		if(cornerCount > maxCornerCount) return;
		if(t > 1 && place[t - 1] == place[0]) return;
		if(t == 0) {
			for (int p = 0; p < field.getSize(); p++) {
				int c = 0;
				for (int d = 0; d < 3; d++) if(field.jump(p, d) >= 0) ++c;
				if(c >= 2) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		} else {
			int p = place[t - 1];
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q < 0 || q < place[0] 
				   || (t > 1 && q == place[t - 2]) 
				   || state[q] > 0) continue;
				ways[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
	}
	
	void move() {
		if(t == 0) {
			place[t] = ways[t][way[t]];
		} else {
			int p = place[t - 1];
			int d = ways[t][way[t]];
			int q = field.jump(p, d);
			state[q]++;
			place[t] = q;
			if(t == 1 || d != ways[t - 1][way[t - 1]]) {
				cornerCount++;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		if(t == 0) {
			//nothing
		} else {
			int p = place[t - 1];
			int d = ways[t][way[t]];
			int q = field.jump(p, d);
			state[q]--;
			if(t == 1 || d != ways[t - 1][way[t - 1]]) {
				cornerCount--;
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
			if(t > 1 && place[t - 1] == place[0] 
			    && cornerCount <= maxCornerCount
			    && place[1] <= place[t - 2]
			    ) {
				onLoopFound();
			}
		}
	}
	
	void onLoopFound() {
		loopCount++;
//		System.out.println(t + " " + cornerCount);
//		for (int i = 0; i < t; i++) {
//			System.out.print(" " + field.getX(place[i]) + ":" + field.getY(place[i]));
//		}
//		System.out.println("");
		makeLoopState();
	}
	
	void makeLoopState() {
		int[][] lines = new int[3][field.getWidth()];
		int[][] ds = new int[state.length][6];
		for (int i = 1; i < t; i++) {
			int p1 = place[i - 1];
			int p2 = place[i];
			int d1 = ways[i][way[i]];
			int d2 = field.reverse[d1];
			ds[p1][d1] = 1;
			ds[p2][d2] = 1;
			if(d1 % 3 == 0) {
				lines[0][field.getY(p1)]++;
			} else if(d1 % 3 == 1) {
				lines[1][getZ(p1)]++;
			} else if(d1 % 3 == 2) {
				lines[2][field.getX(p1)]++;
			}
		}
		int[] ts = new int[field.getTriangleCount()];
		int[] stack = new int[state.length * 2];
		int v = 1;
		int c = 0;
		int d0 = ways[1][way[1]];
		if(d0 == 2) d0 = 1;
		int tr = field.getTriangleIndex(place[0], d0);
		ts[tr] = 1;
		stack[0] = tr;
		while(c < v) {
			tr = stack[c];
			for (int d = 0; d < 3; d++) {
				int pc = field.getTriangleJumpBorderStart(tr, d);
				if(pc < 0) continue;
				if(ds[pc][d] > 0) continue;
				int trn = field.jumpTriangle(tr, d);
				if(trn < 0 || ts[trn] > 0) continue;
				ts[trn] = 1;
				stack[v] = trn;
				v++;
			}
			++c;
		}
//		System.out.println(v);
//		for (int d = 0; d < 3; d++) {
//			for (int i = 0; i < lines[d].length; i++) {
//				System.out.print(" " + lines[d][i]);
//			}
//			System.out.println("");
//		}
//		System.out.println("");
		RoundAndRoundLoop loop = new RoundAndRoundLoop();
		loop.cornerCount = cornerCount;
		loop.lines = lines;
		loop.triangleState = ts;
		boolean ok = loopFilter.check(loop);
//		System.out.println(ok);
		if(ok) {
			loops.add(loop);
		}
	}
	
	int getZ(int p) {
		return field.getY(p) - field.getX(p) + field.length;
	}
	
	public RoundAndRoundLoop[] getLoops() {
		return (RoundAndRoundLoop[])loops.toArray(new RoundAndRoundLoop[0]);
	}

}
