package graph;

import java.util.Random;

public class TravelSalesmanSimpleSolver {
	double[][] points; //[n][2]
	double[][] distances;
	

	double foundDistance;
	int[] solution;
	int treeCount;
	
	public TravelSalesmanSimpleSolver(double[][] points) {
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

	void runRundom() {
		for (int k = 0; k < 20; k++) {
			startFromRandomPathAndReduce();
			double best = getDistance();
			int attempt = 0;
			for (int i = 0; i < 5000; i++) {
				startFromRandomPathAndReduce();
				double d = getDistance();
				if(d < best) {
					best = d;
					attempt = i;
				}
			}
			System.out.println(best + "(" + attempt + ")");
		}
	}

	public void startFromRandomPathAndReduce() {
		solution = new int[points.length];
		for (int i = 0; i < solution.length; i++) solution[i] = i;
		Random seed = new Random();
		for (int i = 0; i < solution.length; i++) {
			int j = i + seed.nextInt(solution.length - i);
			int q = solution[i];
			solution[i] = solution[j];
			solution[j] = q;
		}
		reducePath();
		
	}

	public void reducePath() {
		while(reducePathOnce());
	}

	boolean reducePathOnce() {
		//rotate
		int s = solution[0];
		for (int i = 0; i < solution.length - 1; i++) {
			solution[i] = solution[i + 1];
		}
		solution[solution.length - 1] = s;

		for (int i = 0; i < points.length; i++) {
			for (int j = i + 2; j < points.length - 1; j++) {
				double d1 = distances[solution[i]][solution[i + 1]] + distances[solution[j]][solution[j + 1]];
				double d2 = distances[solution[i]][solution[j]] + distances[solution[j + 1]][solution[i + 1]];
				if(d2 < d1) {
					flip(i + 1, j);
					return true;
				}
			}
		}
		return false;
	}

	void flip(int from, int to) {
		for (int k = from; k < to; k++) {
			int q = from + to - k;
			if(q <= k) return;
			int s = solution[q];
			solution[q] = solution[k];
			solution[k] = s;
		}
	}

	double getDistance() {
		double d = 0;
		for (int t = 1; t < points.length; t++) {
			d += distances[solution[t - 1]][solution[t]];
		}
		d += distances[solution[0]][solution[points.length - 1]];
		return d;
	}

}
