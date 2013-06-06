package ic2006_3;

import com.slava.common.RectangularField;

public class OpticomizationFiller {
	int[][] rays = {
		{0,0}, {1,1}, {35,2}, {54,0}, {75,3}, {77,3}, {80,2}	
	};
	RectangularField field;
	
	int[] numbers;
	int[] state;
	
	int[] rayShadows;
	int[] shipShadows;
	
	public void setField(RectangularField f) {
		field = f;
	}

	public void setNumbers(int[] ns) {
		numbers = ns;
	}
	
	public void setState(int[] state) {
		this.state = state;
		rayShadows = new int[state.length];
		shipShadows = new int[state.length];
	}
	
	public boolean createShadows() {
		for (int i = 0; i < state.length; i++) {
			rayShadows[i] = -1;
			shipShadows[i] = -1;
		}
		for (int r = 0; r < rays.length; r++) {
			int p = getFirstObstacle(r);
			if(p < 0) return false;
			int length = numbers[p];
			int d = rays[r][1];
			int s = state[p];
			for (int i = 0; i < length; i++) {
				p = field.jump(p, d);
				if(p < 0) {
					// shadow must be at least partially inside
					if(i == 0) return false;
					break;
				}
				if(rayShadows[p] >= 0 || shipShadows[p] >= 0) return false;
				rayShadows[p] = r;
				shipShadows[p] = s;				
			}
		}
		return compareSums();
	}
	
	int getFirstObstacle(int r) {
		int p = rays[r][0];
		if(state[p] >= 0) return p;
		int d = rays[r][1];
		while(p >= 0) {
			if(state[p] >= 0) return p;
			p = field.jump(p, d);			
		}
		return -1;
	}
	
	boolean compareSums() {
		int delta = 0;
		for (int p = 0; p < state.length; p++) {
			if(shipShadows[p] >= 0) {
				int n = numbers[p];
				if(state[p] < 0) delta -= n; else delta += n;
			}
		}
		return delta == 0;
	}
	
	public int getGoodShipCount() {
		int c = 0;
		int[] sh = new int[10];
		int[] se = new int[10];
		for (int p = 0; p < state.length; p++) {
			if(shipShadows[p] < 0) continue;
			int s = shipShadows[p];
			if(state[p] < 0) se[s] = 1; else sh[s] = 1;
		}
		for (int i = 0; i < sh.length; i++) {
			if(sh[i] == 1 && se[i] == 1) c++;
		}		
		return c;
	}
	
	public int getGoodRayCount() {
		int c = 0;
		int[] sh = new int[rays.length];
		int[] se = new int[rays.length];
		for (int p = 0; p < state.length; p++) {
			if(rayShadows[p] < 0) continue;
			int s = rayShadows[p];
			if(state[p] < 0) se[s] = 1; else sh[s] = 1;
		}
		for (int i = 0; i < sh.length; i++) {
			if(sh[i] == 1 && se[i] == 1) c++;
		}		
		return c;
	}
	

}
