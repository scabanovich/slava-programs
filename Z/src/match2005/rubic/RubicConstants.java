package match2005.rubic;

public interface RubicConstants {
	public int CORNER_CUBE_COUNT = 8;
	public int EDGE_CUBE_COUNT = 12;
	public int CUBE_COUNT = CORNER_CUBE_COUNT + EDGE_CUBE_COUNT;
	public int SQUARE_COUNT = 48;
	public int FACE_COUNT = 6;
	
	public int CUBE_STATE_COUNT = 24;
	
	/**
	 * Maps 8 corner cubes to sets of 3 face squares.
	 */	
	public int[][] CORNERS = {
		{ 5, 8,42},	{ 7,16,10},	{ 2,39,18},	{ 0,40,37},
		{24,47,13},	{26,15,21},	{31,23,34},	{29,32,45}
	};
	
	/**
	 * Maps 12 edge cubes to sets of 2 fase squares.
	 */
	public int[][] EDGES = {
		{ 6, 9}, {17, 4}, { 1,38}, {41, 3}, {25,14}, {22,28},
		{30,33}, {46,27}, {44,11}, {19,12}, {43,35}, {20,36}
	};
	
	/**
	 * [rotation_index][face_index]
	 */	
	public int[][] CORNER_ROTATION = {{0,1,2}, {2,0,1}, {1,2,0}};
	
	/**
	 * [rotation_index][face_index]
	 */	
	public int[][] EDGES_ROTATION = {{0,1},{1,0}};
	
	
	public int[][][][] CORNER_ROTATIONS_DESCRIPTIONS = {
		{{{0,0},{3,0},{2,0},{1,0}}}, //T
		{{{0,0},{1,0},{2,0},{3,0}}}, //T'
		{{{0,0},{2,0}},{{1,0},{3,0}}}, //T2
		{{{0,1},{1,2},{5,1},{4,2}}}, //F
		{{{0,1},{4,2},{5,1},{1,2}}}, //F'
		{{{0,0},{5,0}},{{1,0},{4,0}}}, //F2
		{{{1,1},{2,2},{6,1},{5,2}}}, //R
		{{{1,1},{5,2},{6,1},{2,2}}}, //R'
		{{{1,0},{6,0}},{{2,0},{5,0}}}, //R2
		{{{4,0},{5,0},{6,0},{7,0}}}, //B
		{{{4,0},{7,0},{6,0},{5,0}}}, //B'
		{{{4,0},{6,0}},{{7,0},{5,0}}}, //B2
		{{{7,1},{6,2},{2,1},{3,2}}}, //H
		{{{7,1},{3,2},{2,1},{6,2}}}, //H'
		{{{7,0},{2,0}},{{6,0},{3,0}}}, //H2
		{{{3,1},{0,2},{4,1},{7,2}}}, //L
		{{{3,1},{7,2},{4,1},{0,2}}}, //L'
		{{{3,0},{4,0}},{{0,0},{7,0}}}, //L2
	};
	
	public String[] ROTATIONS_NOTATION = {
		"U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2",
		"D", "D'", "D2", "B", "B'", "B2", "L", "L'", "L2",
	};

	public int[][][][] EDGE_ROTATIONS_DESCRIPTIONS = {
		{{{0,1},{3,1},{2,1},{1,1}}}, //U
		{{{0,1},{1,1},{2,1},{3,1}}}, //U'
		{{{0,0},{2,0}},{{3,0},{1,0}}}, //U2
		{{{0,0},{9,0},{4,0},{8,0}}}, //F
		{{{0,0},{8,0},{4,0},{9,0}}}, //F'
		{{{0,0},{4,0}},{{8,0},{9,0}}}, //F2
		{{{1,0},{11,0},{5,0},{9,0}}}, //R
		{{{1,0},{9,0},{5,0},{11,0}}}, //R'
		{{{1,0},{5,0}},{{11,0},{9,0}}}, //R2
		{{{4,1},{5,1},{6,1},{7,1}}}, //D
		{{{4,1},{7,1},{6,1},{5,1}}}, //D'
		{{{4,0},{6,0}},{{5,0},{7,0}}}, //D2
		{{{6,0},{11,0},{2,0},{10,0}}}, //B
		{{{6,0},{10,0},{2,0},{11,0}}}, //B'
		{{{6,0},{2,0}},{{11,0},{10,0}}}, //B2
		{{{3,0},{8,0},{7,0},{10,0}}}, //L
		{{{3,0},{10,0},{7,0},{8,0}}}, //L'
		{{{3,0},{7,0}},{{8,0},{10,0}}}, //L2
			
	};
	
	public int[] REVERSE_ROTATION = {1,0,2, 4,3,5, 7,6,8, 10,9,11, 13,12,14, 16,15,17, 19,18,20, 22,21,23, 25,24,26};

	class RubicRotationInitializer {
		public static RubicRotation[] init() {
			RubicRotation[] rs = new RubicRotation[CORNER_ROTATIONS_DESCRIPTIONS.length]; 
			for (int i = 0; i < CORNER_ROTATIONS_DESCRIPTIONS.length; i++) {
				int[][][] corners = CORNER_ROTATIONS_DESCRIPTIONS[i];
				int[][][] edges = EDGE_ROTATIONS_DESCRIPTIONS[i];
				int axe = (i < 18) ? ((i / 3) % 3) : (i - 18) / 3;
				int range = (i / 9);
				rs[i] = new RubicRotation(axe, range, corners, edges);
			}
			return rs;
		}
	}
	public RubicRotation[] ROTATIONS = RubicRotationInitializer.init();
	
}
