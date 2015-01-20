package slava.puzzles.checkers;

import java.util.List;
import java.util.Random;

import slava.puzzles.game.GameConstants;
import slava.puzzles.game.Solver;

public class CheckersRunner implements CheckersConstants, GameConstants {
	static byte w = WHITE_PAWN;
	static byte W = WHITE_KING;
	static byte b = BLACK_PAWN;
	static byte B = BLACK_KING;
	static byte e = EMPTY;
	
//ckoxy.v
//abmpr.z0
//adeos.z0 //bcknr.z0 //dimnp.z0 //ahkox.z0 //ahnot.z0
//Dg.A4
	static byte[] s2 = {
		w,  w,  e,  e,
		  e,  e,  e,  e,
		e,  e,  w,  e,
		  e,  e,  w,  e,
		e,  b,  e,  e,
		  e,  e,  e,  e,
		e,  e,  e,  e,
		  e,  e,  b,  b,
	};

	static Random seed = new Random();
	
	static byte[] getRandomPosition(int wp) {
		byte[] s = new byte[32];
		s[20] = b; //s[26] = b; s[30] = b;
		while(wp > 0) {
			int p = seed.nextInt(24);
			if(s[p] != e) continue;
			s[p] = w;
//			if(wp == 1) s[p] = W;
			wp--;
		}
		return s;
	}

	static byte[] getRandomPosition(int wp, int bp) {
		byte[] s = new byte[32];
		while(wp > 0) {
			int p = seed.nextInt(12);
			if(s[p] != e) continue;
			s[p] = w;
			wp--;
		}
		while(bp > 0) {
			int p = 12 + seed.nextInt(20);
			if(s[p] != e) continue;
			s[p] = b;
			bp--;
		}
		return s;
	}

	static void runRandom() {
		Solver s = new Solver();
		CheckersPosition position = new CheckersPosition();
		position.setField(new CheckersField());
		s.setPosition(position);
		int win = 0;
		int loss = 0;
		int draw = 0;
		while(win + loss + draw < 1000) {
			byte[] pos = getRandomPosition(4, 2);
			for (byte i = 0; i < pos.length; i++) {
				position.setValue(i, pos[i]);
			}
			position.setTurn(BLACK);
			List<Move> ms = position.getMoves();
			if(!ms.isEmpty() && ms.get(0).getBeatenCount() > 0) {
				continue; //trivial - white have to eat.
			}
			s.getResults().clearIndefinite();
			System.out.println(position.getCode());
			int r = s.solve();
			if(r == WHITE_WIN) {
				win++;
			} else if(r == BLACK_WIN) {
				loss++;
			} else if(r == DRAW) {
				draw++;
			}
		}
		System.out.println("+" + win + " -" + loss + " =" + draw);
		
	}

	static void runPosition() {
		Solver s = new Solver();
//		s.loadStorage(DRAWS_FILE);
		CheckersPosition position = new CheckersPosition();
		position.setField(new CheckersField());
		
//		position.setPosition("gHl:9");
		for (byte i = 0; i < s2.length; i++) {
			position.setValue(i, s2[i]);
		}
		position.setTurn(WHITE);
		System.out.println(position.getCode());
		s.setPosition(position);
		int r = s.solve();
		System.out.println(r);
		System.out.println(s.getBestMove());
		
//		MoveSearcher searcher = new  MoveSearcher();
//		searcher.setPosition(position);
//		List<Move> moves = searcher.getMoves(WHITE);
//		for (Move m: moves) {
//			System.out.println(m.toString());
//		}
	}

	public static void main(String[] args) {
//		runRandom();
		runPosition();
//		Draws.enumerate4();
//		Draws.runFromFile();
	}
}
