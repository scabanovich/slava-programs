package olia.coloring;

public class ColorAnalysis {
	Field field;
	int size;
	int colorCount = 3;
	
	int[] values;
	int[] form;
	
	int timeLimit;
	
	int solutionCountLimit = 0;//30000;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;	
	
	public ColorAnalysis() {}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		size = field.getSize();
		values = new int[size];
		timeLimit = 0;
		for (int i = 0; i < size; i++) {
			if(form[i] == 1) {
				timeLimit++;
			}
		} 
		place = new int[timeLimit];
		int k = 0;
		for (int i = 0; i < size; i++) {
			if(form[i] == 1) {
				place[k] = i;
				++k;				
			}
		} 
		wayCount = new int[timeLimit + 1];
		way = new int[timeLimit + 1];
		ways= new int[timeLimit + 1][colorCount];
		t = 0;
		solutionCount = 0;
		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= timeLimit) return;
		int p = place[t];
		for (int q = 0; q < colorCount; q++) {
			if(canPut(p, q)) {
				ways[t][wayCount[t]] = q;
				++wayCount[t];
			}
		}
	}
	
	boolean canPut(int p, int q) {
		for (int d = 2; d < 4; d++) {
			int p1 = field.jump(d, p);
			if(p1 >= 0 && form[p1] == 1 && values[p1] == q) return false;
		}
		return true;
	}
	
	void move() {
		int q = ways[t][way[t]];
		int p = place[t];
		values[p] = q;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			if(t == timeLimit) {
				++solutionCount;
				if(solutionCountLimit > 0 && solutionCount >= solutionCountLimit) return;
			} 
		}		
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	

	public static void main(String[] args) {
	}
}
