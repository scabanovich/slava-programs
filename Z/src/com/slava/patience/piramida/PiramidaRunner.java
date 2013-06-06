package com.slava.patience.piramida;

public class PiramidaRunner {
	static String PACK = 
		"9dJd2c" + 
		"QcAdKc7d5dJh" + 
		"7h9s7s0d4c0cKd8h0h" + 
		"As4h3s2s6d2d0s8cAh4s" +
		//Reserve
		"8dQsKh5cAcQhJs7c9c6h2h3c6s3dJc4d5h8s6cQdKs9h3h" +
		// base
		"5s";

// 
	
	static int[] getRandomPack() {
		int[] cs = new int[52];
		for (int i = 0; i < cs.length; i++) cs[i] = i;
		for (int i = cs.length - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i == j) continue;
			int c = cs[i];
			cs[i] = cs[j];
			cs[j] = c;
		}
		return cs;
	}
	
	static void run(int[] pack) {
		Cards cards = new Cards();
		cards.setSize(4, 13);
		PiramidaSolver solver = new PiramidaSolver();
		solver.setCards(cards);
		solver.setPack(pack);
		solver.solve();
	}

	static void run() {
		Cards cards = new Cards();
		cards.setSize(4, 13);
		int[] pack = cards.parse(PACK);
		PiramidaSolver solver = new PiramidaSolver();
		solver.setCards(cards);
		solver.setPack(pack);
		solver.solve();
	}


	public static void main(String[] args) {
		//run(getRandomPack());
		run();
	}
}
