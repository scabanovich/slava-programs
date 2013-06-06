package champ2006;

import com.slava.common.RectangularField;

public class SegmentedLinePaker {
	PathSegment[] segments;
	RectangularField field;
	
	int diffSegmentCount;
	
	int[] form;
	
	int start;
	int end;
	
	int[] usedSegments;
	int[] state;
	int vacancies;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	public SegmentedLinePaker() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setSegments(PathSegment[] s) {
		segments = s;
	}
	
	public void setStartAndEnd(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		wayCount = new int[segments.length + 1];
		way = new int[segments.length + 1];
		ways = new int[segments.length + 1][segments.length];
		place = new int[segments.length + 1];
		usedSegments = new int[segments.length];
		vacancies = 0;
		for (int i = 0; i < form.length; i++) if(form[i] > 0) vacancies++;
		t = 0;
		place[0] = start;
		state[start] = 0;
		vacancies--;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!isEndReachable()) return;
		for (int i = 0; i < segments.length; i++) {
			if(usedSegments[segments[i].index] > 0) continue;
			if(canAdd(segments[i].path)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	boolean isEndReachable() {
		if(state[end] >= 0) return true;
		int v = 1;
		int[] stack = new int[field.getSize()];
		stack[0] = end;
		state[end] = 100;
		int c = 0;
		while(c < v) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || state[q] >= 0 || form[q] == 0) continue;
				state[q] = 100;
				stack[v] = q;
				v++;
			}
			++c;
		}
		for (int i = 0; i < v; i++) {
			state[stack[i]] = -1;
		}
		return v == vacancies;
	}
	
	boolean canAdd(int[] path) {
		int p = place[t];
		for (int i = 0; i < path.length; i++) {
			p = field.jump(p, path[i]);
			if(p < 0 || form[p] == 0 || state[p] >= 0) return false;
			if(p == end && (i < path.length - 1 || vacancies > path.length + 1)) return false;
		}
		return true;
	}
	
	void move() {
		int s = ways[t][way[t]];
		usedSegments[segments[s].index]++;
		int[] path = segments[s].path;
		int p = place[t];
		for (int i = 0; i < path.length; i++) {
			p = field.jump(p, path[i]);
			state[p] = t + 1;
			vacancies--;
		}
		place[t + 1] = p;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int s = ways[t][way[t]];
		usedSegments[segments[s].index]--;
		int[] path = segments[s].path;
		int p = place[t];
		for (int i = 0; i < path.length; i++) {
			p = field.jump(p, path[i]);
			state[p] = -1;
			vacancies++;
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
				printSolution();
				System.out.println("");
			}
			if(vacancies == 0) {
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			char c = form[i] == 0 ? '+' : (char)(97 + state[i]);
			System.out.print(" " + c);
			if(field.getWidth() - 1 == field.getX(i)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}
	
	
}
