package slava.puzzles.hexa.fence.analysis;

import com.slava.common.RectangularField;

public class HexaFenceContinuityCheck {
	RectangularField field;
	int[] form;
	int[] area;
	int[] visited;
	int[] stack;
	int directionsCount;
	
	public HexaFenceContinuityCheck(int directionsCount) {
		this.directionsCount = directionsCount;
	}
	
	public void setField(RectangularField f) {
		this.field = f;
		visited = new int[f.getSize()];
		stack = new int[f.getSize()];
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public boolean check(int[] area) {
		int areaCount = 0;
		int outAreaCount = 0;
		int innerOutAreaCount = 0;
		for (int p = 0; p < visited.length; p++) {
			visited[p] = 0;
		}
		for (int p = 0; p < visited.length; p++) {
			if(form[p] == 0) continue;
			if(visited[p] > 0) continue;
			if(area[p] == 1) {
				if(areaCount > 0) return false;
				areaCount++;
				buildArea(area, p);
			}			
		}
		for (int p = 0; p < visited.length; p++) {
			visited[p] = 0;
		}
		for (int p = 0; p < visited.length; p++) {
			if(form[p] == 0) continue;
			if(visited[p] > 0) continue;
			if(area[p] == 0) {
				boolean isNotInner = buildOutArea(area, p);
				outAreaCount++;
				if(!isNotInner) innerOutAreaCount++;
				if(innerOutAreaCount > 0 && outAreaCount > 1) return false;
			}			
		}		
		return true;
	}
	
	void buildArea(int[] area, int p0) {
		int vc = 1;
		visited[p0] = 1;
		stack[0] = p0;
		int ic = 0;
		while(ic < vc) {
			int p = stack[ic];
			for (int d = 0; d < directionsCount; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && visited[q] == 0 && form[q] == 1 && area[q] != 0) {
					stack[vc] = q;
					visited[q] = 1;
					++vc;
				}
			}
			ic++;
		}
	}
	
	boolean buildOutArea(int[] area, int p0) {
		boolean border = false;
		int vc = 1;
		visited[p0] = 1;
		stack[0] = p0;
		int ic = 0;
		while(ic < vc) {
			int p = stack[ic];
			for (int d = 0; d < directionsCount; d++) {
				int q = field.jump(p, d);
				if(q < 0 || form[q] == 0) border = true;
				if(q >= 0 && visited[q] == 0 && form[q] == 1 && area[q] != 1) {
					stack[vc] = q;
					visited[q] = 1;
					++vc;
				}
			}
			ic++;
		}
		return border;
	}

}
