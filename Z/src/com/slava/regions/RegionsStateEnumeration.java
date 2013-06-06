package com.slava.regions;

public class RegionsStateEnumeration {
	RegionsState[] list = new RegionsState[100];
	StateWrapper initialRoot;
	int size = 0;
	RegionsState finalState;
	
	int movesLimit = 12;
	
	public void setInitialState(RegionsState state) {
		initialRoot = new StateWrapper(state);
		list[0] = state;
		size = 1;
	}
	
	void addState(RegionsState state) {
		if(!isNewState(state)) return;
		if(size == list.length) {
			RegionsState[] l = new RegionsState[(size * 3 / 2)];
			System.arraycopy(list, 0, l, 0, size);
			list = l;
		}
		list[size] = state;
		size++;
		if(size % 100000 == 0) System.out.println("Size=" + size);
	}
	
	boolean isNewState(RegionsState state) {
		return initialRoot.add(state);
	}
	
	public void setFinalState(RegionsState state) {
		finalState = state;
	}
	
	public void execute() {
		int index = 0;
		while(index < size) {
			RegionsState[] ns = list[index].getNeighbourStates();
			for (int i = 0; i < ns.length; i++) {
				if(!canFinishInMoveLimit(ns[i])) continue;
				addState(ns[i]);
				if(finalState.equals(ns[i])) {
					finalState = ns[i];
					return;
				}
			}
			++index;
		}
	}
	
	boolean canFinishInMoveLimit(RegionsState s) {
		int t = getMoveCount(s);
		int dt = finalState.lengths.length - s.lengths.length;
		return t + dt <= movesLimit;
	}

	public int getMoveCount() {
		return getMoveCount(finalState);
	}

	public int getMoveCount(RegionsState s) {
		int t = 0;
		s = s.parent;
		while(s != null) {
			++t;
			s = s.parent;
		}
		return t;
	}
	
	public void printSolution() {
		RegionsState s = finalState;
		while(s != null) {
			System.out.println(s.toString());
			s = s.parent;
		}
	}
	
}
