package champukr2013;

import java.util.Random;

import com.slava.common.RectangularField;

public class ChessFigures {
	boolean lookingForMinimum = false;
	RectangularField f;
	Figure[] figures = new Figure[]{new Queen(), new Rook(), new Rook(), new Bound(), new Bound(), new Knight(), new Knight()};
	int[] occupied;
	int[] state;
	int attacked;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int treeCount;
	int solutionCount;

	int bestAttacks = 50;

	public ChessFigures() {
		f = new RectangularField();
		f.setSize(8, 8);
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		occupied = new int[f.getSize()];
		for (int i = 0; i < occupied.length; i++) occupied[i] = -1;
		state = new int[f.getSize()];
		attacked = 0;
		
		wayCount = new int[figures.length + 1];
		way = new int[figures.length + 1];
		ways = new int[figures.length + 1][state.length];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t == figures.length) return;
		if(lookingForMinimum && attacked >= bestAttacks) return; //for minimum
		if(tooLittle()) return; // for maximum
		
		int min = 0;
		if(t > 0 && figures[t].getKind() == figures[t - 1].getKind()) {
			min = figures[t - 1].getPosition();
		}
		
		for (int p = min; p < state.length; p++) {
			if(state[p] == 0 && (t > 0 || isPlaceForFirstFigure(p)) && isPlaceForSecondBound(p)) {
				if(occupied[p] == -1) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		}
//		if(t < 4 && !lookingForMinimum) {
//			randomize();
//		}
	}
	
	Random seed = new Random();

	void randomize() {
		if(wayCount[t] > 1) {
			for (int i = 0; i < wayCount[t]; i++) {
				int j = i + seed.nextInt(wayCount[t] - i);
				int c = ways[t][i];
				ways[t][i] = ways[t][j];
				ways[t][j] = c;
			}
			if(wayCount[t] > 12) wayCount[t] = 12;
		}
	}

	boolean isPlaceForFirstFigure(int p) {
		int x = f.getX(p);
		int y = f.getY(p);
		return x < 4 && y <= x;
	}

	boolean isPlaceForSecondBound(int p) {
		if(t == 0) {
			return true;
		}
		if(figures[t].getKind() == 4 && figures[t - 1].getKind() == 4) {
			int p1 = figures[t - 1].getPosition();
			int c1 = (f.getX(p1) + f.getY(p1)) % 2;
			int c = (f.getX(p) + f.getY(p)) % 2;
			return c != c1;
		}
		return true;
	}

	boolean tooLittle() {
		if(lookingForMinimum || t != 3) return false;
		for (int i = 0; i < 3; i++) {
			compute();
		}
		int r = attacked;
		for (int i = 0; i < 3; i++) {
			undo();
		}
		return r != 39;
	}

	void move() {
		int pos = ways[t][way[t]];
		occupied[pos] = figures[t].getKind();
		figures[t].setPosition(pos);
//		figures[t].set(true);
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int pos = ways[t][way[t]];
		occupied[pos] = -1;
//		figures[t].set(false);
		figures[t].setPosition(-1);
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
//				if(treeCount >= 7000000) return;
			}
			if(t > tm) {
				System.out.println(t);
				tm = t;
			}
			if(t == figures.length) {
				compute();
				if(isSolution()) {
					bestAttacks = attacked;
					solutionCount++;
					printSolution();
//					return;
				}
				undo();
			}
		}
	}

	void compute() {
		for (int i = 0; i < t; i++) {
			figures[i].set(true);
		}
	}

	void undo() {
		for (int i = t - 1; i >= 0; i--) {
			figures[i].set(false);
		}
	}

	public boolean isSolution() {
		return t == figures.length && (lookingForMinimum ? attacked <= bestAttacks : attacked >= bestAttacks);
	}

	void printSolution() {
		System.out.println("attacked=" + attacked);
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(f.isRightBorder(i)) System.out.println("");
		}
		System.out.println("-figures-");
		for (int i = 0; i < state.length; i++) {
			int k = occupied[i] < 0 ? 0 : occupied[i];
			System.out.print(" " + k);
			if(f.isRightBorder(i)) System.out.println("");
		}
		System.out.println("---");
		
		
	}

	void handleAttack(int p, boolean add) {
		if(p >= 0) {
			if(add) {
				addAttack(p);
			} else {
				removeAttack(p);
			}
		}
	}

	void addAttack(int p) {
		state[p]++;
		if(state[p] == 1) {
			attacked++;
		}
	}

	void removeAttack(int p) {
		state[p]--;
		if(state[p] == 0) {
			attacked--;
		}
	}

	class Figure {
		int pos;

		public void setPosition(int pos) {
			this.pos = pos;
		}

		public int getPosition() {
			return pos;
		}

		public void set(boolean add) {}

		public int getKind() {
			return 0;
		}
	}

	class Knight extends Figure {
		int[][] deltas = new int[][]{{1,2}, {2,1}, {-1,2}, {2,-1}, {1,-2}, {-2,1}, {-1,-2}, {-2,-1}};

		public void set(boolean add) {
			handleAttack(pos, add);
			for (int i = 0; i < deltas.length; i++) {
				int p = f.jumpXY(pos, deltas[i][0], deltas[i][1]);
				handleAttack(p, add);
			}
		}
		public int getKind() {
			return 1;
		}
	}

	class LongRange extends Figure {
		int[][] deltas = new int[0][];

		public void set(boolean add) {
			handleAttack(pos, add);
			for (int i = 0; i < deltas.length; i++) {
				int p = pos;
				while(true) {
					p = f.jumpXY(p, deltas[i][0], deltas[i][1]);
					if(p < 0) {
						break;
					} else {
						handleAttack(p, add);
						if(occupied[p] >= 0) {
							break;
						}
					}
				}
			}
		}
	}

	class Queen extends LongRange {
		public Queen() {
			deltas = new int[][]{{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};
		}
		public int getKind() {
			return 2;
		}
	}
	class Rook extends LongRange {
		public Rook() {
			deltas = new int[][]{{1,0}, {0,1}, {-1,0}, {0,-1}};
		}
		public int getKind() {
			return 3;
		}
	}
	class Bound extends LongRange {
		public Bound() {
			deltas = new int[][]{{1,1}, {-1,1}, {-1,-1}, {1,-1}};
		}
		public int getKind() {
			return 4;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChessFigures p = new ChessFigures();
		p.solve();
		System.out.println("Tree=" + p.treeCount + " " + p.attacked);
	}

}
