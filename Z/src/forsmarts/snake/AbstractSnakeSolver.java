package forsmarts.snake;

public abstract class AbstractSnakeSolver {
	protected SnakeField field;

	//data section
	protected int snakeLength;

	//state section
	protected int[] state;
	protected int[] restriction;

	//move section
	protected int t;
	protected int[] place;
	protected int[] wayCount;
	protected int[] way;
	protected int[][] ways;

	protected int solutionCount;
	
	public AbstractSnakeSolver() {}
	
	public void setField(SnakeField field) {
		this.field = field;
	}
	
	public void setSnakeLength(int snakeLength) {
		this.snakeLength = snakeLength;
	}

	public void solve() {
		init();
		anal();
	}
	
	protected void init() {
		state = new int[field.getSize()];
		restriction = new int[field.getSize()];
		wayCount = new int[snakeLength + 1];
		way = new int[snakeLength + 1];
		ways = new int[snakeLength + 1][4];
		ways[0] = new int[field.getSize()];
		place = new int[snakeLength + 1];
		place[0] = -1;
		t = 0;
		solutionCount = 0;
	}

	protected void srch() {
		wayCount[t] = 0;
		if(t == snakeLength) return;
		if(!isOk()) return;
		if(t == 0 && place[0] < 0) {
			srchInitial();
			return;
		}
		int p = place[t];
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || state[q] > 0 || restriction[q] > 0) continue;
			if(canMove(q)) addWay(q);
		}
	}
	
	protected void addWay(int q) {
		ways[t][wayCount[t]] = q;
		wayCount[t]++;
	}
	
	/**
	 * Called if start place is not set.
	 */
	protected void srchInitial() {
		
	}
	
	/**
	 * Test if specific state data allows for this move.
	 * @param q
	 * @return
	 */
	protected abstract boolean canMove(int q);

	/**
	 * Check specific state data here at t.
	 * @return
	 */
	protected abstract boolean isOk();
		
	protected void move() {
		int p = ways[t][way[t]];
		state[p] = t + 2;
		place[t + 1] = p;
		if(t >= 1) {
			int q = place[t - 1];
			for (int d = 0; d < 8; d++) {
				int n = field.jump(q, d);
				if(n >= 0) restriction[n]++;
			}
		}
		onMove(p);
		++t;
		srch();
		way[t] = -1;
	}
	
	/**
	 * Update specific state data here
	 * @param p
	 */
	protected abstract void onMove(int p);
	
	protected void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = 0;
		if(t >= 1) {
			int q = place[t - 1];
			for (int d = 0; d < 8; d++) {
				int n = field.jump(q, d);
				if(n >= 0) restriction[n]--;
			}
		}
		onBack(p);
	}

	/**
	 * Update specific state data here
	 * @param p
	 */
	protected abstract void onBack(int p);

	protected void anal() {
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
				System.out.println(tm);
			}
			if(t == snakeLength && isSolution()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	/**
	 * Check specific state data here
	 * @return
	 */
	protected abstract boolean isSolution();

	protected void printSolution() {
		if(solutionCount > 10) {
			if(solutionCount % 100 == 0) System.out.println(solutionCount);
			return;
		}
		System.out.println("Solution found");
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] == 0) ? '.' : (char)(97 + ((state[i] - 1) % 26));
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
