package slava.puzzle.paths.nook.model;

public class NookPathsPuzzle {
	NookPathsModel model;
	int[] data;  // 0 - no data; 1..size - value
	int[] filter; // 0 - use that cell, 1 - rule that cell out

	public NookPathsPuzzle() {}
	
	public void setModel(NookPathsModel model) {
		this.model = model;
	}
	
	public NookPathsModel getModel() {
		return model;
	}

	public int[] getData() {
		return data;
	}
	
	public int[] getFilter() {
		return filter;
	}
	
	public void init() {
		data = new int[model.getField().getSize()];
		filter = new int[model.getField().getSize()];
	}

}
