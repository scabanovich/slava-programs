package match2005;

public class SudokuSolver {
	SudokuField field;

	int[] initialValues;

	int[][] used; // [p][v]
	int[] freeCount; // [p]

	int[][] usedRegionColors;
	int[] freeRegionColorCount;

	int[] values;
	int vacancies;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysC;
	
	int[][] statistics;

	boolean randomizing;
	int solutionCount;
	int solutionLimit;
	int treeCount;
	int treeCountLimit = -1;

	int[] solution;

	public SudokuSolver() {}

	public void setField(SudokuField field) {
	  this.field = field;
	  init0();
	}

	public void setInitialValues(int[] vs) {
	  initialValues = vs;
	}

	public void setSolutionLimit(int i) {
	  solutionLimit = i;
	}

	/**
	  * If treeCountLimit option is set to value > 0,
	  * then too "complex" problem will be considered as
	  * having too many solutions (even if in reality it
	  * has a unique solution)
	  * @param i
	  */
	public void setTreeCountLimit(int i) {
	  treeCountLimit = i;
	}

	public void setRandomizing(boolean b) {
	  randomizing = b;
	}

	public void solve() {
	  init();
	  anal();
	}

	void init0() {
	  vacancies = field.size;
	  values = new int[vacancies];
	  used = new int[field.size][field.width];
	  freeCount = new int[vacancies];
	  usedRegionColors = new int[field.regions.length][field.width];
	  freeRegionColorCount = new int[field.regions.length];
	  wayCount = new int[field.size + 1];
	  way = new int[field.size + 1];
	  waysP = new int[field.size + 1][field.width];
	  waysC = new int[field.size + 1][field.width];
	  
	  statistics = new int[field.size][9];
	}

	void init() {
	  vacancies = field.size;
	  for (int i = 0; i < values.length; i++) values[i] = -1;

	  for (int i = 0; i < field.size; i++) for (int j = 0; j < field.width; j++)
	used[i][j] = 0;
	  for (int i = 0; i < freeCount.length; i++) freeCount[i] = field.width;

	  for (int i = 0; i < field.regions.length; i++) for (int j = 0; j <
	field.width; j++) usedRegionColors[i][j] = 0;
	  for (int i = 0; i < freeRegionColorCount.length; i++)
	freeRegionColorCount[i] = field.width;

	  if(initialValues != null) for (int i = 0; i < initialValues.length; i++) {
	   if(initialValues[i] >= 0) add(i, initialValues[i]);
	  }
	  
	  for (int i = 0; i < field.size; i++) for (int c = 0; c < 9; c++) statistics[i][c] = 0;

	  t = 0;
	  solutionCount = 0;
	  treeCount = 0;
	  solution = null;
	}

	void srch() {
	  wayCount[t] = 0;
	  if(isFinished()) return;
	  doSrch();
	  if(randomizing) {
	   randomize();
	  }
	}

	protected void doSrch() {
	  int pb = -1;
	  int w = field.width + 1;
	  for (int p = 0; p < field.size; p++) {
	   if(values[p] >= 0) continue;
	   if(freeCount[p] < w) {
		w = freeCount[p];
		pb = p;
		if(w == 0) break;
	   }
	  }
	  if(pb < 0 || w == 0) return;
	  for (int v = 0; v < field.width; v++) {
	   if(used[pb][v] > 0) continue;
	   waysP[t][wayCount[t]] = pb;
	   waysC[t][wayCount[t]] = v;
	   wayCount[t]++;
	  }
	  if(w == 1) return;
	  for (int r = 0; r < freeRegionColorCount.length; r++) {
	   if(freeRegionColorCount[r] == 0) continue;
	   for (int v = 0; v < field.width; v++) {
		if(usedRegionColors[r][v] == 1) continue;
		int wr = 0;
		for (int i = 0; i < field.regions[r].length; i++) {
		 int p = field.regions[r][i];
		 if(values[p] >= 0 || used[p][v] > 0) continue;
		 ++wr;
		}
		if(wr < wayCount[t]) {
		 wayCount[t] = 0;
		 for (int i = 0; i < field.regions[r].length; i++) {
		  int p = field.regions[r][i];
		  if(values[p] >= 0 || used[p][v] > 0) continue;
		  waysP[t][wayCount[t]] = p;
		  waysC[t][wayCount[t]] = v;
		  wayCount[t]++;
		 }
		 if(wr == 0) return;
		}
	   }
	  }
	}

	void randomize() {
	  if(wayCount[t] < 2) return;
	  for (int i = wayCount[t] - 1; i >= 1; i--) {
	   int k = (int)((i + 1) * Math.random());
	   if(k == i) continue;
	   int c = waysC[t][i];
	   waysC[t][i] = waysC[t][k];
	   waysC[t][k] = c;
	   c = waysP[t][i];
	   waysP[t][i] = waysP[t][k];
	   waysP[t][k] = c;
	  }
	}

	void move() {
	  int p = waysP[t][way[t]];
	  int v = waysC[t][way[t]];
	  add(p, v);
	  ++t;
	  srch();
	  way[t] = -1;
	}

	void add(int p, int v) {
	  vacancies--;
	  values[p] = v;
	  int[] ri = field.placeToRegions[p];
	  for (int r = 0; r < ri.length; r++) {
	   int[] region = field.regions[ri[r]];
	   for (int k = 0; k < region.length; k++) {
		used[region[k]][v]++;
		if(used[region[k]][v] == 1) {
		 freeCount[region[k]]--;
		}
	   }
	   usedRegionColors[ri[r]][v] = 1;
	   freeRegionColorCount[ri[r]]--;
	  }
	}

	void back() {
	  --t;
	  int p = waysP[t][way[t]];
	  int v = waysC[t][way[t]];
	  remove(p, v);
	}

	void remove(int p, int v) {
	  vacancies++;
	  values[p] = -1;
	  int[] ri = field.placeToRegions[p];
	  for (int r = 0; r < ri.length; r++) {
	   int[] region = field.regions[ri[r]];
	   for (int k = 0; k < region.length; k++) {
		used[region[k]][v]--;
		if(used[region[k]][v] == 0) {
		 freeCount[region[k]]++;
		}
	   }
	   usedRegionColors[ri[r]][v] = 0;
	   freeRegionColorCount[ri[r]]++;
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
		treeCount++;

		if(treeCountLimit > 0 && treeCount > treeCountLimit) {
		 solutionCount = solutionLimit + 1;
		 return;
		}
	   }
	   if(isFinished()) {
		solutionCount++;
		if(solutionCount == 1) {
		 solution = (int[])values.clone();
		}
		for (int i = 0; i < values.length; i++) statistics[i][values[i]]++;
		
		if(solutionCount >= solutionLimit) return;
	   }
	  }
	}

	protected boolean isFinished() {
	  return vacancies == 0;
	}

	public int[] getSolution() {
	  return solution;
	}

	public void printSolution(int[] solution) {
	  System.out.print(solutionToString(solution));
	}

	public String solutionToString(int[] solution) {
	  StringBuffer sb = new StringBuffer();
	  for (int i = 0; i < field.size; i++) {
	   char c = (solution[i] < 0) ? '+' : ("" + (solution[i] + 1)).charAt(0);
	   sb.append(" " + c);
	   if(field.x[i] == field.width - 1) sb.append("\n");
	  }
	  return sb.toString();
	}

	public int[] getNarrowest() {
		int res = -1;
		int bp = -1;
		int bc = -1;
		for (int i = 0; i < statistics.length; i++) {
			for (int c = 0; c < 9; c++) {
				if(statistics[i][c] == 0) continue;
				if(res < 0 || statistics[i][c] < res) {
					res = statistics[i][c];
					bp = i;
					bc = c;
				}
			}
		}
		return new int[]{bp, bc, res};
	}

}
