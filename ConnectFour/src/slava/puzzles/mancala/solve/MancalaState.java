package slava.puzzles.mancala.solve;

public class MancalaState {
	public static int FIRST = 0;
	public static int SECOND = 1;
	public static int UNKNOWN = -1;

	static int[] OPPOSITE = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
	static int[] NEXT = {1,2,3,4,5,6,7,8,9,10,11,0};

	int turn = FIRST;
	int[] pits = new int[12];
	int deltascore; // used to keep result of last move
	
	int firstPieces = 0; //computed in constructor
	int secondPieces = 0; //computed in constructor
	
	static int MOD1 = 49756743;
	static int MOD2 = 48235178;

	static int[][] cacheCode = new int[2][MOD1];
	static short[][] cacheFirstCanMake = new short[2][MOD1];
	static short[][] cacheFirstCanNotMake = new short[2][MOD1];
	
	static {
		for (int r = 0; r < cacheCode.length; r++) {
		for (int i = 0; i < MOD1; i++) {
			cacheCode[r][i] = -1;
			cacheFirstCanMake[r][i] = -100;
			cacheFirstCanNotMake[r][i] = 100;
		}
		}
	}
	
	public MancalaState() {
		this(FIRST, new int[]{4,4,4,4,4,4,4,4,4,4,4,4});
	}

	public MancalaState(int turn, int[] pits) {
		this.turn = turn;
		this.pits = pits;
		for (int i = 0; i < 6; i++) {
			firstPieces += pits[i];
		}
		for (int i = 6; i < pits.length; i++) {
			secondPieces += pits[i];
		}
	}

	public String code() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pits.length; i++) {
			sb.append((char)(97+pits[i]));
		}
		sb.append(turn);
		return sb.toString();
	}

	public int code1() {
		int result = 0;
		for (int i = 0; i < pits.length; i++) {
			result = 7*result + pits[i];
			if(result >= MOD1) result = result % MOD1;
		}
		result = 2 * result + turn;
		if(result >= MOD1) result = result % MOD1;
		return result;
	}

	public int code2() {
		int result = 0;
		for (int i = 0; i < pits.length; i++) {
			result = 6 * result + pits[i];
			if(result >= MOD2) result = result % MOD2;
		}
		result = 2 * result + turn;
		if(result >= MOD2) result = result % MOD2;
		return result;
	}

	public boolean isComplete() {
		return firstPieces == 0 || secondPieces == 0;
	}

	public int getDeltaForCompleteState() {
		return firstPieces - secondPieces;
	}

	public int getAll() {
		return firstPieces + secondPieces;
	}

	public MancalaState move(int pit) {
		int turn1 = SECOND - turn;
		int[] pits1 = new int[pits.length];
		System.arraycopy(pits, 0, pits1, 0, pits.length);
		int n = pits[pit];
		pits1[pit] = 0;
		int delta = 0;
		while(n > 0) {
			if(turn == FIRST && pit == 5) {
				delta++;
				n--;
				if(n == 0) {
					turn1 = turn;
					break;
				}
			} else if(turn == SECOND && pit == 11) {
				delta--;
				n--;
				if(n == 0) {
					turn1 = turn;
					break;
				}
			}
			pit = NEXT[pit];
			n--;
			pits1[pit]++;			
		}
		if(turn1 != turn) {
			if(turn == FIRST && pit < 6 && pits1[pit] == 1) {
				int p = OPPOSITE[pit];
				delta += pits1[p] + 1;
				pits1[p] = 0;
				pits1[pit] = 0;
			} else if(turn == SECOND && pit > 5 && pits1[pit] == 1) {
				int p = OPPOSITE[pit];
				delta -= pits1[p] + 1;
				pits1[p] = 0;
				pits1[pit] = 0;
			}
		}
		
		MancalaState result = new MancalaState(turn1, pits1);
		result.deltascore = delta;
		return result;
	}

	static long[] adds = new long[2];

	/**
	 * FIRST - if first can make it
	 * SECOND - if first cannot make it
	 * UNKNOWN - if it is not clear
	 * @param firstCanMake
	 * @return
	 */
	public int getImmediateResult(int firstCanMake) {
		if(isComplete()) {
			int d = getDeltaForCompleteState();
			return firstCanMake <= d ? FIRST : SECOND;
		}
		int q = getAll();
		if(firstCanMake > q) {
			return SECOND;
		}
		if(firstCanMake < -q) {
			return FIRST;
		}
		return UNKNOWN;
	}

	public boolean solve(short firstCanMake) {
		int q = getResultInMoves(firstCanMake, 1);
		if(q != UNKNOWN) {
			return q == FIRST;
		}
		int r = (getAll() > 12) ? 1 : 0;
		int code1 = code1();
		int code2 = code2();
		if(cacheCode[r][code1] == code2) {
			int min = cacheFirstCanMake[r][code1];
			int max = cacheFirstCanNotMake[r][code1];
			if(min >= firstCanMake) {
				return true;
			}
			if(max <= firstCanMake) {
				return false;
			}
		}
		int b = turn == FIRST ? 0 : 6;
		int e = turn == FIRST ? 6 : 12;
		boolean result = turn == SECOND;
		for (int pit = b; pit < e; pit++) {
			if(pits[pit] == 0) continue;
			MancalaState s = move(pit);
			boolean res = s.solve((short)(firstCanMake - s.deltascore));
			if(turn == FIRST && res) {
				result = true;
				break;
			} else if(turn == SECOND && !res) {
				result = false;
				break;
			}
		}
		if(cacheCode[r][code1] != code2) {
			cacheCode[r][code1] = code2;
			cacheFirstCanMake[r][code1] = -100;
			cacheFirstCanNotMake[r][code1] = 100;
		}
		if(result) {
			if(firstCanMake > cacheFirstCanMake[r][code1]) {
				cacheFirstCanMake[r][code1] = firstCanMake;
			}
		} else {
			if(firstCanMake < cacheFirstCanNotMake[r][code1]) {
				cacheFirstCanNotMake[r][code1] = firstCanMake;
			}
		}
		adds[r]++;
		if(adds[1] % 10000000 == 0) {
			System.out.println("added " + adds[1] + " " + adds[0]);
		}
		return result;
	}

	/**
	 * FIRST - if first can make it in no more than given number of moves;
	 * SECOND - if first cannot make it;
	 * UNKNOWN - if it is not clear.
	 * 
	 * @param firstCanMake
	 * @param moves
	 * @return
	 */
	public int getResultInMoves(short firstCanMake, int moves) {
		int q = getImmediateResult(firstCanMake);
		if(q != UNKNOWN || moves == 0) {
			return q;
		}
		int b = turn == FIRST ? 0 : 6;
		int e = turn == FIRST ? 6 : 12;
		boolean hasUnknown = false;
		for (int pit = b; pit < e; pit++) {
			if(pits[pit] == 0) continue;
			MancalaState s = move(pit);
			q = s.getResultInMoves((short)(firstCanMake - s.deltascore), s.turn == turn ? moves : moves - 1);
			if(q == turn) {
				return q;
			} else {
				hasUnknown = true;
			}
		}
		return hasUnknown ? UNKNOWN : SECOND - turn;
	}
}
