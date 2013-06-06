package forsmarts.fitting2d;

public class Problem2D_b extends Problem2D {
	
	Problem2D_b() {
		setB();
	}

	void setB() {
		xSize = 5;
		ySize = 4;
		figures = new int[][][] {
			{{0,0},{0,1},{0,2},{0,3},{1,3}}, //L
			{{0,0},{1,0},{2,0},{1,1},{1,2}}, //T
			{{0,0},{0,1},{1,1},{2,1},{2,2}}, //S	                       
		};
		isReflectionAllowed = true;
		usageLimit = new int[]{1,1,1};
		designations = new char[]{'L','T','S'};
		shapeMode = PackingAnalysis.NO_SHAPE;
		fieldRestriction = new int[]{
			0,0,0,0,1,
			0,0,1,0,0,
			0,0,0,0,0,
			0,0,0,0,0,
		};
	}

}
