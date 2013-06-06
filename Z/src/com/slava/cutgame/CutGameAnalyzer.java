package com.slava.cutgame;

public class CutGameAnalyzer {
	int minDelta;
	State initialState;
	int time;
	
	int[] turn;
	int t;
	
	CutGenerator[] generators;
	int[] wayCount;
	int[] way;
	State[] states;
	
	int looser = -1;
	
	public void setMinDelta(int minDelta) {
		this.minDelta = minDelta;
	}
	
	public void setInitialState(State s) {
		initialState = s;
	}
	
	public void setMoveNumber(int m) {
		time = m * 2;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		generators = new CutGenerator[time + 1];
		wayCount = new int[time + 1];
		way = new int[time + 1];
		states = new State[time + 1];
		states[0] = initialState;
		turn = new int[time + 1];
		for (int i = 0; i < turn.length; i++) turn[i] = (i % 2);
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == time) return;
		generators[t] = (t % 2 == 0) 
		  ? (CutGenerator)new FirstPlayerCutGenerator(states[t], minDelta)
		  : (CutGenerator)new SecondPlayerCutGenerator(states[t], minDelta);
		wayCount[t] = generators[t].getCutCount();
		
	}
	
	void move() {
		states[t + 1] = generators[t].getCutAt(way[t]).getCut();
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t < 2) {
					looser = turn[t];
					return;
				}
				back();
				back();
			}
			++way[t];
			move();
			if(t == time) {
				int delta = states[t].getDelta();
				if(delta > minDelta) back();
			}
		}
	}
	
	public int getLooser() {
		return looser;
	}

}
