package slava.puzzles.seabattlef.analysis;

import java.util.ArrayList;

import slava.puzzles.seabattlef.model.SeaBattleFirstConstants;
import slava.puzzles.seabattlef.model.SeaBattleFirstPuzzle;
import com.slava.common.RectangularField;

/**
 * bValues - [d][i] - give size of first ship seen from i-th cell of 
 * border situated in direction d from center
 *   
 * @author slava
 *
 */

public class SeaBattleFirstSolver implements SeaBattleFirstConstants {
	RectangularField field;
	RectangularField octafield;
	
	SeaBattleFirstPuzzle puzzle;
	int size;

	int[] state;
	int[] restrictions;
	int[] required;
	int[] ships;
	
	int[][] bValues;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	int[][] waysS;
	int[] waysEdge;
	int[] waysEdgeIndex;

	int solutionCount;
	ArrayList solutions = new ArrayList();
	int solutionLimit;
	boolean randomizing;
	
	public SeaBattleFirstSolver() {}

	public void setField(RectangularField field) {
		this.field = field;
		octafield = new RectangularField();
		octafield.setOrts(RectangularField.DIAGONAL_ORTS);
		octafield.setSize(field.getWidth(), field.getHeight());
	}
	
	public void setPuzzle(SeaBattleFirstPuzzle puzzle) {
		this.puzzle = puzzle;
		bValues = puzzle.getBValues();
	}
	
	public void setSolutionLimit(int k) {
		solutionLimit = k;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void solve() {
		checkBValues();
		init();
		anal();
	}
	
	void checkBValues() {
		
	}
	
	void init() {
		ships = (int[])puzzle.getShips().clone();
		state = new int[field.getSize()];
		required = new int[field.getSize()];
		restrictions = new int[field.getSize()];
		
		initInitialData();
		
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][200];
		waysD = new int[field.getSize() + 1][200];
		waysS = new int[field.getSize() + 1][200];
		waysEdge = new int[field.getSize() + 1];
		waysEdgeIndex = new int[field.getSize() + 1];
	
		t = 0;
		solutions.clear();
		solutionCount = 0;
//		distribution = new int[field.getSize()][8];
	}
	
	void initInitialData() {
		for (int p = 0; p < state.length; p++) {
			int v = puzzle.getData()[p];
			if(v == EMPTY) continue;
			if(v == WATER) {
				restrictInitially(p);
			} else if(v == SINGLE) {
				required[p] = 1;
				for (int d = 0; d < 8; d++) {
					int q = octafield.jump(p, d);
					restrictInitially(q);
				}
			} else if(v == MIDDLE) {
				required[p] = 1;
				for (int d = 1; d < 8; d += 2) {
					int q = octafield.jump(p, d);
					restrictInitially(q);
				}
			} else {
				required[p] = 1;
				int exq = (v == EAST_END) ? 4 :
						  (v == WEST_END) ? 0 :
						  (v == NORTH_END) ? 2 :
						  (v == SOUTH_END) ? 6 :
							  -1; 
				for (int d = 0; d < 8; d++) {
					int q = octafield.jump(p, d);
					if(q < 0) continue;
					if(d == exq) {
						required[q] = 1;
						for (int d2 = 1; d2 < 8; d2 += 2) {
							int q2 = octafield.jump(q, d2);
							restrictInitially(q2);
						}
					} else {
						restrictInitially(q);
					}
				}
			}
		}
	}
	
	void restrictInitially(int q) {
		if(q < 0) return;
		if(required[q] > 0) {
			throw new RuntimeException("Data is contradictory at (" + field.getX(q) + "," + field.getY(q) + ")");
		}
		restrictions[q]++;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isCompleted()) return;
		if(!checkRows()) return;
		int[] row = getRowToFill();
		if(row != null) {
			fillWaysForRow(row[0], row[1]);
		} else {
			waysEdge[t] = -1;
			waysEdgeIndex[t] = -1;
			int p = getPlaceToFill();
			if(p >= 0) {
				fillWaysForPlace(p);
			}
		}		
	}
	
	void addWay(int p, int d, int s) {
		waysP[t][wayCount[t]] = p;
		waysD[t][wayCount[t]] = d;
		waysS[t][wayCount[t]] = s;
		wayCount[t]++;
	}
	
	boolean checkRows() {
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < bValues[d].length; i++) {
				if(isRowWrong(d, i)) return false;
			}
		}
		return true;
	}
	
	int[] reverse = {2,3,0,1};
	int[] direct = {0,1,0,1};
	int[] perpendicular = {1,0,1,0};
	
	boolean isRowWrong(int d, int i) {
		int s = bValues[d][i];
		if(s <= 0) return false;
		int p = getRowStart(d, i);
		boolean opp = false;
		while(p >= 0) {
			if(state[p] > 0) {
				return state[p] != s && (ships[s] == 0 || !opp);
			} else if(restrictions[p] == 0) {
				opp = true;
			}
			p = field.jump(p, reverse[d]);
		}
		return ships[s] == 0 || !opp;
	}
	
	int getRowStart(int d, int i) {
		return (d == 0) ? field.getIndex(field.getWidth() - 1, i) :
			   (d == 1) ? field.getIndex(i, field.getHeight() - 1) :
			   (d == 2) ? field.getIndex(0, i) :
			   (d == 3) ? field.getIndex(i, 0) :
			   -1;
	}
	
	boolean isRowUnresolved(int d, int i) {
		int s = bValues[d][i];
		if(s <= 0) return false;
		int p = getRowStart(d, i);
		while(p >= 0) {
			if(state[p] > 0) {
				return state[p] != s;
			}
			p = field.jump(p, reverse[d]);
		}
		return true;
	}

	int getRowVacancies(int d, int i) {
		int s = bValues[d][i];
		if(s <= 0) return 1000;
		int p = getRowStart(d, i);
		int v = 0;
		while(p >= 0) {
			if(state[p] > 0) {
				return v;
			} else if(restrictions[p] == 0) {
				v++;
			}
			p = field.jump(p, reverse[d]);
		}
		return v;
	}
	
	int[] getRowToFill() {
		int db = -1;
		int ib = -1;
		int wb = -100;
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < bValues[d].length; i++) {
				if(!isRowUnresolved(d, i)) continue;
				int w = 2 * bValues[d][i] - getRowVacancies(d, i);
				if(w > wb) {
					db = d;
					ib = i;
					wb = w;
				}
			}
		}
		return db < 0 ? null : new int[]{db, ib};
	}
	
	int getPlaceToFill() {
		for (int p = 0; p < state.length; p++) {
			if(state[p] <= 0 && restrictions[p] == 0) return p;
		}
		return -1;
	}
	
	void fillWaysForRow(int d, int i) {
		waysEdge[t] = d;
		waysEdgeIndex[t] = i;
		int p = getRowStart(d, i);
		int s = bValues[d][i];
		if(ships[s] == 0) return;
		while(p >= 0 && state[p] <= 0) {
			if(restrictions[p] == 0) {
				if(canAdd(p, direct[d], s)) {
					addWay(p, direct[d], s);
				}
				if(s > 1) {
					int q = p;
					int dq = perpendicular[d] + 2;
					for (int j = 0; j < s; j++) {
						if(q < 0 || state[q] > 0 || restrictions[q] > 0) break;
						if(canAdd(q, perpendicular[d], s)) {
							addWay(q, perpendicular[d], s);
						}
						q = field.jump(q, dq);
					}
				}
			}
			p = field.jump(p, reverse[d]);
		}
	}
	
	void fillWaysForPlace(int p) {
		if(required[p] <= 0) {
			addWay(p, 0, 0);
		}
		for (int s = 1; s < ships.length; s++) {
			if(ships[s] == 0) continue;
			int dm = (s == 1) ? 1 : 2;
			for (int d = 0; d < dm; d++) {
				int q = p;
				int qd = d + 2;
				for (int j = 0; j < s; j++) {
					if(q < 0 || state[q] > 0 || restrictions[q] > 0) break;
					if(canAdd(q, d, s)) {
						addWay(q, d, s);
					}
					q = field.jump(q, qd);
				}
			}
		}
	}

	boolean canAdd(int p, int d, int s) {
		if(restrictions[p] > 0 || state[p] > 0) return false;
		if(ships[s] == 0) return false;
//		if(s == 1 && d != 0) return false;
		for (int i = 0; i < s; i++) {
			if(p < 0 || restrictions[p] > 0 || state[p] > 0) return false;
			p = field.jump(p, d);
		}		
		return true;
	}
	
	void move() {
		int ed = waysEdge[t];
		int ei = waysEdgeIndex[t];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]++;
		} else {
			int q = p;
			ships[s]--;
			for (int i = 0; i < s; i++) {
				state[q] = s;
				for (int d2 = 0; d2 < 8; d2++) {
					int q2 = octafield.jump(q, d2);
					if(q2 >= 0) restrictions[q2]++;
				}
				q = field.jump(q, d);
			}
			if(ed >= 0) {
				int ep = getRowStart(ed, ei);
				while(ep >= 0 && state[ep] == 0) {
					restrictions[ep]++;
					ep = field.jump(ep, reverse[ed]);
				}
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int ed = waysEdge[t];
		int ei = waysEdgeIndex[t];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]--;
		} else {
			if(ed >= 0) {
				int ep = getRowStart(ed, ei);
				while(ep >= 0 && state[ep] == 0) {
					restrictions[ep]--;
					ep = field.jump(ep, reverse[ed]);
				}
			}
			int q = p;
			ships[s]++;
			for (int i = 0; i < s; i++) {
				state[q] = 0;
				for (int d2 = 0; d2 < 8; d2++) {
					int q2 = octafield.jump(q, d2);
					if(q2 >= 0) restrictions[q2]--;
				}
				q = field.jump(q, d);
			}
		}
	}

	void anal() {
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
//				System.out.println("---->" + tm + " " + getShipCount());
			}
			if(isCompleted() && checkRowsFinally()) {
				int[] solution = createVisualSolution();
				if(isSolutionOk(solution)) {
					solutionCount++;
					if(solutionCount <= solutionLimit) {
						solutions.add(solution);
					}
//					for (int i = 0; i < field.getSize(); i++) {
//						distribution[i][solution[i]]++;
//					}
					if(solutionLimit > 0 && solutionCount > solutionLimit) {
						return;
					}
				}
			}
		}		
	}
	
	boolean isCompleted() {
		for (int i = 1; i < ships.length; i++) {
			if(ships[i] > 0) return false;
		}
		return true;
	}
	
	boolean checkRowsFinally() {
		return checkRows();
	}
	
	int getShipCount() {
		int c = 0;
		for (int i = 1; i < ships.length; i++) {
			c += ships[i];
		}
		return c;
	}
	
	int[] createVisualSolution() {
		int[] solution = new int[state.length];
		for (int i = 0; i < state.length; i++) solution[i] = WATER;
		for (int k = 0; k < t; k++) {
			int p = waysP[k][way[k]];
			int d = waysD[k][way[k]];
			int s = waysS[k][way[k]];
			if(s == 0) continue;
			if(s == 1) {
				solution[p] = SINGLE;
			} else {
				solution[p] = (d == 0) ? WEST_END : NORTH_END;
				int q = field.jump(p, d);
				for (int i = 2; i < s; i++) {
					solution[q] = MIDDLE;
					q = field.jump(q, d);
				}
				solution[q] = (d == 0) ? EAST_END : SOUTH_END;
			}
		}
		return solution;
	}
	
	boolean isSolutionOk(int[] solution) {
		int[] data = puzzle.getData();
		for (int i = 0; i < data.length; i++) {
			if(data[i] != EMPTY && data[i] != solution[i]) return false;
		}
		return true;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public ArrayList getSolutions() {
		return solutions;
	}
	
}
