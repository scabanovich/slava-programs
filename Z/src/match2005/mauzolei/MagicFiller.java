package match2005.mauzolei;

public class MagicFiller {
	MauzoleiField field;
	int[] form;
	int[] sequence;
	Equation[][] positionToEquations;

	int[] state;
	int[][] usedNumbers;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public void setField(MauzoleiField field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setSequence(int[] sequence) {
		this.sequence = sequence;		
	}
	
	public void setPositionToEquations(Equation[][] positionToEquations) {
		this.positionToEquations = positionToEquations;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.size];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		usedNumbers = new int[24][10];
		wayCount = new int[sequence.length + 1];
		way = new int[sequence.length + 1];
		ways = new int[sequence.length + 1][10];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == sequence.length) return;
		int p = sequence[t];
		for (int v = 0; v < 10; v++) {
			if(usedNumbers[form[p]][v] == 1) continue;
			boolean ok = true;
			for (int i = 0; i < positionToEquations[p].length && ok; i++) {
				Equation eq = positionToEquations[p][i];
				eq.add(v);
				ok = eq.isCorrect();
				eq.remove(v);
			}
			if(ok) {
				ways[t][wayCount[t]] = v;
				wayCount[t]++;
			}
		}
		if(t > 8 && t < 80) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int k = (int)((i + 1) * Math.random());
			if(k == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][k];
			ways[t][k] = c;
		}
		if(wayCount[t] > 2) wayCount[t] = 2;
	}

	void move() {
		int p = sequence[t];
		int v = ways[t][way[t]];
		state[p] = v;
		usedNumbers[form[p]][v] = 1;
		for (int i = 0; i < positionToEquations[p].length; i++) {
			Equation eq = positionToEquations[p][i];
			eq.add(v);
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = sequence[t];
		int v = ways[t][way[t]];
		state[p] = -1;
		usedNumbers[form[p]][v] = 0;
		for (int i = 0; i < positionToEquations[p].length; i++) {
			Equation eq = positionToEquations[p][i];
			eq.remove(v);
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 70;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
				System.out.println(t);
				printSolution();
			}
			if(t == sequence.length) {
				printSolution();
				return;
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < field.size; i++) {
			String s = form[i] < 0 ? "+" : state[i] < 0 ? "*" : "" + state[i];
			System.out.print(" " + s);
			if(field.x[i] == field.xSize - 1) {
				System.out.println("");
				if(field.y[i] == field.ySize - 1) {
					System.out.println("");
				}
			}
		}
	}

}
