package forsmarts;

import java.util.*;

public class DistortedChameleonFigure {
	int value;
	int[][] points;    //[index]{x,y}
	int[][] crossings; //[d][index]=relative line
	
	public DistortedChameleonFigure(int[][] points, int[][] crossings) {
		this.points = points;
		this.crossings = crossings;
	}
	
	public DistortedChameleonFigure copy(int value) {
		DistortedChameleonFigure f = new DistortedChameleonFigure(points, crossings);
		f.value = value;
		return f;
	}
	
	
	static DistortedChameleonFigure ONE_X = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0},{2,0}},
		new int[][]{{0,1}, {}, {-2,-1}}
	);
	static DistortedChameleonFigure ONE_Y = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1},{0,2}},
		new int[][]{{}, {0,1}, {0,1}}
	);
	static DistortedChameleonFigure ONE_Z = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1},{2,2}},
		new int[][]{{0,1}, {0,1}, {}}
	);
	
	static DistortedChameleonFigure[] ONE_LIST = {ONE_X, ONE_Y, ONE_Z};
	
	static DistortedChameleonFigure EIGHT_X_0 = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0}, {0,1},{1,1}, {0,2},{1,2}},
		new int[][]{{0}, {0,1}, {-1,0,1}}
	);
	static DistortedChameleonFigure EIGHT_X_1 = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1}, {0,1},{1,2}, {0,2},{1,3}},
		new int[][]{{0}, {0,1,2}, {0,1}}
	);
	static DistortedChameleonFigure EIGHT_Y_0 = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1}, {1,0},{2,1}, {2,0},{3,1}},
		new int[][]{{0,1,2}, {0}, {-2,-1}}
	);
	static DistortedChameleonFigure EIGHT_Y_1 = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,0},{1,1}, {2,0},{2,1}},
		new int[][]{{0,1}, {0}, {-2,-1,0}}
	);
	static DistortedChameleonFigure EIGHT_Z_0 = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,1},{1,2}, {2,2},{2,3}},
		new int[][]{{0,1}, {0,1,2}, {0}}
	);
	static DistortedChameleonFigure EIGHT_Z_1 = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0}, {1,1},{2,1}, {2,2},{3,2}},
		new int[][]{{0,1,2}, {0,1}, {-1}}
	);

	static DistortedChameleonFigure[] EIGHT_LIST = {EIGHT_X_0, EIGHT_X_1, EIGHT_Y_0, EIGHT_Y_1, EIGHT_Z_0, EIGHT_Z_1};

	static DistortedChameleonFigure FOUR_X_0a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0}, {0,1},{1,1},  {1,2}},
		new int[][]{{0}, {0,1}, {-1,0}}
	);
	static DistortedChameleonFigure FOUR_X_0b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {0,1},{1,1}, {0,2},{1,2}},
		new int[][]{{0}, {0,1}, {0,1}}
	);
	static DistortedChameleonFigure FOUR_X_1a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1}, {0,1},{1,2},  {1,3}},
		new int[][]{{0}, {0,1,2}, {0,1}}
	);
	static DistortedChameleonFigure FOUR_X_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {0,1},{1,2}, {0,2},{1,3}},
		new int[][]{{0}, {0,1,2}, {0,1}}
	);
	static DistortedChameleonFigure FOUR_Y_0a = new DistortedChameleonFigure(
		new int[][]{ {1,1}, {1,0},{2,1}, {2,0},{3,1}},
		new int[][]{{1,2}, {0}, {-2,-1}}
	);
	static DistortedChameleonFigure FOUR_Y_0b = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1}, {1,0},{2,1}, {2,0} },
		new int[][]{{0,1}, {0}, {-2,-1}}
	);
	static DistortedChameleonFigure FOUR_Y_1a = new DistortedChameleonFigure(
		new int[][]{ {0,1}, {1,0},{1,1}, {2,0},{2,1}},
		new int[][]{{0,1}, {0}, {-2,-1,0}}
	);
	static DistortedChameleonFigure FOUR_Y_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,0},{1,1}, {2,0} },
		new int[][]{{0,1}, {0}, {-2,-1,0}}
	);
	static DistortedChameleonFigure FOUR_Z_0a = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,1},{1,2}, {2,2} },
		new int[][]{{0,1}, {0,1}, {0}}
	);
	static DistortedChameleonFigure FOUR_Z_0b = new DistortedChameleonFigure(
		new int[][]{ {0,1}, {1,1},{1,2}, {2,2},{2,3}},
		new int[][]{{0,1}, {1,2}, {0}}
	);
	static DistortedChameleonFigure FOUR_Z_1a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0}, {1,1},{2,1},  {3,2}},
		new int[][]{{0,1,2}, {0,1}, {-1}}
	);
	static DistortedChameleonFigure FOUR_Z_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {1,1},{2,1}, {2,2},{3,2}},
		new int[][]{{0,1,2}, {0,1}, {-1}}
	);

	static DistortedChameleonFigure[] FOUR_LIST = {
		FOUR_X_0a, FOUR_X_0b, FOUR_X_1a, FOUR_X_1b,
		FOUR_Y_0a, FOUR_Y_0b, FOUR_Y_1a, FOUR_Y_1b,
		FOUR_Z_0a, FOUR_Z_0b, FOUR_Z_1a, FOUR_Z_1b,
	};

	static DistortedChameleonFigure SEVEN_X_0a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0},  {1,1},  {1,2}},
		new int[][]{{0}, {0,1}, {-1,0}}
	);
	static DistortedChameleonFigure SEVEN_X_0b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {0,1},  {0,2},{1,2}},
		new int[][]{{0}, {0,1}, {0,1}}
	);
	static DistortedChameleonFigure SEVEN_X_1a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1},  {1,2},  {1,3}},
		new int[][]{{0}, {0,1,2}, {0,1}}
	);
	static DistortedChameleonFigure SEVEN_X_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {0,1},  {0,2},{1,3}},
		new int[][]{{0}, {0,1,2}, {0,1}}
	);
	static DistortedChameleonFigure SEVEN_Y_0a = new DistortedChameleonFigure(
		new int[][]{ {1,1},  {2,1}, {2,0},{3,1}},
		new int[][]{{1,2}, {0}, {-2,-1}}
	);
	static DistortedChameleonFigure SEVEN_Y_0b = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,1}, {1,0},  {2,0} },
		new int[][]{{0,1}, {0}, {-2,-1}}
	);
	static DistortedChameleonFigure SEVEN_Y_1a = new DistortedChameleonFigure(
		new int[][]{ {0,1},  {1,1}, {2,0},{2,1}},
		new int[][]{{0,1}, {0}, {-2,-1,0}}
	);
	static DistortedChameleonFigure SEVEN_Y_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,0},  {2,0} },
		new int[][]{{0,1}, {0}, {-2,-1,0}}
	);
	static DistortedChameleonFigure SEVEN_Z_0a = new DistortedChameleonFigure(
		new int[][]{{0,0},{0,1}, {1,1},  {2,2} },
		new int[][]{{0,1}, {0,1}, {0}}
	);
	static DistortedChameleonFigure SEVEN_Z_0b = new DistortedChameleonFigure(
		new int[][]{ {0,1},  {1,2}, {2,2},{2,3}},
		new int[][]{{0,1}, {1,2}, {0}}
	);
	static DistortedChameleonFigure SEVEN_Z_1a = new DistortedChameleonFigure(
		new int[][]{{0,0},{1,0},  {2,1},  {3,2}},
		new int[][]{{0,1,2}, {0,1}, {-1}}
	);
	static DistortedChameleonFigure SEVEN_Z_1b = new DistortedChameleonFigure(
		new int[][]{{0,0},  {1,1},  {2,2},{3,2}},
		new int[][]{{0,1,2}, {0,1}, {-1}}
	);
	
	static DistortedChameleonFigure[] SEVEN_LIST = {
		SEVEN_X_0a, SEVEN_X_0b, SEVEN_X_1a, SEVEN_X_1b,
		SEVEN_Y_0a, SEVEN_Y_0b, SEVEN_Y_1a, SEVEN_Y_1b,
		SEVEN_Z_0a, SEVEN_Z_0b, SEVEN_Z_1a, SEVEN_Z_1b,
	};

	public static DistortedChameleonFigure[] FIGURES_1_7;
	public static DistortedChameleonFigure[] FIGURES_0_9;
	
	static void add(List list, DistortedChameleonFigure[] templateList, int value) {
		for (int j = 0; j < templateList.length; j++) {
			list.add(templateList[j].copy(value));
		}
	}

	static {
		List list = new ArrayList();
		int[] eights = new int[]{2,3,5,6};
		for (int i = 0; i < eights.length; i++) {
			add(list, EIGHT_LIST, eights[i]);
		}
		add(list, ONE_LIST, 1);
		add(list, FOUR_LIST, 4);
		add(list, SEVEN_LIST, 7);		
		FIGURES_1_7 = (DistortedChameleonFigure[])list.toArray(new DistortedChameleonFigure[0]);
		
		list = new ArrayList();
		eights = new int[]{0,2,3,5,6,8,9};
		for (int i = 0; i < eights.length; i++) {
			add(list, EIGHT_LIST, eights[i]);
		}
		add(list, ONE_LIST, 1);
		add(list, FOUR_LIST, 4);
		add(list, SEVEN_LIST, 7);		
		FIGURES_0_9 = (DistortedChameleonFigure[])list.toArray(new DistortedChameleonFigure[0]);
	}


}
