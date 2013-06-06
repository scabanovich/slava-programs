package slava.puzzles.seabattle.model;

public interface SeaBattleConstants {
	public int EMPTY = -1;
	public int WATER = 0;
	public int EAST_END = 1;
	public int SOUTH_END = 2;
	public int WEST_END = 3;
	public int NORTH_END = 4;
	public int MIDDLE = 5;
	public int SINGLE = 6;
	
	public int GENERSTE_BY_REDUCING = 0;
	public int GENERSTE_BY_NAROWING_WITH_WATER_ONLY = 1;
	public int GENERSTE_BY_NAROWING_WITH_ALL = 2;
	
	public int[] DEFAULT_SHIPS = new int[]{0, 4, 3, 2, 1};

}
