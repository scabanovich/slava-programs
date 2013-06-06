package com.slava.mover.model;

public class MoverSolver {
	static int MAX_CODE = 1333033;
	static int[] PW = new int[31];
	static {
		PW[0] = 1;
		for (int i = 1; i < PW.length; i++) PW[i] = 2 * PW[i - 1];
	}
	MoverProblem problem;
	MoverPosition[] positions = new MoverPosition[200];
	int[] codes = new int[MAX_CODE];
	int positionCount;
	
	public void setProblem(MoverProblem p) {
		problem = p;
	}
	
	public void solve() {
		positionCount = 0;
		MoverPosition finalP = problem.getFinalPosition();
		MoverPosition initial = problem.getInitialPosition();
		MoverPosition last = null;
		for (int i = 0; i < problem.getField().getSize(); i++) {
			if(problem.getFilter()[i] == 1 && !finalP.isBox(i)) {
				MoverPosition n = finalP.copy();
				n.mover = (byte)i;
				add(n);
			}
		}
		int limit = positionCount;
		int current = 0;
		while(current < positionCount) {
			MoverPosition c = positions[current];
			for (int d = 0; d < 4; d++) {
				MoverPosition n = c.move(d, problem.getField(), problem.getFilter());
				if(add(n) && initial.equals(n)) {
					printSolution(n);
//					return;
				}
				n = c.pull(d, problem.getField(), problem.getFilter());
				if(add(n) && initial.equals(n)) {
					printSolution(n);
//					return;
				}
			}
			++current;
			if(current == limit) {
				for (int i = limit; i < positionCount; i++) {
					positions[i - limit] = positions[i];
				}
				current = 0;
				positionCount -= limit;
				limit = positionCount;
				last = positions[0];
			}
		}
		if(last != null) {
			System.out.println("LastPosition: " + last.toString());
			printSolution(last);
		}
	}
	
	boolean add(MoverPosition n) {
		if(n == null) return false;
		if(!canAdd(n)) return false;
		checkArraySize();
		positions[positionCount] = n;
		positionCount++;
		return true;
	}
	
	private boolean canAdd(MoverPosition n) {
		int code = n.code(problem.getField().getSize(), MAX_CODE);
		int code2 = n.code(problem.getField().getSize(), PW.length);
		if((codes[code] & PW[code2]) != 0) return false;
		codes[code] |= PW[code2];
		return true;
	}
	
	void checkArraySize() {
		if(positionCount < positions.length) return;
		MoverPosition[] ps = new MoverPosition[positions.length * 2];
		System.arraycopy(positions, 0, ps, 0, positions.length);
		positions = ps;
	}
	
	public void printSolution(MoverPosition p) {
		StringBuffer sb = new StringBuffer();
		int moveCount = 0;
		while(p != null && p.previous != null) {
			int d = problem.getField().getOppositeDirection(p.direction);
			sb.append(' ').append(d);
			++moveCount;
			p = p.previous;
		}
		System.out.println("MoveCount=" + moveCount);
		System.out.println(sb.toString());
	}

}
