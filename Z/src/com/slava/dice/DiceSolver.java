package com.slava.dice;

import java.util.ArrayList;

public class DiceSolver {
	int fieldLength;
	int[] diceSequence;
	boolean canJumpToFinish = true;
	boolean canJumpToStart = true;
	int transitionCountOption = -1;
	
	int solutionLimit;
	
	int[] transition;
	int[] start, end;
	
	int t;
	int[] place;
	int[] sequenceIndex;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int treeCount;
	int transitionCount;
	
	ArrayList solutions = new ArrayList();
	
	public DiceSolver() {}
	
	public void setFieldLength(int i) {
		fieldLength = i;
	}
	
	public void setTransitionCountOption(int c) {
		transitionCountOption = c;
	}
	
	public void setDiceSequence(int[] s) {
		diceSequence = s;
	}
	
	public void setSoltionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		transition = new int[fieldLength + 1];
		for (int i = 0; i < transition.length; i++) transition[i] = -1;
		start = new int[fieldLength + 1];
		end = new int[fieldLength + 1];
		place = new int[fieldLength + 1];
		sequenceIndex = new int[fieldLength + 1];
		wayCount = new int[fieldLength + 1];
		way = new int[fieldLength + 1];
		ways = new int[fieldLength + 1][fieldLength + 1];
		
		t = 0;
		place[0] = 0;
		sequenceIndex[0] = 0;
		transitionCount = 0;
		solutionCount = 0;
		solutions.clear();
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(place[t] >= fieldLength) return;
		if(sequenceIndex[t] == diceSequence.length) {
			if(canJumpToFinish && start[place[t]] == 0 && end[place[t]] == 0 && start[fieldLength] == 0) {
				ways[t][wayCount[t]] = fieldLength;
				wayCount[t] = 1;
			}
			return;
		}
		if(t == 0 || (transitionCountOption > 0 && transitionCount >= transitionCountOption)) {
			ways[t][wayCount[t]] = -1;
			wayCount[t] = 1;
			return;
		}
		int q0 = canJumpToStart ? 0 : 1;
		for (int q = q0; q <= fieldLength; q++) {
			if(start[q] > 0) continue;
			if(q == place[t]) {
				ways[t][wayCount[t]] = -1;
			} else {
				ways[t][wayCount[t]] = q;
			}
			++wayCount[t];
		}
	}
	
	void move() {
		int q = ways[t][way[t]];
		end[place[t]]++;
		if(q >= 0) {
			transition[place[t]] = q;
			start[place[t]]++;
			end[q]++;
			transitionCount++;
		}
		doMove();
		++t;
		srch();
		way[t] = -1;
	}
	
	void doMove() {
		int p = place[t];
		int s = sequenceIndex[t];
		while(true) {
			if(p >= fieldLength) break;
			if(transition[p] >= 0) {
				p = transition[p];
			} else {
				if(s == diceSequence.length) break;
				if(start[p] == 0 && end[p] == 0) break;
				p += diceSequence[s];
				++s;				
			}			
		}
		place[t + 1] = p;
		sequenceIndex[t + 1] = s;
	}
	
	void back() {
		--t;
		int q = ways[t][way[t]];
		end[place[t]]--;
		transition[place[t]] = -1;
		if(q >= 0) {
			start[place[t]]--;
			end[q]--;
			transitionCount--;
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
			if(wayCount[t] == 0) treeCount++;
			if(isSolution()) {
				++solutionCount;
				solutions.add(transition.clone());
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
//				printSolution(transition);
			}
		}
		
	}
	
	boolean isSolution() {
		if(transitionCountOption > 0 && transitionCount != transitionCountOption) return false;
		return (place[t] == fieldLength && sequenceIndex[t] == diceSequence.length);
	}
	
	void printSolution(int[] _trantision) {
		for (int i = 0; i < _trantision.length; i++) {
			System.out.print(" ");
			if(_trantision[i] < 0) {
				System.out.print("-");
			} else {
				System.out.print(_trantision[i]);
			}
		}
		System.out.println("");
	}
	
	public void printSolutions() {
		int[][] ss = (int[][])solutions.toArray(new int[0][]);
		System.out.println("SolutionCount=" + solutionCount + " TreeCount=" + treeCount);
		for (int i = 0; i < ss.length; i++) {
			printSolution(ss[i]);
		}
	}
	

}
