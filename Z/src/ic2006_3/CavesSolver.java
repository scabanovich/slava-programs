package ic2006_3;

import com.slava.common.RectangularField;

public class CavesSolver {
	static int FILLING_MOVE = 0;
	static int RESTRICTING_MOVE = 1;
	RectangularField field;
	int[] visualData;
	
	CavesConnector cavesConnector = new CavesConnector();
	
	int cavesCount = 5;

	int unfilledCells;
	int[] cavesUsage;
	
	int[] state;
	int[][] restrictions;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysK;
	int[][] waysV;
	int[] temp;
	
	int solutionCount;
	int treeCount;
	
	public CavesSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setVisualData(int[] vs) {
		visualData = vs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		restrictions = new int[field.getSize()][cavesCount];
		cavesUsage = new int[cavesCount];
		
		cavesConnector.init(field, cavesCount, state, restrictions);
		
		place = new int[200];
		wayCount = new int[200];
		way = new int[200];
		waysK = new int[200][cavesCount];
		waysV = new int[200][cavesCount];
		temp = new int[cavesCount];
		
		unfilledCells = state.length;
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unfilledCells == 0) return;
		if(!checkVisuals()) return;
		if(!cavesConnector.isConnectable()) return;
		int p = getUnfinishedPlace();
		if(p >= 0) {
			int v = state[p];
			int q = getGrowsPlace(p);
			if(q < 0) return;
			if(restrictions[q][v] > 0) return;
			place[t] = q;
			waysK[t][0] = FILLING_MOVE;
			waysV[t][0] = v;
			waysK[t][1] = RESTRICTING_MOVE;
			waysV[t][1] = v;
			wayCount[t] = 2;			
			return;
		}
		int wc = getNarrowestPlace();
		if(wc < 2) {
			wayCount[t] = wc;
			return;
		}
		if(wc == 100) return;
		
		p = getUnstartedPlace();
		if(p >= 0) {
			place[t] = p;
			for (int v = 0; v < cavesCount; v++) {
				if(restrictions[p][v] == 0) {
					waysK[t][wayCount[t]] = FILLING_MOVE;
					waysV[t][wayCount[t]] = v;
					wayCount[t]++;
				}				
				if(cavesUsage[v] == 0) break;
			}
			return;
		}		
		if(wc > 5) throw new RuntimeException("Fuck " + wc);
		wayCount[t] = wc;
	}
	
	int getUnfinishedPlace() {
		for (int p = 0; p < state.length; p++) {
			if(visualData[p] <= 0) continue;
			if(state[p] < 0) continue;
			if(visualData[p] > getVisible(p)) return p;
		}
		return -1;
	}
	
	int getUnstartedPlace() {
		int pc = -1;
		int max = 0;
		for (int p = 0; p < state.length; p++) {
			if(visualData[p] <= 0) continue;
			if(state[p] < 0) {
				if(visualData[p] > max) {
					max = visualData[p];
					pc = p;
				}
			}
		}
		return pc;
	}
	
	int getNarrowestPlace() {
		int min = 100;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc < min) {
				for (int i = 0; i < wc; i++) {
					waysK[t][i] = FILLING_MOVE;
					waysV[t][i] = temp[i];
				}
				min = wc;
				place[t] = p;
				if(min < 2) return min;
			}
		}
		return min;
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int v = 0; v < cavesCount; v++) {
			if(restrictions[p][v] > 0) continue;
			state[p] = v;
			boolean b = checkVisuals() && cavesConnector.isConnectable();
			state[p] = -1;
			if(b) {
				temp[wc] = v;
				++wc;
			}
			if(cavesUsage[v] == 0) break;
		}			
		return wc;
	}
	
	int getGrowsPlace(int p) {
		int v = state[p];
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && state[q] == v) q = field.jump(q, d);
			if(q < 0) continue;
			if(state[q] < 0 && restrictions[q][v] == 0) return q;
		}
		return -1;
	}
	
	boolean checkVisuals() {
		for (int p = 0; p < state.length; p++) {
			if(visualData[p] <= 0) continue;
			if(state[p] < 0) continue;
			if(visualData[p] < getVisible(p)) return false;
			if(visualData[p] > getMaxVisible(p)) return false;
		}
		return true;
	}
	
	int getVisible(int p) {
		int c = 0;
		int v = state[p];
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && state[q] == v) {
				++c;
				q = field.jump(q, d);
			}
		}		
		return c;
	}
	
	int getMaxVisible(int p) {
		int c = 0;
		int v = state[p];
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && (state[q] == v || (state[q] < 0 && restrictions[q][v] == 0))) {
				++c;
				q = field.jump(q, d);
			}
		}		
		return c;
	}
	
	void move() {
		int k = waysK[t][way[t]];
		int v = waysV[t][way[t]];
		int p = place[t];
		if(k == FILLING_MOVE) {
			state[p] = v;
			unfilledCells--;
			cavesUsage[v]++;
		} else {
			restrictions[p][v]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = waysK[t][way[t]];
		int v = waysV[t][way[t]];
		int p = place[t];
		if(k == FILLING_MOVE) {
			state[p] = -1;
			unfilledCells++;
			cavesUsage[v]--;
		} else {
			restrictions[p][v]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int u = unfilledCells;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCount % 1000000 == 0) {
					System.out.println(treeCount + " : " + getCompleteness());
				}
			}
			if(unfilledCells < u) {
				u = unfilledCells;
				System.out.println(unfilledCells);
			}
			if(unfilledCells == 0 && checkVisuals() 
					&& cavesConnector.isConnectable()
					) {
				++solutionCount;
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		for (int p = 0; p < state.length; p++) {
			System.out.print(" " + state[p]);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		System.out.println("key=" + getKey());
	}
	
	double getCompleteness() {
		double p = 1;
		double s = 0;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}		
		return s;
	}
	
	String getKey() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getWidth(); i++) {
			int p = field.getIndex(i, i);
			sb.append(cavesUsage[state[p]]).append(',');
		}
		return sb.toString();
	}

}
