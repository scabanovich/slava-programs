package slava.puzzles.reversi.solve;

import java.util.Random;


public class ReversiRunner {

	static int[] ANGLE_RESTRICTIONS = {
		0,0,1,1,1,1,0,0,
		0,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,0,
		0,0,1,1,1,1,0,0,
	};

	static ReversiState runFewRundomMoves(int c) {
		ReversiState s = new ReversiState(ANGLE_RESTRICTIONS);
		Random seed = new Random();
		for (int t = 0; t < c; t++) {
			int[] moves = s.getMoves();
			if(moves.length == 0) break;
			int n = seed.nextInt(moves.length);
			s = new ReversiState(s,moves[n]);
		}
		s.print();
		return s;
	}

	static int stateCount() {
		int count = 0;
		ReversiState s = runFewRundomMoves(48);
		int t = 0;
		int[] way = new int[20];
		way[0] = -1;
		while(true) {
			while(way[t] == s.getMoves().length - 1) {
				if(t == 0) return count;
				s = s.previous;
				t--;
			}
			way[t]++;
			s = new ReversiState(s, s.getMoves()[way[t]]);
			t++;
			way[t] = -1;
			if(t == 10) {
				way[t] = s.getMoves().length - 1;
				count++;
			}
		}
	}

	static int firstSolveEasily(ReversiState s) {
		int min = -65;
		int max = 65;
		while(max - min > 1) {
			int avr = (min + max) / 2;
			if(s.canReachDifference(ReversiState.FIRST, avr, true)) {
				min = avr;
				System.out.println("easily can " + min);
			} else {
				max = avr;
				System.out.println("easily cannot " + max);
			}
		}
		
		return min;
	}

	static void solveRandom() {
		ReversiState s = runFewRundomMoves(34);
		solve(s);
		s.previous.print();
		solve(s.previous);
	}
	static void solve(ReversiState s) {
		int min = -65;//firstSolveEasily(s);
		int max = 65;
		while(max - min > 1) {
			int diff = (min + max) / 2;
			System.out.println(diff);
			if(s.canReachDifference(ReversiState.FIRST, diff, false)) {
				min = diff;
			} else {
				max = diff;
			}
		}
		System.out.println("Solution " + min);
	}

	

	public static void main(String[] args) {
//		runFewRundomMoves();
//		int c = stateCount(); System.out.println(c);
		solveRandom();
	}

}
