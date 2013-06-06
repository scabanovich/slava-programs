package match2005;

import com.slava.common.RectangularField;

public class DigitSolver {
	RectangularField field;
	DigitFigure[] figures;
	
	int[] restriction;

	int[] state;
	int[] point;
	int startPoint;
	int endPoint;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int weight;
	int minusCount;
	int[] digitCount;
	
	int maxWeight = 120;
	
	int flips;

	public DigitSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setRestriction(int[] r) {
		restriction = r;
	}
	
	public void setFigures(DigitFigure[] figures) {
		this.figures = figures;
	}
	
	public void setPoints(int start, int end) {
		startPoint = start;
		endPoint = end;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		if(restriction != null) {
			for (int i = 0; i < state.length; i++) 
				if(restriction[i] > 0) state[i] = 20;
		}
		point = new int[100];
		wayCount = new int[100];
		way = new int[100];
		ways = new int[100][50];
		t = 0;
		weight = 0;
//		maxWeight = 0;
		minusCount = 0;
		digitCount = new int[6];
		point[0] = startPoint;
		flips = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t > 0 && point[t] == endPoint) return;
		
//			if(t == 8 && state[field.position(0,8)] < 0) return;
//			if(t == 8 && state[field.position(0,7)] < 0) return;
		
		for (int fi = 0; fi < figures.length; fi++) {
			if(!canPut(figures[fi])) continue;
			if(figures[fi].weight < 0 && minusCount >= 3) continue;
			
			if(figures[fi].weight == 2 && t > 2 && t < 20) {
				if(digitCount[2] > 2) continue;
			}
			if(figures[fi].weight == 1 && t > 2 && t < 31) {
				if(t == 10 && digitCount[1] > 2) continue;
				if(t < digitCount[1] * 4) continue;
				//no 1 three times in a row
				int fi1 = ways[t - 1][way[t - 1]];
				int fi2 = ways[t - 2][way[t - 2]];
				if(figures[fi1].weight == 1 && figures[fi2].weight == 1) continue;
			}
			
			ways[t][wayCount[t]] = fi;
			++wayCount[t];
		}
		if(t < 11) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int) ((i + 1) * Math.random());
			if(i == j) continue;
			int c = ways[t][i];
			ways[t][i]= ways[t][j];
			ways[t][j] = c;
		}
		if(t > 0 && t < 10 && wayCount[t] > 8) wayCount[t] = 8;
	}
	
	boolean canPut(DigitFigure f) {
		int[][] path = f.getPath();
		int p = point[t];
		for (int i = 1; i < path.length; i++) {
			int dx = path[i][0];
			int dy = path[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(q < 0) return false;
			if(q == startPoint && startPoint != endPoint) return false;
			if(state[q] >= 0) {
//				if(q != point[0] || i != path.length - 1) 
				return false;
			}
			if(t > 0 && q == endPoint && i != path.length - 1) return false;
		}
		return true;
	}
	
	void move() {
		int fi = ways[t][way[t]];
		put(figures[fi]);
		++t;
		srch();
		way[t] = -1;
	}
	
	void put(DigitFigure f) {
		int[][] path = f.getPath();
		int p = point[t];
		for (int i = 1; i < path.length; i++) {
			int dx = path[i][0];
			int dy = path[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(state[q] < 0) state[q] = t;
			if(i == path.length - 1) point[t + 1] = q;
		}
		int dw = f.weight;
		if(dw > 0) {
			weight += dw;
			++digitCount[dw];
		} else {
			minusCount++;
			weight -= minusCount;
		}
	}
	
	void back() {
		--t;
		int fi = ways[t][way[t]];
		remove(figures[fi]);
	}
	
	void remove(DigitFigure f) {
		int[][] path = f.getPath();
		int p = point[t];
		for (int i = 1; i < path.length; i++) {
			int dx = path[i][0];
			int dy = path[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(state[q] == t) state[q] = -1;
		}
		int dw = f.weight;
		if(dw > 0) {
			weight -= dw;
			--digitCount[dw];
		} else {
			weight += minusCount;
			minusCount--;
		}
	}
	
	int[][] limits = {
		{40000, 50}, 
		{100000, 70},
		{400000, 90},
		{5000000, 100},
		{20000000, 110},
		{1000000000, 122}
	};
	void anal() {
		System.out.println("Starting maxWeight=" + maxWeight);
		srch();
		way[t] = -1;
		int wm = 40;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			int dist = getDistance();
			if(dist < 0 || (dist > 0 && getEstimation() < maxWeight)) {
				wayCount[t] = 0;
			} else {
				int w = weight - getDeltaWeight(dist);
				if(w > maxWeight) {
					maxWeight = w;
					printSolution();
				}
				if(w > wm) {
					wm = w;
					if(w > 90) System.out.println("w=" + wm + " t=" + t);
					if(w > maxWeight - 10) {
						printSolution();
					}
				}
			}
			if(wayCount[t] == 0) {
				flips++;
				for (int i = 0; i < limits.length; i++) {
					if(flips % limits[i][0] == 0 && wm < limits[i][1]) {
						System.out.println("time out");
						return;
					}
				}
			}
		}
	}
	
	int[] distances;
	int[] stack;
	
	int getDistance() {
		if(endPoint == point[t]) return 0;
		if(stack == null) {
			distances = new int[field.getSize()];
			stack = new int[field.getSize()];
		}
		for (int i = 0; i < field.getSize(); i++) distances[i] = -1;
		distances[point[t]] = 0;
		stack[0] = point[t];
		int dist = -1;
		int v = 1;
		int c = 0;
		while(c < v) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q == endPoint && dist < 0) {
					dist = distances[p] + 1;
				}
				if(q < 0 || distances[q] >= 0 || state[q] >= 0) continue;
				distances[q] = distances[p] + 1;
				stack[v] = q;
				v++;
			}
			++c;
		}
		return dist;
	}

	int getEstimation() {
		int estimation = weight;
		
			if(weight > 100) {
				return maxWeight;
			}
		if(endPoint == point[t]) return weight;
		if(stack == null) {
			distances = new int[field.getSize()];
			stack = new int[field.getSize()];
		}
		for (int i = 0; i < field.getSize(); i++) distances[i] = -1;
		distances[point[t]] = 0;
		stack[0] = point[t];
		int v = 1;
		int c = 0;
		while(c < v) {
			int p = stack[c];
			for (int dx = -1; dx < 2; dx++) {
				for (int dy = -1; dy < 2; dy++) {
					if(dy == 0 && dx == 0) continue;
					int q = field.jumpXY(p, dx, dy);
					if(q < 0 || distances[q] >= 0 || state[q] >= 0) continue;
					distances[q] = distances[p] + 1;
					stack[v] = q;
					v++;
				}
			}
			++c;
		}
		int n = 0;
		int isolated = 0;
		for (int p = 0; p < distances.length; p++) {
			if(distances[p] > 0 && distances[p] < 30) ++n;
			else if(state[p] < 0) ++isolated;
		}
//		if(isolated > 20) return 0;
		if(n > 100) {
			estimation += (n + 1) * 48 / 100;
		} else if(n > 50) {
			estimation += (n + 1) * 52 / 100;
		} else {
			estimation += (n * 2 + 2) / 3;
		}
		return estimation;
	}

	int getDeltaWeight(int dist) {
		if(dist == 0) return 0;
		return (2 * minusCount + dist + 1) * dist / 2;
	}
	
	void printSolution() {
		System.out.println("Weight=" + maxWeight);
		for (int i = 0; i < field.getSize(); i++) {
			char c = state[i] < 0 ? '+' : (char)(97 + (state[i] % 26));
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
	}

}
