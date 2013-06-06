package com.slava.cutgame;

public class CutGameRunner {
	
	public static void test1() {
		State s = new State(1);
		s.getValues()[0] = 30;
		SecondPlayerCutGenerator g = new SecondPlayerCutGenerator(s, 1);
		int count = g.getCutCount();
		for (int i = 0; i < count; i++) {
			Cut cut = g.getCutAt(i);
			System.out.println(cut.toString() + " --> " + cut.getCut().toString());
		}
		System.out.println("bestDelta=" + g.getBestDelta());
	}

	public static void test2() {
		State s = new State(1);
		s.getValues()[0] = 30;
		FirstPlayerCutGenerator g = new FirstPlayerCutGenerator(s, 3);
		int count = g.getCutCount();
		for (int i = 0; i < count; i++) {
			Cut cut = g.getCutAt(i);
			System.out.println(cut.toString() + " --> " + cut.getCut().toString());
		}
	}
	
	public static void test3() {
		State s = new State(1);
		s.getValues()[0] = 63;

		CutGameAnalyzer a = new CutGameAnalyzer();
		a.setMinDelta(7);
		a.setInitialState(s);
		a.setMoveNumber(4);
		
		a.solve();
		
		System.out.println("looser=" + a.getLooser());
	}
	

	public static void main(String[] args) {
		test3();
	}

}
