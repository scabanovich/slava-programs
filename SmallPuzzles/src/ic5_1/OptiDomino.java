package ic5_1;

public class OptiDomino {
	int[][] dominoIndex;
	int dominoCount;
	int[] digitStore;
	
	OptiDominoPath pathSource;
	int[] pathDigits;
	int[] digitDistribution;
	int[] usedDigitDistribution;
	int[] sums;
	int[] usedDomino;
	
	int[] state;
	int[] up, down;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysA, waysB;
	
	long maxNumber;
	
	int[] solution;
	
	public void setPathSource(OptiDominoPath pathSource) {
		this.pathSource = pathSource;
		setPathDigits(pathSource.getPathDigits()); 
	}
	
	public void setPathDigits(int[] pathDigits) {
		this.pathDigits = pathDigits;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		dominoCount = 0;
		dominoIndex = new int[7][7];
		for (int i = 6; i >= 0; i--) {
			for (int j = i; j >= 0; j--) {
				dominoIndex[i][j] = dominoCount;
				dominoIndex[j][i] = dominoCount;
				++dominoCount;				
			}
		}
		if(pathDigits.length != dominoCount * 2) throw new RuntimeException("Wrong path");
		digitStore = new int[7];
		for (int i = 0; i < 7; i++) digitStore[i] = 8;
		digitDistribution = new int[11];
		for (int i = 0; i < pathDigits.length; i++) digitDistribution[pathDigits[i]]++;
		usedDigitDistribution = new int[11];
		sums = new int[11];
		
		state = new int[pathDigits.length];
		for (int i = 0; i < pathDigits.length; i++) state[i] = -1;
		up = new int[pathDigits.length];
		down = new int[pathDigits.length];
		for (int i = 0; i < pathDigits.length; i++) {
			int j = i + 1;
			if(j == pathDigits.length) j = 0;
			up[i] = j;
			down[j] = i;
		}		
		
		usedDomino = new int[dominoCount];
		place = new int[dominoCount + 1];
		wayCount = new int[dominoCount + 1];
		way = new int[dominoCount + 1];
		waysA = new int[dominoCount + 1][dominoCount * 2];
		waysB = new int[dominoCount + 1][dominoCount * 2];
		
		t = 0;
		maxNumber = 67883000000L;
	}
	
	long getNumber() {
		long n = 0;
		int v = 7;
		int s = 0;
		for (int i = 10; i >= 0; i--) {
			n = n * 10 + sums[i];
			int delta = digitDistribution[i] - usedDigitDistribution[i];
			if(delta <= 0) continue;
			while(delta > 0) {
				while(s == 0 && v > 0) {
					--v;
					s = digitStore[v];
				}
				if(s == 0) break;
				--delta;
				n += v;
				--s;
			}
			
		}
		return n;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == dominoCount) return;
		if(getNumber() < maxNumber) return;
		if(hasForced()) return;
		findMostImportantPlace();
		if(place[t] < 0) return;
		int pa = place[t];
		int pb = pa + 1;
		int an = state[down[pa]];
		int bn = state[up[pb]];
		for (int a = 0; a < 7; a++) {
			if(an >= 0 && a != an) continue;
			for (int b = 0; b < 7; b++)	{
				if(bn >= 0 && b != bn) continue;
				addWay(a, b);
			} 
		} 
	}
	
	boolean hasForced() {
		for (int i = 0; i < dominoCount; i++) {
			int p = i * 2;
			if(state[p] >= 0) continue;
			int a = state[down[p]];
			int b = state[up[p + 1]];
			if(a < 0 || b < 0) continue;
			place[t] = p;
			addWay(a, b);
			return true;
		}
		return false;
	}
	
	void findMostImportantPlace() {
		place[t] = -1;
		int wm = -1;
		for (int i = 0; i < dominoCount; i++) {
			int pa = i * 2;
			int pb = pa + 1;
			if(state[pa] >= 0) continue;
			int a = state[down[pa]];
			int b = state[up[pb]];
			int w = pathDigits[pa] * pathDigits[pa] + pathDigits[pb] * pathDigits[pb];
			if(a >= 0) w += 5;
			if(b >= 0) w += 5;
			if(w > wm) {
				wm = w;
				place[t] = pa;
			}
		}
	}
	
	void addWay(int a, int b) {
		int index = dominoIndex[a][b];
		if(usedDomino[index] == 1) return;
		waysA[t][wayCount[t]] = a;
		waysB[t][wayCount[t]] = b;
		wayCount[t]++;
	}
	
	void move() {
		int a = waysA[t][way[t]];
		int b = waysB[t][way[t]];
		int ia = place[t];
		int ib = ia + 1;
		state[ia] = a;
		state[ib] = b;
		sums[pathDigits[ia]] += a;
		sums[pathDigits[ib]] += b;
		usedDigitDistribution[pathDigits[ia]]++;
		usedDigitDistribution[pathDigits[ib]]++;
		digitStore[a]--;
		digitStore[b]--;		
		usedDomino[dominoIndex[a][b]] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int a = waysA[t][way[t]];
		int b = waysB[t][way[t]];
		int ia = place[t];
		int ib = ia + 1;
		state[ia] = -1;
		state[ib] = -1;
		sums[pathDigits[ia]] -= a;
		sums[pathDigits[ib]] -= b;
		usedDigitDistribution[pathDigits[ia]]--;
		usedDigitDistribution[pathDigits[ib]]--;
		digitStore[a]++;
		digitStore[b]++;		
		usedDomino[dominoIndex[a][b]] = 0;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			++way[t];
			move();
			if(t == dominoCount && getNumber() > maxNumber) {
				maxNumber = getNumber();
///				printSolution();
				solution = (int[])state.clone();
			}
		}		
	}
	
	void printSolution() {
		System.out.println("maxNumber=" + maxNumber);
		for (int i = 0; i < dominoCount; i++) {
			int pa = 2 * i;
			int pb = pa + 1;
			System.out.print(" " + state[pa] + "" + state[pb]);
		}
		System.out.println("");
	}
	
	public long getMaxNumber() {
		return maxNumber;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public static void main(String[] args) {
		OptiDominoPath p = OptiDominoPath.createDefault();
		int[] path = p.getPathDigits();
		OptiDomino d = new OptiDomino();
		d.setPathSource(p);
		d.solve();
		long maxNumber = d.getMaxNumber();
		int[] solution = d.getSolution();
		if(solution == null) {
			System.out.println("No solution");
		} else {
			System.out.println("maxNumber=" + maxNumber);
			p.printValues(solution);
		}
	}

}

/*
maxNumber=67883910558
         - - - - -        
       - - - - - - -      
     - - - - - - - - -    
   6 6 6 6 3 3 5 5 2 2 2  
 1 1 - - - - - - - - - 2 -
 3 - - 5 5 5 4 4 3 3 3 3 -
 3 0 - 5 - - - - - - - - -
 - 0 - 6 6 4 4 4 4 2 2 0 -
 - 5 - - - - - - - - - 0 -
   5 1 1 1 - 6 6 2 2 1 1  
     - - 1 - 0 - - - -    
       - 4 - 0 - - -      
         4 0 0 - -        
67883910558:-----,-------,---------,66663355222,11---------2-,3--555443333-,30-5---------,-0-664444220-,-5---------0-,5111-662211,--1-0----,-4-0---,400--
*/