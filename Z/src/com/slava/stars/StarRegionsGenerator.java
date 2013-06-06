package com.slava.stars;

public class StarRegionsGenerator {
	StarsRegionField field;
	
	int[] colors;
	int[] visited;
	int[] stack;
	
	public StarRegionsGenerator() {}
	
	public void setField(StarsRegionField field) {
		this.field = field;
		colors = new int[field.size];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = field.x[i];
		}
		visited = new int[field.size];
		stack = new int[field.size];
	}

	public void generate() {
		StarsRegionField f = new StarsRegionField();
		f.setSize(field.getWidth());
		StarsSolver solver = new StarsSolver();
		solver.setSolutionLimit(2);
		for (int i = 0; i < 20; i++) next();
		int attemptCount = 0;
		while(solver.getSolutionCount() != 1) {
			++attemptCount;
			next();
			f.setRegions(getColors());
			solver.setField(f);
			solver.solve();
		}
		System.out.println("Problem attempt=" + attemptCount);
		printSolution();
		solver.printSolution(solver.getSolution()[0]);
		System.out.println("treeSize = " + solver.getTreeSize());
	}

	public void next() {
		int k = 0;
		while(k < 100) {
			if(flip()) ++k;
		}
	}
	
	public int[] getColors() {
		return colors;		
	}
	
	boolean flip() {
		int p = (int)(field.size * Math.random());
		int c = colors[p];
		int d = (int)(4 * Math.random());
		int q = field.jump(p, d);
		if(q < 0 || colors[q] == c) return false;
		int qc = colors[q];
		colors[q] = c;
		if(isContinuous(qc)) return true;
		colors[q] = qc;
		return false;
	}
	
	boolean isContinuous(int c) {
		int v = 0;
		int p0 = -1;
		for (int p = 0; p < field.size; p++) {
			if(colors[p] == c) {
				++v;
				if(v == 1) p0 = p;
			}
		}
		if(v < 5) return false;
		int vc = 1;
		visited[p0] = 1;
		stack[0] = p0;
		int ic = 0;
		while(ic < vc) {
			int p = stack[ic];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && visited[q] == 0 && colors[q] == c) {
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

	public void printSolution() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.size; i++) {
			sb.append(" ");
			if(colors[i] > 9) {
				char c = (char)(97 + colors[i] - 10);
				sb.append(c);
			} else {
				sb.append(colors[i]);
			}
			if(field.x[i] == field.width - 1) sb.append("\n");
		}
		System.out.println(sb.toString());
	}
}
