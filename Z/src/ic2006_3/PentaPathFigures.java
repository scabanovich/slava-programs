package ic2006_3;

public class PentaPathFigures {
	public int[][][] FIGURES = {
			{{0,1, 1},{1,0, 1},{1,1, 0},{1,2, 1},{2,1, 1}}, //X
			{{0,0, 0},{0,1, 0},{1,1, 0},{1,2, 1},{2,1, 0}}, //F
			{{0,0, 1},{1,0, 0},{1,1, 0},{2,1, 0},{2,2, 1}}, //W
			{{0,0, 1},{1,0, 0},{2,0, 1},{1,1, 0},{1,2, 0}}, //T
			{{0,0, 1},{0,1, 0},{0,2, 0},{0,3, 0},{0,4, 1}}, //I
			{{0,0, 0},{0,1, 0},{0,2, 0},{1,2, 0},{1,3, 1}}, //N
			{{0,0, 0},{0,1, 0},{0,2, 0},{0,3, 0},{1,3, 1}}, //L
			{{0,0, 0},{0,1, 0},{0,2, 1},{1,0, 0},{1,1, 0}}, //P
			{{0,0, 0},{0,1, 0},{0,2, 0},{0,3, 0},{1,2, 1}}, //Y
			{{0,0, 1},{0,1, 0},{1,1, 0},{2,1, 0},{2,2, 1}}, //Z	                       
			{{0,0, 0},{0,1, 0},{0,2, 1},{1,0, 0},{2,0, 1}}, //V
			{{0,0, 0},{0,1, 0},{0,2, 0},{1,0, 1},{1,2, 1}}, //U
	};
	public static char[] DESIGNATIONS = new char[]{'X','F','W','T','I','N','L','P','Y','Z','V','U'};

}
