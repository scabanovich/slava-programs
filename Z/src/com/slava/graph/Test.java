package com.slava.graph;

public class Test {

	public static void main(String[] args) {
		IPegs p = new Pegs();
		try {
			p.check("8:1-3,1-4,1-7,2-4,2-5,2-6,2-7,2-8,3-6,3-7,4-5,4-6,4-7,5-8,7-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
