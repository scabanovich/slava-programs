package slava.puzzles.pusher.solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.slava.common.RectangularField;

public class PusherSolver {
	Random seed = new Random();

	PusherField pField = new PusherField();
	int[] finalBoxes;

	public PusherSolver() {}

	public void setField(RectangularField f) {
		pField.setField(f);
		finalBoxes = new int[f.getSize()];
	}

	public boolean isSolution(PusherState state) {
		for (int p = 0; p < finalBoxes.length; p++) {
			if(finalBoxes[p] != state.getBoxes()[p]) return false;
		}
		return true;
	}

	public int solveProblem(PusherState state) {
		Map<String, Integer> codes = new HashMap<String, Integer>();
		List<PusherState> states = new ArrayList<PusherState>();
		states.add(state);
		codes.put(state.code(), 0);
		int c = 0;
		while(c < states.size() && states.size() < 10000000) {
			PusherState current = states.get(c);
			int m = codes.get(current.code());
			for (int d = 0; d < 4; d++) {
				if(current.canPush(pField, d)) {
					PusherState next = current.pushToCopy(pField, d);
					String code = next.code();
					if(!codes.containsKey(code)) {
						codes.put(code, m + 1);
						states.add(next);
						if(isSolution(next)) {
							System.out.println("Solved in " + (m + 1) + " moves.");
							return m + 1;
						}
					}
				}
			}
			c++;
		}
		System.out.println("Cannot solve " + codes.size());
		return -1;
	}

	public PusherState generateState(int numberOfBoxes) {
		int size = pField.getField().getSize();
		PusherState state = new PusherState();
		state.boxes = new int[size];
		while(true) {
			state.pusher = seed.nextInt(size);
			if(!pField.isWall(state.pusher)) break;
		}
		while(numberOfBoxes > 0) {
			int p = seed.nextInt(size);
			if(p == state.pusher || pField.isWall(p) || state.boxes[p] == 1) continue;
			numberOfBoxes--;
			state.boxes[p] = 1;
		}
		return state;
	}

	//first and last
	public PusherState[] generateProblem(int numberOfBoxes) {
		PusherState state = generateState(numberOfBoxes);
		Map<String, Integer> codes = new HashMap<String, Integer>();
		List<PusherState> states = new ArrayList<PusherState>();
		states.add(state);
		codes.put(state.code(), 0);
		int c = 0;
		int MAX_STATE = 1; //1000000
		while(c < states.size() && states.size() < MAX_STATE) {
			PusherState current = states.get(c);
			int m = codes.get(current.code());
			int d = seed.nextInt(4);
			for (int dd = 0; dd < 4; dd++) {
				d++;
				if(d == 4) d = 0;
				if(current.canPush(pField, d)) {
					PusherState next = current.pushToCopy(pField, d);
					String code = next.code();
					if(!codes.containsKey(code)) {
						codes.put(code, m + 1);
						states.add(next);
					}
				}
			}
			c++;
		}
		if(states.size() == MAX_STATE) {
			System.out.println("Exhausted " + states.size());
		}
		PusherState finalState = states.get(states.size() - 1);
		finalBoxes = finalState.getBoxes();
//		System.out.println("Moves " + codes.get(finalState.code()));
//		writeState(state);
		return new PusherState[]{state, finalState};
	}

	public PusherState generateByPull(PusherState finalState) {
		PusherState state = finalState;
		Map<String, Integer> codes = new HashMap<String, Integer>();
		List<PusherState> states = new ArrayList<PusherState>();
		states.add(state);
		codes.put(state.code(), 0);
		int c = 0;
		int MAX_STATE = 700000;
		while(c < states.size() && states.size() < MAX_STATE) {
			PusherState current = states.get(c);
			int m = codes.get(current.code());
			for (int d = 0; d < 4; d++) {
				PusherState next = current.pullToCopy(pField, d, false);
				if(next != null) {					
					String code = next.code();
					if(!codes.containsKey(code)) {
						codes.put(code, m + 1);
						states.add(next);
					}
				}
				next = current.pullToCopy(pField, d, true);
				if(next != null) {					
					String code = next.code();
					if(!codes.containsKey(code)) {
						codes.put(code, m + 1);
						states.add(next);
					}
				}
			}
			c++;
		}
		System.out.println("Exhausted pull in " + states.size());

		PusherState firstState = states.get(states.size() - 1);
		finalBoxes = finalState.getBoxes();
		System.out.println("Moves " + codes.get(firstState.code()));
		writeState(firstState);
		return firstState;
	}

	public void writeState(PusherState state) {
		for (int p = 0; p < pField.getField().getSize(); p++) {
			boolean isFinal = finalBoxes[p] == 1;
			boolean isWall = pField.isWall(p);
			String s = isWall ? "w" : 
				(isFinal) ? (p == state.getPusher() ? "P" : state.getBoxes()[p] == 1 ? "B" : "E") 
						  : (p == state.getPusher() ? "p" : state.getBoxes()[p] == 1 ? "b" : "e");
			System.out.print(" " + s);
			if(pField.getField().isRightBorder(p)) {
				System.out.println("");
			}
		}
		System.out.println("");
	}


	static int[] WALLS = {
		0,0,0,0,0,0,0,
		0,1,1,1,0,1,0,
		0,0,0,0,0,0,0,
		0,1,0,1,0,1,0,
		0,1,0,0,0,0,0,
		0,1,0,1,1,1,0,
		0,0,0,0,0,0,0,
	};

	public static void main(String[] args) {
		while(true) {
			System.out.println("-----------------------");
			PusherSolver s = new PusherSolver();
			RectangularField f = new RectangularField();
			f.setSize(7, 7);
			s.setField(f);
			for (int i = 0; i < WALLS.length; i++) {
				if(WALLS[i] > 0) s.pField.addWall(i);
			}
			PusherState[] states = s.generateProblem(5);
			PusherState firstState = s.generateByPull(states[1]);
			int m = 0;
			states[1].parent = null;
			PusherState z = firstState;
			while(z.parent != null) {
				m++; z = z.parent;
			}
			System.out.println("--->" + m);
			if(m > 250) {
				m = s.solveProblem(firstState);
				break;
			}
		}
	}

}

/*

 e e e e e e e
 e w w w e w e
 e b e e e e e
 E w b w e w e
 E w p b e b e
 E w e w w w e
 E b e e e E e
--->251

 e e e e e e
 b w E w w e
 P b b E e E
 e w e e b e
 e w b w w e
 e E e e e e
Solved in 199 moves. (5 boxes)

 e e e E E e
 b w e w w e
 p b b e e E
 E w e e b e
 e w b w w E
 e e e e e e
Solved in 188 moves. (5 boxes)

 e E e e b e
 e w p w w E
 e b b E e e
 e w e e e e
 E w b w w e
 e e e e e e
Solved in 139 moves. (4 boxes)

 E e e E E e
 e e w e w b
 e w p b e E
 e w B b e e
 e e e w b e
 e e e e b E
Solved in 194 moves.

 e e e E E e
 e e w e w e
 e w e b e E
 e w p b b e
 e e b w e e
 e b e E E e
Solved in 189 moves.

 E E e e e E
 e e w e w e
 e w e b e e
 e w p b b e
 e e b w e e
 e b e E E e
Solved in 185 moves.

 e e E e e E
 e w e e w e
 e e w b e e
 e b e e w e
 e b w e e B
 B P e e e e
Solved in 173 moves.

 E E E e e E
 e e w e w e
 e w e e e e
 e e b e b b
 p w b w e e
 B e e e e e
Solved in 162 moves.

 e b e e e E
 E e e b w e
 e w b b e e
 b p e w e E
 e e w e e E
 e e e e e E
Solved in 154 moves.

 e e b e e E
 e e b b w e
 p w b e e e
 e e e w e E
 e e w e e E
 e E b e e E
Solved in 134 moves.

 e e e e e e
 E e E b w e
 b w e e b e
 e b E w b e
 E p w e e e
 E e e e e e
Solved in 115 moves.

 E e e B e e
 b b e e w E
 e w b e b E
 e e e w e e
 e e w p e e
 e e e e e E
Solved in 107 moves.

 e e e b e E
 E e b e w e
 e w b e e e
 b e e w e e
 e e w e e E
 e E e b e P
Solved in 104 moves.



 */

