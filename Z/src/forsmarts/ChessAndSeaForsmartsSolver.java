package forsmarts;

import ik6_1.*;

import com.slava.common.CellSet;

public class ChessAndSeaForsmartsSolver {
	static char[] CHESS_FIGURES = {'Q', 'R', 'B', 'N', 'K'};
	static int QUEEN = 0;
	static int ROOK = 1;
	static int BOUND = 2;
	static int KNIGHT = 3;
	static int KING = 4;

	static int CHESS_STEP = 0;
	static int SHIP_STEP = 1;
	
	Figure[] figureDelegates = new Figure[]{
			(Figure)new Queen(), 
			(Figure)new Rook(), 
			(Figure)new Bound(), 
			(Figure)new Knight(), 
			(Figure)new King()};

	ChessAndSeaField field;
	int[] ships = {4,3,3,2,2,2,1,1,1,1};
	int[] figures = {QUEEN, ROOK, BOUND, KNIGHT, KING};
	
	int[] chessForm;

	CellSet shipCells;
	CellSet chessCells;
	CellSet occupiedCells;
	CellSet shipNeighbourCells;

	int usedShipsCount;

	int t;
	int[] wayCount;
	int[] way;
	int[] waysK; // kind - 0 - chess, 1 - ship
	int[][] waysP;
	int[][] waysC; // for chess figure; for ship direction

	int solutionCount;
	
	public ChessAndSeaForsmartsSolver() {}

	public void setField(ChessAndSeaField f) {
		field = f;
	}
	
	public void setChessForm(int[] chessForm) {
		this.chessForm = chessForm;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		shipCells = new CellSet(field.getSize());
		chessCells = new CellSet(field.getSize());
		occupiedCells = new CellSet(field.getSize());
		shipNeighbourCells = new CellSet(field.getSize());
		
		usedShipsCount = 0;
		
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
		if(t < figures.length) {
			srchChessFigure();
		} else {
			srchShip();
		}
	}
	
	void srchChessFigure() {
		waysK[t] = CHESS_STEP;
		int f = figures[t];
		for (int p = 0; p < chessForm.length; p++) {
			if(chessForm[p] != 1 || occupiedCells.value(p) != 0) continue;
			waysP[t][wayCount[t]] = p;
			waysC[t][wayCount[t]] = f;
			wayCount[t]++;
		}
	}

	void srchShip() {
		waysK[t] = SHIP_STEP;
		if(usedShipsCount == ships.length) return;
		if(!checkFigures()) return;
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
		chessCells.add(p, f + 1);
		occupiedCells.add(p);
	}
	
	void addShip() {
		int p = waysP[t][way[t]];
		int d = waysC[t][way[t]];
		int length = ships[usedShipsCount];
		for (int i = 0; i < length; i++) {
			shipCells.add(p, usedShipsCount + 1);
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
		chessCells.remove(p, f + 1);
		occupiedCells.remove(p);
	}
	
	void removeShip() {
		usedShipsCount--;
		int p = waysP[t][way[t]];
		int d = waysC[t][way[t]];
		int length = ships[usedShipsCount];
		for (int i = 0; i < length; i++) {
			shipCells.remove(p, usedShipsCount + 1);
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
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(isSolution()) {
				solutionCount++;
				System.out.println("Solution");
				printSolution();
//				debug();
			}			
		}		
	}

	boolean isSolution() {
		return usedShipsCount == ships.length 
			&& checkFigures()
			&& chechRestrictions()
			;
	}
	
	boolean chechRestrictions() {
//		return checkColumn(5, 4);  //first problem
		return checkRow(5, 5);     //second problem
	}
	
	boolean checkRow(int iy, int n) {
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(shipCells.value(p) > 0) --n;
		}
		return n == 0;
	}
	boolean checkColumn(int ix, int n) {
		for (int iy = 0; iy < field.getWidth(); iy++) {
			int p = field.getIndex(ix, iy);
			if(shipCells.value(p) > 0) --n;
		}
		return n == 0;
	}
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			char c = '+';
			if(shipCells.value(i) > 0) {
				c = 'x';
			} else if(chessCells.value(i) > 0) {
				c = CHESS_FIGURES[chessCells.value(i) - 1];
			}
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("Key: " + getKey());
//		System.out.println("Code=" + getCode());
	}
	
	void debug() {
		for (int p = 0; p < field.getSize(); p++) {
			int f = chessCells.value(p) - 1;
			if(f < 0) continue;
			System.out.print(CHESS_FIGURES[f]);
			figureDelegates[f].debug(p);
			System.out.println("");
		}
	}

	boolean isChessOrShip(int p) {
		return chessCells.value(p) > 0 || shipCells.value(p) > 0;
	}
	
	boolean checkFigures() {
		for (int p = 0; p < field.getSize(); p++) {
			int f = chessCells.value(p) - 1;
			if(f < 0) continue;
			if(!figureDelegates[f].check(p)) return false;
		}
		return true;
	}
	
	String getKey() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getSize(); i++) {
			int k = shipCells.value(i) - 1;
			if(k < 0) continue;
			if(ships[k] != 1) continue;
			char ch = (char)(65 + field.getX(i));
			int cy = field.getY(i) + 1;
			sb.append("" + ch + "" + cy + ",");
		}		
		return sb.toString();
	}

	abstract class Figure {
		int[] distributionA;
		int[] distributionB;
		boolean check(int p) {
			int k = usedShipsCount == ships.length ? 1 : 
				usedShipsCount == 0 ? -1 :
				ships[usedShipsCount] == ships[usedShipsCount - 1] ? ships[usedShipsCount] + 1 : 
				ships[usedShipsCount - 1];
			if(k < 0) return true;
			boolean fin = usedShipsCount == ships.length;
			makeDistribution(p);
			for (int i = 1; i < distributionB.length; i++) {
				if(fin && distributionB[i] != 1) return false;
//				if(distribution[i] > 1) return false;
				if(i >= k && distributionB[i] == 0) return false;
			}			
			return true;
		}
		abstract void collect(int p);
		
		void makeDistribution(int p) {
			distributionA = new int[ships.length];
			collect(p);
			distributionB = new int[5];
			for (int i = 0; i < distributionA.length; i++) {
				if(distributionA[i] > 0) distributionB[ships[i]]++;
			}
		}
		
		void debug(int p) {
			makeDistribution(p);
			for (int i = 1; i < distributionB.length; i++) {
				System.out.print(" " + distributionB[i]);
			}
			
		}
		
		void registerShip(int q) {
			if(q < 0 || shipCells.value(q) == 0) return;
			distributionA[shipCells.value(q) - 1]++;
		}
	}

	class King extends Figure {
		void collect(int p) {
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				registerShip(q);
			}
		}
	}
	class LongRangeFigure extends Figure {
		int[] directions;
		void collect(int p) {
			for (int i = 0; i < directions.length; i++) {
				int d = directions[i];
				int q = field.jump(p, d);
				while(q >= 0 && occupiedCells.value(q) == 0) {
					q = field.jump(q, d);
				}
				registerShip(q);
			}
		}
	}

	class Queen extends LongRangeFigure {
		public Queen() {
			directions = new int[]{0,1,2,3,4,5,6,7};
		}
	}

	class Rook extends LongRangeFigure {
		public Rook() {
			directions = new int[]{0,1,2,3};
		}
	}

	class Bound extends LongRangeFigure {
		public Bound() {
			directions = new int[]{4,5,6,7};
		}
	}

	static int[][] KNIGHT_JUMPS = {
		{1,2},{2,1},{-1,2},{2,-1},{1,-2},{-2,1},{-1,-2},{-2,-1}
	};
	class Knight extends Figure {
		void collect(int p) {
			for (int i = 0; i < KNIGHT_JUMPS.length; i++) {
				int q = field.jumpXY(p, KNIGHT_JUMPS[i][0], KNIGHT_JUMPS[i][1]);
				registerShip(q);
			}
		}
	}
}
