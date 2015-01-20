package slava.puzzles.game;

public class ResultStorage {
	int size1 = 51030013;
	int size2 = 52020001;

	int filled = 0;

	Result[] results;
	int[] code2;
	
//	String[] codes;

	public ResultStorage() {
		results = new Result[size1];
		code2 = new int[size1];
//		codes = new String[size1];
	}

	public byte put(String code, byte result, Object bestMove, int depth) {
		int c1 = getCode(code, size1);
		int c2 = getCode(code, size2);
		if(results[c1] == null || c2 == code2[c1]) {
			if(results[c1] == null) {
				results[c1] = new Result(result, bestMove, depth);
				filled++;
			} else {
				if(results[c1].result != GameConstants.INDEFINITE
						&& results[c1].result != result) {
					if(result == GameConstants.INDEFINITE) {
						return results[c1].result;
					} else {
						throw new RuntimeException(code + " " + results[c1].result + " " + result);
					}
				}
				results[c1].result = result;
				if(bestMove != null) results[c1].bestMove = bestMove;
				if(depth > results[c1].depth || result != GameConstants.INDEFINITE) {
					results[c1].depth = depth;
				}
//				if(!code.equals(codes[c1])) {
//					throw new RuntimeException();
//				}
			}
			code2[c1] = c2;
//			codes[c1] = code;
			return results[c1].result;
		}
		return result;
	}

	public Result get(String code) {
		int c1 = getCode(code, size1);
		if(results[c1] != null && getCode(code, size2) == code2[c1]) {
//			if(!code.equals(codes[c1])) {
//				throw new RuntimeException(code + " " + codes[c1] + " " + code2[c1]);
//			}
			return results[c1];
		}
		return null;
	}

	int getCode(String code, int s) {
		long l = 0;
		for (int i = 0; i < code.length(); i++) {
			l = (l * 119 + (int)code.charAt(i)) % s;
		}
		return (int)l;
	}

	public int size() {
		return filled;
	}

	public void clearIndefinite() {
		for (int i = 0; i < results.length; i++) {
			if(results[i] != null && results[i].getResult() == GameConstants.INDEFINITE) {
				results[i] = null;
				code2[i] = 0;
				filled--;
				//
			}
		}
	}
}
