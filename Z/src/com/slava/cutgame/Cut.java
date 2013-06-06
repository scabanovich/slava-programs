package com.slava.cutgame;

public class Cut {
	State state;
	int index;
	int position;
	State cut;
	
	public Cut() {}
	
	public void execute(State state, int index, int position) {
		this.state = state;
		this.index = index;
		this.position = position;
		execute();
	}
	
	public void execute() {
		State s = new State(state.getLength() + 1);
		int[] vs = state.getValues();
		int[] nvs = s.getValues();
		int v1 = position;
		int v2 = vs[index] - position;
		if(v1 < v2) {
			int q = v1;
			v1 = v2;
			v2 = q;
		}
		int m = 0;
		int j = 0;
		for (int i = 0; i < vs.length; i++) {
			if(i == index) continue;
			if(m == 0) {
				if(v1 > vs[i]) {
					nvs[j] = v1;
					++j;
					++m;
				}
			}
			if(m == 1) {
				if(v2 > vs[i]) {
					nvs[j] = v2;
					++j;
					++m;
				}
			}
			nvs[j] = vs[i];
			++j;
		}
		if(m == 0) {
			nvs[j] = v1;
			++j;
			++m;
		}
		if(m == 1) {
			nvs[j] = v2;
			++j;
			++m;
		}
		cut = s;
	}
	
	public State getCut() {
		return cut;
	}
	
	public String toString() {
		return "index = " + index + " position = " + position;
	}
	
}
