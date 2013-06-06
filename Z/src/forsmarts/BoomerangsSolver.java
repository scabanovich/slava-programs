package forsmarts;

public class BoomerangsSolver {
	BoomerangsField field;
	
	int[] state;
	int vacanciesCount;
	
	int[] distribution;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int treeCount;
	int solutionCount;

	public BoomerangsSolver() {}
	
	public void setField(BoomerangsField f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		distribution = new int[field.getSize()];
		vacanciesCount = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(field.form[i] == 1) ++vacanciesCount;
		}
		wayCount = new int[100];
		way = new int[100];
		ways = new int[100][200];
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(vacanciesCount == 0) return;
		getDistribution();
		int p = getNarrowestPlace();
		if(p < 0 || distribution[p] == 0) return;
		for (int k = 0; k < field.placements.length; k++) {
			if(!canAdd(k) || !contains(k, p)) continue;
			ways[t][wayCount[t]] = k;
			wayCount[t]++;
		}
	}
	
	boolean canAdd(int k) {
		int[] placement = field.placements[k];
		for (int i = 0; i < placement.length; i++) {
			if(field.form[placement[i]] == 0 || state[placement[i]] > 0) return false;
		}
		return true;
	}
	
	boolean contains(int k, int p) {
		int[] placement = field.placements[k];
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p) return true;
		}
		return false;
		
	}
	
	void getDistribution() {
		for (int i = 0; i < field.getSize(); i++) distribution[i] = 0;
		for (int k =0; k < field.placements.length; k++) {
			if(!canAdd(k)) continue;
			int[] placement = field.placements[k];
			for (int i = 0; i < placement.length; i++) {
				distribution[placement[i]]++;
			}
		}
	}
	
	int getNarrowestPlace() {
		int pc = -1;
		int wc = 10000;
		for (int p = 0; p < field.getSize(); p++) {
			if(field.form[p] == 0 || state[p] > 0) continue;
			if(distribution[p] < wc) {
				wc = distribution[p];
				pc = p;
			}
		}
		return pc;
	}
	
	void move() {
		int k = ways[t][way[t]];
		int[] placement = field.placements[k];
		for (int i = 0; i < placement.length; i++) {
			state[placement[i]] = t + 1;
			vacanciesCount--;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = ways[t][way[t]];
		int[] placement = field.placements[k];
		for (int i = 0; i < placement.length; i++) {
			state[placement[i]] = 0;
			vacanciesCount++;
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
			if(vacanciesCount == 0) {
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("t=" + t);
		for (int i = 0; i < field.getSize(); i++) {
			char c = field.form[i] == 0 ? '+' : (char)(97 + state[i]);
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		int[] ns = getBoomerangNumbers();
		for (int i = 0; i < ns.length; i++) {
			if(i > 0) System.out.print(",");
			System.out.print(ns[i]);
		}
		System.out.println("");
	}
	
	int[] getBoomerangNumbers() {
		int[] res = new int[field.getHeight()];
		int[][] u = new int[field.getHeight()][t + 1];
		for (int i = 0; i < field.getSize(); i++) {
			if(field.form[i] == 0) continue;
			int y = field.getY(i);
			int k = state[i];
			if(u[y][k] == 0) res[y]++;
			u[y][k]++;
		}
		return res;
	}
	
}
