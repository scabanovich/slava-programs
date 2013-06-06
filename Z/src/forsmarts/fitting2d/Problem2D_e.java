package forsmarts.fitting2d;

public class Problem2D_e extends Problem2D {
	
	Problem2D_e() {
		setWorld();
	}
	
	void setWorld() {
		xSize = 9;
		ySize = 9;
		figures = WORLD_FIGURES;
		isReflectionAllowed = false;
		usageLimit = new int[figures.length];
		for (int i = 0; i < figures.length; i++) usageLimit[i] = 1;
		designations = WORLD_DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;

		setRestrictions(4, WORLD_RESTRICTIONS, false);
	}
	
	void setCup() {
		xSize = 7;
		ySize = 7;
		figures = CUP_FIGURES;
		isReflectionAllowed = false;
		usageLimit = new int[figures.length];
		for (int i = 0; i < figures.length; i++) usageLimit[i] = 1;
		designations = CUP_DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;

		setRestrictions(3, CUP_RESTRICTIONS, true);
	}

	private void setRestrictions(int n, int[] points, boolean random) {
		int[] selected = new int[xSize * ySize];
		for (int i = 0; i < selected.length; i++) selected[i] = 1;
		if(random || points == null) {
			setRandomRestrictions(selected, n);
		} else {
			for (int i = 0; i < points.length; i++) {
				selected[points[i]] = 0;
			}
		}
		fieldRestriction = new int[selected.length];
		for (int i = 0; i < selected.length; i++) fieldRestriction[i] = 1 - selected[i];
		
		if(random) {
			showSolutionLimit = 0;
			solutionLimit = 2;
		} else {
			showSolutionLimit = 2;
		}
	}

	private void setRandomRestrictions(int[] selected, int n) {
		for (int q = 0; q < n; q++) {
			int p = (int)(selected.length * Math.random());
			selected[p] = 0;
			System.out.print(" " + p);
		}
		System.out.println("");
	}
	
	static int[][][] WORLD_FIGURES = {
		{{0,0},{0,1},{0,2},{0,3},{1,3},{2,0},{2,1},{2,2},{2,3},{3,3},{4,0},{4,1},{4,2},{4,3}}, //W
		{{0,0},{0,1},{0,2},{0,3},{1,0},{1,3},{2,0},{2,1},{2,2},{2,3}}, //O
		{{0,0},{0,1},{0,2},{0,3},{1,0},{1,2},{2,0},{2,1},{2,2},{3,2},{3,3}}, //R
		{{0,0},{0,1},{0,2},{0,3},{1,3},{2,3}}, //L
		{{0,0},{0,1},{0,2},{0,3},{1,0},{1,3},{2,1},{2,2}}, //D
	};
	public char[] WORLD_DESIGNATIONS = new char[]{'W','O','R','L','D'};
//	int[] WORLD_RESTRICTIONS = {20, 28, 44, 56, 64, 78};
	int[] WORLD_RESTRICTIONS = {20, 47, 56, 57, 79};

	static int[][][] CUP_FIGURES = {
		{{0,0},{0,1},{0,2},{0,3},{1,0},{1,3},{2,0},{2,3}}, //C
		{{0,0},{0,1},{0,2},{0,3},{1,3},{2,0},{2,1},{2,2},{2,3}}, //U
		{{0,0},{0,1},{0,2},{0,3},{1,0},{1,2},{2,0},{2,1},{2,2}}, //P
	};
	public char[] CUP_DESIGNATIONS = new char[]{'C','U','P'};
	int[] CUP_RESTRICTIONS = {9, 11, 17, 30};
	//9 11 17 30
}

/*
(world) 5: C3 C6 C7 D7 H9
(cup) 4: C2 E2 D3 C5
*/