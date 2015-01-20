package slava.puzzles.game;

import java.util.List;

public interface IPosition extends GameConstants {

	/**
	 * WHITE or BLACK
	 * @return
	 */
	public byte getTurn();

	/**
	 * Returns trivial result or INDEFINITE.
	 * @return
	 */
	public byte getResult();

	public String getCode();

	public List<? extends IMove> getMoves();
}
