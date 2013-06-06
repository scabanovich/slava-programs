package ik6_1;

import java.util.HashSet;
import java.util.Set;

import com.slava.common.CellSet;

public class ChessAndSeaSolver {
	static char[] CHESS_FIGURES = {'Q', 'R', 'B', 'N', 'K'};
	Figure[] figureDelegates = new Figure[]{
		(Figure)new Queen(), 
		(Figure)new Rook(), 
		(Figure)new Bound(), 
		(Figure)new Knight(), 
		(Figure)new King()};
	
	static int CHESS_STEP = 0;
	static int SHIP_STEP = 1;
	
	static int QUEEN = 0;
	static int ROOK = 1;
	static int BOUND = 2;
	static int KNIGHT = 3;
	static int KING = 4;

	ChessAndSeaField field;
	int[] ships = {4,3,3,2,2,2,1,1,1,1};
	int[] chessWeights = {10, 6, 4, 3, 2};
	
	CellSet hittedCells;
	CellSet shipCells;
	CellSet chessCells;
	CellSet occupiedCells;
	CellSet shipNeighbourCells;
	
	int usedShipsCount;
	int chessWeight;
	int[] figureCount;

	int t;
	int[] wayCount;
	int[] way;
	int[] waysK; // kind - 0 - chess, 1 - ship
	int[][] waysP;
	int[][] waysC; // for chess figure; for ship direction
	
	int solutionCount;
	
	Set maxSolutionCodes = new HashSet();
	Set minSolutionCodes = new HashSet();

	public ChessAndSeaSolver() {}
	
	public void setField(ChessAndSeaField f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		hittedCells = new CellSet(field.getSize());
		shipCells = new CellSet(field.getSize());
		chessCells = new CellSet(field.getSize());
		occupiedCells = new CellSet(field.getSize());
		shipNeighbourCells = new CellSet(field.getSize());
		
		usedShipsCount = 0;
		chessWeight = 0;
		figureCount = new int[figureDelegates.length];

		wayCount = new int[50];
		way = new int[50];
		waysK = new int[50];
		waysP = new int[50][300];
		waysC = new int[50][300];

		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!notEnoughPlace()) return;
		int p = getShipNeighbourToHit();
		if(p >= 0) {
			srchChessFigure(p);
		} else {
			srchShip();
		}
	}
	
	boolean notEnoughPlace() {
		int free = 0;
		for (int p = 0; p < field.getSize(); p++) {
			if(occupiedCells.value(p) > 0 || shipNeighbourCells.value(p) > 0) continue;
			++free;
		}
		for (int i = usedShipsCount; i < ships.length; i++) {
			free -= ships[i];
		}
		return free >= 0;
	}
	
	int getShipNeighbourToHit() {
		for (int p = 0; p < field.getSize(); p++) {
			if(shipNeighbourCells.value(p) == 0) continue;
			if(occupiedCells.value(p) > 0) continue;
			return p;
		}
		if(usedShipsCount == ships.length) {
			for (int p = 0; p < field.getSize(); p++) {
				if(occupiedCells.value(p) > 0) continue;
				return p;
			}
		}
		return -1;
	}
	
	void srchChessFigure(int p) {
		waysK[t] = CHESS_STEP;
		int fb = 0;
			     //1; //no queens
		for (int f = fb; f < chessWeights.length; f++) {
//			if(f != KNIGHT && (f != ROOK || figureCount[ROOK] > 0) && (f != BOUND || figureCount[BOUND] > 1)) continue;
			if(chessWeight + chessWeights[f] > 19) continue;
			figureDelegates[f].srch(p);
		}
	}
	
	void srchShip() {
		waysK[t] = SHIP_STEP;
		if(usedShipsCount == ships.length) return;
		int length = ships[usedShipsCount];
		int p = 0;
		if(usedShipsCount > 0 && ships[usedShipsCount - 1] == length) {
			int t1 = t - 1;
			while(waysK[t1] != SHIP_STEP) --t1;
			p = waysP[t1][way[t1]] + 1;
		}
		int de = length == 1 ? 1 : 2;
		int db = 0;
			if(t == 0) {
				de = 1;
				p = 76;
			}
		for (; p < field.getSize(); p++) {
			for (int d = db; d < de; d++) {
				if(canAddShip(p, length, d)) {
					waysP[t][wayCount[t]] = p;
					waysC[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean canAddShip(int p, int length, int d) {
		for (int i = 0; i < length; i++) {
			if(p < 0 || occupiedCells.value(p) > 0 || shipNeighbourCells.value(p) > 0) return false;
			p = field.jump(p, d);
		}
		return true;
	}	
	
	void move() {
		if(waysK[t] == CHESS_STEP) {
			addChess();
		} else {
			addShip();
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void addChess() {
		int p = waysP[t][way[t]];
		int f = waysC[t][way[t]];
		figureDelegates[f].add(p);
	}
	
	void addShip() {
		int p = waysP[t][way[t]];
		int d = waysC[t][way[t]];
		int length = ships[usedShipsCount];
		for (int i = 0; i < length; i++) {
			shipCells.add(p);
			occupiedCells.add(p);
			for (int d1 = 0; d1 < 8; d1++) {
				int q = field.jump(p, d1);
				if(q >= 0) shipNeighbourCells.add(q);
			}
			p = field.jump(p, d);
		}
		usedShipsCount++;
	}
	
	void back() {
		--t;
		if(waysK[t] == CHESS_STEP) {
			removeChess();
		} else {
			removeShip();
		}
	}
	
	void removeChess() {
		int p = waysP[t][way[t]];
		int f = waysC[t][way[t]];
		figureDelegates[f].remove(p);
	}
	
	void removeShip() {
		usedShipsCount--;
		int p = waysP[t][way[t]];
		int d = waysC[t][way[t]];
		int length = ships[usedShipsCount];
		for (int i = 0; i < length; i++) {
			shipCells.remove(p);
			occupiedCells.remove(p);
			for (int d1 = 0; d1 < 8; d1++) {
				int q = field.jump(p, d1);
				if(q >= 0) shipNeighbourCells.remove(q);
			}
			p = field.jump(p, d);
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int max = 0;
		int min = 50;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
//			if(usedShipsCount > sc) {
//				sc = usedShipsCount;
//				System.out.println(sc);
//			}
//			printSolution();
//			System.out.println("");
			
			if(isSolution()) {
				solutionCount++;
				if(chessWeight >= max) {
					String code = getCode();
					if(chessWeight > max) {
						maxSolutionCodes.clear();
					}
					if(!maxSolutionCodes.contains(code)) {
						maxSolutionCodes.add(code);
						max = chessWeight;
						System.out.println("Max Weight=" + max);
						printSolution();
					}
				}
				if(chessWeight <= min) {
					String code = getCode();
					if(chessWeight < min) {
						minSolutionCodes.clear();
					}
					if(!minSolutionCodes.contains(code)) {
						minSolutionCodes.add(code);
						min = chessWeight;
						System.out.println("Min Weight=" + min);
						printSolution();
					}
				}
			}			
		}		
	}
	
	boolean isSolution() {
		return usedShipsCount == ships.length && occupiedCells.getCount() == field.getSize();
	}
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			char c = '.';
			if(shipCells.value(i) > 0) {
				c = 'x';
			} else if(chessCells.value(i) > 0) {
				c = CHESS_FIGURES[chessCells.value(i) - 1];
			} else if(hittedCells.value(i) > 0) {
				c = '+';
			}
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("Code=" + getCode());
	}
	static char[] FIGURE_LETTER = {'Q','R','B','N','K'};
	String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getSize(); i++) {
			if(chessCells.value(i) == 0) continue;
			if(sb.length() > 0) sb.append(", ");
			sb.append(FIGURE_LETTER[chessCells.value(i) - 1]);
			sb.append((char)(97 + field.getX(i)));
			sb.append(field.getY(i) + 1);
		}
		return sb.toString();
		
	}
	
	boolean isChessOrShip(int p) {
		return chessCells.value(p) > 0 || shipCells.value(p) > 0;
	}
	
	abstract class Figure {

		abstract protected void srch(int p);			
		abstract protected boolean canAddWay(int p);
		abstract protected void add(int p);
		abstract protected void remove(int p);
		
		protected final void addWay(int p, int f) {
			if(p < 0 || occupiedCells.value(p) > 0 || !canAddWay(p)) return;
			waysP[t][wayCount[t]] = p;
			waysC[t][wayCount[t]] = f;
			wayCount[t]++;			
		}
		protected final void add(int p, int f) {
			figureCount[f]++;
			chessCells.add(p, f + 1);
			chessWeight += chessWeights[f];
			occupiedCells.add(p);
		}
		protected final void remove(int p, int f) {
			figureCount[f]--;
			chessCells.remove(p, f + 1);
			chessWeight -= chessWeights[f];
			occupiedCells.remove(p);
		}
		protected final void doHit(int p) {
			hittedCells.add(p);
			occupiedCells.add(p);
		}
		protected final void unHit(int p) {
			hittedCells.remove(p);
			occupiedCells.remove(p);
		}
	}
	
	class LongRangeFigure extends Figure {
		int[] directions;
		int figure;

		protected void srch(int p) {
			addWay(p, figure);
			for (int i = 0; i < directions.length; i++) {
				int d = directions[i];
				int q = field.jump(p, d);
				while(q >= 0) {
					addWay(q, figure);
					q = field.jump(q, d);
				}
			}
		}
		
		protected boolean canAddWay(int p) {
			for (int i = 0; i < directions.length; i++) {
				int d = directions[i];
				int q = field.jump(p, d);
				while(q >= 0) {
					if(isChessOrShip(q)) return false;
					q = field.jump(q, d);
				}
			}
			return true;
		}

		protected void add(int p) {
			add(p, figure);
			for (int i = 0; i < directions.length; i++) {
				int d = directions[i];
				int q = field.jump(p, d);
				while(q >= 0) {
					doHit(q);
					q = field.jump(q, d);
				}
			}
		}
		
		protected void remove(int p) {
			remove(p, figure);
			for (int i = 0; i < directions.length; i++) {
				int d = directions[i];
				int q = field.jump(p, d);
				while(q >= 0) {
					unHit(q);
					q = field.jump(q, d);
				}
			}
		}
	}

	class Queen extends LongRangeFigure {
		public Queen() {
			figure = QUEEN;
			directions = new int[]{0,1,2,3,4,5,6,7};
		}
	}

	class Rook extends LongRangeFigure {
		public Rook() {
			figure = ROOK;
			directions = new int[]{0,1,2,3};
		}
	}

	class Bound extends LongRangeFigure {
		public Bound() {
			figure = BOUND;
			directions = new int[]{4,5,6,7};
		}
	}

	static int[][] KNIGHT_JUMPS = {
		{1,2},{2,1},{-1,2},{2,-1},{1,-2},{-2,1},{-1,-2},{-2,-1}
	};
	class Knight extends Figure {
		protected void srch(int p) {
			addWay(p, KNIGHT);
			for (int i = 0; i < KNIGHT_JUMPS.length; i++) {
				int q = field.jumpXY(p, KNIGHT_JUMPS[i][0], KNIGHT_JUMPS[i][1]);
				addWay(q, KNIGHT);
			}
		}
		
		protected boolean canAddWay(int p) {
			for (int i = 0; i < KNIGHT_JUMPS.length; i++) {
				int q = field.jumpXY(p, KNIGHT_JUMPS[i][0], KNIGHT_JUMPS[i][1]);
				if(q >= 0 && isChessOrShip(q)) return false;
			}
			return true;
		}
		protected void add(int p) {
			add(p, KNIGHT);
			for (int i = 0; i < KNIGHT_JUMPS.length; i++) {
				int q = field.jumpXY(p, KNIGHT_JUMPS[i][0], KNIGHT_JUMPS[i][1]);
				if(q >= 0) doHit(q);
			}
		}
		
		protected void remove(int p) {
			remove(p, KNIGHT);
			for (int i = 0; i < KNIGHT_JUMPS.length; i++) {
				int q = field.jumpXY(p, KNIGHT_JUMPS[i][0], KNIGHT_JUMPS[i][1]);
				if(q >= 0) unHit(q);
			}
		}
	}

	class King extends Figure {
		protected void srch(int p) {
			addWay(p, KING);
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				addWay(q, KING);
			}
		}
		
		protected boolean canAddWay(int p) {
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && isChessOrShip(q)) return false;
			}
			return true;
		}
		
		protected void add(int p) {
			add(p, KING);
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				if(q >= 0) doHit(q);
			}
		}
		
		protected void remove(int p) {
			remove(p, KING);
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				if(q >= 0) unHit(q);
			}
		}
	}
	
	void dump() {
		System.out.println("Ship");
		dump(shipCells);
		System.out.println("Chess");
		dump(chessCells);
		System.out.println("Hit");
		dump(hittedCells);
		System.out.println("Occupied");
		dump(occupiedCells);
		System.out.println("Neighbours");
		dump(shipNeighbourCells);
	}
	
	void dump(CellSet set) {
		System.out.println(set.getCount());
		for (int i = 0; i < field.getSize(); i++) {
			System.out.print(" " + set.value(i));
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

}
