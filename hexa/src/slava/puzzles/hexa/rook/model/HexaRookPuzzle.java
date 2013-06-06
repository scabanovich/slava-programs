package slava.puzzles.hexa.rook.model;

import slava.puzzles.hexa.common.HexaField;

public class HexaRookPuzzle implements HexaRookConstants {
	HexaRookModel model;
	protected int size;
	protected int[] positions;
	
	public HexaRookPuzzle() {
	}

	public void setModel(HexaRookModel model) {
		this.model = model;
		init();
	}
	
	public int[] getPositions() {
		return positions;		
	}
	
	public void init() {
		HexaField field = model.getField();
		size = field.getSize();
		positions = new int[size];
		for (int i = 0; i < positions.length; i++) positions[i] = NOT_POSITION;
	}
	
}
