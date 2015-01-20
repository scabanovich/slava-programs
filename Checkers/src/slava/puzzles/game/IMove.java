package slava.puzzles.game;

public interface IMove {

	public Object getInfo();

	public void apply();
	
	public void back();
}
