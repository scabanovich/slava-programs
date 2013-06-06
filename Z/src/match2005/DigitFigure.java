package match2005;

public class DigitFigure {
	int index;
	int weight;
	int[][] path;
	
	public DigitFigure(int index, int weight, int[][] path) {
		this.index = index;
		this.weight = weight;
		this.path = path;
	}
	
	public boolean equals(DigitFigure f) {
		if(path.length != f.path.length) return false;
		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < 2; j++) {
				if(path[i][j] != f.path[i][j]) return false;
			}
		}
		return true;
	}
	
	public int[][] getPath() {
		return path;
	}
	
}
