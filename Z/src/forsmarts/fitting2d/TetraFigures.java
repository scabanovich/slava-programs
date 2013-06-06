package forsmarts.fitting2d;

public interface TetraFigures {
	public int[][][] FIGURES = {
		{{0,0},{0,1},{0,2},{0,3}}, //I
		{{0,0},{0,1},{0,2},{1,0}}, //L
		{{0,0},{0,1},{1,0},{1,1}}, //O
		{{0,0},{0,1},{1,1},{1,2}}, //S
		{{0,0},{0,1},{0,2},{1,1}}, //T
	};
	public char[] DESIGNATIONS = new char[]{'I','L','O','S','T'};

}
