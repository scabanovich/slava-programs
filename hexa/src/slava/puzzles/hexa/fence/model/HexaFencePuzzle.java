package slava.puzzles.hexa.fence.model;

import slava.puzzles.hexa.common.HexaField;

public class HexaFencePuzzle implements HexaFenceConstants {
	HexaFenceModel model;
	protected int size;
	protected int[] data;
	protected int[] form;

	public HexaFencePuzzle() {}
	
	public void setModel(HexaFenceModel model) {
		this.model = model;
		init();
	}
	
	public int[] getData() {
		return data;
	}
	
	public int[] getForm() {
		return form;
	}

	public void init() {
		HexaField field = model.getField();
		size = field.getSize();
		data = new int[size];
		for (int i = 0; i < data.length; i++) data[i] = NO_VALUE;
		form = new int[size];
		for (int p = 0; p < size; p++) {
			if(field.isInField(p)) {
				form[p] = CELL_IN_FIELD;
			} else {
				form[p] = CELL_OUT_OF_FIELD;
			}
		}
	}

}
