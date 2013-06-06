package ic2006_2;

public class MirrorQuarterEnumerator {
	MirrorQuorterField field;
	MirrorSeaBattle listener;
	
	int limit = 5;
	
	int[] state;
	int[] restriction;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int stateCount;
	
	public MirrorQuarterEnumerator() {}
	
	public void setField(MirrorQuorterField field) {
		this.field = field;
	}
	
	public void setListener(MirrorSeaBattle listener) {
		this.listener = listener;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		restriction = new int[field.getSize()];
		wayCount = new int[limit + 1];
		way = new int[limit + 1];
		ways = new int[limit + 1][field.getSize()];
		t = 0;
		stateCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == limit) return;
		int p0 = t == 0 ? 0 : ways[t - 1][way[t - 1]] + 1;
		for(int p = p0; p < state.length; p++) {
			if(state[p] > 0 || restriction[p] > 0) continue;
			ways[t][wayCount[t]] = p;
			wayCount[t]++;
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = 1;
		for (int d = 1; d < 8; d += 2) { //diagonals
			int q = field.jump(p, d, 1);
			if(q >= 0) restriction[q]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = 0;
		for (int d = 1; d < 8; d += 2) { //diagonals
			int q = field.jump(p, d, 1);
			if(q >= 0) restriction[q]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] ==wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == limit) {
				stateCount++;
				
				onSolutionCount();
			}
		}
	}
	
	void onSolutionCount() {
		MirrorSeaBattle b = listener;
		b.setField(field);
		int[][] quorterStates = new int[8][state.length];
		quorterStates[0] = (int[])state.clone();
		for (int i = 1; i < 4; i++) quorterStates[i] = field.rotate(quorterStates[i - 1]);
		for (int i = 0; i < 4; i++) quorterStates[i + 4] = field.reflect(quorterStates[i]);
		b.setQuorterStates(quorterStates);
		b.solve();
		if(b.stateCount > 0) System.out.println("-->" + b.stateCount);
	}
	
	

}
