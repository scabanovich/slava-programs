package slava.puzzles.seabattle.model;

import com.slava.common.RectangularField;

public class AbstractSeaBattlePuzzle {
	protected RectangularField field;
	protected int size;
	
	protected int[] shipCount;
	protected int[] data;
	protected int[] hValues;
	protected int[] vValues;
	
	public AbstractSeaBattlePuzzle() {
		shipCount = SeaBattleConstants.DEFAULT_SHIPS;
	}
	
	public int[] getData() {
		return data;
	}
	
	public int[] getHValues() {
		return hValues;
	}
	
	public int[] getVValues() {
		return vValues;
	}
	
	public int[] getShips() {
		return shipCount;
	}
	
	public void setShips(int[] shipCount) {
		this.shipCount = shipCount;
	}
	
	public int getShipCount(int size) {
		return size >= shipCount.length ? 0 : shipCount[size];
	}
	
	public void setShipCount(int size, int count) {
		if(size == 5) {
			if(count == 0) {
				if(shipCount.length == 6) {
					int[] sc = new int[5];
					System.arraycopy(shipCount, 0, sc, 0, 5);
					shipCount = sc;
				}
			} else {
				if(shipCount.length == 5) {
					int[] sc = new int[6];
					System.arraycopy(shipCount, 0, sc, 0, 5);
					shipCount = sc;
					shipCount[size] = count;
				}
			}
		} else {
			shipCount[size] = count;
		}
	}

}
