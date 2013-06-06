package com.slava.regions;

public class RegionsStateRunner {
	
	static RegionsState createOneRegionState(int n) {
		RegionsState s = new RegionsState();
		s.lengths = new byte[]{(byte)n};
		byte sum = (byte)((n * (n + 1)) / 2);
		s.numbers = new byte[]{sum};
		return s;
	}
	static RegionsState createOrderedState(int n) {
		RegionsState s = new RegionsState();
		s.lengths = new byte[n];
		s.numbers = new byte[n];
		for (int i = 0; i < n; i++) {
			s.lengths[i] = (byte)1;
			s.numbers[i] = (byte)(i + 1);
		}
		return s;
	}
	static RegionsState createBackwardOrderedState(int n) {
		RegionsState s = new RegionsState();
		s.lengths = new byte[n];
		s.numbers = new byte[n];
		for (int i = 0; i < n; i++) {
			s.lengths[n - i - 1] = (byte)1;
			s.numbers[n - i - 1] = (byte)(i + 1);
		}
		return s;
	}

	public static void main(String[] args) {
		int n = 7;
		RegionsState initialState = createOneRegionState(n);
//			createOrderedState(n);
		RegionsState finalState = createBackwardOrderedState(n);
		
		RegionsStateEnumeration e = new RegionsStateEnumeration();
		e.setInitialState(initialState);
		e.setFinalState(finalState);
		e.execute();
		if(e.finalState.parent != null) {
			System.out.println("Solution found in " + e.getMoveCount() + " moves. Size=" + e.size);
			e.printSolution();
		} else {
			System.out.println("Size=" + e.size);
		}
	}
}
