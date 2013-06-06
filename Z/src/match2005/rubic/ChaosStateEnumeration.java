package match2005.rubic;

import java.util.ArrayList;

public class ChaosStateEnumeration implements RubicConstants {
	
	int[] stateOfCubes;
	int[] colorOfSquares;
	int[][] colorDistribution; //[face][color]
	
	int[] cornerCubeUsage;
	int[] edgeCubeUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	ArrayList listOfSquareStates;
	
	public ChaosStateEnumeration() {}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		stateOfCubes = new int[CUBE_COUNT];
		for (int i = 0; i < CUBE_COUNT; i++) stateOfCubes[i] = -1;
		colorOfSquares = new int[SQUARE_COUNT];
		for (int i = 0; i < SQUARE_COUNT; i++) colorOfSquares[i] = -1;
		colorDistribution = new int[FACE_COUNT][FACE_COUNT];
		for (int c = 0; c < FACE_COUNT; c++) colorDistribution[c][c] = 2;
		cornerCubeUsage = new int[CORNER_CUBE_COUNT];
		edgeCubeUsage = new int[EDGE_CUBE_COUNT];
		
		wayCount = new int[CUBE_COUNT + 1];
		way = new int[CUBE_COUNT + 1];
		ways = new int[CUBE_COUNT + 1][CUBE_STATE_COUNT];
		
		t = 0;
		solutionCount = 0;
		listOfSquareStates = new ArrayList();
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == CUBE_COUNT) return;
		if(t == CORNER_CUBE_COUNT && !RubicUtil.isTotalCornerOrientationZero(stateOfCubes)) return;
		for (int s = 0; s < CUBE_STATE_COUNT; s++) {
			if(!canPut(s)) continue;
			ways[t][wayCount[t]] = s;
			wayCount[t]++;
		}
	}
	
	boolean canPut(int s) {
		if(t < 8) {
			int cube = RubicUtil.CORNER_INDEX[s];
			if(cornerCubeUsage[cube] > 0) return false;
			for (int k = 0; k < 3; k++) {
				int f1 = RubicUtil.getCornerSquareIndex(t, k);
				int f2 = RubicUtil.getCornerSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				if(c1 > 6 || c2 > 6) {
					System.out.println("");
				}
				if(colorDistribution[c1][c2] >= 2) return false;
			}			
		} else {
			int cube = RubicUtil.EDGE_INDEX[s];
			if(edgeCubeUsage[cube] > 0) return false;
			int s0 = t - 8;
			for (int k = 0; k < 2; k++) {
				int f1 = RubicUtil.getEdgeSquareIndex(s0, k);
				int f2 = RubicUtil.getEdgeSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				if(colorDistribution[c1][c2] >= 2) return false;
				if(!checkNeighbours(f1, c2)) return false;
			}			
		}
		return true;
	}
	
	boolean checkNeighbours(int place, int color) {
		int[] h = RubicUtil.NEIGHBOURS[place];
		for (int i = 0; i < h.length; i++) {
			if(colorOfSquares[h[i]] == color) return false;
		}		
		return true;
	}
	
	void move() {
		int s = ways[t][way[t]];
		stateOfCubes[t] = s;
		if(t < 8) {
			int cube = RubicUtil.CORNER_INDEX[s];
			cornerCubeUsage[cube]++;
			for (int k = 0; k < 3; k++) {
				int f1 = RubicUtil.getCornerSquareIndex(t, k);
				int f2 = RubicUtil.getCornerSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				colorDistribution[c1][c2]++;
				colorOfSquares[f1] = c2;
			}			
		} else {
			int cube = RubicUtil.EDGE_INDEX[s];
			edgeCubeUsage[cube]++;
			int s0 = t - 8;
			for (int k = 0; k < 2; k++) {
				int f1 = RubicUtil.getEdgeSquareIndex(s0, k);
				int f2 = RubicUtil.getEdgeSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				colorDistribution[c1][c2]++;
				colorOfSquares[f1] = c2;
			}			
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int s = ways[t][way[t]];
		stateOfCubes[t] = -1;
		if(t < 8) {
			int cube = RubicUtil.CORNER_INDEX[s];
			cornerCubeUsage[cube]--;
			for (int k = 0; k < 3; k++) {
				int f1 = RubicUtil.getCornerSquareIndex(t, k);
				int f2 = RubicUtil.getCornerSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				colorDistribution[c1][c2]--;
				colorOfSquares[f1] = -1;
			}			
		} else {
			int cube = RubicUtil.EDGE_INDEX[s];
			edgeCubeUsage[cube]--;
			int s0 = t - 8;
			for (int k = 0; k < 2; k++) {
				int f1 = RubicUtil.getEdgeSquareIndex(s0, k);
				int f2 = RubicUtil.getEdgeSquareIndex(s, k);
				int c1 = RubicUtil.DEFAULT_SQUARE_COLORS[f1];
				int c2 = RubicUtil.DEFAULT_SQUARE_COLORS[f2];
				colorDistribution[c1][c2]--;
				colorOfSquares[f1] = -1;
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
			++way[t];
			move();
			if(t == CUBE_COUNT && RubicUtil.isTotalEdgeOrientationZero(stateOfCubes) 
					&& RubicUtil.isEvenCubePermutation(stateOfCubes)
				) {
				RubicState state = getCurrentRubicState();
				int addedSymmetries = isNewSolution(state);
				if(addedSymmetries > 0) {
					solutionCount++;
//					if(solutionCount == 1) printColorOfSquares();
					if(solutionCount >= 55) {
						System.out.println("variant " + solutionCount);
						findPath(state); //60?   40-45 50-54 (53!) 1-28-  55- 
					}
//					if(solutionCount % 1000 == 0) System.out.println(solutionCount);
				}
			}
		}
	}
	
	RubicState getCurrentRubicState() {
		RubicState state = new RubicState();
		System.arraycopy(stateOfCubes, 0, state.corners, 0, CORNER_CUBE_COUNT);
		System.arraycopy(stateOfCubes, CORNER_CUBE_COUNT, state.edges, 0, EDGE_CUBE_COUNT);
		return state;
	}
	
	int isNewSolution(RubicState state) {
		int q = RubicTransform.addToList(state, listOfSquareStates);
		if(q > 0) System.out.println("Simmetries=" + q + " " + " Total square states=" + listOfSquareStates.size());
		return q;
	}
	
	private void findPath(RubicState state) {
		state.print();
		RubicSolver solver = new RubicSolver();
		solver.setState(state);
		solver.setMaximumTime(15);
		solver.setOutFileName("RubicSolutions.txt");
		if(solver.execute() != null) {
			System.out.println("Solution found!");
//			throw new RuntimeException("Solution found!");
		}
	}
	
	void printColorOfSquares() {
		for (int i = 0; i < SQUARE_COUNT; i++) {
			System.out.print(colorOfSquares[i]);
			if(i % 8 == 7) System.out.print(" ");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		ChaosStateEnumeration p = new ChaosStateEnumeration();
		p.solve();
		System.out.println("solutionCount=" + p.solutionCount);
	}

}

//54225343 50225404 14003314 51225010 532251

//6 7 11 18 21 12 0 1   12 13 16 15 14 23 18 19 20 21 22 17
//T F' L' B' F2 R' T F L F' L' F T' H' R' T2
//U2 R B U F' L F L' F' U' R F2 D L F U'

//variant 53
//Corners: 8 9 18 19 20 21 14 15 Edges: 18 7 16 17 14 3 12 13 11 22 9 20
//R F B U D R' U2 D2 F2 B2 R F' B' U' D' R'
//R D U B F R' B2 F2 D2 U2 R D' U' B' F' R'

//variant 62
//Corners: 8 14 18 20 19 21 9 15 Edges: 16 21 12 5 18 23 14 1 19 10 15 8
//R U2 F R2 L2 F2 U2 D2 F' R' L U D B2 R
//R' B2 D' U' L' R F D2 U2 F2 L2 R2 F' U2 R'
