package slava.puzzles.game;

public class Result {
	byte result;
	int depth;
	Object bestMove;

	public Result(byte result, Object bestMove, int depth) {
		this.result = result;
		this.depth = depth;
		this.bestMove = bestMove;
	}

	public byte getResult() {
		return result;
	}

	public Object getBestMove() {
		return bestMove;
	}

	public int getDepth() {
		return depth;
	}
}
