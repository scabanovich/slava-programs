package olia.triangles;

import java.util.Random;

import com.slava.common.RectangularField;

public class PackedTrianglesSolver {
	RectangularField field;
	PackedTriangleFactory triangles;
	
	int solutionLimit;
	int treeSizeLimit = 0;
	boolean randomize = false;
	
	int totalTrianglesCount;
	int[][] hData;
	int[][] vData;
	
	int[] state;
	int[] usage;
	int used;
	int[][] hUsed;
	int[][] vUsed;
	int trianglesUsed;
	
	int t = 0;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[] solution;
	int[][] hDataCreated;
	int[][] vDataCreated;
	int solutionCount;
	int treeSize;
	
	public PackedTrianglesSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
		triangles = new PackedTriangleFactory();
		triangles.create(field);
	}
	
	public void setData(int total, int[][] hData, int[][] vData) {
		this.totalTrianglesCount = total;
		if(hData != null && vData != null) {
			this.hData = hData;
			this.vData = vData;
			int c1 = 0, c2 = 0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < hData[i].length; j++) {
					c1 += hData[i][j];
				}
				for (int j = 0; j < vData[i].length; j++) {
					c2 += vData[i][j];
				}
			}
			if(c1 != c2) throw new IllegalArgumentException("hData does not match vData: " + c1 + " triangles and " + c2 + " triangles.");
			totalTrianglesCount = c1;
		}
	}
	
	public void setRandomized(boolean r) {
		randomize = r;
	}
	
	public void setSolutionLimit(int solutionLimit) {
		this.solutionLimit = solutionLimit;
	}
	
	public void setTreeSizeLimit(int treeSizeLimit) {
		this.treeSizeLimit = treeSizeLimit;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		usage = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			state[i] = -1;
		}
		used = 0;
		hUsed = new int[2][field.getHeight() - 1];
		vUsed = new int[2][field.getWidth() - 1];
		trianglesUsed = 0;
		
		wayCount = new int[field.getSize()];
		place = new int[field.getSize()];
		way = new int[field.getSize()];
		ways = new int[field.getSize()][20];
		
		t = 0;
		solution = null;
		solutionCount = 0;
		treeSize = 0;
	}
	
	boolean check = false;
	
	void srch() {
		wayCount[t] = 0;
		if(trianglesUsed == totalTrianglesCount) return;
		if(state.length - used < 3 * (totalTrianglesCount - trianglesUsed)) {
			return;
		}
		if(check && !checkRows()) {
			return;
		}
		int p = getPlace();
		place[t] = p;
		for (int i = 0; i < triangles.byPoint[p].length; i++) {
			if(canPlace(triangles.byPoint[p][i])) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
		if(state.length - used > 3 * (totalTrianglesCount - trianglesUsed)) {
			ways[t][wayCount[t]] = -1;
			wayCount[t]++;
		}
		if(randomize && wayCount[t] > 1) {
			randomize();
		}
	}
	
	Random seed = new Random();

	void randomize() {
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + seed.nextInt(wayCount[t] - i);
			if(j == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}
	
	int getPlace() {
		if(randomize && t < 5) {
			int p = seed.nextInt(state.length);
			while(usage[p] > 0) {
				p = seed.nextInt(state.length);
			}
			return p;
		}
		for (int p = 0; p < state.length; p++) {
			if(usage[p] == 0) return p;
		}
		return -1;
	}
	
	boolean canPlace(PackedTriangle triangle) {
		for (int i = 0; i < triangle.points.length; i++) {
			int p = triangle.points[i];
			if(usage[p] > 0) return false;
		}
		if(hData != null && vData != null) {
			if(hData[triangle.diagonal][triangle.y] <= hUsed[triangle.diagonal][triangle.y]) {
				return false;
			}
			if(vData[triangle.diagonal][triangle.x] <= vUsed[triangle.diagonal][triangle.x]) {
				return false;
			}
		}
		return true;
	}
	
	boolean checkRows() {
		if(hData == null) return true;
		for (int y = 0; y < hData[0].length; y++) {
			if(isHRowFilled(y)) {
				if(hData[0][y] != hUsed[0][y] || hData[1][y] != hUsed[1][y]) return false;
			}
		}
		for (int x = 0; x < vData[0].length; x++) {
			if(isVRowFilled(x)) {
				if(vData[0][x] != vUsed[0][x] || vData[1][x] != vUsed[1][x]) return false;
			}
		}
		
		
		return true;
	}
	
	boolean isHRowFilled(int y) {
		for (int x = 0; x < field.getWidth() - 1; x++) {
			int p = field.getIndex(x, y);
			int px = field.jump(p, 0);
			int py = field.jump(p, 1);
			int pxy = field.jump(px, 1);
			if(usage[p] + usage[px] + usage[py] + usage[pxy] < 2) return false;
		}
		return true;
	}
	boolean isVRowFilled(int x) {
		for (int y = 0; y < field.getHeight() - 1; y++) {
			int p = field.getIndex(x, y);
			int px = field.jump(p, 0);
			int py = field.jump(p, 1);
			int pxy = field.jump(px, 1);
			if(usage[p] + usage[px] + usage[py] + usage[pxy] < 2) return false;
		}
		return true;
	}

	void move() {
		int w = ways[t][way[t]];
		int p = place[t];
		if(w < 0) {
			used++;
			usage[p] = 1;
		} else {
			trianglesUsed++;
			PackedTriangle triangle = triangles.byPoint[p][w];
			hUsed[triangle.diagonal][triangle.y]++;
			vUsed[triangle.diagonal][triangle.x]++;
			for (int i = 0; i < triangle.points.length; i++) {
				int q = triangle.points[i];
				usage[q] = 1;
				state[q] = t;
				used++;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int w = ways[t][way[t]];
		int p = place[t];
		if(w < 0) {
			used--;
			usage[p] = 0;
		} else {
			trianglesUsed--;
			PackedTriangle triangle = triangles.byPoint[p][w];
			hUsed[triangle.diagonal][triangle.y]--;
			vUsed[triangle.diagonal][triangle.x]--;
			for (int i = 0; i < triangle.points.length; i++) {
				int q = triangle.points[i];
				usage[q] = 0;
				state[q] = -1;
				used--;
			}
		}
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
			if(wayCount[t] == 0) {
				treeSize++;
				if(treeSizeLimit > 0 && treeSize >= treeSizeLimit) {
					solutionCount = 0;
					return;
				}
			}
			if(isSolution()) {
				solutionCount++;
				if(solutionCount == 1) {
					storeSolution();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) {
					return;
				}
				
			}
		}
	}
	
	boolean isSolution() {
		if(trianglesUsed == totalTrianglesCount) {
			if(hData == null || vData == null) {
				return true;
			}
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < hData.length; j++) {
					if(hData[i][j] != hUsed[i][j]) return false;
				}
				for (int j = 0; j < vData.length; j++) {
					if(vData[i][j] != vUsed[i][j]) return false;
				}
			}
			return true;
		}
		return false;
	}
	
	void storeSolution() {
		solution = (int[])state.clone();
		if(hData == null) {
			hDataCreated = new int[2][hUsed[0].length];
			vDataCreated = new int[2][vUsed[0].length];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < hUsed[i].length; j++) {
					hDataCreated[i][j] = hUsed[i][j];
				}
				for (int j = 0; j < vUsed[i].length; j++) {
					vDataCreated[i][j] = vUsed[i][j];
				}
			}
			
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int[][] getCreatedHData() {
		return hDataCreated;
	}
	
	public int[][] getCreatedVData() {
		return vDataCreated;
	}
	
	public int getTreeSize() {
		return treeSize;
	}


}
