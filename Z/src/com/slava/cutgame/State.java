package com.slava.cutgame;

public class State {
	int[] values;
	
	public State(int length) {
		values = new int[length];
	}
	
	public int getLength() {
		return values.length;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public int getDelta() {
		int d = 0;
		boolean b = true;
		for (int i = 0; i < values.length; i++, b = !b) {
			if(b) d += values[i]; else d -= values[i];
		}
		return d;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			if(i > 0) sb.append(' ');
			sb.append(values[i]);
		}
		return sb.toString();
	}
	
}
