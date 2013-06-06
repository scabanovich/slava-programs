package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoSatProblem2 {
	int[][] conditions;
	int size;

	int[] value; // 0 - unknown; -1 - false; +1 - true;
	int[] valueIsSetAt;
	int[] conditionIsSatisfiedAt;	

	int time;

	int[] node;
	int[] way;
	int[] wayCount;
	int[][] ways;

	boolean result = false;
	int treeCount;

	public TwoSatProblem2(int[][] conditions) {
		this.conditions = conditions;
		size = TwoSatProblem.getMax(conditions) + 1;
		value = new int[size];
		valueIsSetAt = new int[size];
		for (int i = 0; i < size; i++) {
			valueIsSetAt[i] = -1;
		}
		conditionIsSatisfiedAt = new int[conditions.length];
		for (int i = 0; i < conditions.length; i++) {
			conditionIsSatisfiedAt[i] = -1;
		}
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		node = new int[1000];
		way = new int[1000];
		wayCount = new int[1000];
		ways = new int[1000][2];
		time = 0;
	}

	void srch() {
		wayCount[time] = 0;
		int q = reduce();
		System.out.println("time=" + time + " unsatisfied=" + getUnsatisfied() + " " + getContradictions());
//		if(q >= 0) return;
		node[time] = getBestNode();
		if(node[time] <= 0) return;
		wayCount[time] = 2;
		ways[time][0] = -1;
		ways[time][1] = 1;
	}

	int getBestNode() {
		int[][] st = getStatistics();
		int max = 0;
		int bestNode = 0;
		for (int i = 1; i < size; i++) {
			int s = st[i][0] + st[i][1];
			if(s > max) {
				max = s;
				bestNode = i;
			}
		}
		return bestNode;
	}

	void move() {
		int n = node[time];
		int w = ways[time][way[time]];
		value[n] = w;
		valueIsSetAt[n] = time + 1;
		++time;
		srch();
		way[time] = -1;
	}

	void back() {
		for (int i = 0; i < size; i++) {
			if(valueIsSetAt[i] == time) {
				value[i] = 0;
				valueIsSetAt[i] = -1;
			}
		}
		for (int i = 0; i < conditions.length; i++) {
			if(conditionIsSatisfiedAt[i] == time) {
				conditionIsSatisfiedAt[i] = -1;
			}
		}
		--time;
	}

	void anal() {
		srch();
		way[time] = -1;
		if(isSatisfied()) {
			result = true;
			return;
		}
		while(true) {
			while(way[time] == wayCount[time] - 1) {
				if(time == 0) return;
				back();
			}
			way[time]++;
			move();
			if(isSatisfied()) {
				result = true;
				return;
			}
		}
	}

	int getUnsatisfied() {
		int n = 0;
		for (int i = 0; i < conditions.length; i++) {
			if(conditionIsSatisfiedAt[i] < 0) n++;
		}
		return n;
	}

	int getContradictions() {
		int c = 0;
		for (int i = 0; i < conditions.length; i++) {
			if(conditionIsSatisfiedAt[i] < 0) {
				int k1 = conditions[i][0];
				int k2 = conditions[i][1];
				int a1 = Math.abs(k1);
				int a2 = Math.abs(k2);
				if(valueIsSetAt[a1] >= 0 && valueIsSetAt[a2] >= 0) {
					if(k1 * value[a1] < 0 && k2 * value[a2] < 0) c++;
				}
			}
		}
		return c;
	}

	boolean isSatisfied() {
		return getUnsatisfied() == 0;
	}

	/**
	 * Returns unsatisfiable condition index, or -1 if everithing is ok
	 * 
	 * @return
	 */
	int reduce() {
		int contradictory = -1;
		while(true) {
			System.out.print("x");
			int changes = 0;
			for (int i = 0; i < conditions.length; i++) {
				if(conditionIsSatisfiedAt[i] < 0) {
					int k1 = conditions[i][0];
					int k2 = conditions[i][1];
					int a1 = Math.abs(k1);
					int a2 = Math.abs(k2);
					if(a1 == a2) {
						if(k1 != k2) {
							conditionIsSatisfiedAt[i] = time;
						} else if(valueIsSetAt[a1] >= 0) {
							if(k1 * value[a1] > 0) {
								conditionIsSatisfiedAt[i] = time;
							} else {
								contradictory = i;
//								return i;
							}
						} else {
							value[a1] = (k1 > 0) ? 1 : -1;
							valueIsSetAt[a1] = time;
							conditionIsSatisfiedAt[i] = time;
						}
					} else if(valueIsSetAt[a1] >= 0 && valueIsSetAt[a2] >= 0) {
						if(k1 * value[a1] > 0 || k2 * value[a2] > 0) {
							conditionIsSatisfiedAt[i] = time;
						} else {
							contradictory = i;
//							return i;
						}
					} else if(valueIsSetAt[a1] >= 0) {
						if(k1 * value[a1] > 0) {
							conditionIsSatisfiedAt[i] = time;
						} else {
							value[a2] = (k2 > 0) ? 1 : -1;
							valueIsSetAt[a2] = time;
							conditionIsSatisfiedAt[i] = time;
							
						}
					} else if(valueIsSetAt[a2] >= 0) {
						if(k2 * value[a2] > 0) {
							conditionIsSatisfiedAt[i] = time;
						} else {
							value[a1] = (k1 > 0) ? 1 : -1;
							valueIsSetAt[a1] = time;
							conditionIsSatisfiedAt[i] = time;
							
						}
					}
					if(conditionIsSatisfiedAt[i] >= 0) {
						changes++;
					}
				}
			}
			int[][] st = getStatistics();
			for (int i = 1; i < st.length; i++) {
				if(st[i][0] > 0 && st[i][1] == 0) {
					if(valueIsSetAt[i] < 0) {
						value[i] = 1;
						valueIsSetAt[i] = time;
						changes++;
					}
				} else if(st[i][1] > 0 && st[i][0] == 0) {
					if(valueIsSetAt[i] < 0) {
						value[i] = -1;
						valueIsSetAt[i] = time;
						changes++;
					}
				}
			}
			if(changes == 0) {
				return contradictory;// -1;
			}
		}
	}

	int[][] getStatistics() {
		int[][] result = new int[size][2];
		for (int i = 0; i < conditions.length; i++) {
			if(conditionIsSatisfiedAt[i] < 0) {
				int k1 = conditions[i][0];
				int k2 = conditions[i][1];
				int a1 = Math.abs(k1);
				int a2 = Math.abs(k2);
				if(valueIsSetAt[a1] < 0) {
					if(k1 < 0) result[a1][1]++; else result[a1][0]++;
				}
				if(valueIsSetAt[a2] < 0) {
					if(k2 < 0) result[a2][1]++; else result[a2][0]++;
				}
			}
		}
		return result;
	}

}
