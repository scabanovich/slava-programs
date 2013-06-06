package com.slava.common;

public class ContinuityCheck {
	
	public interface Acceptor {
		public boolean accept(int p);
	}
	
	TwoDimField field;
	int[] visited;
	int[] stack;
	int directionsCount = 4;
	
	Acceptor acceptor;
	
	public ContinuityCheck() {}
	
	public void setField(TwoDimField f) {
		field = f;
		visited = new int[f.getSize()];
		stack = new int[f.getSize()];
	}
	
	public void setAcceptor(Acceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	/**
	 * Returns true if cells for which acceptor.accept(p) = true
	 * make one region.
	 * @return
	 */
	public boolean isContinuous() {
		int v = 0;
		int p0 = -1;
		for (int p = 0; p < field.getSize(); p++) {
			if(acceptor.accept(p)) {
				++v;
				if(v == 1) p0 = p;
			}
		}
		if(p0 < 0) return true; //nothing
		int vc = 1;
		visited[p0] = 1;
		stack[0] = p0;
		int ic = 0;
		while(ic < vc) {
			int p = stack[ic];
			for (int d = 0; d < directionsCount; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && visited[q] == 0 && acceptor.accept(q)) {
					stack[vc] = q;
					visited[q] = 1;
					++vc;
				}
			}
			ic++;
		}
		for (int i = 0; i < vc; i++) visited[stack[i]] = 0;
		return vc == v;
	}

}
