package slava.puzzles.connectfour.solve;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.slava.common.RectangularField;

public class Runner {
	public static final int[] OPPOSITE_DIRECTION = {4,5,6,7,0,1,2,3};

	public static RectangularField createField76() {
		RectangularField f = new RectangularField();
		f.setOrts(RectangularField.DIAGONAL_ORTS);
		f.setSize(7, 6);
		return f;
	}
	
	static void testState() {
		RectangularField f = createField76();
		ConnectFourState s = new ConnectFourState(f);
		Random seed = new Random();
		while(s.getFilled() < 32) {
			int r = s.getSmartState().getResult();
			int x = s.getSmartState().getForcedMove();
			if(r == s.getTurn() && x >= 0) {
				System.out.println("Stopped - won");
				s.move(x);
				break;
			}				
			if(x >= 0) {
				if(r == ConnectFourState.NEXT_TURN[s.getTurn()]) {
					System.out.println("Stopped - lost");
					break;
				}
				s.move(x);
				continue;
			}
			x = seed.nextInt(f.getWidth());
			if(s.canMove(x) && !s.isLosing(x)) {
				s.move(x);
			}
		}
		s.print();
	}

	static void testFork() {
		RectangularField f = createField76();
		ConnectFourState s = new ConnectFourState(f);
		s.move(new int[]{3,3,2,3});
		int x = s.getWinningIn2Moves();
		System.out.println(x);
		s.move(4);
		s.print();
		System.out.println(s.isLostByNextMove());
	}

	static void testSecondStrategy() {
		RectangularField f = createField76();
		ConnectFourState s = new ConnectFourState(f);
		s.move(new int[]{1,2,3,2,0,3,1,4,0});
		int r = s.canSecondGoAsFirst();
		System.out.println(r);
	}

	static ConnectFourState getRandomPlay(int moves) {
		RectangularField f = createField76();
		ConnectFourState s = new ConnectFourState(f);
		Random seed = new Random();
		while(s.getFilled() < moves) {
			int r = s.getSmartState().getResult();
			int x = -1;
			if(r == s.getTurn()) x = s.getSmartState().getForcedMove();
			if(x < 0) x = s.getWinningIn2Moves();
			if(x < 0) x = s.getWinningIn3Moves();
			if(x >= 0) return null;
			if(s.isLostByNextMove() || s.isLostIn2Moves()) return null;
			x = s.getSmartState().getForcedMove();
			if(x >= 0) {
				s.move(x);
				continue;
			}
			x = seed.nextInt(f.getWidth());
			if(s.canMove(x) && !s.isLosing(x)) {
				s.move(x);
				x = -1;
				r = s.getSmartState().getResult();
				if(r == s.getTurn()) x = s.getSmartState().getForcedMove();
				if(x < 0) x = s.getWinningIn2Moves();
				if(x >= 0) {
					s.back();
				}
			}
		}
		return s;
	}

	public static void countPositions() {
		RectangularField f = createField76();
		ConnectFourState s = null;
		int initialMoves = 24;
		while(s == null) {
			s = getRandomPlay(initialMoves);
		}
		int tInit = s.getFilled();
		Set<String> states = new HashSet<String>();
		int[] way = new int[f.getSize() + 1];
		way[s.getFilled()] = -1;
		boolean completed = false;
		int visited = 0;
		int draws = 0;
		while(true) {
			while(way[s.getFilled()] == f.getWidth() - 1) {
				if(s.getFilled() == tInit) {
					completed = true;
					break;
				}
				s.back();
			}
			if(completed) break;
			int t = s.getFilled();
			way[t]++;
			if(!s.canMove(way[t]) || s.isLosing(way[t])) {
				continue;
			}
			int x = s.getSmartState().getForcedMove();
			if(x >= 0 && (way[s.getFilled()] != x || s.getSmartState().getResult() == ConnectFourState.NEXT_TURN[s.getTurn()])) {
				continue;
			}
			s.move(way[t]);
			t = s.getFilled();
			String code = s.code();
			if(states.contains(code)) {
				way[t] = f.getWidth() - 1;
				continue;
			}
			states.add(code);
			visited++;
			
			if(s.isLostIn2Moves() || s.getWinningIn3Moves() >= 0 || s.canSecondGoAsFirst() == ConnectFourState.FIRST) {
				draws++;
				way[t] = f.getWidth() - 1;
				continue;
			}
			
			way[t] = -1;
			if(s.getSmartState().isWinning() || s.getWinningIn2Moves() >= 0 || t == initialMoves + 15) {
				way[t] = f.getWidth() - 1;
			}
		}
		
		System.out.println(states.size() + " " + visited);
		System.out.println("Draws=" + draws);
	}

	public static void main(String[] args) {
//		testFork();
//		testSecondStrategy();
		countPositions();
	}
}
