package graph;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;

public class GraphDistances {
	int[][] graph;
	
	public GraphDistances(int[][] graph) {
		this.graph = graph;
	}

	class Path {
		int distance;
		int[] points;

		public Path(int s, int e, int d) {
			distance = d;
			points = new int[]{s, e};
		}
		
		public Path(Path s, Path e) {
			distance = s.distance + e.distance;
			points = new int[s.points.length + e.points.length - 1];
			System.arraycopy(s.points, 0, points, 0, s.points.length);
			System.arraycopy(e.points, 1, points, s.points.length, e.points.length - 1);
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Length=" + distance + " Path=");
			for (int i = 0; i < points.length; i++) {
				sb.append(points[i] + ", ");
			}
			return sb.toString();
		}
	}

	public boolean run() {
		int n = graph.length;
		Path[][] distances = new Path[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				distances[i][j] = new Path(i, j, graph[i][j]);
			}
		}
		int changes = n;
		while(changes > 0) {
			System.out.println(changes);
			changes = 0;
//			int[][] distances2 = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					boolean c = false;
					int min = distances[i][j].distance;
					int km = -1;
					for (int k = 0; k < n; k++) {
						int d = distances[i][k].distance + distances[k][j].distance;
						if(d < min) {
							min = d;
							km = k;
							c = true;
						}
					}
//					distances2[i][j] = min;
					if(c) {
						changes++;
						distances[i][j] = new Path(distances[i][km], distances[km][j]);
						if(i == j && distances[i][i].distance < 0) {
							System.out.println("negative at " + distances[i][i]);
							return false;
						}
					}
				}
			}
//			distances = distances2;
			for (int i = 0; i < n; i++) {
				if(distances[i][i].distance < 0) {
					System.out.println("negative at " + distances[i][i]);
					return false;
				}
			}
		}
		Path min = distances[0][0];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(distances[i][j].distance < min.distance) {
					min = distances[i][j];
				}
			}
		}
		System.out.println("Min=" + min);
		
		return true;
	}

	static int[][] randomGraph(int n) {
		Random seed = new Random();
		int[][] graph = new int[n][n];
		int neg = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(i == j) {
					graph[i][j] = 0;
				} else {
					graph[i][j] = seed.nextInt(12500) > 1 ? seed.nextInt(15) + 4 : (- seed.nextInt(2) - 1);
					if(graph[i][j] < 0) neg++;
				}
			}
		}
		System.out.println("Negative=" + neg);
		return graph;
	}

	static int[][] readGraph(String uri) {
		StringTokenizer st = Util.read(uri);
		String s = st.nextToken();
		int[] firstLine = Util.parseInt(s);
		int n = firstLine[0];
		System.out.println("Size = " + n);
		int[][] graph = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(i == j) {
					graph[i][j] = 0;
				} else {
					graph[i][j] = 1000000;
				}
			}
		}
		int edgeCount = 0;
		while(st.hasMoreTokens()) {
			int[] edge = Util.parseInt(st.nextToken());
			System.out.println(edge[0] + " " + edge[1] + " " + edge[2]);
			graph[edge[0] - 1][edge[1] - 1] = edge[2];
			edgeCount++;
		}
		System.out.println("Edges = " + edgeCount);
		return graph;
	}

	static String GRAPH_1 = "http://spark-public.s3.amazonaws.com/algo2/datasets/g1.txt";
	static String GRAPH_2 = "http://spark-public.s3.amazonaws.com/algo2/datasets/g2.txt";
	static String GRAPH_3 = "http://spark-public.s3.amazonaws.com/algo2/datasets/g3.txt";
	
	public static void main(String[] args) {
		GraphDistances g = new GraphDistances(readGraph(GRAPH_1));
		boolean result = g.run();
		System.out.println(result);
	}
}
