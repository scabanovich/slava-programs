package forsmarts;

import com.slava.common.TwoDimField;

public class XPlusYLetters {
	static int X = 0;
	static int P = 1;
	static int Y = 2;
	XPlusYCircles circleProgram;
	TwoDimField field;
	
	int[] initialState;
	int[] circles;
	
	int[] state;
	int[] letterCount;
	int[][] hLetterUsage;
	int[][] vLetterUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public XPlusYLetters() {}
	
	public void setCircleProgram(XPlusYCircles p) {
		circleProgram = p;
		field = p.field;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			state[i] = -1;
		}

		int cc = 0;
		for (int i = 0; i < circleProgram.state.length; i++) {
			if(circleProgram.state[i] == 1) ++cc;
		}
		circles = new int[cc];
		int k = 0;
		for (int i = 0; i < circleProgram.state.length; i++) {
			if(circleProgram.state[i] == 1) {
				circles[k] = i;
				k++;
			}
		}

		hLetterUsage = new int[field.getHeight()][3];
		vLetterUsage = new int[field.getWidth()][3];
		letterCount = new int[3];

		wayCount = new int[30];
		way = new int[30];
		ways = new int[30][200];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == circles.length) return;
		int p = circles[t];
		if(circleProgram.initialState[p] == XPlusYRunner.Y) {
			addVar(p, Y);
		} else {
			for (int v = 0; v < 3; v++) addVar(p, v);
		}
	}
	
	void addVar(int p, int v) {
		if(hLetterUsage[field.getY(p)][v] > 0) return;
		if(vLetterUsage[field.getX(p)][v] > 0) return;
		if(letterCount[v] >= circles.length / 3) return;
		if(circleProgram.neighbours[p] > 0) {
			int n = circleProgram.getNeighbours(p, circleProgram.figureDirections[v]);
			if(n != circleProgram.neighbours[p]) return;
		}
		ways[t][wayCount[t]] = v;
		wayCount[t]++;
	}
	
	void move() {
		int p = circles[t];
		int v = ways[t][way[t]];
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int v) {
		state[p] = v;
		hLetterUsage[field.getY(p)][v] = 1;
		vLetterUsage[field.getX(p)][v] = 1;
		letterCount[v]++;
	}
	
	void back() {
		--t;
		int p = circles[t];
		int v = ways[t][way[t]];
		remove(p, v);
	}
	
	void remove(int p, int v) {
		state[p] = -1;
		hLetterUsage[field.getY(p)][v] = 0;
		vLetterUsage[field.getX(p)][v] = 0;
		letterCount[v]--;
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
//				System.out.println("t=" + tm);
			}
			if(t == circles.length) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	char[] ch = {'x', '+', 'y'};
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] < 0) ? '.' : ch[state[i]];
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		int[] sums = new int[3];
		for (int i = 0; i < circles.length; i++) {
			int p = circles[i];
			int v = state[p];
			int n = circleProgram.getNeighbours(p, circleProgram.figureDirections[v]);
			sums[v] += n;
		}
		for (int i = 0; i < sums.length; i++) {
			if(i > 0) System.out.print(", ");
			System.out.print(sums[i]);
		}
		System.out.println("");
	}
	

}
