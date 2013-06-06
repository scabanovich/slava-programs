package graph;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;

public class TravelSalesman {
	double[][] points; //[n][2]
	double[][] distances;
	
	int[] visited;
	double distanceLimit;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] path;
	double[] currentLength;

	double foundDistance;
	int[] solution;
	int treeCount;
	
	public TravelSalesman(double[][] points) {
		this.points = points;
		distances = new double[points.length][points.length];
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				if(i == j) {
					distances[i][j] = 0;
				} else {
					double dx = points[i][0] - points[j][0];
					double dy = points[i][1] - points[j][1];
					distances[i][j] = Math.sqrt(dx * dx + dy * dy);
				}
			}
		}
	}

	public void solve(double distanceLimit) {
		this.distanceLimit = distanceLimit;
		init();
		anal();
	}

	void init() {
		visited = new int[points.length];
		wayCount = new int[points.length + 1];
		way = new int[points.length + 1];
		ways = new int[points.length + 1][points.length];
		path = new int[points.length + 1];
		currentLength = new double[points.length + 1];
		t = 0;
		solution = null;
		treeCount = 0;
		foundDistance = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t == points.length) {
			return;
		}
		if(t == 0) {
			wayCount[t] = 1;
			ways[t][0] = 0;
		} else {
			for (int i = 0; i < points.length; i++) {
				if(visited[i] == 0) {
					double estimate = currentLength[t - 1] + distances[path[0]][i] + distances[path[t - 1]][i];
					if(estimate > distanceLimit) return;
					for (int j = i + 1; j < points.length; j++) {
						if(visited[j] == 0) {
							double estimate1 = currentLength[t - 1] + distances[path[0]][i] + distances[i][j] + distances[path[t - 1]][j]; 
							double estimate2 = currentLength[t - 1] + distances[path[0]][j] + distances[i][j] + distances[path[t - 1]][i];
							if(estimate1 > distanceLimit && estimate2 > distanceLimit) {
								return;
							}
						}
					}
				}
			}
			for (int i = 0; i < points.length; i++) {
				if(visited[i] == 0 && !isCrossing(i)) {
//					double estimate = currentLength[t - 1] + distances[path[0]][i] + distances[path[t - 1]][i];
//					if(estimate > distanceLimit) continue;
					ways[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			}
		}
	}

	boolean isCrossing(int i) {
		if(t < 3) return false;
		for (int t0 = 0; t0 < t - 2; t0++) {
			double d1 = distances[path[t0]][path[t0 + 1]] + distances[path[t - 1]][i];
			double d2 = distances[path[t0]][path[t - 1]] + distances[i][path[t0 + 1]];
			if(d2 < d1) return true;			
		}
		return false;
	}

	void move() {
		int i = ways[t][way[t]];
		path[t] = i;
		currentLength[t] = (t == 0) ? 0 : currentLength[t - 1] + distances[path[t - 1]][i];
		visited[i] = 1;
		t++;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int i = ways[t][way[t]];
		visited[i] = 0;
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
				System.out.println(t);
			}
			if(t == points.length) {
				double length = currentLength[t - 1] + distances[path[t - 1]][path[0]];
				if(length <= distanceLimit) {
					foundDistance = length;
					solution = new int[path.length];
					System.arraycopy(path, 0, solution, 0, points.length);
					System.out.println("x <= " + foundDistance);
					distanceLimit = foundDistance;
//					return;
				}
			}
		}
	}

	public void solve() {
		double max = 2851;
		while(true) {
			System.out.println("max=" + max);
			solve(max);
			if(solution != null) {
				break;
			}
			max = max * 1.1;
		}
	}
	
	static double[][] getPoints() {
		return new double[][]{
				{462, 226}, {81, 398}, {85, 437}, {134, 265}, {16, 411}, 
				{187, 357}, {330, 211}, {33, 278}, {307, 243}, {130, 294}, 
				{263, 163}, {407, 301}, {27, 550}, {481, 216}, {156, 6}, 
				{69, 186}, {542, 14}, {95, 210}, {591, 393}, {534, 8}, 
				{272, 412}, {423, 420}, {71, 454}, {411, 488}, {147, 412}, 
				{260, 50}, {560, 313}, {549, 469}, {136, 511}, {523, 323}
		};
	}

	static double[][] getPoints(int n) {
		Random seed = new Random();
		double[][] result = new double[n][2];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < 2; k++) {
				result[i][k] = seed.nextInt(100);
			}
		}
		return result;
	}

	static double[][] readPoints(String uri) {
		StringTokenizer st = Util.read(uri);
		int n = 0;
		String s = st.nextToken();
		if(s.indexOf(':') >= 0) {
			while(s.indexOf(':') >= 0) {
				if(s.startsWith("DIM")) {
					s = s.substring(s.indexOf(':') + 1).trim();
					n = Integer.parseInt(s);
				}
				s = st.nextToken();
				if(s.trim().equals("NODE_COORD_SECTION")) {
					break;
				}
			}
		} else {
			int[] firstLine = Util.parseInt(s);
			n = firstLine[0];
			System.out.println("Size = " + n);
		}
		double[][] result = new double[n][2];
		int c = 0;
		while(st.hasMoreTokens()) {
			String s1 = st.nextToken();
			if(s1.equals("EOF")) break;
			double[] line = parseDouble(s1);
			result[c][0] = line[line.length - 2];
			result[c][1] = line[line.length - 1];
			System.out.println(result[c][0] + " " + result[c][1]);
			c++;
		}
		return result;
	}

	static double[] parseDouble(String s) {
		StringTokenizer st = new StringTokenizer(s, " \t");
		double[] result = new double[st.countTokens()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Double.parseDouble(st.nextToken());
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double[][] points = //readPoints("http://spark-public.s3.amazonaws.com/algo2/datasets/tsp.txt");
			readPoints("http://www.tsp.gatech.edu/world/dj38.tsp");
//			getPoints();
//			getPoints(60);
		TravelSalesmanSimpleSolver p1 = new TravelSalesmanSimpleSolver(points);
		p1.runRundom();
//		p1.solve();
//		p1.reducePath();
//		System.out.println(p1.foundDistance);
//		TravelSalesman p = new TravelSalesman(points);
//		p.solve();
	}

	

}
