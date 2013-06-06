package com.slava.regions;

public class StateWrapper {
	RegionsState state;
	StateWrapper less;
	StateWrapper more;
	
	public StateWrapper(RegionsState state) {
		this.state = state;
	}
	
	public boolean add(RegionsState s) {
		int d = compare(s, state);
		if(d == 0) return false;
		if(d > 0) {
			if(more == null) {
				more = new StateWrapper(s);
			} else {
				return more.add(s);
			}
		} else {
			if(less == null) {
				less = new StateWrapper(s);
			} else {
				return less.add(s);
			}
		}		
		return true;
	}
	
	public int compare(RegionsState s1, RegionsState s2) {
		if(s1.lengths.length != s2.lengths.length) {
			return s1.lengths.length < s2.lengths.length ? -1 : 1;
		}
		for (int i = 0; i < s1.lengths.length; i++) {
			if(s1.lengths[i] != s2.lengths[i]) {
				return s1.lengths[i] < s2.lengths[i] ? -1 : 1;
			}
			if(s1.numbers[i] != s2.numbers[i]) {
				return s1.numbers[i] < s2.numbers[i] ? -1 : 1;
			}
		}		
		return 0;
	}
	

}
