package champukr;

public class RandomPathBuilder {
	int size;
	Point[] points;
	int linePointCount = 2;
	
	int[] usage;
	int[] order;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int bestResult;
	int[] bestOrder;
	
	static void run(int size, int linePointCount, int minPoints) {
		RandomPathBuilder p = new RandomPathBuilder();
		while(true) {
			p.generatePoints(size);
			p.setLinePointCount(linePointCount);
			p.solve();
			if(p.bestResult >= minPoints) break;
		}
		Point[] ps = p.getOrderedPoints();
		PathComponent.show(ps);
	}

	public RandomPathBuilder() {}

	public void generatePoints(int size) {
		this.size = size;
		points = new Point[size];
		for (int i = 0; i < size; i++) {
			points[i] = new Point(Math.random(), Math.random());
		}
	}
	
	public void setLinePointCount(int c) {
		linePointCount = c;
	}
	
	public Point[] getOrderedPoints() {
		Point[] ps = new Point[size];
		for (int i = 0; i < size; i++) {
			ps[i] = points[bestOrder[i]];
		}
		return ps;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		order = new int[size];
		usage = new int[size];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][size];
		t = 0;
///		bestResult = 0;
		bestOrder = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		if(t == 0) {
			wayCount[t] = 1;
			ways[0][0] = 0;
			return;
		}
		for (int i = 0; i < size; i++) {
			if(usage[i] > 0) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	void move() {
		int w = ways[t][way[t]];
		usage[w] = 1;
		order[t] = w;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int w = ways[t][way[t]];
		usage[w] = 0;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == size) {
				int r = getResult();
				if(r > bestResult) {
					bestResult = r;
					bestOrder = (int[])order.clone();
					printSolution();
				}
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Result=" + bestResult);
		for (int i = 0; i < size; i++) {
			Point p = points[order[i]];
			System.out.println(i + " x=" + p.x + " y=" + p.y);
		}
	}
	
	int getResult() {
		int c = 0;
		for (int i = 0; i < size; i++) {
			int cross = 0;
			for (int j = 0; j < size; j++) {
				if(j == i || next(i) == j || next(j) == i) continue;
				if(cross(order[i], order[next(i)], order[j], order[next(j)])) cross++;
			}
			if(cross == linePointCount) c++;
		}
		return c;
	}
	
	int next(int i) {
		i = i + 1;
		if(i >= size) i -= size;
		return i;
	}

	boolean cross(int b1, int e1, int b2, int e2) {
		if(b1 == b2 || b1 == e2 || e1 == b2 || e1 == e2) return false;
		return cross(points[b1], points[e1], points[b2], points[e2]);
	}
	
	boolean cross(Point b1, Point e1, Point b2, Point e2) {
		double x = (getAlpha(b1, e1) - getAlpha(b2, e2)) / (getBeta(b2, e2) - getBeta(b1, e1));
		return isXInside(b1, e1, x) && isXInside(b2, e2, x);
	}
	
	double getAlpha(Point b, Point e) {
		return (e.x * b.y - e.y * b.x) / (e.x - b.x);
	}
	
	double getBeta(Point b, Point e) {
		return (e.y - b.y) / (e.x - b.x);
	}
	
	boolean isXInside(Point b, Point e, double x) {
		return b.x < e.x ? (x > b.x && x < e.x) : (x < b.x && x > e.x);
	}	

}

class Point {
	double x;
	double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
/*
Result=6
0 x=0.9832570151816896 y=0.2095516047634568
1 x=0.17549388957347933 y=0.6646815000313635
2 x=0.7484746539596019 y=0.4090565347110099
3 x=0.47472100480207413 y=0.9919147887113088
4 x=0.9004977128720112 y=0.07929002223320736
5 x=0.5052256752625248 y=0.8471143003476195
6 x=0.7879815867384178 y=0.24558902754804313
7 x=0.2852959781534036 y=0.4243907234880946
*/