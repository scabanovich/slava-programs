package forsmarts;

import com.slava.common.RectangularField;

public class SquaresSolver {
	RectangularField field;
	int squaresCount;
	int[] sums;
	int[] checkedSumPlaces; //{p,s}
	
	int[] state;
	int[][] restrictions;
	int[] currentSums;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public SquaresSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setSquaresCount(int s) {
		squaresCount = s;
	}
	
	public void setSums(int[] s) {
		sums = s;
	}
	
	public void solve() {
		init();
		anal();
	}	
	
	void init() {
		state = new int[field.getSize()];
		restrictions = new int[field.getSize()][2];
		currentSums = new int[field.getSize()];
		
		wayCount = new int[squaresCount + 1];
		way = new int[squaresCount + 1];
		ways = new int[squaresCount + 1][field.getSize()];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == squaresCount) return;
		if(!checkSumsT()) return;
		int length = squaresCount - t;
		for (int ix = 0; ix < field.getWidth() - length; ix++) {
			for (int iy = 0; iy < field.getHeight() - length; iy++) {
				if(canAdd(ix, iy, length)) {
					int p = field.getIndex(ix, iy);
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean checkSumsT() {
		for (int p = 0; p < field.getSize(); p++) {
			if(sums[p] >= 0 && currentSums[p] > sums[p]) return false;
		}
		return true;
	}
	
	boolean checkSumsF() {
		for (int p = 0; p < field.getSize(); p++) {
			if(sums[p] >= 0 && currentSums[p] != sums[p]) return false;
		}
		return true;
	}
	
	boolean canAdd(int ix, int iy, int length) {
		for (int i = 0; i < length; i++) {
			int p = field.getIndex(ix + i, iy);
			if(restrictions[p][0] > 0) return false;
			p = field.getIndex(ix + i, iy + length);
			if(restrictions[p][0] > 0) return false;
			p = field.getIndex(ix, iy + i);
			if(restrictions[p][1] > 0) return false;
			p = field.getIndex(ix + length, iy + i);
			if(restrictions[p][1] > 0) return false;
		}
		return true;
	}
	
	void move() {
		int length = squaresCount - t;
		int p = ways[t][way[t]];
		state[p] = length;
		add(field.getX(p), field.getY(p), length);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int ix, int iy, int length) {
		for (int i = -1; i < length + 1; i++) {
			int p = field.getIndex(ix + i, iy);
			if(p >= 0) restrictions[p][0]++;
			p = field.getIndex(ix + i, iy + length);
			if(p >= 0) restrictions[p][0]++;
			p = field.getIndex(ix, iy + i);
			if(p >= 0) restrictions[p][1]++;
			p = field.getIndex(ix + length, iy + i);
			if(p >= 0) restrictions[p][1]++;
		}
		for (int dx = 0; dx < length; dx++) {
			for (int dy = 0; dy < length; dy++) {
				int p = field.getIndex(ix + dx, iy + dy);
				currentSums[p] += length;
			}
		}
	}
	
	void back() {
		--t;
		int length = squaresCount - t;
		int p = ways[t][way[t]];
		state[p] = 0;
		remove(field.getX(p), field.getY(p), length);
	}
	
	void remove(int ix, int iy, int length) {
		for (int i = -1; i < length + 1; i++) {
			int p = field.getIndex(ix + i, iy);
			if(p >= 0) restrictions[p][0]--;
			p = field.getIndex(ix + i, iy + length);
			if(p >= 0) restrictions[p][0]--;
			p = field.getIndex(ix, iy + i);
			if(p >= 0) restrictions[p][1]--;
			p = field.getIndex(ix + length, iy + i);
			if(p >= 0) restrictions[p][1]--;
		}
		for (int dx = 0; dx < length; dx++) {
			for (int dy = 0; dy < length; dy++) {
				int p = field.getIndex(ix + dx, iy + dy);
				currentSums[p] -= length;
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
			if(t == squaresCount && checkSumsF()) {
				solutionCount++;
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] == 0) continue;
			System.out.println(" " + field.getX(p) + ":" + field.getY(p) + "-" + state[p]);
		}
		System.out.println("");
		System.out.println("key=" + getKey());
	}
	
	String getKey() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getWidth() - 1; i++) {
			int p = field.getIndex(i, field.getHeight() - 2 - i);
			sb.append(" ").append(currentSums[p]);
		}
		return sb.toString();
	}

}
