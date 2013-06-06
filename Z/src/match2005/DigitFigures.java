package match2005;

import java.util.ArrayList;

public class DigitFigures {
	int[] weight = {
		3,
		4,
//		5,
		2,
		1,
//		-1
	};
	int[][][] paths = {
		{{0,0},{1,0},{1,1},{2,1},{2,2},{2,3}}, //3

		{{0,0},
		 {-1,1},      //{0,1},
		 {-2,2},{-1,2},{0,2},{0,3},{0,4}},     //4

//		{{0,0},{-1,0},{-2,0},                  
//		 {-2,1},      {-1,1}, //{0,1},
//		 {-2,2},{-1,2},{0,2},
//		 {0,3},       {-1,3}, //{-2,3},
//		 {0,4},{-1,4},{-2,4}},                 //5
		 
		{{0,0},{1,0},{1,1},{0,1},{0,2},{1,2}}, //2
		{{0,0},{1,-1},{1,0},{1,1}},            //1
//		{{0,0},{1,0}}                          //-
	};
	
	int[][] rotate(int[][] path) {
		int[][] npath = new int[path.length][2];
		for (int i = 0; i < path.length; i++) {
			npath[i][0] = -path[i][1];
			npath[i][1] = path[i][0];
		}
		return npath;
	}
	
	int[][] reverse(int[][] path) {
		int[][] npath = new int[path.length][2];
		int dx = path[path.length - 1][0];
		int dy = path[path.length - 1][1];
		for (int i = 0; i < path.length; i++) {
			npath[path.length - 1 - i][0] = path[i][0] - dx;
			npath[path.length - 1 - i][1] = path[i][1] - dy;
		}
		return npath;
	}

	DigitFigure[] figures;
	
	public void init() {
		ArrayList list = new ArrayList();
		PentapipeField f = new PentapipeField();
		f.setSize(7);
		for (int i = 0; i < paths.length; i++) {
			DigitFigure figure = new DigitFigure(i, weight[i], paths[i]);
			list.add(figure);
			int[][] path = rotate(paths[i]);
			for (int k = 0; k < 4; k++) {
				figure = new DigitFigure(i, weight[i], path);
				if(contains(list, figure)) break;
				list.add(figure);
				path = rotate(path);
			}
			path = reverse(paths[i]);
			for (int k = 0; k < 4; k++) {
				figure = new DigitFigure(i, weight[i], path);
				if(contains(list, figure)) break;
				list.add(figure);
				path = rotate(path);
			}
		}		
		figures = (DigitFigure[])list.toArray(new DigitFigure[0]);
	}
	
	boolean contains(ArrayList list, DigitFigure f) {
		for (int i = 0; i < list.size(); i++) {
			DigitFigure fi = (DigitFigure)list.get(i);
			if(fi.equals(f)) return true;
		}
		return false;
	}
	
}
