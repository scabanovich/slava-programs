package slava.puzzles.checkers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import slava.puzzles.game.GameConstants;
import slava.puzzles.game.Result;
import slava.puzzles.game.Solver;
import slava.puzzles.game.StateReader;

public class Draws implements GameConstants, CheckersConstants {

	public static void enumerate3() {
		Solver s = new Solver();
		CheckersPosition position = new CheckersPosition();
		position.setField(new CheckersField());
		s.setPosition(position);
		Runner runner = new Runner(s);
		for (int i = 4; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				if(j == i) continue;
				for (int k = 0; k < 32; k++) {
					if(k == i || k == j) continue;
					byte[] pos = new byte[32];
					pos[i] = BLACK_PAWN;
					pos[j] = WHITE_KING;
					pos[k] = BLACK_KING;
					runner.run(position, pos, WHITE);
				}
			}
		}
		System.out.println("+" + runner.win + " -" + runner.loss + " =" + runner.draw);
		runner.save();
	}

	public static void enumerate4() {
		Solver s = new Solver();
		s.loadStorage(DRAWS_FILE);
		CheckersPosition position = new CheckersPosition();
		position.setField(new CheckersField());
		s.setPosition(position);
		Runner runner = new Runner(s);
		for (int i = 0; i < 28; i++) {  //WHITE_PAWN
			for (int j = i + 1; j < 28; j++) { //WHITE_PAWN
				if(j == i) continue;
				for (int k = 0; k < 32; k++) { //WHITE_KING
					if(k == i || k == j) continue;
					for (int l = 0; l < 32; l++) { //BLACK_KING
						if(l == k || l == j || l == i) continue;
						byte[] pos = new byte[32];
						pos[i] = WHITE_PAWN;
						pos[j] = WHITE_PAWN;
						pos[k] = WHITE_KING;
						pos[l] = BLACK_KING;
						runner.run(position, pos, BLACK);
					}
				}
			}
		}
		System.out.println("+" + runner.win + " -" + runner.loss + " =" + runner.draw);
		runner.save();
	}

	static class Runner {
		int win = 0;
		int loss = 0;
		int draw = 0;

		Solver s;

		TreeSet<String> draws = new TreeSet<String>();

		public Runner(Solver s) {
			this.s = s;
		}

		public void run(CheckersPosition position, byte[] pos, byte turn) {
			for (byte p = 0; p < pos.length; p++) {
				position.setValue(p, pos[p]);
			}
			position.setTurn(turn);
			List<Move> ms = position.getMoves();
			if(!ms.isEmpty() && ms.get(0).getBeatenCount() > 0) {
				return; //trivial - white have to eat.
			}
			String code = position.getCode();
			Result res = s.getResults().get(code);
			if(res != null && res.getResult() != INDEFINITE) {
				return; //already done
			}
			int r = s.solve();
			s.getResults().clearIndefinite();
			if(r == WHITE_WIN) {
				win++;
				System.out.println(position.getCode() + " +");
			} else if(r == BLACK_WIN) {
				loss++;
			} else if(r == DRAW) {
				draw++;
			} else {
				draws.add(position.getCode());
				System.out.println(position.getCode());
			}
		}

		public void save() {
			try {
				File f = new File("./data/checkers/reverse/new-draws.txt");
				BufferedWriter w = new BufferedWriter(new FileWriter(f));
				for (String s: draws) {
					w.write(s + " =\n");
				}
				w.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void runFromFile() {
		Solver s = new Solver();
		s.loadStorage(DRAWS_FILE);
		CheckersPosition position = new CheckersPosition();
		position.setField(new CheckersField());
		s.setPosition(position);
		int indefinite = 0;
		StateReader reader = new StateReader();
//		reader.setFile(new File(DRAWS_FILE));
		reader.setFile(new File("./data/checkers/reverse/new-draws.txt"));
		while(reader.hasNext()) {
			reader.next();
			String code = reader.getCurrentCode();
			position.setPosition(code);
			int r = s.solve();
			s.getResults().clearIndefinite();
			if(r != INDEFINITE && r != DRAW) {
				System.out.println(code + " " + r);
			} else {
				indefinite++;
				if(indefinite % 100 == 0) System.out.println(indefinite);
			}
		}
		System.out.println("indefinite=" + indefinite);	
	}
}
