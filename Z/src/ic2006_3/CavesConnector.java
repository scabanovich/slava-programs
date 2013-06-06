package ic2006_3;

import com.slava.common.RectangularField;

public class CavesConnector {
	RectangularField field;
	int valuesCount;
	int[] state;
	int[][] restrictions;
	
	int[] visited;
	int[] stack;
	
	public void init(RectangularField field, int valuesCount, int[] state, int[][] restrictions) {
		this.field = field;
		this.valuesCount = valuesCount;
		this.state = state;
		this.restrictions = restrictions;
		
		visited = new int[state.length];
		stack = new int[state.length];
	}
	
	public boolean isConnectable() {
		for (int v = 0; v < valuesCount; v++) {
			if(!isConnectable(v)) return false;
		}
		return true;
	}
	
	boolean isConnectable(int v) {
		int p0 = find(v);
		if(p0 < 0) return true;
		stack[0] = p0;
		visited[p0]++;
		int volume = 1;
		int c = 0;
		while(c < volume) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || visited[q] > 0) continue;
				if(restrictions[q][v] > 0) continue;
				if(state[q] >= 0 && state[q] != v) continue;
				visited[q]++;
				stack[volume] = q;
				volume++;
			}
			++c;
		}
		int pu = findUnvisited(v);
		for (int i = 0; i < volume; i++) visited[stack[i]] = 0;
		return pu < 0;
	}
	
	int find(int v) {
		for (int p = 0; p < state.length; p++) {
			if(state[p] == v) return p;
		}
		return -1;
	}
	int findUnvisited(int v) {
		for (int p = 0; p < state.length; p++) {
			if(state[p] == v && visited[p] == 0) return p;
		}
		return -1;
	}

}
