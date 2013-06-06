package com.slava.domino;

public class DamkaMove {
	DamkaMove parent;
	byte begin;
	byte end;
	
	public DamkaMove(DamkaMove parent, byte begin, byte end) {
		this.parent = parent;
		this.begin = begin;
		this.end = end;
	}

}
