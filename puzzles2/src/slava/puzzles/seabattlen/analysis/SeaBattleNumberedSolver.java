package slava.puzzles.seabattlen.analysis;

import java.util.ArrayList;

import slava.puzzles.seabattle.model.SeaBattleConstants;
import slava.puzzles.seabattlen.model.SeaBattleNumberedPuzzle;

import com.slava.common.RectangularField;

public class SeaBattleNumberedSolver implements SeaBattleConstants {
	RectangularField field;
	RectangularField octafield;

	SeaBattleNumberedPuzzle puzzle;
	int size;
	
	int[] state;
	int[] restrictions;
	int[] required;
	int[] ships;
	
	int[] hUsage;
	int[] vUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	int[][] waysS;
	
	int solutionCount;
	ArrayList solutions = new ArrayList();
	int solutionLimit;
	boolean randomizing;
	
	boolean makeDistribution = false;
	int[][] distribution;

	public SeaBattleNumberedSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
		octafield = new RectangularField();
		octafield.setOrts(RectangularField.DIAGONAL_ORTS);
		octafield.setSize(field.getWidth(), field.getHeight());
	}
	
	public void setPuzzle(SeaBattleNumberedPuzzle puzzle) {
		this.puzzle = puzzle;
	}
	
	public void setSolutionLimit(int k) {
		solutionLimit = k;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void setMakingDistribution(boolean b) {
		makeDistribution = b;
	}
	
	public void solve() {
		checkData();
		init();
		anal();
	}
	
	void checkData() {
		int[] ns = puzzle.getNumbers();
		for (int i = 0; i < field.getSize(); i++) {
			if(ns[i] < 0) throw new RuntimeException("All numbers must be set.");
		}
	}
	
	void init() {
		ships = (int[])puzzle.getShips().clone();
		state = new int[field.getSize()];
		required = new int[field.getSize()];
		restrictions = new int[field.getSize()];
		
		hUsage = new int[field.getHeight()];
		vUsage = new int[field.getWidth()];
		
		initInitialData();
		
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][200];
		waysD = new int[field.getSize() + 1][200];
		waysS = new int[field.getSize() + 1][200];
		
		t = 0;
		solutions.clear();
		solutionCount = 0;
		distribution = new int[field.getSize()][8];
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
		
		int[] ns = puzzle.getNumbers();
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int vc = puzzle.getVValues()[ix];
			if(vc < 0) continue;
			int c = 0;
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int p = field.getIndex(ix, iy);
				if(required[p] > 0) c += ns[p];
				if(ns[p] > vc) restrictInitially(p);
			}
			if(c > vc) {
				throw new RuntimeException("V-row " + ix + " is incorrect.");
			} else if(c == vc) {
				for (int iy = 0; iy < field.getHeight(); iy++) {
					int p = field.getIndex(ix, iy);
					if(required[p] == 0) restrictInitially(p);
				}
			}
		}
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int vc = puzzle.getHValues()[iy];
			if(vc < 0) continue;
			int c = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(required[p] > 0) c += ns[p];
				if(ns[p] > vc) restrictInitially(p);
			}
			if(c > vc) {
				throw new RuntimeException("H-row " + iy + " is incorrect.");
			} else if(c == vc) {
				for (int ix = 0; ix < field.getWidth(); ix++) {
					int p = field.getIndex(ix, iy);
					if(required[p] == 0) restrictInitially(p);
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
		if(hasContradiction()) return;
		int p = getBestPlace();
		if(p >= 0) {
			getWays(p);
		}
	}
	
	void addWay(int p, int d, int s) {
		waysP[t][wayCount[t]] = p;
		waysD[t][wayCount[t]] = d;
		waysS[t][wayCount[t]] = s;
		wayCount[t]++;
	}
	
	boolean checkRows() {
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int v = puzzle.getVValues()[ix];
			if(v >= 0 && v < vUsage[ix]) return false;
			if(v >= 0 && v > vUsage[ix] + getVFree(ix)) return false;
		}
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int v = puzzle.getHValues()[iy];
			if(v >= 0 && v < hUsage[iy]) return false;
			if(v >= 0 && v > hUsage[iy] + getHFree(iy)) return false;
		}		
		return true;
	}
	
	boolean hasContradiction() {
		for (int p = 0; p < required.length; p++) {
			if(state[p] == 0 && required[p] > 0 && restrictions[p] > 0) return true;
		}
		return false;
	}
	
	int getBestPlace() {
		int bp = -1;
		int bc = -100;
		for (int p = 0; p < state.length; p++) {
			if(state[p] > 0 || restrictions[p] > 0) continue;
			if(isRequired(p)) return p;
			int ix = field.getX(p), iy = field.getY(p);
			int hv = puzzle.getHValues()[iy], vv = puzzle.getVValues()[ix];
			int c = 0;
			if(hv > 0) c += 2 * hv;
			if(vv > 0) c += 2 * vv;
			c += field.getWidth() - getHFree(iy) + field.getHeight() - getVFree(ix);
			if(c > bc) {
				bc = c;
				bp = p;
			}
		}		
		return bp;
	}
	
	void getWays(int p) {
		if(!isRequired(p)) {
			addWay(p, 0, 0);
		}
		if(canAdd(p, 0, 1)) {
			addWay(p, 0, 1);
		}
		for (int d = 0; d < 2; d++) {
			int q = p;
			int sm = 1;
			while(sm < ships.length) {
				if(q < 0 || restrictions[q] > 0) break;
				for (int s = sm; s < ships.length; s++) {
					if(s > 1 && canAdd(q, d, s)) addWay(q, d, s);
				}
				q = field.jump(q, d + 2);
				sm++;
			}
		}
	}
	
	boolean isRequired(int p) {
		if(required[p] > 0) return true;
		int ix = field.getX(p), iy = field.getY(p);
		int hv = puzzle.getHValues()[iy], vv = puzzle.getVValues()[ix];
		if(hv >= 0 && hUsage[iy] + getHFree(iy) <= hv) return true;
		if(vv >= 0 && vUsage[ix] + getVFree(ix) <= vv) return true;		
		return false;
	}
	
	int getHFree(int iy) {
		int c = 0;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(state[p] == 0 && restrictions[p] == 0) c += puzzle.getNumbers()[p];
		}
		return c;
	}
	
	int getVFree(int ix) {
		int c = 0;
		for (int iy = 0; iy < field.getWidth(); iy++) {
			int p = field.getIndex(ix, iy);
			if(state[p] == 0 && restrictions[p] == 0) c += puzzle.getNumbers()[p];
		}
		return c;
	}
	
	boolean canAdd(int p, int d, int s) {
		if(restrictions[p] > 0 || state[p] > 0) return false;
		if(ships[s] == 0) return false;
		if(s == 1 && d != 0) return false;
		int[] ns = puzzle.getNumbers();
		int ix = field.getX(p), iy = field.getY(p);
		int hv = puzzle.getHValues()[iy], vv = puzzle.getVValues()[ix];
		int dh = ns[p], dv = ns[p];
		int q = p;
		for (int i = 1; i < s; i++) {
			q = field.jump(q, d);
			if(q < 0) return false;
			if(d == 0) dh += ns[q]; else dv += ns[q];
			
		}
		if(hv >= 0 && hUsage[iy] + dh > hv) {
			return false;
		}
		if(vv >= 0 && vUsage[ix] + dv > vv) {
			return false;
		}
		for (int i = 0; i < s; i++) {
			if(p < 0 || restrictions[p] > 0 || state[p] > 0) return false;
			int xv = (d == 0) ? puzzle.getVValues()[field.getX(p)] : puzzle.getHValues()[field.getY(p)];
			int xu = (d == 0) ? vUsage[field.getX(p)] : hUsage[field.getY(p)];
			if(xv >= 0 && xu + ns[p] > xv) return false;
			p = field.jump(p, d);
		}		
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]++;
		} else {
			int q = p;
			ships[s]--;
			for (int i = 0; i < s; i++) {
				state[q]++;
				hUsage[field.getY(q)] += puzzle.getNumbers()[q];
				vUsage[field.getX(q)] += puzzle.getNumbers()[q];
				for (int d2 = 0; d2 < 8; d2++) {
					int q2 = octafield.jump(q, d2);
					if(q2 >= 0) restrictions[q2]++;
				}
				q = field.jump(q, d);
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]--;
		} else {
			int q = p;
			ships[s]++;
			for (int i = 0; i < s; i++) {
				state[q]--;
				hUsage[field.getY(q)] -= puzzle.getNumbers()[q];
				vUsage[field.getX(q)] -= puzzle.getNumbers()[q];
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
					for (int i = 0; i < field.getSize(); i++) {
						distribution[i][solution[i]]++;
					}
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
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int v = puzzle.getVValues()[ix];
			if(v >= 0 && v != vUsage[ix]) return false;
		}
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int v = puzzle.getHValues()[iy];
			if(v >= 0 && v != hUsage[iy]) return false;
		}		
		return true;
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
	
	public int[] getNarrowestData() {
		int bp = -1;
		int bv = -1;
		int bc = solutionLimit + 1;
		for (int p = 0; p < distribution.length; p++) {
			if(puzzle.getData()[p] != EMPTY) continue;
			for (int v = 0; v < 7; v++) {
				int c = distribution[p][v];
				if(c > 0 && c < bc) {
					bp = p;
					bv = v;
					bc = distribution[p][v];
				}
			}
		}
		return new int[]{bp, bv, bc};
	}
	
	public int[] getNarrowestDataWaterOnly() {
		int bp = -1;
		int bv = -1;
		int bc = solutionLimit + 1;
		for (int p = 0; p < distribution.length; p++) {
			if(puzzle.getData()[p] != EMPTY) continue;
			int v = WATER;
			int c = distribution[p][v];
			if(c > 0 && c < bc) {
				bp = p;
				bv = v;
				bc = distribution[p][v];
			}
		}
		return new int[]{bp, bv, bc};
	}
	
}
