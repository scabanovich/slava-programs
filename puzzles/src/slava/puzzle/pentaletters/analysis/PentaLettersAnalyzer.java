package slava.puzzle.pentaletters.analysis;

public class PentaLettersAnalyzer extends AbstractPentaLettersAnalyzer {
	protected boolean ignoreLetters = false;
	
	int t;
	int[] wayCount;
	int[][] ways;
	int[] way;
	int[] place;
	
	int solutionCountLimit = -1;
	int solutionCount;

	public void setIgnoreLetters(boolean b) {
		ignoreLetters = b;
		setSolutionCountLimit(1);
	}
	
	public void setSolutionCountLimit(int q) {
		solutionCountLimit = q;
	}

	public void solve() {
		if(!ignoreLetters) check();
		init();
		anal();
	}

	protected void init() {
		figureCount = field.getSize() / figureSize;
		field.resetGroups();
		groups = (int[])field.getGroups().clone();
		int size = field.getSize() + 1;
		wayCount = new int[size];
		ways = new int[size][100];
		way = new int[size];
		place = new int[size];
		t = 0;
		solutionCount = 0;
		solutions.clear();
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == figureCount) return;
		nextPlace();
		if(place[t] == field.getSize()) return;
		for (int i = 0; i < figures.length; i++) {
			if(!checkFigure(i, place[t])) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
		if(true) randomize();
	}

	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i == j) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}

	void nextPlace() {
		if(t == 0) {
			place[t] = 0;
		} else {
			place[t] = place[t - 1] + 1;
			while(place[t] < field.getSize() && groups[place[t]] >= 0) ++place[t];
		}
	}
	
	int[] temp = new int[figureSize];

	protected boolean checkFigure(int f, int p) {
		for (int i = 0; i < temp.length; i++) temp[i] = 0;
		for (int i = 0; i < figures[f].length; i++) {
			int k = field.jump(p, figures[f][i][0], figures[f][i][1]);
			if(k < 0 || groups[k] >= 0) return false;
			int v = field.getLetterAt(k);
			if(!ignoreLetters) {
				if(temp[v] > 0) return false;
				temp[v]++;
			}
		}
		return true;
	}

	void move() {
		int fg = ways[t][way[t]];
		int p = place[t];
		addFigure(fg, p);
		++t;
		srch();
		way[t] = -1;
	}

	void addFigure(int fg, int p) {
		for (int i = 0; i < figures[fg].length; i++) {
			int k = field.jump(p, figures[fg][i][0], figures[fg][i][1]);
			if(k >= 0) groups[k] = t;
		}
	}

	void back() {
		--t;
		int fg = ways[t][way[t]];
		int p = place[t];
		removeFigure(fg, p);
	}

	void removeFigure(int fg, int p) {
		for (int i = 0; i < figures[fg].length; i++) {
			int k = field.jump(p, figures[fg][i][0], figures[fg][i][1]);
			if(k >= 0) groups[k] = -1;
		}
	}

	protected void anal() {
		srch();
		way[t] = -1;
		//int maxt = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == figureCount) {
				solutionCount++;
				if(solutionCount < 5) {
					solutions.add(groups.clone());
				}
				if(solutionCount == solutionCountLimit) return;
			}
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
