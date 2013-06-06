package com.slava.circuit;

public class Bind extends Variable {
	String beginning;
	String ending;
	
	public Bind(String beginning, String ending, int value) {
		this.beginning = beginning;
		this.ending = ending;
		if(value != -1) setValue(value); else unset();
	}
	
	public String toString() {
		return beginning + "-" + ending + ":" + super.toString();
	}

}
