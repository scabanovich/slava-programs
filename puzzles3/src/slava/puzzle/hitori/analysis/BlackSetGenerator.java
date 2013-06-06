package slava.puzzle.hitori.analysis;

import com.slava.common.ContinuityCheck;
import com.slava.common.RectangularField;

public class BlackSetGenerator {
	RectangularField field;
	
	public BlackSetGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public int[] generate() {
		final int[] set = new int[field.getSize()];
		for (int i = 0; i < set.length; i++) set[i] = -1;
		
		ContinuityCheck check = new ContinuityCheck();
		check.setField(field);
		ContinuityCheck.Acceptor acceptor = new ContinuityCheck.Acceptor() {
			public boolean accept(int p) {
				return set[p] < 1;
			}			
		};
		check.setAcceptor(acceptor);
		
		int[] order = getRandomArray(field.getSize());
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			if(set[p] >= 0) continue;
			set[p] = 1;
			if(!check.isContinuous()) {
				set[p] = 0;
			} else {
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q >= 0) set[q] = 0;
				}
			}
		}		
		return set;
	}
	
	int[] getRandomArray(int n) {
		int[] ns = new int[n];
		for (int i = 0; i < n; i++) ns[i] = i;
		for (int i = 0; i < ns.length; i++) {
			int j = i + (int)((ns.length - i) * Math.random());
			if(j == i) continue;
			int k = ns[i];
			ns[i] = ns[j];
			ns[j] = k;
		}
		return ns;
	}
	
	
	

}
