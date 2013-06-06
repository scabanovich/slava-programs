package slava.puzzles.flow.solve;

import java.util.Random;

public class GraphUtil {

	public static int getNumberOfEdges(int[][] matrix) {
		int c = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				if(matrix[i][j] != 0) c++;
			}
		}
		return c;
	}

	public static boolean isDoubleConnectedOrientedGraph(int[][] matrix) {
		return isConnectedOrientedGraph(matrix, -1) && isConnectedOrientedGraph(matrix, 1);
	}

	static boolean isConnectedOrientedGraph(int[][] matrix, int value) {
		if(matrix.length == 0) {
			return false;
		}
		int[] nodes = new int[matrix.length];
		int[] visited = new int[matrix.length];
		int nodeCount = 1;
		nodes[0] = 0;
		visited[0] = 1;
		int c = 0;
		while(c < nodeCount) {
			int n = nodes[c];
			for (int i = 0; i < matrix[n].length; i++) {
				if(visited[i] == 0 && matrix[n][i] == value) {
					visited[i] = 1;
					nodes[nodeCount] = i;
					nodeCount++;
				}
			}
			c++;
		}
		return nodeCount == matrix.length;
	}

	public static int findMaximumContributingEdges(int[][] matrix, boolean byIncoming) {
		int v = byIncoming ? 1 : -1;
		int[] weights = new int[matrix.length];
		for (int i = 0; i < weights.length; i++) weights[i] = 1;
		boolean changed = true;
		int max = 0;
		int nm = -1;
		while(changed && max < matrix.length * matrix.length) {
			changed = false;
			for (int n = 0; n < matrix.length; n++) {
				if(isExactlyOneOfValue(matrix, n, -v)) {
					int w = 0;
					for (int i = 0; i < matrix.length; i++) {
						if(matrix[n][i] == v) {
							w += weights[i];
						}
					}
					if(w > weights[n]) {
						weights[n] = w;
						changed = true;
						if(w > max) {
							max = weights[n];
							nm = n;
						}
					}
				}
			}
		}
		return max;
	}

	static boolean isExactlyOneOfValue(int[][] matrix, int n, int v) {
		int k = 0;
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[n][i] == v) k++;
		}
		return k == 1;
	}

	/**
	 * Returns size of maximal full subgraph.
	 * 
	 * @param matrix
	 * @return
	 */
	public static int getMaximumFullGraph(int[][] matrix) {
		int[] filter = new int[matrix.length];
		for (int i = 0; i < filter.length; i++) filter[i] = 1;
		return getMaximumFullGraph(matrix, filter, 1);
	}

	private static int getMaximumFullGraph(int[][] matrix, int[] filter, int minimum) {
		if(minimum < 1) minimum = 1;
		int result = 0;
		for (int i = 0; i < matrix.length; i++) {
			if(filter[i] == 0) continue;
			if(getNeighbourCount(matrix, filter, i) + 1 >= minimum) {
				int[] fns = getNeighbourFilter(matrix, filter, i);
				int s = getMaximumFullGraph(matrix, fns, minimum - 1) + 1;
				if(s > result) {
					result = s;
				}
				if(s >= minimum) {
					minimum = s + 1;
				}
			}
			filter[i] = 0;
		}
		return result;
	}

	public static int getNeighbourCount(int[][] matrix, int[] filter, int n) {
		int k = 0;
		for (int i = 0; i < matrix.length; i++) {
			if(filter[i] == 1 && matrix[n][i] != 0) k++;
		}
		return k;
	}

	public static int[] getNeighbourFilter(int[][] matrix, int[] filter, int n) {
		int[] f = new int[filter.length];
		for (int i = 0; i < matrix.length; i++) {
			if(filter[i] == 1 && matrix[n][i] != 0) f[i] = 1;
		}
		return f;
	}

	static Random seed = new Random();

	public static int[][] createRandomGraph(int nodes, int edges) {
		int[][] result = new int[nodes][nodes];
		int max = (nodes - 1) * nodes / 2;
		if(edges > max) edges = max;
		while(edges > 0) {
			int n1 = seed.nextInt(nodes);
			int n2 = seed.nextInt(nodes);
			if(n1 != n2 && result[n1][n2] == 0) {
				result[n1][n2] = 1;
				result[n2][n1] = 1;
				edges--;
			}
		}
		return result;
	}

}
