package slava.puzzle.stars.analysis;

import slava.puzzle.stars.model.StarsSetsField;

public class CaveRunner {
	static int[] NUMBERS = {
		0,0,0,0,0,0,5,0,5,
		0,0,2,2,0,0,0,0,0,
		0,4,0,2,3,0,0,2,0,
		0,0,0,0,0,0,0,0,0,
		3,0,0,4,0,0,0,3,0,
		0,4,0,0,0,7,0,2,0,
		0,0,0,2,0,0,0,0,0,
		5,3,0,0,0,0,0,0,0,
		0,0,0,0,0,7,0,1,0
	};
	
	static int[] INITIAL_CAVES = {
		0,0,0,0,0,0,1,1,1,
		0,0,0,0,0,0,1,1,1,
		0,0,0,0,0,1,1,1,1,
		2,2,2,2,2,1,1,1,1,
		2,2,2,2,2,1,3,3,3,
		4,2,2,2,2,1,3,4,3,
		4,4,2,2,2,1,3,4,3,
		4,4,4,2,4,4,4,4,3,
		4,4,4,4,4,4,4,3,3,
	};
	
	static int[] FIXED = {
		1,1,1,0,0,1,1,1,1,
		0,0,0,0,0,0,1,0,1,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		1,0,0,0,0,0,0,0,1,
		1,0,0,0,0,0,0,0,1,
		1,0,0,0,0,1,0,0,1,
		1,1,1,1,0,1,0,0,1,
		1,1,1,1,1,1,1,1,1,
	};
	
	public static void main(String[] args) {
		StarsSetsField f = new StarsSetsField();
		f.setSize(9);
		CavesGenerator g = new CavesGenerator();
		g.setField(f);
		g.setInitialColors(INITIAL_CAVES);
		g.setFixed(FIXED);
		ProcessorImpl p = new ProcessorImpl();
		p.setField(f);
		p.setNumbers(NUMBERS);
		g.setProcessor(p);
		g.process();
	}

}

class ProcessorImpl implements CavesGenerator.Processor {
	StarsSetsField field;
	int[] numbers;
	int bestBad;

	public void setField(StarsSetsField field) {
		this.field = field;
		bestBad = 100;
	}
	
	public void setNumbers(int[] ns) {
		numbers = ns;
	}

	public int process(int[] colors) {
		int bad = 0;
		for (int p = 0; p < numbers.length; p++) {
			if(numbers[p] <= 0) continue;
			if(!check(p, colors)) bad++;
		}
		if(bestBad == bad) return 1;
		if(bad < bestBad) {
			bestBad = bad;
			System.out.println(bad);
			if(bad == 0) return 2;
			return 1;
		}
		return 0;
	}
	
	boolean check(int p, int[] colors) {
		int n = numbers[p];
		int c = colors[p];
		int s = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && colors[q] == c) {
				++s;
				if(s > n) return false;
				q = field.jump(q, d);
			}
		}
		return s == n;
	}
}

class CavesGenerator {
	public interface Processor {
		public int process(int[] colors);
	}
	StarsSetsField field;
	int[] fixed;
	
	int[] colors;
	int[] visited;
	int[] stack;
	
	Processor processor;
	
	public void setField(StarsSetsField field) {
		this.field = field;
		colors = new int[field.getSize()];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = field.x(i);
		}
		visited = new int[field.getSize()];
		stack = new int[field.getSize()];
	}
	
	public void setInitialColors(int[] cs) {
		colors = cs;
	}
	
	public void setFixed(int[] c) {
		fixed = c;
	}
	
	public void setProcessor(Processor p) {
		processor = p;
	}
	
	public void process() {
		while(true) {
			next();
		}
	}

	public void next() {
		while(!flip()) {}
	}
	
	public int[] getColors() {
		return colors;		
	}
	
	boolean flip() {
		int p = (int)(field.getSize() * Math.random());
		int c = colors[p];
		int d = (int)(4 * Math.random());
		int q = field.jump(p, d);
		if(q < 0 || colors[q] == c) return false;

		if(!isFlippable(q)) return false;

		int qc = colors[q];
		colors[q] = c;
		if(isContinuous(qc)) {
			int r = processor.process(colors);
			if(r == 2) {
				printSolution();
				return true;
			} else if(r == 1) {
				return true;
			} else if(r == 0) {
				if(Math.random() > 0.95) return true;
			}			
		}
		
		colors[q] = qc;
		return false;
	}
	
	boolean isFlippable(int p) {
		return fixed[p] == 0;
	}
	
	boolean isContinuous(int c) {
		int v = 0;
		int p0 = -1;
		for (int p = 0; p < field.getSize(); p++) {
			if(colors[p] == c) {
				++v;
				if(v == 1) p0 = p;
			}
		}

		int minimumRegion = 5;
		if(v < minimumRegion) return false;

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
		for (int i = 0; i < field.getSize(); i++) {
			sb.append(" ");
			if(colors[i] > 9) {
				char c = (char)(97 + colors[i] - 10);
				sb.append(c);
			} else {
				sb.append(colors[i]);
			}
			if(field.x(i) == field.getWidth() - 1) sb.append("\n");
		}
		System.out.println(sb.toString());
	}

}
