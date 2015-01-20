package slava.puzzles.game;

public interface GameConstants {
	public byte WHITE = 0;
	public byte BLACK = 1;

	public byte WHITE_WIN = 1;
	public byte DRAW = 0;
	public byte BLACK_WIN = -1;
	public byte INDEFINITE = -2;

	public int[] WEIGHTS = {0, 0, 10, 16, 20,   23, 26, 28, 30, 
			32, 34, 35, 36, 37, 38, 39, 40, 
			41, 42, 43, 44, 44, 45, 45, 46, 46, 47, 47, 48, 48, 49, 49, 50, 50, 50, 51, 51, 51, 52, 52, 52, 53, 53, 53};
}
