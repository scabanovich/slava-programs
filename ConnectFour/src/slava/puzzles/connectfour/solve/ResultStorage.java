package slava.puzzles.connectfour.solve;

public class ResultStorage {
	public static int MOD_1 = 48023671;
	public static int MOD_2 = 79023673;
	int[] codes = new int[MOD_1];
	int[] depth = new int[MOD_1];
	byte[][] results = new byte[MOD_1][];
	
	static ResultStorage instance = new ResultStorage();

	public static ResultStorage getInstance() {
		return instance;
	}

	public ResultStorage() {
		for (int i = 0; i < codes.length; i++) codes[i] = -1;
	}

	public boolean contains(int code1, int code2) {
		return codes[getPosition(code1, code2)] == code2;
	}

	private int getPosition(int code1, int code2) {
		if(codes[code1] < 0 || codes[code1] == code2) {
			return code1;
		}
		int c = code1 + 1;
		int cm = code1 + 5;
		if(cm >= codes.length) cm = codes.length - 1;
		while(c <= cm) {
			if(codes[c] < 0 || codes[c] == code2) {
				return c;
			}
			c++;
		}
		return code1;
	}

	public byte[] get(int code1, int code2) {
		int c = getPosition(code1, code2);
		return codes[c] == code2 ? results[c] : null;
	}

	public int getDepth(int code1, int code2) {
		int c = getPosition(code1, code2);
		return codes[c] == code2 ? depth[c] : -1;
	}

	public void put(int code1, int code2, byte[] res, int depth) {
		int c = getPosition(code1, code2);
		codes[c] = code2;
		this.depth[c] = depth;
		results[c] = res;
	}

	public int getStorageSize() {
		int s = 0;
		for (int i = 0; i < codes.length; i++) {
			if(codes[i] >= 0) s++;
		}
		return s;
	}

}
