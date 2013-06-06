package ic2005_3;

public class Labirint {
	// fromIndex, direction, {toIndex, dx, dy}
	int[][][] transition = {
		{{11, 2, 0}, { 4, 0, 2}, { 9, -1, 0}, { 4, 0, -2}},
		{{ 5, 2, 0}, {10, 0, 2}, { 5, -2, 0}, { 8, 0, -1}},
		{{ 9, 1, 0}, { 6, 0, 2}, {11, -2, 0}, { 6, 0, -2}},
		{{ 7, 2, 0}, { 8, 0, 1}, { 7, -2, 0}, {10, 0, -2}},
		
		{{10, 3, 0}, { 0, 0, 2}, { 8, -2, 0}, { 0, 0, -2}},
		{{ 1, 2, 0}, {11, 0, 3}, { 1, -2, 0}, { 9, 0, -2}},
		{{ 8, 2, 0}, { 2, 0, 2}, {10, -3, 0}, { 2, 0, -2}},
		{{ 3, 2, 0}, { 9, 0, 2}, { 3, -2, 0}, {11, 0, -3}},
		
		{{ 4, 2, 0}, { 1, 0, 1}, { 6, -2, 0}, { 3, 0, -1}},
		{{ 0, 1, 0}, { 5, 0, 2}, { 2, -1, 0}, { 7, 0, -2}},
		{{ 6, 3, 0}, { 3, 0, 2}, { 4, -3, 0}, { 1, 0, -2}},
		{{ 2, 2, 0}, { 7, 0, 3}, { 0, -2, 0}, { 5, 0, -3}}
	};

	// fromIndex, direction, pointIndex, {dx, dy}
	int[][][][] checkPoints = {
		{ //0
			{{1,0}, {2,-1}, {2,1}},
			{{1,0}, {0,2}, {1,2}},
			{{-1,0}, {-1,-1}, {-1,1}},
			{{1,0}, {0,-2}, {1,-2}}
		},
		{ //1
			{{0,1}, {2,0}, {2,1}},
			{{0,1}, {-1,2}, {1,2}},
			{{0,1}, {-2,0}, {-2,1}},
			{{-1,-1}, {0,-1}, {1,-1}}
		},
		{ //2
			{{1,-1}, {1,0}, {1,1}},
			{{-1,0}, {0,2}, {-1,2}},
			{{-1,0}, {-2,-1}, {-2,1}},
			{{-1,0}, {0,-2}, {-1,-2}}
		},
		{ //3
			{{0,-1}, {2,0}, {2,-1}},
			{{-1,1}, {0,1}, {1,1}},
			{{0,-1}, {-2,0}, {-2,-1}},
			{{0,-1}, {-1,-2}, {1,-2}},
		},
		{ //4
			{{2,0}, {3,0}, {4,0}},
			{{0,1}, {0,2}, {0,3}, {1,1}, {1,2}, {1,3}},
			{{-1,0}, {-2,0}, {-3,0}},
			{{0,-1}, {0,-2}, {0,-3}, {1,-1}, {1,-2}, {1,-3}}
		},
		{ //5
			{{1,0}, {2,0}, {3,0}, {1,1}, {2,1}, {3,1}},
			{{0,2}, {0,3}, {0,4}},
			{{-1,0}, {-2,0}, {-3,0}, {-1,1}, {-2,1}, {-3,1}},
			{{0,-1}, {0,-2}, {0,-3}}
		},
		{ //6
			{{1,0}, {2,0}, {3,0}},
			{{0,1}, {0,2}, {0,3}, {-1,1}, {-1,2}, {-1,3}},
			{{-2,0}, {-3,0}, {-4,0}},
			{{0,-1}, {0,-2}, {0,-3}, {-1,-1}, {-1,-2}, {-1,-3}}
		},
		{ //7
			{{1,0}, {2,0}, {3,0}, {1,-1}, {2,-1}, {3,-1}},
			{{0,1}, {0,2}, {0,3}},
			{{-1,0}, {-2,0}, {-3,0}, {-1,-1}, {-2,-1}, {-3,-1}},
			{{0,-2}, {0,-3}, {0,-4}}
		},
		{ //8
			{{2,0}, {3,0}},
			{{-1,1}, {0,1}, {1,1}, {-1,2}, {1,2}},
			{{-2,0}, {-3,0}},
			{{-1,-1}, {0,-1}, {1,-1}, {-1,-2}, {1,-2}}
		},
		{ //9
			{{1,-1}, {1,0}, {1,1}, {2,-1}, {2,1}},
			{{0,2}, {0,3}},
			{{-1,-1}, {-1,0}, {-1,1}, {-2,-1}, {-2,1}},
			{{0,-2}, {0,-3}}
		},
		{ //10
			{{0,0}, {2,0}, {3,0}},
			{{-1,1}, {0,1}, {1,1}, {-1,2}, {0,2}, {1,2}},
			{{0,0}, {-2,0}, {-3,0}},
			{{-1,-1}, {0,-1}, {1,-1}, {-1,-2}, {0,-2}, {1,-2}}
		},
		{ //11
			{{1,-1}, {1,0}, {1,1}, {2,-1}, {2,0}, {2,1}},
			{{0,0}, {0,2}, {0,3}},
			{{-1,-1}, {-1,0}, {-1,1}, {-2,-1}, {-2,0}, {-2,1}},
			{{0,0}, {0,-2}, {0,-3}}
		},
	};
	
	int width;
	int height;
	
	int size;
	int[] x,y;
	int[][] xy;
	
	int[] field;
	
	//state, x,y
	int[] startData = {1,1,0};
	int[] endData = {1,5,0};
	
	int startState;
	int startPlace;
	
	int endState;
	int endPlace;
	
	int[][] states;
	
	public Labirint() {
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
	}
	
	int jump(int p, int dx, int dy) {
		if(p < 0 || p >= size) return -1;
		int px = x[p] + dx;
		int py = y[p] + dy;
		if(px < 0 || px >= width) return -1;
		if(py < 0 || py >= height) return -1;
		return xy[px][py];
	}
	
	public void setObstacles(int[] ps) {
		field = new int[size];
		for (int i = 0;i < ps.length; i++) field[ps[i]] = 1;
	}
	
	public void setStartAndEndData() {
		startState = startData[0];
		startPlace = xy[startData[1]][startData[2]];
		endState = endData[0];
		endPlace = xy[endData[1]][endData[2]];
	}
	
	public int solve() {
		init();
		int t = 0;
		while(true) {
			int changes = makeStep(t);
			if(changes == 0) return -1;
			if(states[endPlace][endState] >= 0) return states[endPlace][endState];
			++t;
		}
	}
	
	void init() {
		states = new int[size][12];
		for (int p = 0; p < size; p++) {
			for (int s = 0; s < 12; s++) states[p][s] = -1;
		}
		states[startPlace][startState] = 0;
	}
	
	int makeStep(int t) {
		int changes = 0;
		for (int p = 0; p < size; p++) {
			for (int s = 0; s < 12; s++) {
				if(states[p][s] != t) continue;
				for (int d = 0; d < 4; d++) {
					if(!canMove(p, s, d)) continue;
					int s1 = transition[s][d][0];
					int p1 = jump(p, transition[s][d][1], transition[s][d][2]);
					if(states[p1][s1] >= 0) continue;
					states[p1][s1] = t + 1;
					changes++;
				}
			}
		}
		return changes;
	}
	
	boolean canMove(int p, int s, int d) {
		int[][] points = checkPoints[s][d];
		for (int i = 0; i < points.length; i++) {
			int q = jump(p, points[i][0], points[i][1]);
			if(q < 0 || field[q] != 0) return false;
		}
		return true;
	}
	
	String obstaclesToString() {
		StringBuffer sb = new StringBuffer();
		for (int ix = 0; ix < width; ix++) {
			for (int iy = 0; iy < height; iy++) {
				int p = xy[ix][iy];
				if(field[p] == 0) continue;
				char cx = (char)(97 + ix);
				if(sb.length() > 0) sb.append(",");
				sb.append(cx).append((iy + 1));
			}
		}
		return sb.toString();
	}
	
	String buildSolution() {
		StringBuffer sb = new StringBuffer();
		int p = endPlace;
		int s = endState;
		while(true) {
			int tt = states[p][s];
			if(tt == 0) break;
			int db = -1;
			for (int d = 0; d < 4 && db < 0; d++) {
				if(!canMove(p, s, d)) continue;
				int s1 = transition[s][d][0];
				int p1 = jump(p, transition[s][d][1], transition[s][d][2]);
				if(states[p1][s1] == tt - 1) db = d;

			}
			if(db < 0) break;
			sb.append(db);
			int s1 = transition[s][db][0];
			int p1 = jump(p, transition[s][db][1], transition[s][db][2]);
			p = p1;
			s = s1;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Labirint lab = new Labirint();
		lab.setSize(7, 7);
		lab.setStartAndEndData();
		
		int[] noObstacle = {
			1,1,1,0,1,1,1,
			1,0,1,0,1,0,1,
			0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,
			0,0,0,0,0,0,0			
		};

//		lab.setObstacles(new int[]{3, 15, 19});
//		int tt = lab.solve();
//		System.out.println(tt);
//		if(true) return;

		int best = 10;
		
		for (int p1 = 0; p1 < lab.size - 4; p1++) {
			if(noObstacle[p1] == 1) continue;
			for (int p2 = p1 + 1; p2 < lab.size - 3; p2++) {
				if(noObstacle[p2] == 1) continue;
				for (int p3 = p2 + 1; p3 < lab.size - 2; p3++) {
					if(noObstacle[p3] == 1) continue;
					for (int p4 = p3 + 1; p4 < lab.size - 1; p4++) {
						if(noObstacle[p4] == 1) continue;
						for (int p5 = p4 + 1; p5 < lab.size; p5++) {
							if(noObstacle[p5] == 1) continue;
							int[] obstacles = new int[]{p1,p2,p3,p4,p5};
							lab.setObstacles(obstacles);
							int t = lab.solve();
							if(t < 0 || t < best) continue;
							best = t;
							System.out.print(best + ":" + lab.obstaclesToString());
							System.out.println("");
							System.out.println(lab.buildSolution());
						}						
					}
				}
			}
		}
	}

}

//58:a4,a5,b6,d1,g6
//1210321012301230012103221032300123012303210321030123012323