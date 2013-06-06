package slava.puzzles.hexa.rook.analysis;

import java.util.ArrayList;

import slava.puzzles.hexa.common.HexaField;
import slava.puzzles.hexa.rook.model.HexaRookConstants;
import slava.puzzles.hexa.rook.model.HexaRookPuzzle;

public class HexaRookSolver implements HexaRookConstants {
	HexaRookPuzzle puzzle;
	HexaField field;
	
	int solutionLimit = -1;
	
	int[] state;
	int vacantPlaces;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	ArrayList solutions = new ArrayList();

	public HexaRookSolver() {}
	
	public void setPuzzle(HexaRookPuzzle puzzle) {
		this.puzzle = puzzle;
	}
	
	public void setField(HexaField field) {
		this.field = field;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		vacantPlaces = 0;
		for (int p = 0; p < state.length; p++) {
			if(puzzle.getPositions()[p] != NOT_POSITION) {
				vacantPlaces++;
				state[p] = SOME_POSITION;
			} else {
				state[p] = NOT_POSITION;
			}
		}
		wayCount = new int[vacantPlaces + 1];
		way = new int[vacantPlaces + 1];
		place = new int[vacantPlaces + 1];
		int mwc = (vacantPlaces > 6) ? vacantPlaces : 6;
		ways = new int[vacantPlaces + 1][mwc];
		t = 0;
		solutions.clear();
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(vacantPlaces == 0) return;
		if(t == 0) {
			for (int p = 0; p < state.length; p++) {
				if(!field.isInField(p)) continue;
				if(state[p] == SOME_POSITION) {
					if(puzzle.getPositions()[p] == 1) {
						ways[t][0] = p;
						wayCount[t] = 1;
						return;
					} else if(puzzle.getPositions()[p] > SOME_POSITION) {
						continue;
					} else {
						ways[t][wayCount[t]] = p;
						wayCount[t]++;
					}
				}
			}
		} else {
			int p = place[t - 1];
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				while(true) {
					if(q < 0 || state[q] > SOME_POSITION) break;
					if(state[q] == SOME_POSITION) {
						if(puzzle.getPositions()[q] == t + 1) {
							ways[t][0] = q;
							wayCount[t] = 1;
							return;
						} else if(puzzle.getPositions()[q] > SOME_POSITION) {
							break;
						} else {
							ways[t][wayCount[t]] = q;
							wayCount[t]++;
							break;						
						}
					}
					q = field.jump(q, d);
				}
			}
		}		
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = t + 1;
		vacantPlaces--;
		place[t] = p;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = SOME_POSITION;
		vacantPlaces++;
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
			if(vacantPlaces == 0) {
				int[] solution = (int[])state.clone();
				solutionCount++;
				if(solutionCount <= solutionLimit) {
					solutions.add(solution);
				}
				if(solutionLimit > 0 && solutionCount > solutionLimit) {
					return;
				}
			}
		}		
	}

	public int getSolutionCount() {
		return solutionCount;
	}
	
	public ArrayList getSolutions() {
		return solutions;
	}
	
}
