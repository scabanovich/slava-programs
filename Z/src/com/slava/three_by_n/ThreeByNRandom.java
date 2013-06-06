package com.slava.three_by_n;

public class ThreeByNRandom {
	int length;
	Field field;
	int[] initialState;
	int[] finalState;
	
	int[] state;
	
	public void setLength(int n) {
		length = n;
		field = new Field();
		field.setSize(n, 3);
		initialState = new int[field.size];
		finalState = new int[field.size];
		for (int i = 0; i < field.size; i++) {
			initialState[i] = field.x[i];
			finalState[i] = n - 1 - field.x[i];
		}
	}
	
	public void run() {
		state = new int[field.size];
		for (int i = 0; i < field.size; i++) state[i] = initialState[i];
		long time = 0;
		while(!isFinished()) {
			while(!flip()) ;
			++time;
			if(time % 500000 == 0) {
				System.out.println(time);
				print();
			}
		}
		print();
	}
	
	boolean isFinished() {
		if(state[field.size - 1] == 0) return true;
		
		return false;
	}
	
	boolean flip() {
		int p = (int)(field.size * Math.random());
		int d = (int)(4 * Math.random());
		int q = field.jump(p, d);
		if(q < 0) return false;
		if(state[p] == state[q]) return false;
		if(state[p] < state[q]) {
			int c = p;
			p = q;
			q = c;
		}
		if(state[p] - state[q] != 1) return false;
		
		if(field.y[p] > 0 && field.x[p] == 0) return false;
		if(field.y[p] < 2 && field.x[p] == length - 1) return false;
		if(field.y[q] > 0 && field.x[q] == 0) return false;
		if(field.y[q] < 2 && field.x[q] == length - 1) return false;
		
		if(state[q] == state[field.size - 1] - 1) {
			if(field.x[p] < field.x[q]) {
				// stay on right border
				if(field.x[q] == length - 1) return false;
				// be steady on way to right border;
				if(Math.random() < 0.15) return false;
			}
		}
		if(q == (field.size - 2) && state[q] > state[field.size - 1]) {
			// prepare path for less number
			return false;
		}
		if(q == field.size - 1) {
			//do not increase value in final point
			return false;
		}
		if(state[field.size - 1] < 1) {
			if(state[p] == state[0] + 1) {
				if(field.x[p] < field.x[q]) {
					// stay on left border
					if(field.x[p] == 0) return false;
					// be steady on way to left border;
					if(Math.random() < 0.2) return false;
				}
			}
			if(p == 0) {
				//do not decrease value in initial point
				return false;
			}
		}
		
		int c = state[p];
		state[p] = state[q];
		state[q] = c;
		
		return true;
	}
	
	boolean isLast(int p) {
		for (int q = 0; q < field.size; q++) {
			if(q != p && state[q] == state[p] && 
				(field.x[q] > field.x[p] || (field.x[q] == field.x[p] && field.y[q] > field.y[p]))) return false;
		}
		return true;
	}
	
	void print() {
		for (int i = 0; i < field.size; i++) {
			System.out.print(" " + state[i]);
			if(field.x[i] == length - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args) {
		ThreeByNRandom p = new ThreeByNRandom();
		p.setLength(10);
		p.run();
	}

}
