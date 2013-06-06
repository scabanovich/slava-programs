package ic2006_1;

public class OktaedtNoncompactSolver {
	TriangularField field;
	OktaedrFigures figures;
	int size;
	int figuresCount;
	
	int[] state;
	int[] usedFigures;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysP;
	
	int minFigures;
	int solutionCount;
	
	public OktaedtNoncompactSolver() {}
	
	public void setField(TriangularField f) {
		field = f;
		size = f.getSize();
	}
	
	public void setFigures(OktaedrFigures figures) {
		this.figures = figures;
		figuresCount = figures.placements.length;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		usedFigures = new int[figuresCount];
		
		wayCount = new int[figuresCount + 1];
		way = new int[figuresCount + 1];
		waysF = new int[figuresCount + 1][5000];
		waysP = new int[figuresCount + 1][5000];
		
		t = 0;
		minFigures =
			4;
			//figuresCount + 1;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= minFigures) return;
		int startFrom = 5;
		int sf = (t == 0) ? startFrom : waysF[t - 1][way[t - 1]];
		for (int f = sf; f < figuresCount; f++) {
			if(usedFigures[f] > 0) continue;
			for (int i = 0; i < figures.placements[f].length; i++) {
				if(!canAdd(f, i)) continue;
				waysF[t][wayCount[t]] = f;
				waysP[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}		
	}
	
	boolean canAdd(int figureIndex, int placementIndex) {
		int[] ps = figures.placements[figureIndex][placementIndex];
		for (int i = 0; i < ps.length; i++) {
			if(state[ps[i]] >= 0) return false;
		}
		return true;
	}

	void move() {
		int figureIndex = waysF[t][way[t]];
		int placementIndex = waysP[t][way[t]];
		usedFigures[figureIndex]++;
		int[] ps = figures.placements[figureIndex][placementIndex];
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = figureIndex;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int figureIndex = waysF[t][way[t]];
		int placementIndex = waysP[t][way[t]];
		usedFigures[figureIndex]--;
		int[] ps = figures.placements[figureIndex][placementIndex];
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = -1;
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
			if(wayCount[t] == 0 && !canPlaceMore() && t >= minFigures) {
				if(t > minFigures) {
					solutionCount = 1;
				} else {
					++solutionCount;
				}
				minFigures = t;
				if(solutionCount < 3 || solutionCount % 1000 == 1) {
					printSolution();
				}
			}
		}
	}
	
	boolean canPlaceMore() {
		for (int f = 0; f < figuresCount; f++) {
			if(usedFigures[f] > 0) continue;
			for (int i = 0; i < figures.placements[f].length; i++) {
				if(canAdd(f, i)) return true;
			}
		}
		return false;
	}
	
	void printSolution() {
		System.out.println("Figures=" + minFigures);
		int xc = -1;
		for (int i = 0; i < size; i++) {
			if(field.x[i] > xc) {
				System.out.println("");
				xc = field.x[i];
			}
			char c = state[i] < 0 ? '.' : (char)(97 + state[i]);
			System.out.print(" " + c);
//			if(state[i] >= 0)
//			System.out.print(" " + i);
		}
		System.out.println("");
	}

}
