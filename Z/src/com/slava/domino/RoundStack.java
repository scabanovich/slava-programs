package com.slava.domino;

public class RoundStack {
	int maxSize = 1000000;
	DamkaState[] list = new DamkaState[maxSize];
	int begin = 0;
	int end = 0;
	
	public boolean isEmpty() {
		return begin == end;
	}
	
	public int size() {
		if(end >= begin) return end - begin;
		return maxSize - begin + end;
	}
	
	public void add(DamkaState state) {
		list[end] = state;
		end++;
		if(end == maxSize) end = 0;
	}
	
	public DamkaState removeFirst() {
		if(isEmpty()) return null;
		DamkaState state = list[begin];
		list[begin] = null;
		begin++;
		if(begin == maxSize) begin = 0;
		return state;
	}

}
